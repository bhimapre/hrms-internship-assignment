import { useQuery } from "@tanstack/react-query"
import { fetchAssignEmployeeApi } from "../../api/travelEmployee"


export const useAssignEmployeesTravel = (travelId: string) =>{
    return useQuery({
        queryKey: ["assign-employees", travelId],
        queryFn: () => fetchAssignEmployeeApi(travelId),
        enabled: !!travelId,
    })
}