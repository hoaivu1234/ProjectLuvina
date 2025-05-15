/*
  Copyright(C) 2025 Luvina Software Company
  adm005.component.ts 15/5/2025 hoaivd
*/

import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, forkJoin, tap, throwError } from 'rxjs';
import { Certification } from 'src/app/model/certification.model';
import { Department } from 'src/app/model/department.model';
import { CertificationService } from 'src/app/service/certification.service';
import { DepartmentService } from 'src/app/service/department.service';
import { CONSOLE_MESSAGES } from 'src/app/shared/utils/console-message.constants';
import { ERROR_CODES } from 'src/app/shared/utils/error-code.constants';

@Component({
  selector: 'app-adm005',
  templateUrl: './adm005.component.html',
  styleUrls: ['./adm005.component.css']
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
    private router: Router,
    private fb: FormBuilder,
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
        return throwError(err); 
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
        return throwError(err); 
      })
    );
  }

  /**
  * Điều hướng về màn hình ADM002
  */
  hanleBack() {
    this.router.navigate(['/user/add'], { state: { dataConfirmBack: this.dataConfirm } });
  }
}
