import { data } from "react-router-dom";
import type { JoinWaitingQueue, waitingQueueResponse } from "../types/WaitingQueue";
import api from "./axios";

export const joinWaitingQueue = async (data: JoinWaitingQueue):
Promise<waitingQueueResponse> => {
    const res = await api.post("/api/waiting-queue", data);
    return res.data;
}