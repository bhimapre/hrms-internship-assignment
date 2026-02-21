import { useQuery } from "@tanstack/react-query";
import { fetchMyOrgChart } from "../../api/orgChartApi";

export const useMyOrgChart = () =>{
    return useQuery({
        queryKey: ["org-chart"],
        queryFn: fetchMyOrgChart
    });
}

