import { Department } from "./department.model";

export interface DepartmentResponse {
    code: number;
    totalRecords: number;
    departments: Department[];
  }