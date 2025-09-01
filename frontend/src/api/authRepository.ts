// src/api/authRepository.ts
import axiosClient from "./axiosClient";
import type { AuthRequest, AuthDto, RegisterRequest } from "../types/api";

export const authRepository = {
  login: async (credentials: AuthRequest): Promise<AuthDto> => {
    const { data } = await axiosClient.post("/auth/login", credentials);
    return data;
  },
  // New method for customer registration
  register: async (registerData: RegisterRequest): Promise<AuthDto> => {
    const { data } = await axiosClient.post("/auth/register", registerData);
    return data;
  },
};
