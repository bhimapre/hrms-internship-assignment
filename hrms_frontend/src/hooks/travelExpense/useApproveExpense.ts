import { useMutation, useQueryClient } from "@tanstack/react-query";
import { approveTravelExpenseApi } from "../../api/travelExpenseApi";
import { toast } from "react-toastify";

export const useApproveExpense = () => {

    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({ expenseId, remark, }: {
            expenseId: string; remark: string;
        }) => approveTravelExpenseApi(expenseId, remark),

        onSuccess: () => {
            toast.success("Travel Expense Approved successfully");
            queryClient.invalidateQueries({ queryKey: ["hr-expenses"]});
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Failed to approved Travel expense");
        },
    });
}