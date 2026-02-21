import { useMutation } from "@tanstack/react-query";
import { updateTravelExpenseApi } from "../../api/travelExpenseApi";
import { toast } from "react-toastify";


export const useUpdateTravelExpnese = () => {
    return useMutation({
        mutationFn: ({ expenseId, data, }: {
            expenseId: string; data: any;
        }) => updateTravelExpenseApi(expenseId, data),

        onSuccess: () => {
            toast.success("Travel expense updated successfully");
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Failed to update Travel expense");
        }
    });
}