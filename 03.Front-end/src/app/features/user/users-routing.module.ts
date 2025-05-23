import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SystemErrorComponent } from 'src/app/shared/component/error/system-error.component';
import { UserListComponent } from './list/user-list.component';
import { AuthorizeGuard } from '../../shared/auth/authorize.guard';
import { ERROR_CODES } from 'src/app/shared/utils/error-code.constants';
import { ADM004Component } from './adm004/adm004.component';
import { Adm005Component } from './adm005/adm005.component';
import { ADM006Component } from './adm006/adm006.component';
import { ADM003Component } from './adm003/adm003.component';

const routes: Routes = [
  { path: 'user', redirectTo: 'user/list', pathMatch: 'full'},
  { path: 'user/list', component: UserListComponent, canActivate: [AuthorizeGuard] },
  { path: 'user/adm003', component: ADM003Component, canActivate: [AuthorizeGuard] },
  { path: 'user/adm004', component: ADM004Component, canActivate: [AuthorizeGuard] },
  { path: 'user/adm005', component: Adm005Component, canActivate: [AuthorizeGuard] },
  { path: 'user/adm006', component: ADM006Component, canActivate: [AuthorizeGuard] },
  { path: 'error', component: SystemErrorComponent, canActivate: [AuthorizeGuard], data: { errorCode: ERROR_CODES.SYSTEM_ERROR} },
  { path: '**', component: SystemErrorComponent, canActivate: [AuthorizeGuard], data: { errorCode: ERROR_CODES.PAGE_NOT_FOUND} },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
