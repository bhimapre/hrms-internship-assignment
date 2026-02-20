export interface GameBase{
    gameName: string;
}


export interface FetchGame extends GameBase{
    gameId?: string;
    active: boolean;
}