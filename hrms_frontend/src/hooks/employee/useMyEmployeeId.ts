import { useQuery } from "@tanstack/react-query"
import { fetchMyEmployeeId } from "../../api/employeeApi"


export const useMyEmployeeId = () =>{
    return useQuery({
        queryKey: ["my-employee"],
        queryFn: fetchMyEmployeeId
    });
}