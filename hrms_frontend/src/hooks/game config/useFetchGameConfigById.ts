import { useQuery } from "@tanstack/react-query";
import { fetchGameById } from "../../api/gameApi";
import { fetchGameConfigByIdApi } from "../../api/gameConfigApi";

export const useFetchGameConfigById = (configId?: string) =>{
    return useQuery({
        queryKey: ["game-config", configId],
        queryFn: () => fetchGameConfigByIdApi(configId!),
        enabled: !!configId
    });
}