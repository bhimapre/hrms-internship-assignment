import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateGamePreferences } from "../../api/employeeApi";
import { toast } from "react-toastify";

export const useUpdateGamePreferences = (employeeId: string) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (gameIds: string[]) =>
      updateGamePreferences(employeeId, gameIds),

    onSuccess: () => {
      toast.success("Game Preference added successfully");
      queryClient.invalidateQueries({queryKey: ["currentEmployee"],});
    },
    onError: (err: any) =>{
      toast.error(err?.response?.data?.message || "Somthing went wrong")
    }
  })
}