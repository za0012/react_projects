export interface User {
  id: number;
  username: string;
  email: string;
  createdAt: string;
  roles: string[];
  admin: boolean;
  manager: boolean;
}

interface LoginData {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user: User;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  data: LoginData;
}
