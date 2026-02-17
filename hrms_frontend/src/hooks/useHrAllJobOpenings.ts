import { useQuery } from "@tanstack/react-query"
import { fetchAllJobsForHRApi } from "../api/JobOpeningApi"

export const useHrAllJobOpenings = (page: number, size = 5) => {
    return useQuery({
        queryKey: ["hr-job-openings", page],
        queryFn: () => fetchAllJobsForHRApi(page, size),
        placeholderData: (prev) => prev
    });
}