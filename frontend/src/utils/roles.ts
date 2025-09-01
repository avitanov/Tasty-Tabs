// src/utils/roles.ts
import { UserType } from "../types/api";

export const ROLES = {
  MANAGER: UserType.MANAGER,
  FRONT_STAFF: UserType.FRONT_STAFF,
  BACK_STAFF: UserType.BACK_STAFF,
  CUSTOMER: UserType.CUSTOMER,
  USER: UserType.USER,
  EMPLOYEE: UserType.EMPLOYEE,
};

// Define which pages each role can see in the navigation
export const ROLE_PERMISSIONS: Record<UserType, string[]> = {
  [UserType.MANAGER]: [
    "/admin/orders",
    "/admin/reservations",
    "/admin/categories",
    "/admin/products",
    "/admin/shifts",
    "/admin/employees",
    "/admin/assignments",
  ],
  [UserType.FRONT_STAFF]: ["/admin/orders", "/admin/reservations"],
  [UserType.BACK_STAFF]: ["/admin/orders"],
  // Other roles are not part of the admin panel MVP
  [UserType.CUSTOMER]: [],
  [UserType.USER]: [],
  [UserType.EMPLOYEE]: [],
};
