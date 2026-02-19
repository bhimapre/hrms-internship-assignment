import { useMutation } from "@tanstack/react-query";
import { addTravelDocument } from "../../api/travelDocumentApi";
import { toast } from "react-toastify";

export const useAddTravelDocument = () => {
    return useMutation({
        mutationFn: ({ travelId, formData }: { travelId: string, formData: FormData }) =>
            addTravelDocument(travelId, formData),

        onSuccess: () => {
            toast.success("Travel Documents added successfully");
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Something went wrong");
        }
    });
};