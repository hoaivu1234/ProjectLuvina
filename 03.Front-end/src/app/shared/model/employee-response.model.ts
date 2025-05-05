import { Employee } from "./employee.model";

export interface EmployeeResponse {
    code: number;
    totalRecords: number;
    employees: Employee[];
  }