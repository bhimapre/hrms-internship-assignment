import { useQuery } from "@tanstack/react-query";
import type { ExpenseFilterParams } from "../../types/TravelExpense";
import { fetchHrExpenses } from "../../api/travelExpenseApi";

export const useHrExpenseFilter = (filters: ExpenseFilterParams) => {
  return useQuery({
    queryKey: ["hr-expenses", filters],
    queryFn: () => fetchHrExpenses(filters),
    enabled: true,
  });
};
