import { useQuery } from "@tanstack/react-query";
import { getJobReferralById } from "../../api/jobReferral";

export const useGetJobReferralById = (jobReferralId?: string) =>{
    return useQuery({
        queryKey: ["job-referral-id", jobReferralId],
        queryFn: () => getJobReferralById(jobReferralId!),
        enabled: !!jobReferralId
    });
}