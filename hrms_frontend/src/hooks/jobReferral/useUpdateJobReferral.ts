import { useMutation } from "@tanstack/react-query";
import { updateJobReferralApi } from "../../api/jobReferral";
import { toast } from "react-toastify";


export const useUpdateJobReferral = () => {
    return useMutation({
        mutationFn: ({ jobReferralId, data, }: {
            jobReferralId: string; data: any;
        }) => updateJobReferralApi(jobReferralId, data),

        onSuccess: () => {
            toast.success("Job Referral updated successfully");
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Failed to update job referral");
        }
    });
}