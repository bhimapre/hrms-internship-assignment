import React from 'react'
import { useUpcomingGameBookings } from '../../hooks/game booking/useUpcomingGameBookings';

const DashboardUpcomingBookings = () => {

    const { data: bookings = [] } = useUpcomingGameBookings();

    return (
        <div className="bg-neutral-900 border border-neutral-800 rounded-lg flex flex-col h-87.5">
            {/* Header */}
            <div className="px-4 py-3 border-b border-neutral-800">
                <h3 className="text-sm font-semibold">
                    Upcoming Game Bookings
                </h3>
            </div>

            <div className="flex-1 overflow-y-auto">
                {bookings.length === 0 && (
                    <p className="p-4 text-sm text-neutral-400">
                        No upcoming bookings
                    </p>
                )}

                {bookings.slice(0, 10).map((b: any) => (
                    <div
                        key={b.gameBookingId}
                        className="px-4 py-3 border-b border-neutral-800 text-sm">
                        <p className="font-medium">{b.gameName}</p>

                        <p className="text-xs text-neutral-400 mt-1">
                            {new Date(b.slotDate).toLocaleDateString()} •{" "}
                            {b.startTime} - {b.endTime}
                        </p>

                        <div className="flex gap-1 flex-wrap mt-2">
                            {b.participants?.map((p: any) => (
                                <span
                                    key={p.employeeId}
                                    className="px-2 py-0.5 text-xs bg-neutral-800 rounded">
                                    {p.name}
                                </span>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default DashboardUpcomingBookings