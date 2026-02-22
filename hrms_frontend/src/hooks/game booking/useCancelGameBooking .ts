import { useMutation, useQueryClient } from "@tanstack/react-query";
import { cancelGameBooking } from "../../api/gameBooking";
import { toast } from "react-toastify";

export const useCancelGameBooking = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (bookingId: string) => cancelGameBooking(bookingId),

        onSuccess: () => {
            toast.success("Booking Cancelled");
            queryClient.invalidateQueries({ queryKey: ["upcomingGameBookings"] });
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Somthing went wrong");
        }
    });
};