// src/api/analyticsRepository.ts
import axiosClient from "./axiosClient";
import type {
  DailyOpsDto,
  TopProductDto,
  ServerPerformanceDto,
  ManagerShiftAboveAvgDto,
  MonthlyRevenueVsLaborDto,
  RevenueSplitDto,
  AnalyticsByChannelResponse,
} from "../types/api";

const getISODateString = (date: Date) => date.toISOString().split("T")[0];

export const analyticsRepository = {
  /**
   * Fetches daily operational summary for the last X days.
   * @param days The number of past days to include (default 30).
   */
  getDailyOps: async (days: number = 30): Promise<DailyOpsDto[]> => {
    const { data } = await axiosClient.get("/analytics/daily-ops", {
      params: { days },
    });
    return data;
  },

  /**
   * Fetches the top-selling products.
   * @param days The number of past days to analyze (default 90).
   * @param limit The number of products to return (default 10).
   */
  getTopProducts: async (
    days: number = 90,
    limit: number = 5,
  ): Promise<TopProductDto[]> => {
    const { data } = await axiosClient.get("/analytics/top-products", {
      params: { days, limit },
    });
    return data;
  },

  /**
   * Fetches performance metrics for all servers.
   */
  getServerPerformance: async (): Promise<ServerPerformanceDto[]> => {
    const { data } = await axiosClient.get("/analytics/servers/performance");
    return data;
  },

  /**
   * Fetches revenue split between different order types (e.g., ONLINE, TAB) for a date range.
   */
  getRevenueSplit: async (): Promise<RevenueSplitDto[]> => {
    const to = new Date();
    const from = new Date();
    from.setDate(to.getDate() - 30); // Default to last 30 days

    const params = { from: getISODateString(from), to: getISODateString(to) };
    const { data } = await axiosClient.get("/analytics/revenue-split", {
      params,
    });
    return data;
  },

  /**
   * Fetches a monthly comparison of total revenue vs. total labor cost.
   */
  getMonthlyRevenueVsLabor: async (): Promise<MonthlyRevenueVsLaborDto[]> => {
    const { data } = await axiosClient.get(
      "/analytics/monthly-revenue-vs-labor",
    );
    return data;
  },

  /**
   * Fetches shifts managed by managers that performed above the monthly average.
   */
  getManagersShiftsAboveAvg: async (): Promise<ManagerShiftAboveAvgDto[]> => {
    const { data } = await axiosClient.get(
      "/analytics/managers/shifts-above-avg",
    );
    return data;
  },

  /**
   * Fetches revenue grouped by shift periods (e.g., Morning, Afternoon, Evening).
   * The backend returns a generic structure that we will handle dynamically.
   */
  getRevenueByShiftPeriod: async (): Promise<Record<string, any>[]> => {
    const { data } = await axiosClient.get(
      "/analytics/revenue-by-shift-period",
    );
    return data;
  },

  getRevenueByChannel: async (params: {
    from: string;
    to: string;
  }): Promise<AnalyticsByChannelResponse> => {
    const { data } = await axiosClient.get("/analytics/reveunueByChannel", {
      params,
    });
    return data;
  },
};
