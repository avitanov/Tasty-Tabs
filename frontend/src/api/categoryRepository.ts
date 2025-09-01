// src/api/categoryRepository.ts
import axiosClient from "./axiosClient";
import type { CategoryDto, CreateCategoryDto } from "../types/api";

export const categoryRepository = {
  getAllCategories: async (): Promise<CategoryDto[]> => {
    const { data } = await axiosClient.get("/categories");
    return data;
  },
  createCategory: async (
    categoryData: CreateCategoryDto,
  ): Promise<CategoryDto> => {
    const { data } = await axiosClient.post("/categories/add", categoryData);
    return data;
  },
  deleteCategory: async (categoryId: number): Promise<void> => {
    await axiosClient.delete(`/categories/delete/${categoryId}`);
  },
};
