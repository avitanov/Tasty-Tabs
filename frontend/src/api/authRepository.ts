// src/api/authRepository.ts
import axiosClient from "./axiosClient";
import type { AuthRequest, AuthDto } from "../types/api";

export const authRepository = {
  login: async (credentials: AuthRequest): Promise<AuthDto> => {
    const { data } = await axiosClient.post("/auth/login", credentials);
    return data;
  },
};
