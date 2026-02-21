import { useMutation, useQueryClient } from "@tanstack/react-query"
import { createTravelApi } from "../../api/travel";
import { toast } from "react-toastify";
import type { CreateTravelRequest } from "../../types/Travel";


export const useCreateTravel = () =>{
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (data: CreateTravelRequest) =>createTravelApi(data),

        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["travels"],
            });
            toast.success("Travel booking suceessfully");
        },

        onError: (err: any) =>{
            toast.error(err?.response?.data?.message || "Something went wrong");
        }
    });
}