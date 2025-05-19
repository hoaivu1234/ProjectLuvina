/**
 * Copyright(C) 2025  Luvina Software Company
 * employee.service.ts, 11/05/2025 hoaivd
 */

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app-constants';
import { AddEmployeeResponse, EmployeeResponse } from '../model/employee-response.model';

@Injectable({
  providedIn: 'root'
})

/**
 * Service dùng để thao tác với dữ liệu nhân viên (Employee).
 * 
 * @author hoaivd
 */
export class EmployeeService {

  /**
   * Constructor khởi tạo service và inject HttpClient để gọi API.
   *
   * @param http Đối tượng HttpClient dùng để thực hiện các request HTTP
   */
  constructor(private http: HttpClient) { }

  /**
   * Gọi API để lấy danh sách nhân viên, có hỗ trợ lọc, sắp xếp và phân trang.
   *
   * @param employeeName Tên nhân viên cần tìm (tuỳ chọn)
   * @param departmentId ID phòng ban cần lọc (tuỳ chọn)
   * @param ordEmployeeName Thứ tự sắp xếp theo tên nhân viên (asc/desc) (tuỳ chọn)
   * @param ordCertificationName Thứ tự sắp xếp theo tên chứng chỉ (asc/desc) (tuỳ chọn)
   * @param ordEndDate Thứ tự sắp xếp theo ngày hết hạn (asc/desc) (tuỳ chọn)
   * @param sortPriority Trường ưu tiên sắp xếp đầu tiên nếu có nhiều trường (tuỳ chọn)
   * @param offset Vị trí bắt đầu lấy dữ liệu (tuỳ chọn, dùng cho phân trang)
   * @param limit Số lượng bản ghi cần lấy (tuỳ chọn, dùng cho phân trang)
   * @returns Observable chứa danh sách nhân viên từ API
   */
  getListEmployee(
    employeeName?: string,
    departmentId?: string,
    ordEmployeeName?: string,
    ordCertificationName?: string,
    ordEndDate?: string,
    sortPriority?: string,
    offset?: string,
    limit?: string
  ): Observable<EmployeeResponse> {
    // Khởi tạo HttpParams
    let params = new HttpParams();

    // Thêm các tham số nếu chúng có giá trị
    if (employeeName) params = params.append('employee_name', employeeName);
    if (departmentId) params = params.append('department_id', departmentId);
    if (ordEmployeeName) params = params.append('ord_employee_name', ordEmployeeName);
    if (ordCertificationName) params = params.append('ord_certification_name', ordCertificationName);
    if (ordEndDate) params = params.append('ord_end_date', ordEndDate);
    if (sortPriority) params = params.append('sort_priority', sortPriority);
    if (offset) params = params.append('offset', offset);
    if (limit) params = params.append('limit', limit);

    return this.http.get<EmployeeResponse>(AppConstants.BASE_URL_API + "/employees", { params });
  }

  /**
   * Gửi yêu cầu POST để thêm mới nhân viên
   * 
   * @param payload - Dữ liệu nhân viên cần thêm.
   * @returns Observable<AddEmployeeResponse> - Kết quả phản hồi từ server
 */
  addEmployee(payload: any): Observable<AddEmployeeResponse> {
    return this.http.post<AddEmployeeResponse>(AppConstants.BASE_URL_API + "/employees", payload);
  }
}

