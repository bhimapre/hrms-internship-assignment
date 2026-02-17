import { useQuery } from "@tanstack/react-query"
import { fetchJobOpeningByIdApi } from "../api/JobOpeningApi"

export const useGetJobOpeningByIdHr = (jobOpeningId?: string) =>{
    return useQuery({
        queryKey: ["job-opening", jobOpeningId],
        queryFn: () => fetchJobOpeningByIdApi(jobOpeningId!),
        enabled: !!jobOpeningId
    });
}