import { useQuery } from "@tanstack/react-query";
import { fetchUpcomingGameBookings } from "../../api/gameBooking";

export const useUpcomingGameBookings = () => {
    return useQuery({
        queryKey: ["upcomingGameBookings"],
        queryFn: fetchUpcomingGameBookings,
    });
}