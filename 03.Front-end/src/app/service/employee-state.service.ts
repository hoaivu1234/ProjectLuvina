/**
 * Copyright(C) 2025  Luvina Software Company
 * employee-state.service.ts, 20/05/2025 hoaivd
 */

import { Injectable } from '@angular/core';
import { EmployeeListComponentState } from '../model/employee.model';
import { PAGINATION } from '../shared/utils/pagination.constants';

@Injectable({
  providedIn: 'root'
})

/**
 * Service lưu trữ tạm thời trạng thái của màn danh sách nhân viên trong bộ nhớ tạm thời (RAM).
 * 
 * Trạng thái bao gồm các thông tin như:
 * - Tên nhân viên đang tìm kiếm
 * - Phòng ban đang chọn
 * - Thông tin sắp xếp (cột, thứ tự, trường)
 * - Phân trang (trang hiện tại, kích thước trang)
 * 
 * Service này giúp giữ nguyên trạng thái giao diện khi người dùng chuyển đổi giữa các màn hình.
 */
export class EmployeeStateService {
  private state: Partial<EmployeeListComponentState> | null = null;

  /**
   * Lưu trạng thái từ component list vào service.
   * @param component - Component đang hiển thị danh sách nhân viên.
   */
  saveStateFrom(component: EmployeeListComponentState): void {
    this.state = {
      employeeName: component.employeeName,
      selectedDepartment: component.selectedDepartment,
      currentSortColumn: component.currentSortColumn,
      currentSortOrder: component.currentSortOrder,
      currentSortField: component.currentSortField,
      currentPage: component.currentPage,
      pageSize: component.pageSize
    };
  }

  /**
   * Gán lại trạng thái đã lưu vào component (nếu có).
   * @param component - Component danh sách nhân viên để khôi phục trạng thái.
   * @returns true nếu có state được khôi phục, ngược lại false.
   */
  restoreStateTo(component: EmployeeListComponentState): boolean {
    if (this.state) {
      component.employeeName = this.state.employeeName ?? '';
      component.selectedDepartment = this.state.selectedDepartment ?? '';
      component.currentSortColumn = this.state.currentSortColumn ?? '';
      component.currentSortOrder = this.state.currentSortOrder ?? '';
      component.currentSortField = this.state.currentSortField ?? '';
      component.currentPage = this.state.currentPage ?? PAGINATION.DEFAULT_PAGE;
      component.pageSize = this.state.pageSize ?? PAGINATION.DEFAULT_PAGE_SIZE;
      return true;
    }
    return false;
  }

  /**
   * Xóa trạng thái đã lưu khỏi bộ nhớ tạm thời.
   */
  clearState(): void {
    this.state = null;
  }
}
