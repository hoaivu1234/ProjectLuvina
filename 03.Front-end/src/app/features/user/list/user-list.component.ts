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

/**
 * Hiển thị danh sách nhân viên, phòng ban và thực hiên các thao tác với danh sách như sort, search, paging.
 * Nếu không có nhân viên nào thì hiển thị thông báo "検索条件に該当するユーザが見つかりません".
 * Nếu có lỗi xảy ra trong quá trình thực hiện thì điều hướng đến màn System Error
 * 
 * @author hoaivd
 */
export class UserListComponent {
  listDepartments: Department[] = [];  // Danh sách các phòng ban, được dùng để hiển thị dropdown hoặc filter
  selectedDepartment: string = '';   // ID của phòng ban đang được chọn
  listEmployees: Employee[] = []; // Danh sách nhân viên được hiển thị trong bảng
  employeeName: string = ''; // Tên nhân viên dùng để tìm kiếm hoặc lọc
  currentPage: number = PAGINATION.DEFAULT_PAGE;   // Trang hiện tại trong phân trang, giá trị mặc định được lấy từ hằng số PAGINATION
  pageSize: number = PAGINATION.DEFAULT_PAGE_SIZE;  // Số lượng bản ghi trên mỗi trang, giá trị mặc định lấy từ PAGINATION
  totalRecords!: number; // Tổng số bản ghi nhân viên (để tính tổng số trang trong phân trang)
  MSG = MSG;  // Hằng chứa các thông điệp (message), có thể là lỗi, cảnh báo, hoặc thông báo thành công
  isShowMessage: boolean = false; // Kiểm soát trạng thái hiển thị mess khi không có dữ liệu

  // Đối tượng chứa icon sắp xếp hiện tại cho từng cột
  sortIcons: { [key: string]: string } = {
    [SORT.COLUMNS.NAME]: SORT.ICONS.DEFAULT,           // Icon sắp xếp cho cột tên
    [SORT.COLUMNS.CERTIFICATION]: SORT.ICONS.DEFAULT,  // Icon sắp xếp cho cột chứng chỉ
    [SORT.COLUMNS.END_DATE]: SORT.ICONS.DEFAULT,       // Icon sắp xếp cho cột ngày kết thúc
  };

  currentSortColumn: string = '';   // Tên cột đang được dùng để sắp xếp (hiển thị cho UI)
  currentSortField: string = '';   // Trường dữ liệu thực tế trong đối tượng được sắp xếp (dùng để gọi API/filter)
  currentSortOrder: string = '';   // Thứ tự sắp xếp hiện tại: 'asc' hoặc 'desc'

  /**
   * Constructor khởi tạo component, inject các service cần thiết.
   *
   * @param http Đối tượng HttpClient dùng để thực hiện các yêu cầu HTTP
   * @param departmentService Service lấy dữ liệu phòng ban
   * @param employeeService Service lấy dữ liệu nhân viên
   * @param router Service định tuyến Router để điều hướng khi xảy ra lỗi
   */
  constructor(
    public http: HttpClient,
    public departmentService: DepartmentService,
    public employeeService: EmployeeService,
    private router: Router,
  ) { }

  /**
   * Lifecycle hook khởi chạy khi component được khởi tạo.
   * Gọi các hàm để lấy dữ liệu phòng ban, nhân viên và phân trang.
   */
  ngOnInit(): void {
    this.getListDepartment();
    this.getListEmployee();
    this.getPageNumbers();
  }

  /**
   * Gọi API để lấy danh sách phòng ban.
   * Nếu thành công, gán dữ liệu vào listDepartment.
   * Nếu thất bại, chuyển hướng sang trang lỗi với mã lỗi tương ứng.
   */
  getListDepartment() {
    this.departmentService.getListDepartment().subscribe({
      next: (value) => {
        this.listDepartments = value?.departments;
        console.log(CONSOLE_MESSAGES.DEPARTMENT.FETCH_SUCCESS);
      },
      error: () => {
        console.log(CONSOLE_MESSAGES.DEPARTMENT.FETCH_FAILED);
        this.router.navigate(['error'], { state: { errorCode: ERROR_CODES.DEPARTMENT_FETCH_FAILED } });
      },
    });
  }

  /**
 * Gọi API để lấy danh sách nhân viên với các tham số lọc, sắp xếp và phân trang.
 *
 * @param employeeName Tên nhân viên dùng để lọc
 * @param departmentId ID phòng ban được chọn
 * @param ordEmployeeName Thứ tự sắp xếp theo tên nhân viên ('asc' hoặc 'desc')
 * @param ordCertificationName Thứ tự sắp xếp theo tên chứng chỉ
 * @param ordEndDate Thứ tự sắp xếp theo ngày kết thúc
 * @param sortPriority Trường sắp xếp ưu tiên
 * @param offset Vị trí bắt đầu lấy dữ liệu (phân trang)
 * @param limit Số lượng bản ghi trên mỗi trang
 */
  getListEmployee(
    employeeName: string = '',
    departmentId: string = '',
    ordEmployeeName: string = '',
    ordCertificationName: string = '',
    ordEndDate: string = '',
    sortPriority: string = '',
    offset: string = '',
    limit: string = ''
  ) {
    // Tính toán offset và limit nếu có thông tin phân trang
    if (this.currentPage && this.pageSize) {
      const offSet = (this.currentPage - 1) * this.pageSize;
      offset = offSet > 0 ? offSet.toString() : offset;
      limit = this.pageSize.toString();
    }

    this.employeeService.getListEmployee(
      employeeName,
      departmentId,
      ordEmployeeName,
      ordCertificationName,
      ordEndDate,
      sortPriority,
      offset,
      limit
    ).subscribe({
      next: (value) => {

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
        this.totalRecords = value?.totalRecords || 0;
        this.listEmployees = value?.employees || [];

        const totalPages = this.totalPages();

        // Nếu currentPage lớn hơn tổng số trang, tự động gọi lại hàm với trang phù hợp
        if (this.currentPage > totalPages) {
          this.currentPage = totalPages;
          this.getListEmployee(
            this.employeeName,
            this.selectedDepartment,
            this.currentSortColumn === SORT.COLUMNS.NAME ? this.currentSortOrder : '',
            this.currentSortColumn === SORT.COLUMNS.CERTIFICATION ? this.currentSortOrder : '',
            this.currentSortColumn === SORT.COLUMNS.END_DATE ? this.currentSortOrder : '',
            this.currentSortField ? this.currentSortField : ''
          );
          return;
        }
        this.isShowMessage = true;
        
        console.log(CONSOLE_MESSAGES.EMPLOYEE.FETCH_SUCCESS);
      },
      error: () => {
        console.log(CONSOLE_MESSAGES.EMPLOYEE.FETCH_FAILED);
        this.router.navigate(['error'], {
          state: { errorCode: ERROR_CODES.EMPLOYEE_FETCH_FAILED }
        });
      },
    });
  }

  /**
   * Tìm kiếm nhân viên theo tên.
   * Reset về trang đầu tiên rồi gọi lại hàm getListEmployee với filter theo tên và phòng ban.
   */
  searchByName() {
    this.currentPage = 1;
    this.getListEmployee(this.employeeName, this.selectedDepartment);
  }

  /**
 * Chuyển đến trang được chọn trong phân trang.
 *
 * @param page Số trang muốn chuyển tới
 */
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

  /**
   * Tạo danh sách số trang dựa trên tổng số bản ghi và kích thước trang.
   *
   * @returns Mảng chứa các số trang
   */
  getPageNumbers(): number[] {
    const total = this.totalPages();
    const pages: number[] = [];
    for (let i = 1; i <= total; i++) {
      pages.push(i);
    }
    this.currentPage = 1;
    return pages;
  }

  /**
   * Tính tổng số trang dựa trên tổng số bản ghi và kích thước mỗi trang.
   *
   * @returns Tổng số trang
   */
  totalPages(): number {
    return Math.ceil(this.totalRecords / this.pageSize) || 1;
  }

  /**
   * Thay đổi biểu tượng sắp xếp giữa trạng thái mặc định và tăng/giảm.
   *
   * @param currentIcon Biểu tượng hiện tại của cột
   * @returns Biểu tượng mới sau khi thay đổi
   */
  changeSortIcon(currentIcon: string): string {
    return currentIcon === SORT.ICONS.DEFAULT
      ? `${SORT.ICONS.ASC}${SORT.ICONS.DESC}`
      : SORT.ICONS.DEFAULT;
  }

  /**
   * Xử lý logic khi người dùng click sắp xếp theo cột.
   * Cập nhật biểu tượng sắp xếp và gọi API với thứ tự sắp xếp mới.
   *
   * @param column Tên cột được chọn để sắp xếp
   * @param sortField Trường dữ liệu tương ứng với cột để gửi lên API
   */
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

  /**
   * Điều hướng đến màn hình ADM004
   */
  openADM004() {
    this.router.navigate(['/user/add']);
  }

  getDetailEmployee(id: number | undefined) {
    this.router.navigate(['/user/detail'], { state : { employeeId: id}} );
  }
}
