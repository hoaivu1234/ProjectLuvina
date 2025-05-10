import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app-constants';
import { DepartmentResponse } from '../shared/model/department-response.model';

@Injectable({
  providedIn: 'root'
})

export class DepartmentService {
  constructor(private http: HttpClient) { }

  getListDepartment(): Observable<DepartmentResponse> {
    return this.http.get<DepartmentResponse>(AppConstants.BASE_URL_API + "/departments");
  }
}
