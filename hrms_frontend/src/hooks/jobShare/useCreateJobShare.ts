import { useMutation } from "@tanstack/react-query"
import type { JobshareRequest } from "../../types/JobShare"
import { createJobShareApi } from "../../api/jobShare"
import { toast } from "react-toastify"

export const useCreateJobShare = () =>{
    return useMutation({
        mutationFn: (data: JobshareRequest) => createJobShareApi(data),

        onSuccess: () => {
            toast.success("Job shared successfully");
        },

        onError: (err:any) => {
            toast.error(err?.response?.data?.message || "Something went wrong");
        }
    })
}