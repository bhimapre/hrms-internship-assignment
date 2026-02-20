import type { FetchGame, GameBase } from "../types/Game";
import api from "./axios";

// Create Game 
export const createGameApi = async (data: GameBase):
    Promise<any> => {
    const res = await api.post(`/api/hr/add-game`, data)
    return res.data;
}

// Fetch All Games
export const fetchAllGames = async ():
    Promise<FetchGame[]> => {
    const res = await api.get(`/api/game`);
    return res.data;
}

// Fetch Game By ID
export const fetchGameById = async (gameId: string):
    Promise<FetchGame> => {
    const res = await api.get(`/api/game/${gameId}`);
    return res.data;
}

// Soft Delete Game
export const deleteGame = async (gameId: string) => {
    const res = await api.put(`/api/game/delete/${gameId}`);
    return res.data;
}

// Update Game
export const updateGameApi = async (gameId: string, data: FetchGame) => {
    const res = await api.put(`/api/game/${gameId}`, data);
    return res.data;
}