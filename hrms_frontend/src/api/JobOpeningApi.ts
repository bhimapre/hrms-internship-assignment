import axios from "axios";
import type { JobOpeningInterface } from "../pages/Job Opening/AddJobOpening";
import type { GetJobOpening, PageResponse } from "../types/JobOpening";
import api from "./axios";

// Create Job Opening
export const createJobOpeningApi = async (formData: FormData) => {
    const res = await api.post("/api/hr/job-opening", formData);
    return res.data;
}

// Fetch Open Jobs
export const fetchOpenJobsApi = async (page: number, size: number):
    Promise<PageResponse<GetJobOpening>> => {
    const res = await api.get("/api/job-opening", {
        params: { page, size }
    });
    return res.data;
}

// Update Job Opening
export const updateJobOpeningApi = async (jobOpeningId: string, data: JobOpeningInterface) => {
    const res = await api.put(`/api/hr/job-opening/${jobOpeningId}`, data);
    return res.data;
}

// Update Job Opening File
export const updateJobDescriptionFileApi = async (jobOpeningId: string, file: File) => {
    const formData = new FormData();
    formData.append("file", file);

    const res = await api.put(`/api/hr/job-opening/file/${jobOpeningId}`, formData);
    return res.data;
}

// Fetch All Job Openings for HR
export const fetchAllJobsForHRApi = async (page: number, size: number):
    Promise<PageResponse<GetJobOpening>> => {
    const res = await api.get(`/api/hr/job-opening`, {
        params: { page, size }
    });
    return res.data;
}

// Fetch Job Opening By Id
export const fetchJobOpeningByIdApi = async (jobOpeningId: string) => {
    const res = await api.get(`/api/hr/job-opening/${jobOpeningId}`);
    return res.data;
}

// Soft Delete Job Opening
export const deleteJobOpeningApi = async (jobOpeningId: string) => {
    const res = await api.patch(`/api/hr/job-opening/${jobOpeningId}`);
    return res.data;
}