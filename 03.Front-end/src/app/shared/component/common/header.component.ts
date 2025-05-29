import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { EmployeeStateService } from 'src/app/service/employee-state.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent {
  constructor(
    private router: Router,
    private stateService: EmployeeStateService
  ) { }

  logout() {
    sessionStorage.removeItem('access_token');
    this.stateService.clearState();
    this.router.navigate(['login']);
    return false;
  }
}