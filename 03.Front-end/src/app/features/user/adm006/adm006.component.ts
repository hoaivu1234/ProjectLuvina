import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MSG } from 'src/app/shared/utils/messages.constants';

@Component({
  selector: 'app-adm006',
  templateUrl: './adm006.component.html',
  styleUrls: ['./adm006.component.css']
})
export class ADM006Component {
  completeMessage: string = '';
  completeCode: string = ''
  navigation: any;
  /**
 * Constructor khởi tạo component, inject các service cần thiết.
 *
 * @param router Service định tuyến Router để điều hướng khi xảy ra thông báo
 */
  constructor(
    private router: Router,
  ) { 
    // Lấy thông tin điều hướng hiện tại từ Router
    this.navigation = this.router.getCurrentNavigation();
  }

  /**
* Lifecycle hook khởi tạo component.
* Lấy mã thông báo từ navigation state và tìm thông điệp thông báo tương ứng.
* Nếu không có mã thông báo, sẽ sử dụng mã thông báo hệ thống mặc định.
*
* @return void
*/
  ngOnInit(): void {
    // Lấy completeCode nếu được truyền qua navigation state
    const codeFromState = this.navigation?.extras?.state?.['completeCode'];

    // Ưu tiên completeCode từ state
    this.completeCode = codeFromState;

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
