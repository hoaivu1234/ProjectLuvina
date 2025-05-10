/*
  Copyright(C) 2025 Luvina Software Company
  user-list.component.ts 10/5 hoaivd
*/

import { Component } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { AppConstants } from "../../../app-constants";
import { DepartmentService } from '../../../service/department.service';
import { EmployeeService } from '../../../service/employee.service';
import { Department } from 'src/app/model/department.model';
import { Employee } from 'src/app/model/employee.model';
import { Router } from '@angular/router';
import { MSG } from 'src/app/shared/message/messages';

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
  currentPage: number = 1;
  pageSize: number = 5;
  totalRecords!: number;
  MSG = MSG;

  sortIcons: { [key: string]: string } = {
    Name: '▲▽',
    Certification: '▲▽',
    EndDate: '▲▽',
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
          console.log("Lấy dữ liệu phòng ban thành công.")
      },
      error: () => {
        console.log("Không thể lấy dữ liệu phòng ban!!!");
        this.router.navigate(['error'], { state: { errorCode: 'ER024' } });
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
        this.totalRecords = value?.totalRecords,
          this.listEmployee = value?.employees,
          console.log("Lấy dữ liệu nhân viên thành công.")
      },
      error: () => {
        console.log("Không thể lấy dữ liệu nhân viên!!!");
        this.router.navigate(['error'], { state: { errorCode: 'ER025' } });
      },
    })
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
      this.currentSortColumn === 'Name' ? this.currentSortOrder : '',
      this.currentSortColumn === 'Certification' ? this.currentSortOrder : '',
      this.currentSortColumn === 'EndDate' ? this.currentSortOrder : '',
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

  changeSortIcon(sortIcon: string): string {
    return sortIcon === '▲▽' ? '▼△' : '▲▽';
  }

  handleSort(column: string, sortField: string) {
    this.currentPage = 1;
    this.sortIcons[column] = this.changeSortIcon(this.sortIcons[column]);
    const sortOrder = this.sortIcons[column] === '▲▽' ? 'ASC' : 'DESC';

    this.currentSortColumn = column;
    this.currentSortField = sortField;
    this.currentSortOrder = sortOrder;

    this.getListEmployee(
      this.employeeName,
      this.selectedDepartment,
      column === 'Name' ? sortOrder : '',
      column === 'Certification' ? sortOrder : '',
      column === 'EndDate' ? sortOrder : '',
      sortField
    );
  }

  shortenText(text: string | undefined, maxLength: number = 17): string {
    if (text && text.length > maxLength) {
      return text.substring(0, maxLength) + '...';
    }
    return text ?? '';
  }


}
