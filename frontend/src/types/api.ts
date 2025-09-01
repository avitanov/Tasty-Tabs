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
