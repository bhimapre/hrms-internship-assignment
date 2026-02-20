import { useQuery } from "@tanstack/react-query";
import { fetchGameById } from "../../api/gameApi";

export const useFetchGameById = (gameId?: string) =>{
    return useQuery({
        queryKey: ["game", gameId],
        queryFn: () => fetchGameById(gameId!),
        enabled: !!gameId
    });
}