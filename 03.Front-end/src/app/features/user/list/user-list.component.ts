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
  currentPage!: number;
  pageSize: number = 5;
  totalItems = 0;
  sortIconColumnName: string = '▲▽';
  sortIconColumnCertification: string = '▲▽';
  sortIconColumnEndDate: string = '▲▽';

  constructor(
    public http: HttpClient,
    public departmentService: DepartmentService,
    public employeeService: EmployeeService
  ) { }

  ngOnInit(): void {
    this.testAuth();
    this.getListDepartment();
    this.getListEmployee();
    this.getPageNumbers();
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

  getListEmployee(
    employeeName: string = '',
    departmentId: string = '',
    ordEmployeeName: string = '',
    ordCertificationName: string = '',
    ordEndDate: string = '',
    sortPriority: string = '',
    offset: string = '',
    limit: string = '') {
    if (this.currentPage && this.pageSize) {
      const offSet = (this.currentPage - 1) * this.pageSize;
      offset = offSet > 0 ? offSet.toString() : offset;
      limit = this.pageSize.toString();
    }
    this.employeeService.getListEmployee(employeeName, departmentId, ordEmployeeName, ordCertificationName, ordEndDate, sortPriority, offset, limit).subscribe({
      next: (value) => {
        this.listEmployee = value?.employees,
          this.totalItems = value?.totalRecords,
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

  goToPage(page: number) {
    this.currentPage = page;
    this.getListEmployee();
  }

  getPageNumbers(): number[] {
    const total = this.totalPages();
    const pages: number[] = [];
    for (let i = 1; i <= total; i++) {
      pages.push(i);
    }
    this.currentPage = 1;
    return pages;
  }

  totalPages(): number {
    return Math.ceil(this.totalItems / this.pageSize) || 1;
  }

  changeSortIcon(sortIcon: string) {
    if (sortIcon === '▲▽') sortIcon = '▼△';
    else sortIcon = '▲▽';
    return sortIcon;
  }

  handleSearchName() {
    this.sortIconColumnName = this.changeSortIcon(this.sortIconColumnName);
    var sortOrder = this.sortIconColumnName == '▲▽' ? 'ASC' : 'DESC';
    this.getListEmployee(this.employeeName, this.selectedDepartment, sortOrder, '', '', 'ord_employee_name')
  }

  handleSearchCertification() {
    this.sortIconColumnCertification = this.changeSortIcon(this.sortIconColumnCertification);
    var sortOrder = this.sortIconColumnCertification == '▲▽' ? 'ASC' : 'DESC';
    this.getListEmployee(this.employeeName, this.selectedDepartment, '', sortOrder, '', 'ord_certification_name')
  }

  handleSearchEndDate() {
    this.sortIconColumnEndDate = this.changeSortIcon(this.sortIconColumnEndDate);
    var sortOrder = this.sortIconColumnEndDate == '▲▽' ? 'ASC' : 'DESC';
    this.getListEmployee(this.employeeName, this.selectedDepartment, '', '', sortOrder, 'ord_end_date')
  }

}
