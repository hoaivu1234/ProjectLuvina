import { Department } from "./department.model";

export interface DepartmentResponse {
    code: number;
    departments: Department[];
  }