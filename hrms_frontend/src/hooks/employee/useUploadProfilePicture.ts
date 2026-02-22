import { useMutation, useQueryClient } from "@tanstack/react-query";
import { uploadProfilePicture } from "../../api/employeeApi";
import { toast } from "react-toastify";

export const useUploadProfilePicture = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: uploadProfilePicture,

        onSuccess: () => {
            toast.success("Your profile picture added successfully");
            queryClient.invalidateQueries({ queryKey: ["currentEmployee"] });
        },

        onError: (err: any) =>{
            toast.error(err?.response?.data?.message || "Something went wrong");
        }
    });
};