/*
  Copyright(C) 2025 Luvina Software Company
  adm004.component.ts 10/5/2025 hoaivd
*/

import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Certification } from 'src/app/model/certification.model';
import { Department } from 'src/app/model/department.model';
import { CertificationService } from 'src/app/service/certification.service';
import { DepartmentService } from 'src/app/service/department.service';
import { CONSOLE_MESSAGES } from 'src/app/shared/utils/console-message.constants';
import { ERROR_CODES } from 'src/app/shared/utils/error-code.constants';

@Component({
  selector: 'app-adm004',
  templateUrl: './adm004.component.html',
  styleUrls: ['./adm004.component.css']
})
export class ADM004Component {
  listDepartments: Department[] = [];  // Danh sách các phòng ban, được dùng để hiển thị trong dropdown
  selectedDepartment: string = '';   // ID của phòng ban đang được chọn
  listCertifications: Certification[] = [];  // Danh sách các trình độ tiếng nhật, được dùng để hiển thị trong dropdown

  employeeForm!: FormGroup;

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
    private fb: FormBuilder
  ) { }

  /**
   * Lifecycle hook khởi chạy khi component được khởi tạo.
   * Gọi các hàm để lấy dữ liệu phòng ban, trình độ tiếng nhật và tạo dữ liệu cho form.
   */
  ngOnInit(): void {
    this.initForm();
    this.getListDepartment();
    this.getListCertification();
    if (this.employeeForm) {
      this.employeeForm.get('certificationId')?.valueChanges.subscribe((value: any) => {
        if (value) {
          const selectedCertification = this.listCertifications.find(cer => cer.certificationId === value);
          if (selectedCertification) {
            console.log('Selected certification name:', selectedCertification.certificationName);
          }
        } else {
          console.log('No certification selected');
        }
      });
    }
  }

  /**
   * Khởi tạo form ban đầu, kèm validate
   */
  initForm() {
    this.employeeForm = this.fb.group({
      employeeLoginId: [null, Validators.required],
      employeeNameKana: [null, Validators.required],
      employeeName: [null, Validators.required],
      dateCertificateIssue: [null, Validators.required],
      expirationDateCertificate: [null, Validators.required],
      scoreCertificate: [null, Validators.required],
      certifications: this.fb.array([]),
      departmentId: [null, Validators.required],
      certificationId: [null, Validators.required],
      certificationStartDate: [null, Validators.required],
      certificationEndDate: [null, Validators.required],
      employeeCertificationScore: [null, Validators.required],
    });
  }


  /**
   * Gọi API để lấy danh sách phòng ban.
   * Nếu thành công, gán dữ liệu vào listDepartments.
   * Nếu thất bại, chuyển hướng sang trang lỗi với mã lỗi tương ứng.
   */
  getListDepartment() {
    this.departmentService.getListDepartment().subscribe({
      next: (value) => {
        this.listDepartments = value?.departments;
        console.log(CONSOLE_MESSAGES.DEPARTMENT.FETCH_SUCCESS);
      },
      error: () => {
        console.log(CONSOLE_MESSAGES.DEPARTMENT.FETCH_FAILED);
        this.router.navigate(['error'], { state: { errorCode: ERROR_CODES.DEPARTMENT_FETCH_FAILED } });
      },
    });
  }

  /**
 * Gọi API để lấy danh sách trình độ tiếng nhật.
 * Nếu thành công, gán dữ liệu vào listCertifications.
 * Nếu thất bại, chuyển hướng sang trang lỗi với mã lỗi tương ứng.
 */
  getListCertification() {
    this.certificationService.getListCertifications().subscribe({
      next: (value) => {
        this.listCertifications = value?.certifications;
        console.log(CONSOLE_MESSAGES.CERTIFICATION.FETCH_SUCCESS);
      },
      error: () => {
        console.log(CONSOLE_MESSAGES.CERTIFICATION.FETCH_FAILED);
        this.router.navigate(['error'], { state: { errorCode: ERROR_CODES.CERTIFICATION_FETCH_FAILED } });
      },
    });
  }

  /**
  * Điều hướng về màn hình ADM002
  */
  hanleBack() {
    this.router.navigate(['/user/list']);
  }

  handleChangeCertificationId(ev: any) {
    console.log(ev)
  }
}
