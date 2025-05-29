import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  public title = 'AngularMock';

  constructor(
    private router: Router
  ) { }

  /**
   * Lifecycle hook `ngOnInit` được gọi khi component khởi tạo.
   *
   * Lắng nghe sự kiện thay đổi định tuyến (NavigationEnd),
   * kiểm tra token đăng nhập trong `sessionStorage`:
   * - Nếu không có token, điều hướng đến trang `/login`.
   * - Nếu đã đăng nhập nhưng truy cập vào root (`/` hoặc rỗng), điều hướng đến `/user/list`.
   */
  ngOnInit(): void {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        const token = sessionStorage.getItem("access_token"); // Lấy token từ sessionStorage
        const currentUrl = this.router.url; // Lấy url hiện tại

        if (!token) { // Nếu không có token
          this.router.navigate(['/login']); // Điều hướng đến trang login
        } else { // Nếu có token
          if (currentUrl === '/' || currentUrl === '') { // Nếu url = `/` hoặc rỗng
            this.router.navigate(['/user/list']); // Điều hướng đến ADM002
          }
        }
      });
  }
}
