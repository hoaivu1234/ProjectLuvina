/*
  Copyright(C) 2025 Luvina Software Company
  adm004.component.ts 10/5/2025 hoaivd
*/

import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Certification } from 'src/app/model/certification.model';
import { Department } from 'src/app/model/department.model';
import { CertificationService } from 'src/app/service/certification.service';
import { DepartmentService } from 'src/app/service/department.service';
import { EmployeeService } from 'src/app/service/employee.service';
import { ValidateFormService } from 'src/app/service/validate-form.service';
import { ERROR_CODES } from 'src/app/shared/utils/error-code.constants';
import { ERROR_MESSAGES } from 'src/app/shared/utils/error-messages.constants';
import { MODE, PAGE } from 'src/app/shared/utils/mode-constant';

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
  @ViewChild('firstInput') firstInput!: ElementRef; // Tham chiếu đến phần tử đầu tiên trong form
  @ViewChild('secondInput') secondInput!: ElementRef; // Tham chiếu đến phần tử thứ 2 trong form

  listDepartments: Department[] = [];  // Danh sách các phòng ban, được dùng để hiển thị trong dropdown
  listCertifications: Certification[] = [];  // Danh sách các trình độ tiếng nhật, được dùng để hiển thị trong dropdown
  employeeForm!: FormGroup; // Form để thao tác với employee
  dataReceived: any; // Dữ liệu dùng để patch vào form, được nhận từ ADM005 chuyển sang hoặc gọi API getEmployeeById với id được truyền từ màn ADM003 sang
  ERROR_MESSAGES = ERROR_MESSAGES; // Danh sách message lỗi constant chứa nội dung message
  ERROR_CODES = ERROR_CODES; // Danh sách mã lỗi  
  employeeId!: number; // Lưu Id của nhân viên được lấy từ router state
  mode!: string; // Chế độ thao tác với form: add hay update
  fromPage!: string; // Kiểm tra xem đã di chuyển từ màn hình nào sang ADM004

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
   * Gọi các hàm để lấy dữ liệu phòng ban, trình độ tiếng nhật, tạo dữ liệu cho form và cập nhật các cài đặt hiện tài cho màn hình.
   */
  ngOnInit(): void {
    this.extractNavigationState(); // Kiểm tra xem di chuyển từ màn hình nào sang và lấy các giá trị được truyền từ router state
    this.initForm(); // Tạo form để thực hiên thao tác add hoặc update nhân viên

    if (this.fromPage === PAGE.ADM003 && this.employeeId) { // Nếu di chuyển từ màn hình ADM003 sang và this.empployeeId có giá trị
      this.getEmployeeById(this.employeeId); // thì gọi hàm để thấy dự liệu của nhân viên theo Id
    }

    if (this.fromPage === PAGE.ADM005 && this.dataReceived) {  // Nếu di chuyển từ màn hình ADM005 sang và this.dataReceived có giá trị
      this.patchValueToForm(); // thì tiến hành patch dữ liệu nhận được vào form
    }

    if (this.mode === MODE.MODE_ADD) { // Nếu mode là Add
      this.setValidatorsForAddMode(); // thì thực hiện set validate cho trường employeeLoginPassword và employeeReLoginPassword
    } else {
      this.setValidatorsForUpdateMode(); // Nếu mode là Update thì cũng thực hiện set validate cho trường employeeLoginPassword và employeeReLoginPassword
    }

    this.getListDepartment(); // Lấy danh sách phòng ban
    this.getListCertification(); // Lấy danh sách chứng chỉ
  }

  /**
   * Trích xuất dữ liệu trạng thái điều hướng từ `history.state` để xác định
   * di chuyển từ màn hình nào (fromPage), ID nhân viên (employeeId), và dữ liệu nhận được (dataReceived).
   *
   * Dựa vào `fromPage`, phương thức thiết lập chế độ hoạt động của form là Thêm mới (ADD) hoặc Cập nhật (UPDATE):
   * - Nếu đến từ màn ADM002 -> chế độ Thêm mới.
   * - Nếu đến từ màn ADM003 -> chế độ Cập nhật.
   * - Nếu đến từ màn ADM005:
   *   - Có `employeeId` hợp lệ -> Cập nhật.
   *   - Ngược lại -> Thêm mới.
   *
   * Nếu không xác định được `fromPage`, mặc định thiết lập chế độ là Thêm mới.
   */
  extractNavigationState() {
    const state = history.state; // Lấy state từ router
    this.fromPage = state?.fromPage; // Lấy giá trị fromPage từ state
    this.employeeId = state?.employeeId; // Lấy giá trị employee từ state
    this.dataReceived = state?.dataReceived; // Lấy giá trị dataReceived từ state

    switch (this.fromPage) {  // Xác định hành động dựa trên màn hình trước đó (fromPage)
      case PAGE.ADM002:
        this.mode = MODE.MODE_ADD; // Nếu đến từ màn ADM002, đặt chế độ là thêm mới nhân viên
        break;

      case PAGE.ADM003:
        this.mode = MODE.MODE_UPDATE; // Nếu đến từ màn ADM003, đặt chế độ là cập nhật nhân viên
        break;

      case PAGE.ADM005: // Nếu đến từ ADM005:
        this.mode = (this.employeeId && !isNaN(Number(this.employeeId)))
          ? MODE.MODE_UPDATE  // Nếu có employeeId hợp lệ => chế độ cập nhật
          : MODE.MODE_ADD; // Ngược lại => chế độ thêm mới
        break;

      default:
        this.mode = MODE.MODE_ADD;   // Nếu không xác định được fromPage, mặc định là chế độ thêm mới
        break;
    }
  }

  /**
   * Focus vào hạng mục đầu tiên khi vào màn hình nếu là mode add
   * Nếu là mode update thì Focus vào hạng mục thứ hai vì employeeLoginId bị disable
   */
  ngAfterViewInit(): void {
    this.mode == MODE.MODE_ADD ? this.firstInput.nativeElement.focus() : this.secondInput.nativeElement.focus();
  }

  /**
   * Patch dữ liệu cho form group
   */
  patchValueToForm() {
    this.employeeForm.patchValue(this.dataReceived); // Patch dữ liệu cho form control
    this.patchValueForCertifications(); // Gọi hàm patch dữ liệu cho form array
  }

  /**
   * Patch dữ liệu cho form array chứa dữ liệu chứng chỉ của nhân viên
   */
  patchValueForCertifications(): void {
    if (this.dataReceived?.certifications) { // Nếu certifications có dữ liệu thì mới patch
      const certificationsArray = this.certifications; // Lấy formArray từ formGroup

      // Xóa các certifications cũ
      while (certificationsArray.length > 0) { // Khi mà formArray vẫn có giá trị
        certificationsArray.removeAt(0); // thì xóa group đầu tiên vì nhân viên chỉ lưu 1 chứng chỉ
      }

      // Thêm certifications mới
      this.dataReceived.certifications.forEach((cert: any) => { // Duyệt qua danh sách nhân viên của data nhận được
        certificationsArray.push( // Truyền vào formArray
          this.fb.group({ // 1 formGroup chứa các thông tin
            certificationId: [cert.certificationId], // Id chứng chỉ
            startDate: [{ value: cert.startDate, disabled: false }],  // Ngày bắt đầu, bỏ disabled để thao tác tiếp với certifications mà không cần phải chọn lại giá trị
            endDate: [{ value: cert.endDate, disabled: false }], // Ngày kết thúc, bỏ disabled để thao tác tiếp với certifications mà không cần phải chọn lại giá trị
            score: [{ value: cert.score, disabled: false }], // Điểm số, bỏ disabled để thao tác tiếp với certifications mà không cần phải chọn lại giá trị
          })
        );
      });
    }
  }

  /**
   * Thiết lập các validator cho Form khi ở chế độ thêm mới.
   * 
   * Các trường cần kiểm tra:
   * - employeeLoginPassword: bắt buộc nhập, độ dài từ 8 đến 50 ký tự.
   * - employeeReLoginPassword: bắt buộc nhập.
   *
   * Sau khi thiết lập validator, gọi updateValueAndValidity() để cập nhật trạng thái FormControl.
   */
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

  /**
   * Thiết lập các validator cho Form khi ở chế độ cập nhật (Update mode).
   * 
   * Các trường được thiết lập validator:
   * - employeeLoginPassword: không bắt buộc, nhưng nếu nhập thì phải có độ dài từ 8 đến 50 ký tự.
   * - employeeReLoginPassword: không yêu cầu nhập, do người dùng có thể không muốn thay đổi mật khẩu.
   *
   * Sau khi thiết lập validator, gọi updateValueAndValidity() để cập nhật trạng thái FormControl.
   */
  setValidatorsForUpdateMode(): void {
    this.employeeForm.get('employeeLoginPassword')?.setValidators([
      this.validationService.checkLengthRangePassword(8, 50) // Set trạng thái employeeLoginPassword phải validate trường hợp độ dài từ 8 đến 50 ký tự.
    ]);
    this.employeeForm.get('employeeReLoginPassword')?.clearValidators(); // không cần required

    this.employeeForm.updateValueAndValidity(); // Cập nhật trạng thái FormControl.
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
      employeeEmail: [null, [Validators.required, Validators.maxLength(125), this.validationService.checkEnglishHalfSize(), this.validationService.checkValidateEmail()]],
      employeeTelephone: [null, [Validators.required, Validators.maxLength(50), this.validationService.checkEnglishHalfSize()]],
      employeeLoginPassword: [null],
      employeeReLoginPassword: [null],
      certifications: this.fb.array([]),
    }, {
      // Validators cấp FormGroup dùng để kiểm tra 2 trường mật khẩu (`employeeLoginPassword`) và
      // xác nhận mật khẩu (`employeeReLoginPassword`) có giống nhau hay không.
      validators: this.validationService.checkPasswordMatch(this.mode)
    });

    this.addCertification();

    // Cập nhật lại toàn bộ validation của form mỗi khi người dùng thay đổi giá trị của trường employeeLoginPassword.
    // Vì đây là validator cấp FormGroup, Angular không tự động re-evaluate form khi chỉ một field con thay đổi.
    // Vì vậy, cần tự kích hoạt lại việc validate toàn form mỗi khi người dùng thay đổi
    this.employeeForm.get('employeeLoginPassword')?.valueChanges.subscribe(() => {
      this.employeeForm.updateValueAndValidity({ onlySelf: false }); // onlySelf:  không chỉ cập nhật riêng field đó, mà cập nhật lại toàn bộ form, bao gồm cả các validator cấp FormGroup
    });

    // Cập nhật lại toàn bộ validation của form mỗi khi người dùng thay đổi giá trị của trường employeeReLoginPassword.
    this.employeeForm.get('employeeReLoginPassword')?.valueChanges.subscribe(() => {
      this.employeeForm.updateValueAndValidity({ onlySelf: false }); // onlySelf:  không chỉ cập nhật riêng field đó, mà cập nhật lại toàn bộ form, bao gồm cả các validator cấp FormGroup
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
      validators: this.validationService.checkLargerThanStartDate() // Validators dùng để kiểm tra `endDate` phải lớn hơn `startDate`.
    });

    // Cập nhật lại toàn bộ validation của formArray mỗi khi người dùng thay đổi giá trị của trường startDate.
    this.certifications.push(certificationGroup);
    this.certifications.get('startDate')?.valueChanges.subscribe(() => {
      this.certifications.updateValueAndValidity({ onlySelf: false });
    });

    // Cập nhật lại toàn bộ validation của formArray mỗi khi người dùng thay đổi giá trị của trường endDate.
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
    const certGroup = this.certifications.at(index) as FormGroup; // Lấy formArray của phần tử thứ index
    return !!certGroup.get('certificationId')?.value; // Nếu formControl certificationId có giá trị thì trả về true, ngược lại là false
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
    const departmentId = item.target.value; // Lấy giá trị departmentId đã chọn từ event
    if (departmentId) { // Nếu departmentId có giá trị
      // Tìm phần tử department trong listDepartments có departmentId bằng với departmentId từ event
      const department = this.listDepartments.find((item: any) => item.departmentId == departmentId);
      this.employeeForm.get('departmentName')?.setValue(department?.departmentName); // set giá trị cho formControl departmentName bằng departmentName của phần tử đó
    } else {
      this.employeeForm.get('departmentName')?.setValue(null); // Nếu departmentId không có giá trị thì set giá trị cho formControl departmentName bằng null
    }
  }

  /**
   * Xử lý khi giá trị của dropdown `certificationId` thay đổi
   * - Nếu chọn rỗng: disable, reset và clear validators các trường liên quan
   * - Nếu chọn có giá trị: enable và thêm required validator vào các trường liên quan
   * @param index - vị trí chứng chỉ trong mảng
   */
  handleChangeCertificationId(index: number): void {
    const certGroup = this.certifications.at(index) as FormGroup; // Lấy formArray của phần tử thứ index
    const selectedId = certGroup.get('certificationId')?.value; // Lấy giá trị của formControl certificationId

    const controlsToUpdate = [ // Tạo object chứa tên các control
      'certificationName',
      'startDate',
      'endDate',
      'score',
    ];

    if (!selectedId) { // Nếu selectedId không có giá trị (chọn lại về rỗng)
      controlsToUpdate.forEach(controlName => { // Duyệt qua danh sách tên control 
        const control = certGroup.get(controlName); // Lấy formControl tương ứng với tên control
        control?.disable(); // disable formControl
        control?.reset(); // reset giá trị formControl
        control?.clearValidators(); // xóa các validator của formControl
        control?.updateValueAndValidity(); // Cập nhật lại toàn bộ validation của formArray
      });
    } else { // Nếu selectedId có giá trị (chọn khác rỗng)
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
        this.dataReceived = data;
        this.patchValueToForm();
      },
      error: (err) => {
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
      },
      error: () => {
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
      },
      error: () => {
        this.router.navigate(['error'], { state: { errorCode: ERROR_CODES.CERTIFICATION_FETCH_FAILED } });
      },
    });
  }

  /**
   * Điều hướng về màn hình ADM002 
   */
  hanleBack() {
    if (this.mode == MODE.MODE_ADD) {
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
  handleConfirm(): void {
    if (this.employeeForm.valid) {
      const state: any = {
        dataConfirm: this.employeeForm.value
      };

      if (this.mode === MODE.MODE_UPDATE) {
        state.employeeId = this.employeeId;
      }

      this.router.navigate(['/user/adm005'], { state });
    } else {
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
