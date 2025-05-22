/*
  Copyright(C) 2025 Luvina Software Company
  adm004.component.ts 10/5/2025 hoaivd
*/

import { Component, ElementRef, ViewChild } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Certification } from 'src/app/model/certification.model';
import { Department } from 'src/app/model/department.model';
import { CertificationService } from 'src/app/service/certification.service';
import { DepartmentService } from 'src/app/service/department.service';
import { EmployeeService } from 'src/app/service/employee.service';
import { ValidateFormService } from 'src/app/service/validate-form.service';
import { CONSOLE_MESSAGES } from 'src/app/shared/utils/console-message.constants';
import { ERROR_CODES } from 'src/app/shared/utils/error-code.constants';
import { ERROR_MESSAGES } from 'src/app/shared/utils/error-messages.constants';

@Component({
  selector: 'app-adm004',
  templateUrl: './adm004.component.html',
  styleUrls: ['./adm004.component.css']
})

/**
 * Màn hình để thao tác với form thêm mới và edit employee
 * Có các chức năng thực hiện validate các hạng mục trước khi chuyển dữ liệu sang màn ADM005
 * Nếu có lỗi xảy ra trong quá trình thực hiện thì điều hướng đến màn System Error
 * 
 * @author hoaivd
 */

export class ADM004Component {
  @ViewChild('firstInput') firstInput!: ElementRef;

  listDepartments: Department[] = [];  // Danh sách các phòng ban, được dùng để hiển thị trong dropdown
  listCertifications: Certification[] = [];  // Danh sách các trình độ tiếng nhật, được dùng để hiển thị trong dropdown
  generalErrorMessage: string = '';  // Lỗi chung của màn hình như gọi API lỗi, server trả về lỗi
  employeeForm!: FormGroup; // Form để thao tác với employee
  dataConfirmBack: any; // Dữ liệu từ ADM005 back về
  ERROR_MESSAGES = ERROR_MESSAGES;
  ERROR_CODES = ERROR_CODES;
  employeeId!: number;
  mode!: string;

  /**
   * Constructor khởi tạo component, inject các service cần thiết.
   *
   * @param departmentService Service lấy dữ liệu phòng ban
   * @param certificationService Service lấy dữ liệu trình độ tiếng nhật
   * @param router Service định tuyến Router để điều hướng khi xảy ra lỗi
   * @param fb FormBuilder dùng để thao tác với form
   * @param validationService Service chứa các hàm dùng để validate form
   */
  constructor(
    public departmentService: DepartmentService,
    public certificationService: CertificationService,
    private router: Router,
    private fb: FormBuilder,
    protected validationService: ValidateFormService,
    private employeeService: EmployeeService,
  ) { }

  /**
   * Lifecycle hook khởi chạy khi component được khởi tạo.
   * Gọi các hàm để lấy dữ liệu phòng ban, trình độ tiếng nhật và tạo dữ liệu cho form.
   */
  ngOnInit(): void {
    this.getDataNavigate();

    this.initForm();
    if (this.dataConfirmBack) {
      this.patchValueBack(); // Nếu có dữ liệu back về thì patch vào form
    }

    if (this.mode === 'add') {
      this.setValidatorsForAddMode();
    } else {
      this.setValidatorsForUpdateMode();
    }

    this.getListDepartment();
    this.getListCertification();
  }

  getDataNavigate() {
    this.employeeId = history.state?.['employeeId'];

    if (isNaN(Number(this.employeeId)) || !this.employeeId) {
      this.mode = 'add';
    } else {
      this.mode = 'edit';
      this.getEmployeeById(this.employeeId);
    }
  }

  // Focus vào hạng mục đầu tiên khi vào màn hình
  ngAfterViewInit(): void {
    setTimeout(() => {
      this.firstInput.nativeElement.focus();
    });
  }

  /**
 * Patch dữ liệu cho form group
 */
  patchValueBack() {
    this.employeeForm.patchValue(this.dataConfirmBack); // Patch dữ liệu cho form control
    this.patchValueForCertifications(); // Gọi hàm patch dữ liệu cho form array
  }

  /**
 * Patch dữ liệu cho form array
 */
  patchValueForCertifications(): void {
    if (this.dataConfirmBack?.certifications) { // Nếu certifications có dữ liệu thì mới patch
      const certificationsArray = this.certifications;

      // Xóa các certifications cũ
      while (certificationsArray.length > 0) {
        certificationsArray.removeAt(0);
      }

      // Thêm certifications mới
      this.dataConfirmBack.certifications.forEach((cert: any) => {
        certificationsArray.push(
          this.fb.group({
            certificationId: [cert.certificationId],
            startDate: [{ value: cert.startDate, disabled: false }],  // Bỏ disabled để thao tác tiếp với certifications mà không cần phải chọn lại giá trị
            endDate: [{ value: cert.endDate, disabled: false }],
            score: [{ value: cert.score, disabled: false }],
          })
        );
      });
    }
  }

  setValidatorsForAddMode(): void {
    this.employeeForm.get('employeeLoginPassword')?.setValidators([
      Validators.required,
      this.validationService.checkLengthRangePassword(8, 50)
    ]);
    this.employeeForm.get('employeeReLoginPassword')?.setValidators([
      Validators.required
    ]);
    this.employeeForm.updateValueAndValidity();
  }

  setValidatorsForUpdateMode(): void {
    this.employeeForm.get('employeeLoginPassword')?.setValidators([
      this.validationService.checkLengthRangePassword(8, 50)
    ]);
    this.employeeForm.get('employeeReLoginPassword')?.clearValidators(); // không cần required

    this.employeeForm.updateValueAndValidity();
  }


  /**
    * Khởi tạo form chính với các trường thông tin nhân viên và danh sách chứng chỉ (certifications)
  */
  initForm() {
    this.employeeForm = this.fb.group({
      employeeId: [null],
      employeeLoginId: [null, [Validators.required, Validators.maxLength(50), this.validationService.checkValidateLoginId()]],
      departmentId: [null, Validators.required],
      departmentName: [null],
      employeeNameKana: [null, [Validators.required, Validators.maxLength(125), this.validationService.checkKanaHalfSize()]],
      employeeName: [null, [Validators.required, Validators.maxLength(125)]],
      employeeBirthDate: [null, Validators.required],
      employeeEmail: [null, [Validators.required, Validators.maxLength(125), this.validationService.checkValidateEmail(), this.validationService.checkEnglishHalfSize()]],
      employeeTelephone: [null, [Validators.required, Validators.maxLength(50), this.validationService.checkEnglishHalfSize()]],
      employeeLoginPassword: [null],
      employeeReLoginPassword: [null],
      certifications: this.fb.array([]),
    }, {
      validators: this.validationService.checkPasswordMatch(this.mode)
    });

    this.addCertification();

    // update lại validatior của employeeLoginPassword khi có thay đổi giá trị
    this.employeeForm.get('employeeLoginPassword')?.valueChanges.subscribe(() => {
      this.employeeForm.updateValueAndValidity({ onlySelf: false });
    });

    // update lại validatior của employeeReLoginPassword khi có thay đổi giá trị
    this.employeeForm.get('employeeReLoginPassword')?.valueChanges.subscribe(() => {
      this.employeeForm.updateValueAndValidity({ onlySelf: false });
    });
  }

  /**
    * Thêm một chứng chỉ mới vào form array `certifications`, 
    * với các trường liên quan được khởi tạo và disable mặc định
  */
  addCertification(): void {
    const certificationGroup = this.fb.group({
      certificationId: [null],
      certificationName: [null],
      startDate: [{ value: null, disabled: true }],
      endDate: [{ value: null, disabled: true }],
      score: [{ value: null, disabled: true }],
    }, {
      validators: this.validationService.checkLargerThanStartDate()
    });

    // update lại validatior của startDate trong certifications khi có thay đổi giá trị
    this.certifications.push(certificationGroup);
    this.certifications.get('startDate')?.valueChanges.subscribe(() => {
      this.certifications.updateValueAndValidity({ onlySelf: false });
    });

    // update lại validatior của endDate trong certifications khi có thay đổi giá trị
    this.certifications.get('endDate')?.valueChanges.subscribe(() => {
      this.certifications.updateValueAndValidity({ onlySelf: false });
    });
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
  * Xử lý sự kiện khi người dùng chọn hoặc bỏ chọn phòng ban.
  * Nếu có giá trị `item`, tìm thông tin phòng ban tương ứng và cập nhật `departmentName` trong form.
  * Nếu không có giá trị `item`, đặt `departmentName` về `null`.
  * Vì this.listDepartments chứa dữ liệu department đã được phân trang nên có thể duyệt qua this.listDepartments mà không ảnh hưởng đến hiệu năng
  * Nếu không phân trang thì nên lưu value ở html là cả object Department thay vì chỉ lưu mỗi departmentId để không phải tìm departmentName chuyển qua ADM005
  *
  * @param item Đối tượng phòng ban được chọn từ dropdown hoặc null khi bỏ chọn.
  */
  handleChangeDepartmentId(item: any) {
    if (item) {
      const department = this.listDepartments.find((item: any) => item.departmentId);
      this.employeeForm.get('departmentName')?.setValue(department?.departmentName);
    } else {
      this.employeeForm.get('departmentName')?.setValue(null);
    }
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
      'certificationName',
      'startDate',
      'endDate',
      'score',
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

      const certification = this.listCertifications.find((item: any) => item.certificationId == selectedId);
      // Khi chọn một chứng chỉ hợp lệ
      controlsToUpdate.forEach(controlName => {
        const control = certGroup.get(controlName);
        control?.enable();
        if (controlName != 'certificationName') control?.setValidators(Validators.required);
        if (controlName === 'score') {
          control?.addValidators(this.validationService.checkNumberHalfSize()); // Kiểm tra nếu là score thì thêm validator kiểm tra số half size
        }

        if (controlName === 'certificationName') {
          control?.setValue(certification?.certificationName);
        } else {
          control?.setValue(null);
        }
        control?.updateValueAndValidity();
      });
    }
  }

  /**
 * Gọi API backend để lấy thông tin chi tiết của nhân viên theo ID.
 *
 * @param {number} employeeId - ID của nhân viên cần truy vấn.
*/
  getEmployeeById(employeeId: number) {
    // Gọi service để lấy thông tin nhân viên theo ID.
    this.employeeService.getEmployeeById(employeeId).subscribe({
      next: (data) => {
        // Xử lý dữ liệu để chỉ giữ chứng chỉ cao nhất.
        this.dataConfirmBack = data;

        // Đợi init xong employeeForm thì mới patch
        if (this.employeeForm) {
          this.patchValueBack();
        }
      },
      error: (err) => {
        console.log(err);
        // Nếu xảy ra lỗi, điều hướng đến SystemError và truyền mã lỗi.
        this.router.navigate(['error'], {
          state: { errorCode: err?.error?.message?.code }
        });
      }
    })
  }

  /**
   * Gọi API để lấy danh sách phòng ban.
   * Nếu thành công, gán dữ liệu vào listDepartments.
   * Nếu thất bại, chuyển hướng sang trang lỗi với mã lỗi tương ứng.
   */
  getListDepartment() {
    this.departmentService.getListDepartment().subscribe({
      next: (value) => {
        this.listDepartments = value?.departments;  // gán giá trị cho listDepartments
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
 * Nếu thất bại, chuyển hướng sang trang lỗi với mã lỗi   tương ứng.
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
    if (this.mode == 'add') {
      this.router.navigate(['/user/list']);
    } else {
      this.router.navigate(['/user/adm003'], { state: { employeeId: this.employeeId } });
    }
  }

  /**
   * Điều hướng về màn hình ADM005 (xác nhận thông tin người dùng)
   * Nếu form hợp lệ, chuyển sang trang xác nhận và truyền dữ liệu form.
   * Nếu form không hợp lệ, đánh dấu các trường chưa hợp lệ để hiển thị thông báo lỗi.
 */
  handleConfirm() {
    if (this.employeeForm.valid) {
      this.router.navigate(['/user/adm005'], { state: { dataConfirm: this.employeeForm.value } });
    } else {
      // Đánh dấu tất cả các control là touched và dirty
      this.markFormGroupTouched(this.employeeForm);
    }
  }

  /**
   * Đệ quy đánh dấu tất cả các control trong FormGroup là 'touched' và 'dirty'.
   * @param formGroup - FormGroup cần đánh dấu
 */
  private markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      control.markAsDirty();
      if ((control as any).controls) {
        this.markFormGroupTouched(control as FormGroup); // xử lý form lồng nhau
      }
    });
  }
}
