import React from 'react'
import { useMarkAsRead } from '../../hooks/notifications/useMarkAsRead';

const NotificationItem = ({ notification }: any) => {

    const { mutate } = useMarkAsRead();

    return (
        <div className={`p-3 border-b border-neutral-800 ${!notification.isRead ? "bg-neutral-800" : ""}`}>
            <p className="font-medium text-sm">{notification.notificationTitle}</p>
            <p className="text-xs text-neutral-400 mt-1">{notification.message}</p>

            {!notification.isRead && (
                <button
                    onClick={() => mutate(notification.notificationId)}
                    className="mt-2 text-xs text-blue-400 hover:underline">
                    Mark as read
                </button>
            )}
        </div>
    )
}

export default NotificationItem