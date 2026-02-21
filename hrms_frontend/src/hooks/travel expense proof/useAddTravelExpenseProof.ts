import { useMutation } from "@tanstack/react-query";
import { createTravelExpenseProofApi } from "../../api/travelExpenseProof";
import { toast } from "react-toastify";

export const useAddTravelExpenseProof = () =>{
    return useMutation({
        mutationFn:({
            expenseId, file
        }: {expenseId: string, file:File}) => createTravelExpenseProofApi(expenseId, file),

        onSuccess: () =>{
            toast.success("Expense proof uploaded successfully");
        },

        onError: (err:any) => {
            toast.error(err?.response?.data?.message ||"Failed to add file");
        }
    })
}