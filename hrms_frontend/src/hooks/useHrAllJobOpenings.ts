import { useQuery } from "@tanstack/react-query"
import { fetchAllJobsForHRApi } from "../api/JobOpeningApi"
import type { GetJobOpening, PageResponse } from "../types/JobOpening";

export const useHrAllJobOpenings = (page: number, size = 5) => {
    return useQuery<PageResponse<GetJobOpening>>({
        queryKey: ["hr-job-openings", page, size],
        queryFn: () => fetchAllJobsForHRApi(page, size),
        placeholderData: (x) => x
    });
}