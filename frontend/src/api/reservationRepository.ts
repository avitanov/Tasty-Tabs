// src/api/reservationRepository.ts
import axiosClient from "./axiosClient";
// Make sure CreateReservationDto is imported
import type { ReservationDto, CreateReservationDto } from "../types/api";

export const reservationRepository = {
  getTodayReservations: async (): Promise<ReservationDto[]> => {
    const { data } = await axiosClient.get("/reservations");
    return data;
  },
  acceptReservation: async (
    reservationId: number,
    tableNumber: number,
  ): Promise<ReservationDto> => {
    const { data } = await axiosClient.post(
      `/reservations/accept/${reservationId}/table/${tableNumber}`,
    );
    return data;
  },
  // Add this new method
  createReservation: async (
    reservationData: CreateReservationDto,
  ): Promise<ReservationDto> => {
    const { data } = await axiosClient.post(
      "/reservations/add",
      reservationData,
    );
    return data;
  },

  getMyReservations: async (): Promise<ReservationDto[]> => {
    const { data } = await axiosClient.get("/reservations/myReservations");
    return data;
  },
  // New method to delete/cancel a reservation
  deleteReservation: async (reservationId: number): Promise<void> => {
    await axiosClient.delete(`/reservations/delete/${reservationId}`);
  },
};
