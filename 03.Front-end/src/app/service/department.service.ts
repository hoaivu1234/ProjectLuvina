/**
 * Copyright(C) 2025  Luvina Software Company
 * department.service.ts, 11/05/2025 hoaivd
 */

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app-constants';
import { DepartmentResponse } from '../model/department-response.model';

@Injectable({
  providedIn: 'root'
})

/**
 * Service dùng để thao tác với dữ liệu phòng ban (Department).
 * 
 * @author hoaivd
 */
export class DepartmentService {

  /**
   * Constructor khởi tạo service và inject HttpClient để gọi API.
   *
   * @param http Đối tượng HttpClient dùng để thực hiện các request HTTP
   */
  constructor(private http: HttpClient) { }

  /**
   * Gọi API để lấy danh sách phòng ban.
   *
   * @returns Observable chứa phản hồi từ server với danh sách phòng ban
   */
  getListDepartment(): Observable<DepartmentResponse> {
    return this.http.get<DepartmentResponse>(AppConstants.BASE_URL_API + "/departments");
  }
}

