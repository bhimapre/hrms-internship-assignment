import { useQuery } from "@tanstack/react-query";
import { fetchTotalExpenseApproved } from "../../api/travelExpenseApi";

export const useTotalTravelExpenseApproved = (travelId?: string) =>{
    return useQuery({
        queryKey: ["total-expense", travelId],
        queryFn: () => fetchTotalExpenseApproved(travelId!),
        enabled: !!travelId
    });
}