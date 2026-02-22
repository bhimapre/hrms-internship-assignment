import { useMutation, useQueryClient } from "@tanstack/react-query"
import { data } from "react-router-dom";
import type { JoinWaitingQueue } from "../../types/WaitingQueue";
import { joinWaitingQueue } from "../../api/waititngQueue";
import { toast } from "react-toastify";


export const useJoinWaitingQueue = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (data: JoinWaitingQueue) => joinWaitingQueue(data),

        onSuccess: () => {
            toast.success("Joined waiting queue successfully")
            queryClient.invalidateQueries({ queryKey: ["timeSlots"] });
            queryClient.invalidateQueries({ queryKey: ["waitingQueue"] });
            queryClient.invalidateQueries({ queryKey: ["myBookings"] });
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Failed to join queue");
        },
    })
}