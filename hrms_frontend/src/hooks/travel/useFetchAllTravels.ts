import { useQuery } from "@tanstack/react-query";
import { fetchAllTravelsApi } from "../../api/travel";

export const useFetchAllTravels = (page: number, size = 5) => {
    return useQuery({
        queryKey: ["all-travels", page],
        queryFn: () => fetchAllTravelsApi(page, size),
        placeholderData: (prev) => prev
    });
}