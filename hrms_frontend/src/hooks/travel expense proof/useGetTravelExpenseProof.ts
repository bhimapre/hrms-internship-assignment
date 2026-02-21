import { useQuery } from "@tanstack/react-query";
import { fetchExpenseProof } from "../../api/travelExpenseProof";

export const useGetExpenseProof = (expenseId?: string) =>{
    return useQuery({
        queryKey: ["expense-proof", expenseId],
        queryFn: () => fetchExpenseProof(expenseId!),
        enabled: !!expenseId
    });
}