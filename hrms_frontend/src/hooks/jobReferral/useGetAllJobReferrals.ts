import { useQuery } from "@tanstack/react-query"
import type { PageResponse } from "../../types/JobOpening"
import type { FetchAllJobReferrals } from "../../types/JobReferral"
import { fetchAllJobReferrals } from "../../api/jobReferral"


export const useGetAllJobReferrals = (page: number, size: 5) =>{
    return useQuery<PageResponse<FetchAllJobReferrals>>({
        queryKey: ["job-referrals", page],
        queryFn: () => fetchAllJobReferrals(page, size)
    })
}