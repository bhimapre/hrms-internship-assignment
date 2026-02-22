import type { CreateGameBooking } from "../types/GameBooking";
import api from "./axios";

// Create Game 
export const createGameBookingApi = async (data: CreateGameBooking):
    Promise<any> => {
    const res = await api.post(`api/game-booking`, data)
    return res.data;
}

// Fetch Upcoming Game booking
export const fetchUpcomingGameBookings = async () => {
    const res = await api.get("/api/game-booking/upcoming");
    console.log(res.data);
    return res.data;
};

// Cancel game booking
export const cancelGameBooking = async (bookingId: string) => {
    const res = await api.post(`/api/game-booking/cancel/${bookingId}`);
    return res.data;
};