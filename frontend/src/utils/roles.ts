// src/utils/roles.ts
import { UserType } from "../types/api";

export const ROLES = {
  MANAGER: UserType.MANAGER,
  FRONT_STAFF: UserType.FRONT_STAFF,
  BACK_STAFF: UserType.BACK_STAFF,
};

// Define which pages each role can see in the navigation
export const ROLE_PERMISSIONS: Record<UserType, string[]> = {
  [UserType.MANAGER]: [
    "/orders",
    "/reservations",
    "/categories",
    "/products",
    "/shifts",
    "/employees",
    "/assignments",
  ],
  [UserType.FRONT_STAFF]: ["/orders", "/reservations"],
  [UserType.BACK_STAFF]: ["/orders"],
  // Other roles are not part of the admin panel MVP
  [UserType.CUSTOMER]: [],
  [UserType.USER]: [],
  [UserType.EMPLOYEE]: [],
};
