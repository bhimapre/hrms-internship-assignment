import { useMutation } from "@tanstack/react-query";
import { updateTravelApi } from "../../api/travel";
import { toast } from "react-toastify";

export const useUpdateTravel = () => {
    return useMutation({
        mutationFn: ({ travelId, data, }: {
            travelId: string; data: any;
        }) => updateTravelApi(travelId, data),

        onSuccess: () => {
            toast.success("Travel updated successfully");
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Failed to update Travel");
        }
    });
}