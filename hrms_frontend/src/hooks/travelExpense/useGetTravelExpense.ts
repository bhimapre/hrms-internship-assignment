import { useQuery } from "@tanstack/react-query";
import { fetchExpenseById } from "../../api/travelExpenseApi";

export const useGetTravelExpense = (expenseId?: string) =>{
    return useQuery({
        queryKey: ["expense", expenseId],
        queryFn: () => fetchExpenseById(expenseId!),
        enabled: !!expenseId
    });
}