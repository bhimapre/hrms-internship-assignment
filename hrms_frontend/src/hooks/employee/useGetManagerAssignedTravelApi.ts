import { useQuery } from "@tanstack/react-query"
import { fetchManagerAssignedEmployee } from "../../api/travelEmployee"


export const useGetManagerAssignedTravelApi = () =>{
    return useQuery({
        queryKey: ["manager-assign-employees"],
        queryFn: fetchManagerAssignedEmployee,
    })
}