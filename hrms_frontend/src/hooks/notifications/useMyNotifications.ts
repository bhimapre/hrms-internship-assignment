import { useQuery } from "@tanstack/react-query";
import { fetchMyNotifications } from "../../api/notificationApi";

export const useMyNotifications = () =>
    useQuery({
        queryKey: ["notifications"],
        queryFn: fetchMyNotifications,
    });