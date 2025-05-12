/*
  Copyright(C) 2025 Luvina Software Company
  adm004.component.ts 10/5/2025 hoaivd
*/

import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
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
    this.addCertification();
    this.getListDepartment();
    this.getListCertification();
  }

  /**
    * Khởi tạo form chính với các trường thông tin nhân viên và danh sách chứng chỉ (certifications)
  */
  initForm() {
    this.employeeForm = this.fb.group({
      employeeLoginId: [null, Validators.required],
      departmentId: [null, Validators.required],
      employeeNameKana: [null, Validators.required],
      employeeName: [null, Validators.required],
      employeeBirthDate: [null, Validators.required],
      employeeEmail: [null, Validators.required],
      employeeTelephone: [null, Validators.required],
      employeeLoginPassword: [null, Validators.required],
      employeeReLoginPassword: [null, Validators.required],
      certifications: this.fb.array([]),
    });
  }

  /**
    * Thêm một chứng chỉ mới vào form array `certifications`, 
    * với các trường liên quan được khởi tạo và disable mặc định
  */
  addCertification(): void {
    const certificationGroup = this.fb.group({
      certificationId: [null],
      certificationStartDate: [{ value: null, disabled: true }],
      certificationEndDate: [{ value: null, disabled: true }],
      employeeCertificationScore: [{ value: null, disabled: true }],
    });

    this.certifications.push(certificationGroup);
  }

  /**
    * Getter để lấy FormArray chứa danh sách chứng chỉ từ form chính
  */
  get certifications(): FormArray {
    return this.employeeForm.get('certifications') as FormArray;
  }

  /**
    * Kiểm tra xem chứng chỉ tại vị trí `index` đã được chọn hay chưa (dựa trên certificationId)
    * @param index - vị trí của chứng chỉ trong mảng
    * @returns true nếu đã chọn chứng chỉ, ngược lại là false
  */
  isCertSelected(index: number): boolean {
    const certGroup = this.certifications.at(index) as FormGroup;
    return !!certGroup.get('certificationId')?.value;
  }

  /**
    * Xác định xem các trường ngày hiệu lực, ngày hết hạn, điểm có bắt buộc hay không
    * Dựa trên việc đã chọn chứng chỉ tại vị trí `index`
    * @param index - vị trí chứng chỉ
    * @returns true nếu cần required, ngược lại là false
  */
  isRequired(index: number): boolean {
    return this.isCertSelected(index);
  }

  /**
    * Xử lý khi giá trị của dropdown `certificationId` thay đổi
    * - Nếu chọn rỗng: disable, reset và clear validators các trường liên quan
    * - Nếu chọn có giá trị: enable và thêm required validator vào các trường liên quan
    * @param index - vị trí chứng chỉ trong mảng
  */
  handleChangeCertificationId(index: number): void {
    const certGroup = this.certifications.at(index) as FormGroup;
    const selectedId = certGroup.get('certificationId')?.value;

    const controlsToUpdate = [
      'certificationStartDate',
      'certificationEndDate',
      'employeeCertificationScore',
    ];

    if (!selectedId) {
      // Khi chọn lại về rỗng
      controlsToUpdate.forEach(controlName => {
        const control = certGroup.get(controlName);
        control?.disable();
        control?.reset();
        control?.clearValidators();
        control?.updateValueAndValidity();
      });
    } else {
      // Khi chọn một chứng chỉ hợp lệ
      controlsToUpdate.forEach(controlName => {
        const control = certGroup.get(controlName);
        control?.enable();
        control?.setValidators(Validators.required);
        control?.updateValueAndValidity();
      });
    }
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
}
