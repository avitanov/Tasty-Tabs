// src/types/api.ts

// Enum for user roles, centralizing the role definitions.
export enum UserType {
  CUSTOMER = "CUSTOMER",
  MANAGER = "MANAGER",
  FRONT_STAFF = "FRONT_STAFF",
  BACK_STAFF = "BACK_STAFF",
  USER = "USER",
  EMPLOYEE = "EMPLOYEE",
}

// DTOs (Data Transfer Objects)

export interface UserDto {
  id: number;
  email: string;
  street?: string;
  city?: string;
  phone_number?: string;
  user_type: UserType;
}

export interface AuthDto {
  token: string;
  user: UserDto;
}

export interface AuthRequest {
  username?: string; // API docs say username, but likely email
  password?: string;
}

export interface OrderItemDto {
  id: number;
  quantity: number;
  price: number;
  is_processed: boolean;
  timestamp: string;
  product_id: number;
  product_name: string;
}

export interface OrderDto {
  id: number;
  timestamp: string;
  status: string; // e.g., "PENDING", "IN_PROGRESS", "COMPLETED"
  type: string; // "ONLINE" or "TAB"
  customer_name?: string;
  delivery_address?: string;
  table_number?: number;
  front_staff_name?: string;
  order_items: OrderItemDto[];
}

export interface CreateOrderDto {
  order_items: CreateOrderItemDto[];
  status: string;
  type: string;
  delivery_address?: string;
  table_number?: number;
}

export interface CreateOrderItemDto {
  product_id: number;
  quantity: number;
  price: number;
  is_processed: boolean;
}

export interface ReservationDto {
  id: number;
  stay_length: number;
  datetime: string;
  creation_timestamp: string;
  number_of_people: number;
  assigned_table_number?: number;
  front_staff_name?: string; // Assuming manager email
  status: "PENDING" | "ACCEPTED" | "CANCELLED" | "COMPLETED";
  email: string; // Assuming user email
}

export interface CreateReservationDto {
  stay_length: number;
  datetime: string;
  number_of_people: number;
}

export interface ShiftDto {
  id: number;
  date: string;
  start: string;
  end: string;
}

export interface AssignmentDto {
  id: number;
  clock_in_time?: string;
  clock_out_time?: string;
  manager: ManagerDto;
  employee: EmployeeDto;
  shift: ShiftDto;
}

export interface CreateAssignmentDto {
  employee_id: number;
  shift_id: number;
}

// src/types/api.ts (add these interfaces)

export interface CreateShiftDto {
  date: string; // "YYYY-MM-DD"
  start: string; // "YYYY-MM-DDTHH:mm:ss"
  end: string; // "YYYY-MM-DDTHH:mm:ss"
}

export interface CreateEmployeeRequest {
  email: string;
  password?: string;
  street?: string;
  city?: string;
  phone_number?: string;
  net_salary: number;
  gross_salary: number;
  employee_type: "MANAGER" | "FRONT_STAFF" | "BACK_STAFF";
  staff_role_id?: number;
  tip_percent?: number;
}

export interface EmployeeDto {
  id: number;
  email: string;
  street: string;
  city: string;
  phone_number: string;
  net_salary: number;
  gross_salary: number;
  user_type: UserType;
}

// src/types/api.ts (add these interfaces)

export interface CategoryDto {
  id: number;
  name: string;
  is_available: boolean;
}

export interface CreateCategoryDto {
  name: string;
  is_available: boolean;
}

export interface ProductDto {
  id: number;
  name: string;
  price: number;
  description: string;
  category: CategoryDto;
}

export interface CreateProductDto {
  name: string;
  price: number;
  description: string;
  category_id: number;
  tax_class: string; // e.g., "STANDARD", "REDUCED"
  manage_inventory: boolean;
  quantity?: number;
  restock_level?: number;
}

export interface ShiftDto {
  id: number;
  date: string; // "YYYY-MM-DD"
  start: string; // ISO DateTime string "YYYY-MM-DDTHH:mm:ss"
  end: string; // ISO DateTime string "YYYY-MM-DDTHH:mm:ss"
}

export interface ManagerDto {
  id: number;
  email: string;
}

// src/types/api.ts (add this interface)

export interface RegisterRequest {
  email: string;
  password?: string;
  password_confirmation?: string;
  first_name?: string;
  last_name?: string;
}

export interface DailyOpsDto {
  operation_date: string;
  total_reservations: number;
  total_orders: number;
  unique_customers: number;
  active_employees: number;
  daily_revenue: number;
}

export interface TopProductDto {
  product_name: string;
  category_name: string;
  total_quantity_sold: number;
  total_revenue: number;
}

export interface ServerPerformanceDto {
  server_email: string;
  total_assignments: number;
  orders_processed: number;
  total_revenue_generated: number;
  avg_order_value: number;
}

// src/types/api.ts (add these interfaces)

export interface RevenueSplitDto {
  order_type: string;
  total_revenue: number;
}

export interface MonthlyRevenueVsLaborDto {
  period: string; // e.g., "2025-08"
  total_revenue: number;
  total_labor_cost: number;
}

export interface ManagerShiftAboveAvgDto {
  manager_email: string;
  shift_date: string;
  shift_revenue: number;
  shift_start_time: string;
  shift_end_time: string;
  avg_revenue_per_shift: number;
}

export interface CreatePaymentDto {
  tip_amount: number;
  payment_type: string; // "CASH" or "CARD"
  amount: number;
  order_id: number;
}

export interface PaymentsDailyChannelDto {
  day: string; // LocalDate
  channel: string;
  paid_orders_cnt: number;
  revenue: number;
  tip_total: number;
}

export interface ChannelTableDto {
  channel: string;
  data: PaymentsDailyChannelDto[];
  total_paid_orders: number;
  total_revenue: number;
  total_tips: number;
}

export interface AnalyticsByChannelResponse {
  from: string; // LocalDate
  to: string; // LocalDate
  channels: ChannelTableDto[];
  grand_total_paid_orders: number;
  grand_total_revenue: number;
  grand_total_tips: number;
}
