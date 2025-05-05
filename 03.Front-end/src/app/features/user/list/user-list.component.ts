import { Component } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { AppConstants } from "../../../app-constants";
import { DepartmentService } from '../../../service/department.service';
import { EmployeeService } from '../../../service/employee.service';
import { Department } from 'src/app/shared/model/department.model';
import { Employee } from 'src/app/shared/model/employee.model';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
})
export class UserListComponent {
  listDepartment: Department[] = [];
  selectedDepartment: string = '';
  listEmployee: Employee[] = [];
  employeeName: string = '';

  constructor(
    public http: HttpClient,
    public departmentService: DepartmentService,
    public employeeService: EmployeeService
  ) { }

  ngOnInit(): void {
    this.testAuth();
    this.getListDepartment();
    this.getListEmployee();
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

  getListDepartment() {
    this.departmentService.getListDepartment().subscribe({
      next: (value) => {
        this.listDepartment = value?.departments,
          console.log("Lấy dữ liệu phòng ban thành công.")
      },
      error: () => {
        console.log("Không thể lấy dữ liệu phòng ban!!!");
      },
    })
  }

  getListEmployee(employeeName?: string, departmentId?: string) {
    this.employeeService.getListEmployee(employeeName, departmentId).subscribe({
      next: (value) => {
        this.listEmployee = value?.employees,
          console.log("Lấy dữ liệu nhân viên thành công.")
      },
      error: () => {
        console.log("Không thể lấy dữ liệu nhân viên!!!");
      },
    })
  }

  searchByName() {
    this.getListEmployee(this.employeeName, this.selectedDepartment);
  }
}
