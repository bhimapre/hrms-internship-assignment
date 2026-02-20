import { useQuery } from "@tanstack/react-query"
import { fetchOpenJobsApi } from "../api/JobOpeningApi"
import type { GetJobOpening, PageResponse } from "../types/JobOpening";


export const useJobOpening = (page: number, size = 6) => {
    return useQuery<PageResponse<GetJobOpening>,Error>({
        queryKey: ["job-openings", page, size],
        queryFn:() => fetchOpenJobsApi(page, size),
    });
}