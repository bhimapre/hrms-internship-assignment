import React from 'react'
import { useMyNotifications } from '../hooks/notifications/useMyNotifications';
import NotificationItem from '../pages/employees/NotificationItem';

const NotificationDropdown = () => {

    const { data = [] } = useMyNotifications();

    return (
        <div className="absolute right-0 mt-2 w-96 bg-neutral-900 rounded-lg shadow-lg border border-neutral-800 z-50">
            <div className="p-3 font-semibold border-b border-neutral-800">
                Notifications
            </div>

            <div className="max-h-96 overflow-y-auto">
                {data.length === 0 && (
                    <p className="p-4 text-sm text-neutral-400"> No notifications </p>
                )}

                {data.map((n: any) => (
                    <NotificationItem key={n.notificationId} notification={n} />
                ))}
            </div>
        </div>
    )
}

export default NotificationDropdown