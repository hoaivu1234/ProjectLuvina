import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SystemErrorComponent } from 'src/app/shared/component/error/system-error.component';
import { UserListComponent } from './list/user-list.component';
import { AuthorizeGuard } from '../../shared/auth/authorize.guard';
import { ERROR_CODES } from 'src/app/shared/utils/error-code.constants';

const routes: Routes = [
  { path: 'user', redirectTo: 'user/list', pathMatch: 'full'},
  { path: 'user/list', component: UserListComponent, canActivate: [AuthorizeGuard] },
  { path: '**', component: SystemErrorComponent, data: { errorCode: ERROR_CODES.PAGE_NOT_FOUND} },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
