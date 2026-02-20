import { useMutation, useQueryClient } from "@tanstack/react-query";
import type { CreateTimeSlot } from "../../types/TimeSlot";
import { createTimeSlotApi } from "../../api/timeSlot";
import { toast } from "react-toastify";

export const useCreateTimeSlot = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({gameId ,data}:{gameId:string, data: CreateTimeSlot}) => createTimeSlotApi(gameId ,data),

        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["games"],
            });
            toast.success("Time Slot creation suceessfully");
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Something went wrong");
        }
    });
}