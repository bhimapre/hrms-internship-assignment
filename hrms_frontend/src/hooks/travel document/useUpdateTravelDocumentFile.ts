import { useMutation } from "@tanstack/react-query";
import { updateTravelDocumentFile } from "../../api/travelDocumentApi";
import { toast } from "react-toastify";

export const useUpdateTravelDocumentFile = () => {
    return useMutation({
        mutationFn: ({
            documentId, file}: {
                documentId: string; file: File;
            }) => updateTravelDocumentFile(documentId, file),
        
        onSuccess: () =>{
            toast.success("Travel Document file update successfully"); 
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Failed to update file");
        }
    })
}