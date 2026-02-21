import { useMutation } from "@tanstack/react-query";
import { updateJobReferralCV } from "../../api/jobReferral";
import { toast } from "react-toastify";

export const useUpdateJobReferralCV = () => {
    return useMutation({
        mutationFn: ({
            jobReferralId, file}: {
                jobReferralId: string; file: File;
            }) => updateJobReferralCV(jobReferralId, file),
        
        onSuccess: () =>{
            toast.success("Job referral CV update successfully"); 
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Failed to update file");
        }
    })
}