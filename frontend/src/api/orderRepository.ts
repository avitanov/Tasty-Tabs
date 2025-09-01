// src/api/orderRepository.ts
import axiosClient from "./axiosClient";
// Add CreateOrderItemDto to imports
import type {
  OrderDto,
  CreateOrderDto,
  CreateOrderItemDto,
  OrderItemDto,
} from "../types/api";

// Helper to convert existing order items to the format needed for the update payload
const mapOrderItemsToCreateDto = (
  items: OrderItemDto[],
): CreateOrderItemDto[] => {
  return items.map((item) => ({
    product_id: item.product_id,
    quantity: item.quantity,
    price: item.price,
    is_processed: item.is_processed,
  }));
};

export const orderRepository = {
  getOpenOrders: async (): Promise<OrderDto[]> => {
    const { data } = await axiosClient.get("/orders/open");
    return data;
  },
  // New method to get a single order
  getOrderById: async (orderId: number): Promise<OrderDto> => {
    const { data } = await axiosClient.get(`/orders/${orderId}`);
    return data;
  },
  createTabOrder: async (orderData: CreateOrderDto): Promise<OrderDto> => {
    const { data } = await axiosClient.post("/orders/tab", orderData);
    return data;
  },
  // New method to add items to an existing order
  addItemToOrder: async (
    orderId: number,
    itemData: CreateOrderItemDto,
  ): Promise<void> => {
    // Note: The API docs show adding one item at a time.
    // If you could add multiple, the payload would be CreateOrderItemDto[]
    await axiosClient.post(`/orders/${orderId}/items`, itemData);
  },

  updateOrderStatus: async (orderId: number, status: string): Promise<void> => {
    // The endpoint expects a raw string, not a JSON object
    await axiosClient.patch(`/orders/${orderId}/status`, { status });
  },

  // New method to handle general updates
  updateOrder: async (order: OrderDto): Promise<OrderDto> => {
    const updatePayload: CreateOrderDto = {
      status: order.status,
      type: order.type,
      table_number: order.table_number,
      delivery_address: order.delivery_address,
      order_items: mapOrderItemsToCreateDto(order.order_items),
    };
    const { data } = await axiosClient.put(
      `/orders/${order.id}`,
      updatePayload,
    );
    return data;
  },
};
