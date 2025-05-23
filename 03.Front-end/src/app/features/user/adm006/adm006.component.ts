/*
  Copyright(C) 2025 Luvina Software Company
  adm006.component.ts 15/5/2025 hoaivd
*/

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MSG } from 'src/app/shared/utils/messages.constants';

@Component({
  selector: 'app-adm006',
  templateUrl: './adm006.component.html',
  styleUrls: ['./adm006.component.css']
})

/**
 * Màn hình để hiển thị thông báo complete khi thực hiện thêm sửa xóa nhân viên
 * 
 * @author hoaivd
 */
export class ADM006Component {
  completeMessage: string = '';   // Thông điệp lỗi tương ứng với completeCode
  completeCode: string = '' // Mã thông báo được lấy từ navigation state

  /**
 * Constructor khởi tạo component, inject các service cần thiết.
 *
 * @param router Service định tuyến Router để điều hướng khi xảy ra thông báo
 */
  constructor(
    private router: Router,
  ) { }

  /**
* Lifecycle hook khởi tạo component.
* Lấy mã thông báo từ navigation state và tìm thông điệp thông báo tương ứng.
* Nếu không có mã thông báo, sẽ sử dụng mã thông báo hệ thống mặc định.
*
* @return void
*/
  ngOnInit(): void {
    // Lấy completeCode nếu được truyền qua navigation state
    this.completeCode = history.state?.['completeCode'];

    if (!this.completeCode) {
      this.router.navigate(['error']);
    }

    // Dựa vào completeCode, tìm thông điệp thông báo trong ERROR_MESSAGES
    this.completeMessage = MSG[this.completeCode];
  }

  /**
  * Điều hướng về màn hình ADM002
  */
  hanleBack() {
    this.router.navigate(['/user/list']);
  }
}
