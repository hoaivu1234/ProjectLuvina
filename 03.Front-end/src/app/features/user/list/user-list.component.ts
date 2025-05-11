/*
  Copyright(C) 2025 Luvina Software Company
  user-list.component.ts 10/5/2025 hoaivd
*/

import { Component } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { DepartmentService } from '../../../service/department.service';
import { EmployeeService } from '../../../service/employee.service';
import { Department } from 'src/app/model/department.model';
import { Employee } from 'src/app/model/employee.model';
import { Router } from '@angular/router';
import { MSG } from 'src/app/shared/utils/messages.constants';
import { PAGINATION } from 'src/app/shared/utils/pagination.constants';
import { SORT } from 'src/app/shared/utils/sort.constants';
import { CONSOLE_MESSAGES } from 'src/app/shared/utils/console-message.constants';
import { ERROR_CODES } from 'src/app/shared/utils/error-code.constants';

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
  currentPage: number = PAGINATION.DEFAULT_PAGE;
  pageSize: number = PAGINATION.DEFAULT_PAGE_SIZE;
  totalRecords!: number;
  MSG = MSG;

  sortIcons: { [key: string]: string } = {
    [SORT.COLUMNS.NAME]: SORT.ICONS.DEFAULT,
    [SORT.COLUMNS.CERTIFICATION]: SORT.ICONS.DEFAULT,
    [SORT.COLUMNS.END_DATE]: SORT.ICONS.DEFAULT,
  };

  currentSortColumn: string = '';
  currentSortField: string = '';
  currentSortOrder: string = '';

  constructor(
    public http: HttpClient,
    public departmentService: DepartmentService,
    public employeeService: EmployeeService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.getListDepartment();
    this.getListEmployee();
    this.getPageNumbers();
  };

  getListDepartment() {
    this.departmentService.getListDepartment().subscribe({
      next: (value) => {
        this.listDepartment = value?.departments,
          console.log(CONSOLE_MESSAGES.DEPARTMENT.FETCH_SUCCESS)
      },
      error: () => {
        console.log(CONSOLE_MESSAGES.DEPARTMENT.FETCH_FAILED);
        this.router.navigate(['error'], { state: { errorCode: ERROR_CODES.DEPARTMENT_FETCH_FAILED } });
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

    // this.employeeService.getListEmployee(employeeName, departmentId, ordEmployeeName, ordCertificationName, ordEndDate, sortPriority, offset, limit).subscribe({
    //   next: (value) => {
    //     this.totalRecords = value?.totalRecords,
    //       this.listEmployee = value?.employees,
    //       console.log(CONSOLE_MESSAGES.EMPLOYEE.FETCH_SUCCESS)
    //   },
    //   error: () => {
    //     console.log(CONSOLE_MESSAGES.EMPLOYEE.FETCH_FAILED);
    //     this.router.navigate(['error'], { state: { errorCode: ERROR_CODES.EMPLOYEE_FETCH_FAILED } });
    //   },
    // })
    this.employeeService.getListEmployee(employeeName, departmentId, ordEmployeeName, ordCertificationName, ordEndDate, sortPriority, offset, limit).subscribe({
      next: (value) => {
        this.totalRecords = value?.totalRecords || 0;
        this.listEmployee = value?.employees || [];
    
        const totalPages = this.totalPages();
        // Nếu currentPage vượt quá số trang hiện có (ví dụ xóa dữ liệu trang cuối)
        if (this.currentPage > totalPages) {
          this.currentPage = totalPages;
          this.getListEmployee( // Gọi lại để đảm bảo đúng page
            this.employeeName,
            this.selectedDepartment,
            this.currentSortColumn === SORT.COLUMNS.NAME ? this.currentSortOrder : '',
            this.currentSortColumn === SORT.COLUMNS.CERTIFICATION ? this.currentSortOrder : '',
            this.currentSortColumn === SORT.COLUMNS.END_DATE ? this.currentSortOrder : '',
            this.currentSortField ? this.currentSortField : ''
          );
          return; // tránh hiển thị dữ liệu sai page
        }
    
        console.log(CONSOLE_MESSAGES.EMPLOYEE.FETCH_SUCCESS);
      },
      error: () => {
        console.log(CONSOLE_MESSAGES.EMPLOYEE.FETCH_FAILED);
        this.router.navigate(['error'], { state: { errorCode: ERROR_CODES.EMPLOYEE_FETCH_FAILED } });
      },
    });
    
  }

  searchByName() {
    this.currentPage = 1;
    this.getListEmployee(this.employeeName, this.selectedDepartment);
  }

  goToPage(page: number) {
    this.currentPage = page;

    this.getListEmployee(
      this.employeeName,
      this.selectedDepartment,
      this.currentSortColumn === SORT.COLUMNS.NAME ? this.currentSortOrder : '',
      this.currentSortColumn === SORT.COLUMNS.CERTIFICATION ? this.currentSortOrder : '',
      this.currentSortColumn === SORT.COLUMNS.END_DATE ? this.currentSortOrder : '',
      this.currentSortField ? this.currentSortField : ''
    );
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
    return Math.ceil(this.totalRecords / this.pageSize) || 1;
  }

  changeSortIcon(currentIcon: string): string {
    return currentIcon === SORT.ICONS.DEFAULT 
      ? `${SORT.ICONS.DESC}${SORT.ICONS.ASC}` 
      : SORT.ICONS.DEFAULT;
  }

  handleSort(column: string, sortField: string) {
    this.currentPage = PAGINATION.DEFAULT_PAGE;
    this.sortIcons[column] = this.changeSortIcon(this.sortIcons[column]);
    
    const sortOrder = this.sortIcons[column] === SORT.ICONS.DEFAULT 
      ? SORT.ORDERS.ASC 
      : SORT.ORDERS.DESC;

    this.getListEmployee(
      this.employeeName,
      this.selectedDepartment,
      column === SORT.COLUMNS.NAME ? sortOrder : '',
      column === SORT.COLUMNS.CERTIFICATION ? sortOrder : '',
      column === SORT.COLUMNS.END_DATE ? sortOrder : '',
      sortField
    );
  }
}
