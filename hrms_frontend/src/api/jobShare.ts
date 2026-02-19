import type { JobshareRequest } from "../types/JobShare";
import api from "./axios";


export const createJobShareApi = async(data: JobshareRequest) =>{
    const res = await api.post(`/api/job-share`, data);
    return res.data;
}