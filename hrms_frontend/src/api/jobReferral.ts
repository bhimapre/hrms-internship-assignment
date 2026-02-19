import type { FetchAllJobReferrals, PageResponse, UpdateJobReferral } from "../types/JobReferral";
import api from "./axios"


// Add Job Referral
export const addJobReferral = async (jobOpeningId: string, formData: FormData) => {
    const res = await api.post(`/api/job-referral/${jobOpeningId}`, formData);
    return res.data;
}

// Fetch All Job Referral
export const fetchAllJobReferrals = async (page: number, size: number):
    Promise<PageResponse<FetchAllJobReferrals>> => {
    const res = await api.get(`/api/hr/job-referral`, {
        params: { page, size }
    });
    return res.data;
}

// Update Job Referral
export const updateJobReferralApi = async (jobReferralId: string, data: UpdateJobReferral) => {
    const res = await api.put(`/api/job-referral/${jobReferralId}`, data);
    return res.data;
}

// Fetch Job Referral By Id
export const getJobReferralById = async (jobReferralId: string) => {
    const res = await api.get(`/api/job-referral/${jobReferralId}`);
    return res.data;
}

// Update Job Referral CV
export const updateJobReferralCV = async (jobReferralId: string, file: File) => {
    const formData = new FormData();
    formData.append("file", file);

    const res = await api.put(`/api/job-referral-cv/${jobReferralId}`, formData);
    return res.data;
}

// Soft Delete Job Referral
export const deleteJobReferral = async (jobReferralId: string) => {
    const res = await api.patch(`/api/job-referral/${jobReferralId}`);
    return res.data;
}