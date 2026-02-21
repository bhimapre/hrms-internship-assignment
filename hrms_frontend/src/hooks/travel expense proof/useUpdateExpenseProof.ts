import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateExpenseProof } from "../../api/travelExpenseProof";
import { toast } from "react-toastify";

export const useUpdateExpenseProof = () =>{

    const queryClient = useQueryClient();

    return useMutation({
        mutationFn:({
            expenseProofId, file
        }: {expenseProofId: string, file:File}) => updateExpenseProof(expenseProofId, file),

        onSuccess: () =>{
            toast.success("Expense proof uploaded successfully");
            queryClient.invalidateQueries({queryKey: ["expense-proof"]})
        },

        onError: (err:any) => {
            toast.error(err?.response?.data?.message ||"Failed to update file");
        }
    })
}