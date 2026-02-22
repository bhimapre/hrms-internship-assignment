import { useQuery } from "@tanstack/react-query";
import { fetchUnreadCount } from "../../api/notificationApi";

export const useUnreadCount = () =>
    useQuery({
        queryKey: ["notifications-unread-count"],
        queryFn: fetchUnreadCount,
        refetchInterval: 15000,
    });