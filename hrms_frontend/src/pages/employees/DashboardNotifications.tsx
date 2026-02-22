import React from 'react'
import NotificationItem from './NotificationItem';
import { useMyNotifications } from '../../hooks/notifications/useMyNotifications';

const DashboardNotifications = () => {

    const { data: notifications = [] } = useMyNotifications();

    return (
        <div className="bg-neutral-900 border border-neutral-800 rounded-lg flex flex-col h-87.5">
            <div className="px-4 py-3 border-b border-neutral-800">
                <h3 className="text-sm font-semibold">Notifications</h3>
            </div>

            <div className="flex-1 overflow-y-auto">
                {notifications.length === 0 && (
                    <p className="p-4 text-sm text-neutral-400">
                        No notifications
                    </p>
                )}

                {notifications.slice(0, 10).map((n: any) => (
                    <NotificationItem
                        key={n.notificationId}
                        notification={n}/>
                ))}
            </div>
        </div>
    )
}

export default DashboardNotifications