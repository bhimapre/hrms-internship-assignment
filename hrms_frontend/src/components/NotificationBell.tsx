import React, { useState } from 'react'
import { useUnreadCount } from '../hooks/notifications/useUnreadCount ';
import { Bell } from 'lucide-react';
import NotificationDropdown from './NotificationDropdown';

const NotificationBell = () => {
    const { data: unread = 0 } = useUnreadCount();
    const [open, setOpen] = useState(false);
    return (
        <div className="relative">
            <button onClick={() => setOpen(!open)} className="relative">
                <Bell className="w-6 h-6 text-gray-300" />
                {unread > 0 && (
                    <span className="absolute -top-1 -right-1 bg-red-500 text-xs text-white px-1.5 rounded-full">
                        {unread}
                    </span>
                )}
            </button>

            {open && <NotificationDropdown />}
        </div>
    )
}

export default NotificationBell