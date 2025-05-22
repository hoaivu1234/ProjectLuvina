/*
  Copyright(C) 2025 Luvina Software Company
  adm005.component.ts 15/5/2025 hoaivd
*/

import { DatePipe } from '@angular/common';
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
   * @param departmentService Service lấy dữ liệu phòng ban
   * @param certificationService Service lấy dữ liệu trình độ tiếng nhật
   * @param employeeService Service thêm dữ liệu nhân viên
   * @param router Service định tuyến Router để điều hướng khi xảy ra lỗi
   * @param datePipe Pipe dùng để chuyển đổi dữ liệu các trường date
   */
  constructor(
    public departmentService: DepartmentService,
    public certificationService: CertificationService,
    public employeeService: EmployeeService,
    private router: Router,
    private datePipe: DatePipe
  ) { }

  ngOnInit() {
    // Lấy dataConfirm nếu được truyền qua navigation state
    this.dataConfirm = history.state?.['dataConfirm'];
  }

  /**
  * Điều hướng về màn hình ADM002
  */
  hanleBack() {
    this.router.navigate(['/user/adm004'], { state: { dataConfirmBack: this.dataConfirm } });
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

    if(clonedData?.departmentName) delete clonedData.departmentName;

    if (clonedData.certifications) {
      const hasEmptyCertId = clonedData.certifications.some(
        (cert: any) => cert.certificationId === "" || !cert.certificationId
      );

      if (hasEmptyCertId) {
        delete clonedData.certifications;
      } else {       
        clonedData.certifications.forEach((cert: any) => {
          if(cert?.certificationName) delete cert.certificationName;
          cert.startDate = this.datePipe.transform(cert.startDate, 'yyyy/MM/dd');
          cert.endDate = this.datePipe.transform(cert.endDate, 'yyyy/MM/dd');
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
        this.router.navigate(['user/adm006'], {
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
