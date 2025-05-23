/*
  Copyright(C) 2025 Luvina Software Company
  adm003.component.ts 20/5/2025 hoaivd
*/

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Certification } from 'src/app/model/certification.model';
import { Department } from 'src/app/model/department.model';
import { EmployeeService } from 'src/app/service/employee.service';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { TemplateRef } from '@angular/core';
import { MSG } from 'src/app/shared/utils/messages.constants';
import { EmployeeFormControls } from 'src/app/shared/utils/form-control-names.constant';
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

  listDepartments: Department[] = [];  // Danh sách các phòng ban, được dùng để hiển thị trong dropdown
  listCertifications: Certification[] = [];  // Danh sách các trình độ tiếng nhật, được dùng để hiển thị trong dropdown
  employeeData: any; // Dữ liệu thông tin của nhân viên
  employeeId!: number; // Id của nhân viên được chuyển từ ADM002 sang

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
    // Lấy id nhân viên truyền sang từ ADM002
    this.employeeId = history.state?.employeeId;
    if (isNaN(Number(this.employeeId)) || !this.employeeId) {
      this.router.navigate(['error']);
    }

    this.getEmployeeById(this.employeeId);
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
        this.transformData(data);
        this.employeeData = data;
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

  handleEdit() {
    this.router.navigate(['/user/adm004'], {
      state: { employeeId: this.employeeId, fromPage: PAGE.ADM003 }
    });
  }

  /**
    * Xử lý hành động xóa nhân viên.
    * 
    * - Thêm class `closing` để tạo hiệu ứng đóng modal.
    * - Ẩn modal thông qua `BsModalRef`.
    * - Sau 400ms (chờ hiệu ứng đóng), gọi API để xóa nhân viên theo `employeeId`.
    * - Nếu thành công, điều hướng đến màn ADM006 với mã `completeCode`.
    * - Nếu thất bại, điều hướng đến màn SystemError với mã `errorCode`.
  */
  handleDelete() {
    // Thêm hiệu ứng đóng trước
    const modal = document.querySelector('.modal-content');
    modal?.classList.add('closing');
    this.modalRef?.hide();

    setTimeout(() => {
      // Gọi API sau khi modal ẩn
      this.employeeService.deleteEmployeeById(this.employeeId).subscribe({
        next: (data) => {
          this.router.navigate(['user/adm006'], {
            state: { completeCode: data?.message?.code }
          });
        },
        error: (err) => {
          this.router.navigate(['error'], {
            state: { errorCode: err?.error?.message?.code }
          });
        }
      });
    }, 400);
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
