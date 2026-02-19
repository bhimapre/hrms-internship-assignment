import { useQuery } from "@tanstack/react-query";
import { fetchTravelByIdApi } from "../../api/travel";

export const useGetTravelById = (travelId?: string) =>{
    return useQuery({
        queryKey: ["travel", travelId],
        queryFn: () => fetchTravelByIdApi(travelId!),
        enabled: !!travelId
    });
}