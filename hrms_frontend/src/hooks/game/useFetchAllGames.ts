import { useQuery } from "@tanstack/react-query";
import { fetchAllGames } from "../../api/gameApi";

export const useFetchAllGames = () => {
    return useQuery({
        queryKey: ["all-games"],
        queryFn: () => fetchAllGames(),
    })
}