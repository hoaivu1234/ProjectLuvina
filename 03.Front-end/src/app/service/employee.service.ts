import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app-constants';
import { Employee } from '../shared/model/employee.model';
import { EmployeeResponse } from '../shared/model/employee-response.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  constructor(private http: HttpClient) { }

  getListEmployee(
    employeeName?: string,
    departmentId?: string,
    ordEmployeeName?: string,
    ordCertificationName?: string,
    ordEndDate?: string,
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
    if (offset) params = params.append('offset', offset);
    if (limit) params = params.append('limit', limit);

    return this.http.get<EmployeeResponse>(AppConstants.BASE_URL_API + "/employees", { params });
  }
}
