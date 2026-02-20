import { useMutation, useQuery } from "@tanstack/react-query"
import { submitExpenseApi } from "../../api/travelExpenseApi";
import { toast } from "react-toastify";

export const useSubmitExpense = () =>{
    return useMutation({
        mutationFn: (expenseId: string) => submitExpenseApi(expenseId),

        onSuccess: () => {
            toast.success("Travel expense submited successfully");
        },

        onError: (err:any) => {
            toast.error(err?.res?.data?.message || "Something went wrong");
        }
    })
}