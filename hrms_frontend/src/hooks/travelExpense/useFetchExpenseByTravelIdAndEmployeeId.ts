import { useQuery } from "@tanstack/react-query"
import { employeeTravelExpenses } from "../../api/travelExpenseApi"


export const useFetchExpenseByTravelIdAndEmployeeId = (travelId:string, employeeId:string) =>{
    return useQuery({
        queryKey: ["travel-expense-employee", travelId],
        queryFn: () => employeeTravelExpenses(travelId, employeeId),
        enabled: !!travelId,
    })
}