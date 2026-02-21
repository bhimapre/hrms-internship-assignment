import type { CreateTimeSlot, GetTimeSlotToday } from "../types/TimeSlot";
import api from "./axios";


// Create Time Slot for Game
export const createTimeSlotApi = async (gameId: string ,data: CreateTimeSlot):
    Promise<any> => {
    const res = await api.post(`/api/hr/slots-generate/${gameId}`, data)
    return res.data;
}

// Shows Today's Game Slots
export const getTimeSlotForGame = async(gameId: string):
Promise<GetTimeSlotToday[]> =>{
    const res = await api.get(`/api/game-slots/${gameId}`)
    console.log(res.data);
    return res.data;
}