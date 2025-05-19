import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { error } from 'jquery';
import { Certification } from 'src/app/model/certification.model';
import { Department } from 'src/app/model/department.model';
import { EmployeeService } from 'src/app/service/employee.service';

@Component({
  selector: 'app-adm003',
  templateUrl: './adm003.component.html',
  styleUrls: ['./adm003.component.css']
})
export class ADM003Component {
  listDepartments: Department[] = [];  // Danh sách các phòng ban, được dùng để hiển thị trong dropdown
  listCertifications: Certification[] = [];  // Danh sách các trình độ tiếng nhật, được dùng để hiển thị trong dropdown
  employeeData: any; // Dữ liệu thông tin của nhân viên
  navigation: any; // Thông tin điều hướng hiện tại từ Router

  /**
 * Constructor khởi tạo component, inject các service cần thiết.
 *
 * @param router Service định tuyến Router để điều hướng khi xảy ra lỗi
 */
  constructor(
    private router: Router,
    private employeeService: EmployeeService
  ) {
    // Lấy thông tin điều hướng hiện tại từ Router
    this.navigation = this.router.getCurrentNavigation();
  }

  /**
   * Lifecycle hook được gọi khi component được khởi tạo.
   * - Lấy employeeId được truyền từ navigation state (từ ADM002).
   * - Kiểm tra tính hợp lệ của employeeId.
   * - Nếu hợp lệ thì gọi API để lấy dữ liệu nhân viên.
   * - Nếu không hợp lệ thì điều hướng sang trang SystemError.
 */
  ngOnInit(): void {
    // Lấy id nhân viên truyền sang từ ADM002
    const employeeId = this.navigation?.extras?.state?.['employeeId'];
    if (isNaN(Number(employeeId)) || !employeeId) {
      this.router.navigate(['error']);
    }

    this.getEmployeeById(employeeId)
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

  }

  handleDelete() {

  }
}
