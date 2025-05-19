import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Certification } from 'src/app/model/certification.model';
import { Department } from 'src/app/model/department.model';

@Component({
  selector: 'app-adm003',
  templateUrl: './adm003.component.html',
  styleUrls: ['./adm003.component.css']
})
export class ADM003Component {
  listDepartments: Department[] = [];  // Danh sách các phòng ban, được dùng để hiển thị trong dropdown
  listCertifications: Certification[] = [];  // Danh sách các trình độ tiếng nhật, được dùng để hiển thị trong dropdown
  employeeData: any; // Dữ liệu gửi từ ADM002 sang
  employeeId!: number; // Id nhân viên gửi từ ADM002 sang
  navigation: any; // Thông tin điều hướng hiện tại từ Router
  
    /**
   * Constructor khởi tạo component, inject các service cần thiết.
   *
   * @param router Service định tuyến Router để điều hướng khi xảy ra lỗi
   */
    constructor(
      private router: Router,
    ) {
      // Lấy thông tin điều hướng hiện tại từ Router
      this.navigation = this.router.getCurrentNavigation();
    }

    ngOnInit(): void {
      // Lấy completeCode nếu được truyền qua navigation state
      this.employeeId = this.navigation?.extras?.state?.['employeeId'];
      if (isNaN(Number(this.employeeId)) || !this.employeeId) {
        this.router.navigate(['error']);
      }
      
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
