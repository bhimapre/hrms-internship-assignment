import { useMutation, useQueryClient } from "@tanstack/react-query";
import type { GameBase } from "../../types/Game";
import { createGameApi } from "../../api/gameApi";
import { toast } from "react-toastify";

export const useCreateGame = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (data: GameBase) => createGameApi(data),

        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ["games"],
            });
            toast.success("Game creation suceessfully");
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Something went wrong");
        }
    });
}