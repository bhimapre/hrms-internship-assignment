import { useMutation } from "@tanstack/react-query"
import { addJobReferral } from "../../api/jobReferral"
import { toast } from "react-toastify"


export const useAddJobReferral = () => {
    return useMutation({
        mutationFn: ({ jobOpeningId, formData }: { jobOpeningId: string, formData: FormData }) =>
            addJobReferral(jobOpeningId, formData),

        onSuccess: () => {
            toast.success("Job Referral sent Successfull");
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Something went wrong");
        }
    })
}