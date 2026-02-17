import { useMutation, useQueryClient } from "@tanstack/react-query"
import { deleteJobOpeningApi } from "../api/JobOpeningApi"
import { toast } from "react-toastify"

export const useDeleteJobOpening = () => {
    const querClient = useQueryClient();

    const mutation = useMutation({

        mutationFn: (jobOpeningId: string) => deleteJobOpeningApi(jobOpeningId),

        onSuccess: () => {
            toast.success("Job Oepning Closed");
            querClient.invalidateQueries({
                queryKey: ["hr-job-openings"]
            });
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Somthing went wrong");
        }
    })

    // Handle Delete or Soft Delete Job Opening Change The Status to Closed
    const handleDelete = (jobOpeningId: string) => {
        const confirmDelete = window.confirm("Are you sure you want to closed the job opening ?");
        if (!confirmDelete) return;
        mutation.mutate(jobOpeningId);
    }

    return{
        handleDelete,
    }
}


