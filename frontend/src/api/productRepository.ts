// src/api/productRepository.ts
import axiosClient from "./axiosClient";
import type { ProductDto, CreateProductDto } from "../types/api";

export const productRepository = {
  getAllProducts: async (): Promise<ProductDto[]> => {
    const { data } = await axiosClient.get("/products");
    return data;
  },
  createProduct: async (productData: CreateProductDto): Promise<ProductDto> => {
    const { data } = await axiosClient.post("/products/add", productData);
    return data;
  },
  deleteProduct: async (productId: number): Promise<void> => {
    await axiosClient.delete(`/products/delete/${productId}`);
  },
};
