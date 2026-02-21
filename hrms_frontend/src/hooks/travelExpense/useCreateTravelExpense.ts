import { useMutation } from "@tanstack/react-query";
import type { TravelExpenseBase } from "../../types/TravelExpense";
import { createTravelExpenseApi } from "../../api/travelExpenseApi";
import { toast } from "react-toastify";


export const useCreateTravelExpense = () =>{
    return useMutation({
        mutationFn: ({travelId, data}:{travelId: string, data: TravelExpenseBase}) => createTravelExpenseApi(travelId ,data),

        onSuccess: () => {
            toast.success("Travel expense created successfully");
        },

        onError: (err:any) => {
            toast.error(err?.response?.data?.message || "Something went wrong");
        }
    })
}