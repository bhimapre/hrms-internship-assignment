import { useMutation } from "@tanstack/react-query";
import { updateGameConfigApi } from "../../api/gameConfigApi";
import { toast } from "react-toastify";

export const useUpdateGameConfig = () => {
    return useMutation({
        mutationFn: ({ configId, data, }: {
            configId: string; data: any;
        }) => updateGameConfigApi(configId, data),

        onSuccess: () => {
            toast.success("Game Config updated successfully");
        },

        onError: (err: any) => {
            toast.error(err?.res?.data?.message || "Failed to update Game");
        }
    });
}