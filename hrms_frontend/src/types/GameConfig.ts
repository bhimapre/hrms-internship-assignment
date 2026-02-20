export interface GameConfigBase {
    gameId: string;
    configStartTime: string;
    configEndTime: string;
    slotDuration: number;
    maxPlayers: number;
    active: boolean;
    gameName: string;
}

export interface FetchGameConfig extends GameConfigBase {
    configId?: string;
}   