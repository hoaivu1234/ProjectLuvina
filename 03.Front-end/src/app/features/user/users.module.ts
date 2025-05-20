import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UsersRoutingModule } from './users-routing.module';
import { UserListComponent } from './list/user-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ShortenTextPipe } from 'src/app/shared/custom-pipe/shorten-text';
import { ADM004Component } from './adm004/adm004.component';
import { Adm005Component } from './adm005/adm005.component';
import { ADM006Component } from './adm006/adm006.component';
import { ADM003Component } from './adm003/adm003.component';
import { ModalModule } from 'ngx-bootstrap/modal';

@NgModule({
  declarations: [
    UserListComponent,
    ShortenTextPipe,
    ADM004Component,
    Adm005Component,
    ADM006Component,
    ADM003Component
  ],
  imports: [
    BrowserAnimationsModule,
    BsDatepickerModule.forRoot(),
    CommonModule,
    SharedModule,
    UsersRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    ModalModule.forRoot(),
  ]
})
export class UsersModule { }
