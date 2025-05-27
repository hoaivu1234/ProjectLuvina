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
import { PAGE } from 'src/app/shared/utils/mode-constant';

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
  MSG = MSG;  // Hằng chứa các thông báo thành công
  isShowMessage: boolean = false; // Kiểm soát trạng thái hiển thị message khi không có dữ liệu

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
    this.getListDepartment();  // Gọi hàm lấy dữ liệu phòng ban
    this.restoreStateIfExists(); // Khôi phục lại state ở sessionStorage nếu nó tồn tại
  }

  /**
   * Lưu trạng thái hiện tại của màn danh sách nhân viên vào sessionStorage.
   * 
   * Dữ liệu được lưu gồm:
   * - Tên nhân viên đang tìm kiếm
   * - Phòng ban được chọn
   * - Thông tin sắp xếp (cột, thứ tự, trường)
   * - Thông tin phân trang (trang hiện tại, kích thước trang)
   * 
   * Giúp khôi phục lại giao diện đúng trạng thái khi quay lại từ các màn hình khác như ADM003, ADM006.
   */
  saveCurrentState() {
    const state = { // Tạo state chứa thông tin của điều kiện search, sort và paging
      employeeName: this.employeeName,
      selectedDepartment: this.selectedDepartment,
      currentSortColumn: this.currentSortColumn,
      currentSortOrder: this.currentSortOrder,
      currentSortField: this.currentSortField,
      currentPage: this.currentPage,
      pageSize: this.pageSize
    };
    sessionStorage.setItem('user_list_state', JSON.stringify(state)); // Lưu state vào sessionStorage với key: user_list_state
  }

  /**
   * Khôi phục trạng thái trước đó của màn danh sách nhân viên nếu tồn tại trong sessionStorage.
   * 
   * Nội dung được khôi phục:
   * - Giá trị tìm kiếm theo tên nhân viên
   * - Phòng ban được chọn
   * - Thông tin sắp xếp (cột, thứ tự, trường)
   * - Phân trang (trang hiện tại, kích thước trang)
   * 
   * Sau khi khôi phục, gọi lại API getListEmployee() để tải dữ liệu theo đúng trạng thái trước đó.
   * Nếu không có dữ liệu lưu trong sessionStorage, sẽ gọi API với trạng thái mặc định.
   */
  restoreStateIfExists() {
    // Lấy giá trị state chứa thông tin của điều kiện search, sort và paging đã được lưu trong sessionStorage
    const userListState = sessionStorage.getItem('user_list_state');

    if (userListState) { // Nếu state tồn tại và có giá trị
      // Parse giá trị về định dạng json 
      const state = JSON.parse(userListState); 
      // Sau đó map từng trường trong state với các giá trị trong component hiện tại
      this.employeeName = state.employeeName;
      this.selectedDepartment = state.selectedDepartment;
      this.currentSortColumn = state.currentSortColumn;
      this.currentSortOrder = state.currentSortOrder;
      this.currentSortField = state.currentSortField;
      this.currentPage = state.currentPage;
      this.pageSize = state.pageSize;

      // Gọi lại API với trạng thái đã khôi phục
      this.getListEmployee(this.employeeName, this.selectedDepartment);
    } else {
      // Trường hợp không có trạng thái cũ thì gọi mặc định
      this.getListEmployee();
    }
  }

  /**
   * Gọi API để lấy danh sách phòng ban.
   * Nếu thành công, gán dữ liệu vào listDepartment.
   * Nếu thất bại, chuyển hướng sang màn hình System Error với mã lỗi tương ứng.
   */
  getListDepartment() {
    this.departmentService.getListDepartment().subscribe({
      next: (value) => { // Nếu thành công
        this.listDepartments = value?.departments;  // Gán dữ liệu cho listDepartment từ response trả về
        console.log(CONSOLE_MESSAGES.DEPARTMENT.FETCH_SUCCESS);
      },
      error: () => { // Nếu thất bại
        console.log(CONSOLE_MESSAGES.DEPARTMENT.FETCH_FAILED);
        // Điều hướng đến màn hình System Error với mã lỗi tương ứng
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
   * @param ordCertificationName Thứ tự sắp xếp theo tên chứng chỉ ('asc' hoặc 'desc')
   * @param ordEndDate Thứ tự sắp xếp theo ngày kết thúc ('asc' hoặc 'desc')
   * @param sortPriority Trường sắp xếp ưu tiên
   * @param offset Vị trí bắt đầu lấy dữ liệu (phân trang)
   * @param limit Số lượng bản ghi trên mỗi trang
   */
  getListEmployee(
    employeeName: string = '',
    departmentId: string = '',
    offset: string = '',
    limit: string = ''
  ) {
    // Tính toán offset và limit nếu có thông tin phân trang
    if (this.currentPage && this.pageSize) { // Nếu currentPage và pageSize có giá trị
      const offSet = (this.currentPage - 1) * this.pageSize; // Tính toán giá trị vị trí bắt đầu lấy dữ liệu
      // Nếu offset > 0 thì chuyển giá trị offset sang string, nếu không thì vẫn giữ nguyên giá trị cho offset
      offset = offSet > 0 ? offSet.toString() : offset;
      limit = this.pageSize.toString(); // Chuyển giá trị số lượng bản ghi mỗi trang sang string
    }

    // Khởi tạo giá trị thứ tự sắp xếp các trường tên nhân viên, tên chứng chỉ, ngày kết thúc và trường đang được sắp xếp 
    // Bằng các giá trị trả về tương ứng từ hàm getSortParams().
    const { ordEmployeeName, ordCertificationName, ordEndDate, sortPriority } = this.getSortParams();

    this.employeeService.getListEmployee(   // Gọi service với các tham số search, sort, paging
      employeeName,
      departmentId,
      ordEmployeeName,
      ordCertificationName,
      ordEndDate,
      sortPriority,
      offset,
      limit
    ).subscribe({
      next: (value) => { // Nếu thành công
        this.totalRecords = value?.totalRecords;  // Gán tổng số bản ghi bằng giá trị của totalRecords trong response trả về từ API
        this.listEmployees = value?.employees; // Gán danh sách nhân viên bằng giá trị của employees trong response trả về từ API

        const totalPages = this.totalPages(); // Tính tổng số trang dựa vào tổng bản ghi và kích thước trang

        // Nếu trang hiện tại lớn hơn tổng số trang (trong trường hợp nếu page cuối có 1 bản ghi và vào DB xóa bản ghi đó đi)
        if (this.currentPage > totalPages) {
          this.currentPage = totalPages; // thì cập nhật lại currentPage = totalPages
          this.getListEmployee(this.employeeName, this.selectedDepartment); // Gọi lại hàm getListEmployee để lấy dữ liệu đúng trang
          return;
        }

        // Thông báo chỉ được cho phép hiển thị khi call API xong
        this.isShowMessage = true;  // Cho phép hiển thị thông báo không có bản ghi nào (nếu có)
        console.log(CONSOLE_MESSAGES.EMPLOYEE.FETCH_SUCCESS);
      },
      error: () => {
        console.log(CONSOLE_MESSAGES.EMPLOYEE.FETCH_FAILED);
        // Chuyển hướng sang màn hình System Error và truyền mã lỗi tương ứng để hiển thị chi tiết lỗi
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
  search() {
    this.currentPage = 1; // Reset về trang đầu tiên
    this.getListEmployee(this.employeeName, this.selectedDepartment); // Gọi lại hàm getListEmployee với filter theo tên và phòng ban.
  }

  /**
   * Chuyển đến trang được chọn trong phân trang.
   *
   * @param page Số trang muốn chuyển tới
   */
  goToPage(page: number) {
    this.currentPage = page;  // Gán trang hiện tại bằng trang được click từ UI
    this.getListEmployee(this.employeeName, this.selectedDepartment); // Gọi lại hàm getListEmployee với filter theo tên và phòng ban.
  }

  /**
   * Tính tổng số trang dựa trên tổng số bản ghi và kích thước mỗi trang.
   *
   * @returns Tổng số trang
   */
  totalPages(): number {
    // Chia tổng số bản ghi cho số lượng bản ghi trên mỗi trang, sau đó làm tròn lên
    // để đảm bảo rằng các bản ghi dư vẫn được tính thành một trang riêng.
    // Ví dụ: 25 bản ghi với pageSize = 10 sẽ cho ra 3 trang.
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

    this.currentSortColumn = column;
    this.currentSortOrder = sortOrder;
    this.currentSortField = sortField;

    this.saveCurrentState();

    this.getListEmployee(this.employeeName, this.selectedDepartment);
  }

  /**
   * Điều hướng đến màn hình ADM004
   */
  openADM004() {
    this.saveCurrentState();
    this.router.navigate(['/user/adm004'], { state: { fromPage: PAGE.ADM002 } });
  }

  /**
   * Điều hướng đến màn lấy chi tiết nhân viên với Id đã chọn
   * @param id Id của employee tương ứng cần xem dữ liệu chi tiết
   */
  getDetailEmployee(id: number | undefined) {
    this.saveCurrentState();
    this.router.navigate(['/user/adm003'], { state: { employeeId: id } });
  }

  /**
   * Trả về các tham số sắp xếp để gửi lên API dựa trên cột đang được chọn để sắp xếp.
   *
   * Hàm sẽ xác định thứ tự sắp xếp tương ứng với từng cột (tên nhân viên, chứng chỉ, ngày kết thúc)
   * dựa vào giá trị của this.currentSortColumn và this.currentSortOrder.
   *
   * Nếu cột đang được sắp xếp trùng với một trong các cột định nghĩa trong hằng số SORT.COLUMNS,
   * thì trả về giá trị asc hoặc desc tương ứng. Nếu không, trả về chuỗi rỗng.
   *
   * @returns Đối tượng chứa các tham số sắp xếp gồm:
   *  - ordEmployeeName: thứ tự sắp xếp theo tên nhân viên
   *  - ordCertificationName: thứ tự sắp xếp theo tên chứng chỉ
   *  - ordEndDate: thứ tự sắp xếp theo ngày hết hạn
   *  - sortPriority: tên trường ưu tiên sắp xếp (được truyền vào API để backend biết sắp theo trường nào)
   */
  private getSortParams() {
    return {
      ordEmployeeName: this.currentSortColumn === SORT.COLUMNS.NAME ? this.currentSortOrder : '',
      ordCertificationName: this.currentSortColumn === SORT.COLUMNS.CERTIFICATION ? this.currentSortOrder : '',
      ordEndDate: this.currentSortColumn === SORT.COLUMNS.END_DATE ? this.currentSortOrder : '',
      sortPriority: this.currentSortField
    };
  }
}
