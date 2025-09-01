// src/api/assignmentRepository.ts
import axiosClient from "./axiosClient";
import type { AssignmentDto, CreateAssignmentDto } from "../types/api";

export const assignmentRepository = {
  getAllAssignments: async (): Promise<AssignmentDto[]> => {
    const { data } = await axiosClient.get("/assignments");
    return data;
  },
  createAssignment: async (
    assignmentData: CreateAssignmentDto,
  ): Promise<AssignmentDto> => {
    const { data } = await axiosClient.post("/assignments", assignmentData);
    return data;
  },
  // New method to get an employee's next shift
  getNextShift: async (employeeId: number): Promise<AssignmentDto> => {
    const { data } = await axiosClient.get(
      `/employees/${employeeId}/shifts/next`,
    );
    return data;
  },
  // New method for clocking in
  clockIn: async (assignmentId: number): Promise<AssignmentDto> => {
    const { data } = await axiosClient.post(
      `/assignments/${assignmentId}/clockin`,
    );
    return data;
  },
  // New method for clocking out
  clockOut: async (assignmentId: number): Promise<AssignmentDto> => {
    const { data } = await axiosClient.post(
      `/assignments/${assignmentId}/clockout`,
    );
    return data;
  },
};
