import { useQuery } from "@tanstack/react-query";
import { fetchOrgChartBySearch } from "../../api/orgChartApi";

export const useOrgChartBySearch = (employeeId: string) =>{
    return useQuery({
        queryKey: ["org-chart", employeeId],
        queryFn: () => fetchOrgChartBySearch(employeeId),
        enabled: !!employeeId
    });
}