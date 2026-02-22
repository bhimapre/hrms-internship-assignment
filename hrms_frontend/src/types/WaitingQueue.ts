export interface JoinWaitingQueue{
    gameId: string;
    timeSlotId: string;
}

export interface waitingQueueResponse extends JoinWaitingQueue{
    queueId: string;
}