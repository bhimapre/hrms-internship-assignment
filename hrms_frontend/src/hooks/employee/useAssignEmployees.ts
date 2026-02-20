import { useQuery } from "@tanstack/react-query";
import { fetchAssignedTravelsApi } from "../../api/travelEmployee";


export const useAssignEmployees = () =>{
    return useQuery({
        queryKey: ["travel-emlpoyees"],
        queryFn: fetchAssignedTravelsApi
    });
}