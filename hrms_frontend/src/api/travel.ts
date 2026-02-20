import type { CreateTravelRequest, GetAllTravels, PageResponse, UpdateTravelRequest } from "../types/Travel";
import api from "./axios";


export const createTravelApi = async(data: CreateTravelRequest):
Promise<any> => {
    const res = await api.post(`/api/hr/travels`, data)
    return res.data;
}

// Update Travel Details
export const updateTravelApi = async (travelId: string, data: UpdateTravelRequest) => {
    const res = await api.put(`/api/travels/${travelId}`, data);
    return res.data;
}

// Fetch Travel By Id
export const fetchTravelByIdApi = async (travelId: string) => {
    const res = await api.get(`/api/travels/${travelId}`);
    return res.data;
}

// Fetch All Travels
export const fetchAllTravelsApi = async (page: number, size: number):
    Promise<PageResponse<GetAllTravels>> => {
    const res = await api.get(`/api/travels`, {
        params: { page, size }
    });
    return res.data;
}

// Soft Delete Travel
export const deleteTravelApi = async (travelId: string) => {
    const res = await api.put(`api/travel/cancel/${travelId}`);
    return res.data;
}
