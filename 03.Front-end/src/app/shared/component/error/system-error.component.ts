/**
 * Copyright(C) 2025  Luvina Software Company
 * system-error.component.ts, 11/05/2025 hoaivd
 */

import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ERROR_MESSAGES } from '../../utils/error-messages.constants';
import { ERROR_CODES } from '../../utils/error-code.constants';

@Component({
  selector: 'app-system-error',
  templateUrl: './system-error.component.html',
})

/**
 * Hiển thị thông báo lỗi hệ thống cho người dùng.
 * 
 * Component này lấy mã lỗi (`errorCode`) từ navigation state hoặc từ route data 
 * để hiển thị thông điệp lỗi phù hợp tương ứng với mã lỗi đó.
 * Nếu không có mã lỗi nào được cung cấp, component sẽ hiển thị thông báo lỗi hệ thống mặc định.
 * 
 * @author hoaivd
 */
export class SystemErrorComponent {
  // Mã lỗi được lấy từ navigation state hoặc route data
  errorCode: string = '';

  // Thông điệp lỗi tương ứng với errorCode
  errorMessage: string = '';

  /**
   * Constructor khởi tạo component, inject các service cần thiết.
   *
   * @param route Đối tượng ActivatedRoute dùng để lấy dữ liệu từ route hiện tại
   * @param router Đối tượng Router dùng để lấy thông tin từ navigation state
   */
  constructor(private route: ActivatedRoute, private router: Router) {}

  /**
   * Lifecycle hook khởi tạo component.
   * Lấy mã lỗi từ navigation state hoặc từ route snapshot và tìm thông điệp lỗi tương ứng.
   * Nếu không có mã lỗi, sẽ sử dụng mã lỗi hệ thống mặc định.
   *
   * @return void
   */
  ngOnInit(): void {
    // Lấy thông tin điều hướng hiện tại từ Router
    const nav = this.router.getCurrentNavigation();
  
    // Lấy errorCode nếu được truyền qua navigation state
    const codeFromState = nav?.extras?.state?.['errorCode'];
  
    // Lấy errorCode nếu được định nghĩa trong data của route
    const codeFromData = this.route.snapshot.data['errorCode'];
  
    // Ưu tiên errorCode từ state, nếu không có thì dùng từ route, nếu không có nữa thì dùng mã lỗi mặc định SYSTEM_ERROR
    this.errorCode = codeFromState || codeFromData || ERROR_CODES.SYSTEM_ERROR;
  
    // Dựa vào errorCode, tìm thông điệp lỗi trong ERROR_MESSAGES
    this.errorMessage = ERROR_MESSAGES[this.errorCode]();
  }

  hanldeClick() {
    this.router.navigate(['user/list']);
  }
}
