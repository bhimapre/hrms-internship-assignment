export interface CreateTimeSlot{
    fromDate: string;
    toDate: string;
}

export interface GetTimeSlotToday{
    timeSlotId: string;
    gameId: string;
    slotDate: string;
    startTime: string;
    endTime: string;
    status: string;
}