import { Employee } from "./employee.model";
import { MessageResponse } from "./message-response.model";

export interface EmployeeResponse {
  code: number;
  totalRecords: number;
  employees: Employee[];
}

export interface AddEmployeeResponse {
  code: number;
  employeeId: number;
  message: MessageResponse;
}