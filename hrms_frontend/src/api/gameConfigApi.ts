import type { FetchGameConfig, GameConfigBase } from "../types/GameConfig";
import api from "./axios";


// Create Game Config
export const createGameConfigApi = async (data: GameConfigBase):
    Promise<any> => {
    const res = await api.post(`/api/hr/add-game-time-slot-config`, data)
    return res.data;
}

// Fetch All Game Configs
export const fetchAllGameConfigsApi = async ():
    Promise<FetchGameConfig[]> => {
    const res = await api.get(`api/hr/game-config`);
    return res.data;
}

// Fetch Game Config By Id
export const fetchGameConfigByIdApi = async (configId: string):
    Promise<FetchGameConfig> => {
    const res = await api.get(`api/hr/game-config/${configId}`);
    return res.data;
}

// Update Game Config
export const updateGameConfigApi = async (configId: string, data: FetchGameConfig) => {
    const res = await api.put(`/api/hr/game-time-slot-config/${configId}`, data);
    return res.data;
}