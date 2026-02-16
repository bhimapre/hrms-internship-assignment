import { useMutation } from "@tanstack/react-query";
import api from "../api/axios";


interface LoginRequest{
    username: string;
    password: string;
}

interface LoginResponse{
    accessToken: string;
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