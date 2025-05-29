/*
  Copyright(C) 2025 Luvina Software Company
  adm003.component.ts 20/5/2025 hoaivd
*/

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { EmployeeService } from 'src/app/service/employee.service';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { TemplateRef } from '@angular/core';
import { MSG } from 'src/app/shared/utils/messages.constants';
import { PAGE } from 'src/app/shared/utils/mode-constant';

@Component({
  selector: 'app-adm003',
  templateUrl: './adm003.component.html',
  styleUrls: ['./adm003.component.css']
})

/**
 * Màn hình để xem chi tiết thông tin employee
 * Có các chức năng như chuyển sang mode edit và xóa bản ghi employee
 * Nếu có lỗi xảy ra trong quá trình thực hiện thì điều hướng đến màn System Error
 * 
 * @author hoaivd
 */
export class ADM003Component {
  modalRef!: BsModalRef; // Tham chiếu đến modal Bootstrap đang được hiển thị
  MSG = MSG; // Hằng số chứa các message hiển thị
  employeeData: any; // Dữ liệu thông tin của nhân viên
  employeeId!: number; // Id của nhân viên được chuyển từ ADM002 sang
  isDeleted: boolean = false; // Biến để kiếm soát trạng thái khi người dùng click liên tục button [OK] để xóa nhân viên

  /**
   * Constructor khởi tạo component, inject các service cần thiết.
   *
   * @param router Service định tuyến Router để điều hướng khi xảy ra lỗi
   * @param employeeService Service dùng để thực hiện xóa employee trong cơ sở dữ liệu
   * @param modalService Service dùng để thao tác với modal
   */
  constructor(
    private router: Router,
    private employeeService: EmployeeService,
    private modalService: BsModalService
  ) { }

  /**
   * Lifecycle hook được gọi khi component được khởi tạo.
   * - Lấy employeeId được truyền từ navigation state (từ ADM002).
   * - Kiểm tra tính hợp lệ của employeeId.
   * - Nếu hợp lệ thì gọi API để lấy dữ liệu nhân viên.
   * - Nếu không hợp lệ thì điều hướng sang trang SystemError.
   */
  ngOnInit(): void {
    this.employeeId = history.state?.employeeId;  // Lấy id nhân viên truyền sang từ ADM002
    if (isNaN(Number(this.employeeId)) || !this.employeeId) { // Nếu id rỗng hoặc không phải số
      this.router.navigate(['error']); // thì điều hướng đến màn hình System Error
    }

    this.getEmployeeById(this.employeeId); // Gọi API để lấy thông tin chi tiết của nhân viên theo ID được truyền sang. 
  }

  /**
   * Gọi API backend để lấy thông tin chi tiết của nhân viên theo ID.
   * Nếu thành công thì biến đổi dữ liệu và lấy dữ liệu nhân viên
   * Nếu thất bại thì điều hướng đến màn hình System Error với mã lỗi được trả về từ response
   *
   * @param {number} employeeId - ID của nhân viên cần truy vấn.
   */
  getEmployeeById(employeeId: number) {
    this.employeeService.getEmployeeById(employeeId).subscribe({  // Gọi service để lấy thông tin nhân viên theo ID.
      next: (data) => { // Nếu thành công
        this.transformData(data); // Xử lý dữ liệu để chỉ giữ chứng chỉ cao nhất.
        this.employeeData = data; // Lấy dữ liệu chi tiết nhân viên sau khi đã biến đổi.
      },
      error: (err) => { // Nếu thất bại
        this.router.navigate(['error'], { // Điều hướng đến System Error và truyền mã lỗi tương ứng từ response trả về.
          state: { errorCode: err?.error?.message?.code }
        });
      }
    })
  }

  /**
   * Xử lý dữ liệu nhân viên để lọc ra chứng chỉ có cấp độ cao nhất
   *
   * @param {any} data - Dữ liệu trả về từ backend bao gồm danh sách chứng chỉ.
   */
  transformData(data: any) {
    // Nếu không có chứng chỉ, không cần xử lý gì thêm.
    if (!data.certifications || data.certifications.length === 0) return;

    // Tìm chứng chỉ có ID nhỏ nhất (tương ứng với cấp độ cao nhất).
    const highestCertification = data.certifications.reduce((minCert: any, currentCert: any) =>
      currentCert.certificationId < minCert.certificationId ? currentCert : minCert
    );

    // Gán lại mảng certifications chỉ chứa chứng chỉ cao nhất.
    data.certifications = [highestCertification];
  }

  /**
   * Điều hướng về màn hình ADM002
   */
  hanleBack() {
    this.router.navigate(['/user/list']);
  }

  /**
   * Điều hướng sang màn hình ADM004 và truyền giá trị cho employeeId và fromPage để màn ADM004 xử lý
   */
  handleEdit() {
    this.router.navigate(['/user/adm004'], { // Điều hướng đến màn hình ADM004
      state: { employeeId: this.employeeId, fromPage: PAGE.ADM003 } // employeeId là Id của nhân viên, fromPage để biết di chuyển từ màn hình nào sang ADM004
    });
  }

  /**
   * Xử lý hành động xóa nhân viên.
   * Nếu thành công thì điều hướng đến màn hình ADM006
   * Nếu thất bại thì điều hướng đến màn hình System Error
   * 
   * - Thêm class `closing` để tạo hiệu ứng đóng modal.
   * - Ẩn modal thông qua `BsModalRef`.
   * - Sau 400ms (chờ hiệu ứng đóng), gọi API để xóa nhân viên theo `employeeId`.
   * - Nếu thành công, điều hướng đến màn ADM006 với mã `completeCode`.
   * - Nếu thất bại, điều hướng đến màn SystemError với mã `errorCode`.
   */
  handleDelete() {
    if (this.isDeleted) return; // Nếu đã xóa trước đó rồi thì không làm gì cả để trách gọi API xóa nhiều lần liên tục
    this.isDeleted = true; // Cập nhật trạng thái bản ghi đã được thao tác xóa

    // Thêm hiệu ứng đóng trước
    const modal = document.querySelector('.modal-content');
    modal?.classList.add('closing');
    this.modalRef?.hide(); // Ẩn modal

    setTimeout(() => {
      // Gọi API sau khi modal ẩn
      this.employeeService.deleteEmployeeById(this.employeeId).subscribe({
        next: (data) => { // Nếu thành công
          this.router.navigate(['user/adm006'], { // Điều hướng đến màn ADM006
            state: { completeCode: data?.message?.code } // Truyền giá trị mã thông báo được trả về từ response để hiển thị nội dung thông báo ở màn ADM006
          });
        },
        error: (err) => { // Nếu thất bại
          this.router.navigate(['error'], { // Điều hướng đến màn System Error 
            state: { errorCode: err?.error?.message?.code } // Truyền giá trị mã lỗi được trả về từ response để hiển thị nội dung lỗi ở màn System Error 
          });
        }
      });
    }, 400); // Gọi sau 400 ms để modal đóng
  }

  /**
   * Mở modal xác nhận xóa nhân viên.
   * 
   * @param template Template modal được truyền vào để hiển thị.
   */
  openConfirmModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
  }

}
