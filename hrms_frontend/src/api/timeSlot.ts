import type { CreateTimeSlot } from "../types/TimeSlot";
import api from "./axios";


// Create Time Slot for Game
export const createTimeSlotApi = async (gameId: string ,data: CreateTimeSlot):
    Promise<any> => {
    const res = await api.post(`/api/hr/slots-generate/${gameId}`, data)
    return res.data;
}