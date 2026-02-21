import { useMutation, useQueryClient } from "@tanstack/react-query";
import  { deleteExpenseApi } from "../../api/travelExpenseApi";
import { toast } from "react-toastify";


export const useExpenseDelete = () => {
    const querClient = useQueryClient();
    const mutation = useMutation({

        mutationFn: (expenseId: string) => deleteExpenseApi(expenseId),

        onSuccess: () => {
            toast.success("Travel Expense Closed");
            querClient.invalidateQueries({
                queryKey: ["expense"]
            });
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Somthing went wrong");
        }
    })

    // Handle Delete or Soft Delete Travel Expense Change The Status to Closed
    const handleDelete = (expenseId: string) => {
        const confirmDelete = window.confirm("Are you sure you want to cancelled the Travel Expense ?");
        if (!confirmDelete) return;
        mutation.mutate(expenseId);
    }

    return{
        handleDelete,
    }
}