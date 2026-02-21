import { useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteJobReferral } from "../../api/jobReferral";
import { toast } from "react-toastify";

export const useDeleteJobReferral = () => {
    const querClient = useQueryClient();

    const mutation = useMutation({

        mutationFn: (jobReferralId: string) => deleteJobReferral(jobReferralId),

        onSuccess: () => {
            toast.success("Job Referral Closed");
            querClient.invalidateQueries({
                queryKey: ["hr-job-referral"]
            });
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Somthing went wrong");
        }
    })

    // Handle Delete or Soft Delete Job Referral Change The Status to Closed
    const handleDelete = (jobReferralId: string) => {
        const confirmDelete = window.confirm("Are you sure you want to hold the job referral ?");
        if (!confirmDelete) return;
        mutation.mutate(jobReferralId);
    }

    return{
        handleDelete,
    }
}