import { useMutation, useQueryClient } from "@tanstack/react-query";
import type { GameConfigBase } from "../../types/GameConfig";
import { createGameConfigApi } from "../../api/gameConfigApi";
import { toast } from "react-toastify";


export const useCreateGameConfig = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (data: GameConfigBase) => createGameConfigApi(data),

        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["game-config"],
            });
            toast.success("Game Config creation suceessfully");
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Something went wrong");
        }
    });
}