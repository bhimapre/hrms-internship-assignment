import { useMutation } from "@tanstack/react-query"
import { createJobOpeningApi } from "../api/JobOpeningApi"
import { data } from "react-router-dom";
import { toast } from "react-toastify";

export const useAddJobopening = () => {
    return useMutation({
        mutationFn: (formData: FormData) =>
            createJobOpeningApi(formData),

        onSuccess: () => {
            toast.success("Job opening created successfully");
        },

        onError: (err: any) => {
            toast.error(err?.response?.data?.message || "Something went wrong");
        }
    });
};

