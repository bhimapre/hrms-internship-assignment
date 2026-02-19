import { useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteTravelApi } from "../../api/travel";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

export const useTravelDelete = () => {
    const querClient = useQueryClient();
    const mutation = useMutation({

        mutationFn: (travelId: string) => deleteTravelApi(travelId),

        onSuccess: () => {
            toast.success("Travel Closed");
            querClient.invalidateQueries({
                queryKey: ["travels"]
            });
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Somthing went wrong");
        }
    })

    // Handle Delete or Soft Delete Travel Change The Status to Closed
    const handleDelete = (travelId: string) => {
        const confirmDelete = window.confirm("Are you sure you want to cancelled the Travel ?");
        if (!confirmDelete) return;
        mutation.mutate(travelId);
    }

    return{
        handleDelete,
    }
}