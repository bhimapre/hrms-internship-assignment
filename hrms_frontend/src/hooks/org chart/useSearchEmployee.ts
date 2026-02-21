import { useQuery } from "@tanstack/react-query";
import { fetchSearchEmployees } from "../../api/orgChartApi";

export const useSearchEmployee = (query: string) => {
    return useQuery({
        queryKey: ["employee-search", query],
        queryFn: () => fetchSearchEmployees(query),
        enabled: query.length >= 2
    });
}