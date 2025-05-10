import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { ERROR_MESSAGES } from '../../message/error-messages';

@Component({
  selector: 'app-system-error',
  templateUrl: './system-error.component.html',
})
export class SystemErrorComponent {
  @Input('errorCode') errorCode!: string;
  errorMessage: string = '';
 
  constructor(private router: Router) {
    const nav = this.router.getCurrentNavigation();
    this.errorCode = nav?.extras?.state?.['errorCode'] || 'Unknown error';
    this.errorMessage = ERROR_MESSAGES[this.errorCode];
  }
}
