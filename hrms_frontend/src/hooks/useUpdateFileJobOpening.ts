import { useMutation } from "@tanstack/react-query"
import { updateJobDescriptionFileApi } from "../api/JobOpeningApi";
import { toast } from "react-toastify";

export const useUpdateFileJobOpening = () => {
    return useMutation({
        mutationFn: ({
            jobOpeningId, file}: {
                jobOpeningId: string; file: File;
            }) => updateJobDescriptionFileApi(jobOpeningId, file),
        
        onSuccess: () =>{
            toast.success("Job description file update successfully"); 
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Failed to update file");
        }
    })
}