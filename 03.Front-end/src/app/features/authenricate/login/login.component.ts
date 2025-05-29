import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { HttpClient } from "@angular/common/http";
import { AppConstants } from "../../../app-constants";
import { ERROR_MESSAGES } from 'src/app/shared/utils/error-messages.constants';
import { EmployeeStateService } from 'src/app/service/employee-state.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  constructor(
    private router: Router,
    public http: HttpClient,
    private stateService: EmployeeStateService
  ) { }

  isValid = true; // Trạng thái của form login
  title: string = ''; // Message thông báo hiển thị ở html
  defaultTitle: string = 'アカウント名およびパスワードを入力してください'; // message mặc định khi không nhập username password

  ngOnInit(): void {
    sessionStorage.removeItem("access_token"); // xóa sessionStorage
    this.stateService.clearState(); // Xóa state ở màn hình ADM002
    if (this.router.url === '/logout') { // Nếu url là logout
      this.router.navigate(['login']); // thì điều hướng đến màn login
    }
    this.title = this.defaultTitle; // gán title với default message
  };
  
  login(form: NgForm) {

    if (form.value.username && form.value.password) { // Nếu có giá trị của username, password

      this.http.post(AppConstants.BASE_URL_API + "/login", JSON.stringify(form.value)).subscribe( // Gọi API login với payload là dữ liệu của form được chuyển đổi sang json
        {
          next: (body: any) => { // Nếu thành công
            if (body && body?.accessToken && body?.tokenType) { // Nếu body, body?.accessToken và body?.tokenType có giá trị
              sessionStorage.setItem("access_token", body?.accessToken); // Trong sessionStorage set giá trị access_token bằng body?.accessToken
              sessionStorage.setItem("token_type", body?.tokenType); // Trong sessionStorage set giá trị token_type bằng body?.tokenType
              this.router.navigate(['user/list']) // Điều hướng đến màn adm002 là màn hình list
            } else {  // Nếu body, body?.accessToken và body?.tokenType không có giá trị
              this.isValid = false; // set trạng thái cho form = false
              this.title = ERROR_MESSAGES[body.errors.code](); // set title bằng message tương ứng với error code từ response trả về
            }
          },
          error: (error) => { // Nếu thất bại
            this.router.navigate(['error']) // Điều hướng đến màn System error
          }
        }
      );
    } else { // Nếu không có giá trị của username, password
      this.isValid = false; // set trạng thái cho form = false
      this.title = this.defaultTitle; // // gán title với default message
    }
  }
}
