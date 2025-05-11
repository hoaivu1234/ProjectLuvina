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
   * Write code on Method
   *
   * @return response()
   */
  ngOnInit(): void {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        const token = sessionStorage.getItem("access_token");
        const currentUrl = this.router.url;

        if (!token) {
          this.router.navigate(['/login']);
        } else {
          if (currentUrl === '/' || currentUrl === '') {
            this.router.navigate(['/user/list']);
          }
        }
      });
  }
}
