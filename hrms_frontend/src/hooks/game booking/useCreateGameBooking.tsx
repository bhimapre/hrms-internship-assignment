import { useMutation, useQueryClient } from "@tanstack/react-query";
import type { CreateGameBooking } from "../../types/GameBooking";
import { createGameBookingApi } from "../../api/gameBooking";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

export const useCreateGameBooking = () => {
    const queryClient = useQueryClient();
    const navigate = useNavigate();

    return useMutation({
        mutationFn: (data: CreateGameBooking) => createGameBookingApi(data),

        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["games"],
            });
            toast.success("Game Booking creation suceessfully");
            navigate(-1);
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Something went wrong");
        }
    });
}