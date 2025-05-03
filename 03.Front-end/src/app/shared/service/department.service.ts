import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppConstants } from 'src/app/app-constants';

@Injectable({
  providedIn: 'root'
})

export class DepartmentService {
  constructor(private http: HttpClient) { }

  getListDepartment(): Observable<any> {
    return this.http.get(AppConstants.BASE_URL_API + "/departments");
  }
}
