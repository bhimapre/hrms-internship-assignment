import { useQuery } from "@tanstack/react-query"
import { fetchAllActiveEmployee } from "../../api/employeeApi"

export const useGetActiveEmployee = () =>{
    return useQuery({
        queryKey: ["active-employees"],
        queryFn: fetchAllActiveEmployee
    });
}