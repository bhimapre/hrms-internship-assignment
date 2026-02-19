import { useMutation } from "@tanstack/react-query";
import api from "../api/axios";


interface LoginRequest{
    username: string;
    password: string;
}

type UserRole = "HR" | "MANAGER" | "EMPLOYEE"

interface LoginResponse{
    accessToken: string;
    role: UserRole;
}

export const useLogin = () => {
    return useMutation({
        mutationFn: async (data: LoginRequest):
        Promise<LoginResponse> => {
            const response = await api.post("/auth/login", data);
            return response.data
        },
    });
};