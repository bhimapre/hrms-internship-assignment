import { QueryClient, useMutation, useQueryClient } from "@tanstack/react-query";
import { rejectTravelExpenseApi } from "../../api/travelExpenseApi";
import { toast } from "react-toastify";

export const useRejectExpense = () => {

    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({ expenseId, remark, }: {
            expenseId: string; remark: string;
        }) => rejectTravelExpenseApi(expenseId, remark),

        onSuccess: () => {
            toast.success("Travel Expense Rejected");
            queryClient.invalidateQueries({ queryKey: ["hr-expenses"]});
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Failed to reject Travel expense");
        },
    });
}