import { useMutation } from "@tanstack/react-query";
import { updateTravelDocument } from "../../api/travelDocumentApi";
import { toast } from "react-toastify";


export const useUpdateTravelDocumentDetails = () => {
    return useMutation({
        mutationFn: ({ travelDocumentId, data, }: {
            travelDocumentId: string; data: any;
        }) => updateTravelDocument(travelDocumentId, data),

        onSuccess: () => {
            toast.success("Travel Document details updated successfully");
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Failed to update Travel Document details");
        }
    });
}