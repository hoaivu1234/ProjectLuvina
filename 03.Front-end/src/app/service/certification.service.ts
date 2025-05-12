/**
 * Copyright(C) 2025  Luvina Software Company
 * certification.service.ts, 11/05/2025 hoaivd
 */

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CertificationResponse } from '../model/certification-response.model';
import { AppConstants } from '../app-constants';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

/**
 * Service dùng để thao tác với dữ liệu trình độ tiếng nhật.
 * 
 * @author hoaivd
 */
export class CertificationService {
  /**
   * Constructor khởi tạo service và inject HttpClient để gọi API.
   *
   * @param http Đối tượng HttpClient dùng để thực hiện các request HTTP
   */
  constructor(private http: HttpClient) { }

  /**
   * Gọi API để lấy danh sách trình độ tiếng nhật.
   *
   * @returns Observable chứa phản hồi từ server với danh sách trình độ tiếng nhật
   */
  getListCertifications(): Observable<CertificationResponse> {
    return this.http.get<CertificationResponse>(AppConstants.BASE_URL_API + "/certifications");
  }
}
