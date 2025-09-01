// src/api/employeeRepository.ts
import axiosClient from "./axiosClient";
import type { EmployeeDto, CreateEmployeeRequest } from "../types/api";

export const employeeRepository = {
  // As per your instruction, the endpoint is /employees
  getAllEmployees: async (): Promise<EmployeeDto[]> => {
    const { data } = await axiosClient.get("/employees");
    return data;
  },
  createEmployee: async (
    employeeData: CreateEmployeeRequest,
  ): Promise<EmployeeDto> => {
    const { data } = await axiosClient.post(
      "/employees/manager/create",
      employeeData,
    );
    return data;
  },
};
