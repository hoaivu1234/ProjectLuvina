import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UsersRoutingModule } from './users-routing.module';
import { UserListComponent } from './list/user-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    UserListComponent,
  ],
  imports: [
    BrowserAnimationsModule,
    BsDatepickerModule.forRoot(),
    CommonModule,
    SharedModule,
    UsersRoutingModule,
    FormsModule
  ]
})
export class UsersModule { }
