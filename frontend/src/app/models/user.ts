import { Role } from "./role";

export interface User {
  id: string;
  name: string;
  email: string;
  phoneNumber: string;
  roles: Role[];
  deleted?: boolean;
  deletedAt?: string;
}