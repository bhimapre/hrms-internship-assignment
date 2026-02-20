import { useQuery } from "@tanstack/react-query"
import { fetchAllGameConfigsApi } from "../../api/gameConfigApi"

export const useFetchAllGameConfig = () => {
    return useQuery({
        queryKey: ["all-game-configs"],
        queryFn: () => fetchAllGameConfigsApi(),
    })
}