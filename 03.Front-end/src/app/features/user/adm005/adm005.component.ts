/*
  Copyright(C) 2025 Luvina Software Company
  adm005.component.ts 15/5/2025 hoaivd
*/

import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, forkJoin, tap, throwError } from 'rxjs';
import { Certification } from 'src/app/model/certification.model';
import { Department } from 'src/app/model/department.model';
import { CertificationService } from 'src/app/service/certification.service';
import { DepartmentService } from 'src/app/service/department.service';
import { EmployeeService } from 'src/app/service/employee.service';
import { CONSOLE_MESSAGES } from 'src/app/shared/utils/console-message.constants';
import { ERROR_CODES } from 'src/app/shared/utils/error-code.constants';

@Component({
  selector: 'app-adm005',
  templateUrl: './adm005.component.html',
  styleUrls: ['./adm005.component.css'],
  providers: [DatePipe]
})

/**
 * Màn hình để confirm dữ liệu employee từ ADM004 gửi sang trước khi ghi vào DB
 * Nếu không muốn lưu dữ liệu vào DB thì có thể back dữ liệu lại về ADM004 để chỉnh sửa
 * Nếu có lỗi xảy ra trong quá trình thực hiện thì điều hướng đến màn System Error
 * 
 * @author hoaivd
 */
export class Adm005Component {
  listDepartments: Department[] = [];  // Danh sách các phòng ban, được dùng để hiển thị trong dropdown
  listCertifications: Certification[] = [];  // Danh sách các trình độ tiếng nhật, được dùng để hiển thị trong dropdown
  dataConfirm: any; // Dữ liệu gửi từ ADM004 sang

  /**
   * Constructor khởi tạo component, inject các service cần thiết.
   *
   * @param http Đối tượng HttpClient dùng để thực hiện các yêu cầu HTTP
   * @param departmentService Service lấy dữ liệu phòng ban
   * @param certificationService Service lấy dữ liệu trình độ tiếng nhật
   * @param router Service định tuyến Router để điều hướng khi xảy ra lỗi
   */
  constructor(
    public http: HttpClient,
    public departmentService: DepartmentService,
    public certificationService: CertificationService,
    public employeeService: EmployeeService,
    private router: Router,
    private fb: FormBuilder,
    private datePipe: DatePipe
  ) {
    const nav = this.router.getCurrentNavigation();

    // Lấy dataConfirm nếu được truyền qua navigation state
    this.dataConfirm = nav?.extras?.state?.['dataConfirm'];
  }

  /**
   * Lifecycle hook khởi chạy khi component được khởi tạo.
   * Gọi các hàm để lấy dữ liệu phòng ban, trình độ tiếng nhật và tạo dữ liệu cho form.
   */
  ngOnInit(): void {
    console.log(this.dataConfirm.certifications)

    forkJoin([
      this.getListDepartment(),
      this.getListCertification()
    ]).subscribe({
      next: () => {
        this.transformData(); // Dùng forkJoin để chờ gọi 2 API trên xong mới biến đổi data
      },
      error: (err) => {
        console.error('Error loading data:', err);
      }
    });
    console.log(this.dataConfirm.certifications)
  }

  /**
   * Biển đổi data để hiển thị trên giao diện
   */
  transformData() {
    this.dataConfirm.departmentName = this.listDepartments.find((item: any) => item.departmentId == this.dataConfirm.departmentId)?.departmentName;
    if (this.dataConfirm.certifications) {
      this.dataConfirm.certifications.forEach((cert: any) => {
        cert.certificationName = this.listCertifications.find((item: any) => item.certificationId == cert.certificationId)?.certificationName;
      })
    }
  }

  /**
 * Gọi API để lấy danh sách phòng ban.
 * Nếu thành công, gán dữ liệu vào listDepartments.
 * Nếu thất bại, chuyển hướng sang trang lỗi với mã lỗi tương ứng.
 */
  getListDepartment() {
    return this.departmentService.getListDepartment().pipe(
      tap((value) => {
        this.listDepartments = value?.departments;
        console.log(CONSOLE_MESSAGES.DEPARTMENT.FETCH_SUCCESS);
      }),
      catchError((err) => {
        console.log(CONSOLE_MESSAGES.DEPARTMENT.FETCH_FAILED);
        this.router.navigate(['error'], { state: { errorCode: ERROR_CODES.DEPARTMENT_FETCH_FAILED } });
        return throwError(() => err);
      })
    );
  }

  /**
 * Gọi API để lấy danh sách trình độ tiếng nhật.
 * Nếu thành công, gán dữ liệu vào listCertifications.
 * Nếu thất bại, chuyển hướng sang trang lỗi với mã lỗi   tương ứng.
 */
  getListCertification() {
    return this.certificationService.getListCertifications().pipe(
      tap((value) => {
        this.listCertifications = value?.certifications;
        console.log(CONSOLE_MESSAGES.CERTIFICATION.FETCH_SUCCESS);
      }),
      catchError((err) => {
        console.log(CONSOLE_MESSAGES.CERTIFICATION.FETCH_FAILED);
        this.router.navigate(['error'], { state: { errorCode: ERROR_CODES.CERTIFICATION_FETCH_FAILED } });
        return throwError(() => err);
      })
    );
  }

  /**
  * Điều hướng về màn hình ADM002
  */
  hanleBack() {
    this.router.navigate(['/user/add'], { state: { dataConfirmBack: this.dataConfirm } });
  }

   /**
   * Chuẩn hóa và chuyển đổi dữ liệu trước khi submit
   * - Format ngày sinh theo định dạng 'yyyy/MM/dd'
   * - Kiểm tra danh sách chứng chỉ:
   *    + Nếu có chứng chỉ nhưng `certificationId` rỗng, loại bỏ toàn bộ danh sách chứng chỉ
   *    + Nếu hợp lệ, format ngày bắt đầu và ngày kết thúc của từng chứng chỉ
   * @returns clonedData - Dữ liệu đã được xử lý sẵn để gửi lên server
   */
  transformDataSubmit(): any {
    const clonedData = { ...this.dataConfirm };

    clonedData.employeeBirthDate = this.datePipe.transform(clonedData.employeeBirthDate, 'yyyy/MM/dd');

    if (clonedData.certifications) {
      const hasEmptyCertId = clonedData.certifications.some(
        (cert: any) => cert.certificationId === "" || !cert.certificationId
      );

      if (hasEmptyCertId) {
        delete clonedData.certifications;
      } else {
        clonedData.certifications.forEach((cert: any) => {
          cert.certificationStartDate = this.datePipe.transform(cert.certificationStartDate, 'yyyy/MM/dd');
          cert.certificationEndDate = this.datePipe.transform(cert.certificationEndDate, 'yyyy/MM/dd');
        });
      }
    }

    return clonedData;
  }

    /**
     * Gửi dữ liệu đăng ký nhân viên lên server
     * - Nếu thành công: điều hướng sang màn hình hoàn tất và truyền mã xác nhận
     * - Nếu thất bại: điều hướng sang màn hình lỗi và truyền mã lỗi + thông tin trường bị lỗi
   */
  submitForm() {
    const clonedData = this.transformDataSubmit();
  
    this.employeeService.addEmployee(clonedData).subscribe({
      next: (data: any) => {
        console.log(data);
        this.router.navigate(['user/complete'], {
          state: { completeCode: data?.message?.code }
        });
      },
      error: (err) => {
        console.log(err);
        this.router.navigate(['error'], {
          state: { errorCode: err?.error?.message?.code, fieldError: err?.error?.message?.params[0] }
        });
      }
    });
  }  

}
