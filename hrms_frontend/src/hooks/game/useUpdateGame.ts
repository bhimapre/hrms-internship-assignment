import { useMutation } from "@tanstack/react-query";
import { updateGameApi } from "../../api/gameApi";
import { toast } from "react-toastify";

export const useUpdateGames = () => {
    return useMutation({
        mutationFn: ({ gameId, data, }: {
            gameId: string; data: any;
        }) => updateGameApi(gameId, data),

        onSuccess: () => {
            toast.success("Game updated successfully");
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Failed to update Game");
        }
    });
}