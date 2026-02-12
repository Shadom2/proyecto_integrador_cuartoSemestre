export type Priority = 'HIGH' | 'MEDIUM' | 'LOW';
export type Status = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';

export interface StudentRequest {
  firstName: string;
  lastName: string;
  email: string;
}

export interface StudentResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  createdAt: string;
}

export interface TaskRequest {
  title: string;
  description?: string;
  dueDate: string; // ISO 8601
  priority: Priority;
  status?: Status;
  studentId: number;
}

export interface TaskResponse {
  id: number;
  title: string;
  description?: string;
  dueDate: string;
  priority: Priority;
  status: Status;
  studentId: number;
  createdAt: string;
}

export interface SubTaskRequest {
  title: string;
  description?: string;
  status?: Status;
  taskId: number;
}

export interface SubTaskResponse {
  id: number;
  title: string;
  description?: string;
  status: Status;
  taskId: number;
  createdAt: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  student: StudentResponse;
}
