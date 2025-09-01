// src/api/shiftRepository.ts
import axiosClient from "./axiosClient";
import type { ShiftDto, CreateShiftDto } from "../types/api";

export const shiftRepository = {
  getAllShifts: async (): Promise<ShiftDto[]> => {
    const { data } = await axiosClient.get("/shifts");
    return data;
  },
  createShift: async (shiftData: CreateShiftDto): Promise<ShiftDto> => {
    const { data } = await axiosClient.post("/shifts", shiftData);
    return data;
  },
};
