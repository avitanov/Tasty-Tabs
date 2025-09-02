// src/api/paymentRepository.ts
import axiosClient from "./axiosClient";
import type { CreatePaymentDto } from "../types/api";

export const paymentRepository = {
  createPayment: async (paymentData: CreatePaymentDto): Promise<any> => {
    const { data } = await axiosClient.post("/payments", paymentData);
    return data;
  },
};
