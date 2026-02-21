import { useQuery } from "@tanstack/react-query"
import { getTimeSlotForGame } from "../../api/timeSlot"

export const useGetTimeSlotToady = (gameId: string) =>{
    return useQuery({
        queryKey: ["time-slot-today"],
        queryFn: () => getTimeSlotForGame(gameId),
        enabled: !!gameId
    })
}