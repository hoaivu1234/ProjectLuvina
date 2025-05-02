import { Component } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { AppConstants } from "../../../app-constants";
import { DepartmentService } from '../../service/department.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent {
  listDepartment: any;
  selectedDepartment: string = '';

  constructor(
    public http: HttpClient,
    public departmentService: DepartmentService
  ) { }

  ngOnInit(): void {
    this.testAuth();
    // this.getListDepartment();
  };

  testAuth() {
    // test call api auto inject token to header
    this.http.post(AppConstants.BASE_URL_API + "/test-auth", null)
      .subscribe({
        next: (response) => {
          console.log(response);
        },
        error: (error) => {
          console.log(error);
        },
        complete: () => {
          console.log('complete');
        }
      });
  }

  // getListDepartment() {
  //   this.departmentService.getListDepartment().subscribe({
  //     next: (value) => {
  //       this.listDepartment = value?.data,
  //         console.log("Lấy dữ liệu phòng ban thành công.")
  //     },
  //     error: () => {
  //       console.log("Không thể lấy dữ liệu phòng ban!!!");
  //     },
  //   })
  // }
}
