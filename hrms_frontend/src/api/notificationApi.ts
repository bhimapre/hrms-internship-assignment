import api from "./axios";


export const fetchMyNotifications = async () => {
  const res = await api.get("/api/notifications/my");
  return res.data;
};

export const fetchUnreadCount = async () => {
  const res = await api.get("/api/notifications/unread-count");
  return res.data;
};

export const markNotificationAsRead = async (notificationId: string) => {
  await api.patch(`/api/notifications/${notificationId}/read`);
};