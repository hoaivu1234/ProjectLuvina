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

export interface EmployeeCertificationResponseDTO {
  certificationId: number;
  certificationName: string;
  startDate: Date;
  endDate: Date;
  score: number;
}

export interface EmployeeResponseDTO {
  code: number;
  employeeId: number;
  employeeName: string;
  employeeBirthDate: Date;
  employeeEmail: string;
  departmentId: number;
  departmentName: string;
  employeeTelephone: string;
  employeeNameKana: string;
  employeeLoginId: string;
  certifications?: EmployeeCertificationResponseDTO[];
}
