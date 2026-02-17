import { useMutation } from "@tanstack/react-query"
import { updateJobOpeningApi } from "../api/JobOpeningApi";
import { toast } from "react-toastify";


export const useUpdateJobOpening = () => {
    return useMutation({
        mutationFn: ({ jobOpeningId, data, }: {
            jobOpeningId: string; data: any;
        }) => updateJobOpeningApi(jobOpeningId, data),

        onSuccess: () => {
            toast.success("Job Opening updated successfully");
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Failed to update job opening");
        }
    });
}