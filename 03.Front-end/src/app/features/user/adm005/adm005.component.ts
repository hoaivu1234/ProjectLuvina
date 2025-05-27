/*
  Copyright(C) 2025 Luvina Software Company
  adm005.component.ts 15/5/2025 hoaivd
*/

import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Certification } from 'src/app/model/certification.model';
import { Department } from 'src/app/model/department.model';
import { CertificationService } from 'src/app/service/certification.service';
import { DepartmentService } from 'src/app/service/department.service';
import { EmployeeService } from 'src/app/service/employee.service';
import { MODE, PAGE } from 'src/app/shared/utils/mode-constant';

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
  employeeId!: number;
  mode!: string;

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

  /**
   * Hàm khởi tạo được gọi khi component được khởi chạy.
   *
   * Thực hiện các bước sau:
   * - Đọc dữ liệu xác nhận dataConfirm từ navigation state.
   * - Nếu không có dữ liệu dataConfirm, chuyển hướng tới trang lỗi.
   * - Đọc employeeId từ navigation state để xác định chế độ (add/update).
   * - Nếu employeeId không hợp lệ hoặc không tồn tại: đặt chế độ là MODE_ADD
   * - Nếu employeeId hợp lệ: đặt chế độ là MODE_UPDATE và gọi getEmployeeById() để lấy thông tin nhân viên.
   */
  ngOnInit() {
    // Lấy dataConfirm nếu được truyền qua navigation state
    this.dataConfirm = history.state?.dataConfirm;
    console.log(this.dataConfirm)
    // Nếu không có dataConfirm, chuyển hướng đến trang lỗi
    if (!this.dataConfirm) {
      this.router.navigate(['error']);
    }

    this.employeeId = history.state?.employeeId;  // Lấy employeeId từ navigation state
    if (isNaN(Number(this.employeeId)) || !this.employeeId) {
      this.mode = MODE.MODE_ADD;
    } else {
      this.mode = MODE.MODE_UPDATE;
      this.getEmployeeById(this.employeeId);
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
        console.log(data);
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
   * Điều hướng về màn hình ADM004 với mode
   * - add: thì truyền this.dataConfirm và fromPage
   * - update: thì truyền this.employeeId, this.dataConfirm và fromPage
   */
  hanleBack() {
    if (this.mode == MODE.MODE_ADD) {
      this.router.navigate(['/user/adm004'], { state: { dataReceived: this.dataConfirm, fromPage: PAGE.ADM005 } });
    } else if (this.mode == MODE.MODE_UPDATE) {
      this.router.navigate(['/user/adm004'], { state: { employeeId: this.employeeId, dataReceived: this.dataConfirm, fromPage: PAGE.ADM005 } });
    }
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

    if (clonedData?.departmentName) delete clonedData.departmentName;

    if (this.mode == MODE.MODE_ADD) delete clonedData.employeeId;

    delete clonedData.employeeReLoginPassword;

    if (clonedData.certifications) {
      const hasEmptyCertId = clonedData.certifications.some(
        (cert: any) => cert.certificationId === "" || !cert.certificationId
      );

      if (hasEmptyCertId) {
        delete clonedData.certifications;
      } else {
        clonedData.certifications.forEach((cert: any) => {
          if (cert?.certificationName) delete cert.certificationName;
          cert.startDate = this.datePipe.transform(cert.startDate, 'yyyy/MM/dd');
          cert.endDate = this.datePipe.transform(cert.endDate, 'yyyy/MM/dd');
        });
      }
    }

    return clonedData;
  }

  /**
   * Gửi dữ liệu đăng ký hoặc cập nhật nhân viên lên server.
   *
   * Thực hiện hành động dựa trên chế độ hiện tại (thêm mới hoặc cập nhật):
   * MODE_ADD: Gọi API addEmployee() với dữ liệu đã chuyển đổi.
   * MODE_UPDATE: Gọi API updateEmployee() với dữ liệu đã chuyển đổi.
   *
   * Kết quả xử lý:
   * - Nếu thành công: điều hướng đến màn hình hoàn tất (/user/adm006) và truyền mã xác nhận (completeCode).
   * - Nếu thất bại: điều hướng đến màn hình lỗi (/error) và truyền mã lỗi (errorCode) cùng thông tin trường gây lỗi (fieldError).
   */
  submitForm() {
    // Chuyển đổi dữ liệu form để chuẩn bị gửi lên server
    const clonedData = this.transformDataSubmit();

    // Xác định hành động cần thực hiện: thêm mới hoặc cập nhật nhân viên
    const request$ = this.mode === MODE.MODE_ADD
      ? this.employeeService.addEmployee(clonedData)
      : this.employeeService.updateEmployee(clonedData);
  
    // Subscribe response trả về
    request$.subscribe({
      next: (data: any) => { // Trường hợp request thành công
        console.log(data);
        this.router.navigate(['user/adm006'], {  // Điều hướng đến màn adm006, truyền mã thông báo để hiển thị thông báo
          state: { completeCode: data?.message?.code }
        });
      },
      error: (err) => { // Trường hợp xảy ra lỗi khi gọi API
        console.log(err);
        const errorMessage = err?.error?.message; // Lấy thông tin lỗi từ đối tượng phản hồi
        const errorCode = err?.error?.code; // Lấy mã lỗi chính
  
        if (errorMessage?.code) { // Nếu có mã lỗi trong phần message (BusinessException định dạng đầy đủ)
          this.router.navigate(['error'], { // Điều hướng màn system error, truyền mã lỗi và thông tin trường lỗi đầu tiên
            state: {
              errorCode: errorMessage.code,
              fieldError: errorMessage.params?.[0]
            }
          });
        } else if (errorCode) { // Nếu chỉ có mã lỗi dạng đơn giản (không có message chi tiết)
          this.router.navigate(['error'], { // Điều hướng đến màn system error lỗi chỉ với mã lỗi
            state: { errorCode }
          });
        }
      }
    });
  }
  
}
