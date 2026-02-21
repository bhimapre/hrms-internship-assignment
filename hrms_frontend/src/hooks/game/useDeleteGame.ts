import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { deleteGame } from "../../api/gameApi";
import { toast } from "react-toastify";

export const useDeleteGame = () => {
    const querClient = useQueryClient();

    const mutation = useMutation({

        mutationFn: (gameId: string) => deleteGame(gameId),

        onSuccess: () => {
            toast.success("Game Inactive sucessfully");
            querClient.invalidateQueries({
                queryKey: ["game"]
            });
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Somthing went wrong");
        }
    })

    // Handle Delete or Soft Delete JGame Change The Status to Closed
    const handleDelete = (gameId: string) => {
        const confirmDelete = window.confirm("Are you sure you want to hold the game ?");
        if (!confirmDelete) return;
        mutation.mutate(gameId);
    }

    return{
        handleDelete,
    }
}