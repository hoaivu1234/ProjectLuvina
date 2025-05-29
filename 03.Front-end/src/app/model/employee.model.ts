export interface Employee {
    employeeId: number;
    employeeName: string;
    employeeEmail: string;
    employeeNameKana: string;
    employeeBirthDate: Date;
    employeeTelephone: string;
    departmentName: string;
    certificationName: string;
    endDate: Date;
    score: number;
    employeeLoginId: string;
    employeeLoginPassword: string;
}

export interface EmployeeListComponentState {
    employeeName: string;
    selectedDepartment: string;
    currentSortColumn: string;
    currentSortOrder: string;
    currentSortField: string;
    currentPage: number;
    pageSize: number;
  }
  