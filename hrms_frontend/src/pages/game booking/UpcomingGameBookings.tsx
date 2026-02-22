import React, { useState } from 'react'
import { useUpcomingGameBookings } from '../../hooks/game booking/useUpcomingGameBookings';
import { useCancelGameBooking } from '../../hooks/game booking/useCancelGameBooking ';
import Loading from '../../components/Loading';
import Navbar from '../../components/Navbar';
import Sidebar from '../../components/Sidebar';

const UpcomingGameBookings = () => {

    const { data = [], isLoading } = useUpcomingGameBookings();
    const cancelMutation = useCancelGameBooking();
    const [isCollapsed, setIsCollapsed] = useState(false);

    if (isLoading) {
        return <Loading />
    }

    return (
        <div className="flex flex-col h-screen">
            {/* Navbar */}
            <Navbar />

            {/* Main Layout */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                <Sidebar isCollapsed={isCollapsed} setIsCollapsed={setIsCollapsed} />

                {/* Page Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="bg-neutral-900 border border-neutral-800 rounded-lg">
                        <div className="px-4 py-3 border-b border-neutral-800">
                            <h3 className="text-sm font-semibold text-white">
                                Upcoming Game Bookings
                            </h3>
                        </div>

                        {/* Table Header */}
                        <div className="grid grid-cols-6 gap-4 px-4 py-2 text-xs text-neutral-400 border-b border-neutral-800">
                            <div>Game</div>
                            <div>Date</div>
                            <div>Time</div>
                            <div>Participants</div>
                            <div>Role</div>
                            <div>Action</div>
                        </div>

                        <div className="max-h-65 overflow-y-auto">
                            {data.length === 0 && (
                                <div className="px-4 py-6 text-sm text-neutral-400"> No upcoming bookings </div>
                            )}

                            {data.map((booking: any) => {
                                const participants = booking.participants ?? [];
                                const isBooker = !!participants.some(
                                    (p: any) => p.gameRole === "BOOKER"
                                );

                                return (
                                    <div
                                        key={booking.gameBookingId}
                                        className="grid grid-cols-6 gap-4 px-4 py-3 text-sm items-center border-b border-neutral-800 hover:bg-neutral-800/50">
                                        <div className="font-medium text-white">{booking.gameName}</div>

                                        <div className="text-neutral-300">
                                            {new Date(booking.slotDate).toLocaleDateString()}
                                        </div>

                                        <div className="text-neutral-300">
                                            {booking.startTime} – {booking.endTime}
                                        </div>

                                        <div className="flex gap-1 flex-wrap">
                                            {participants.slice(0, 2).map((p: any) => (
                                                <span
                                                    key={p.employeeId}
                                                    className="px-2 py-0.5 text-xs bg-neutral-700 rounded">
                                                    {p.employeeName}
                                                </span>
                                            ))}
                                            {participants.length > 2 && (
                                                <span className="px-2 py-0.5 text-xs bg-neutral-600 rounded">
                                                    +{participants.length - 2}
                                                </span>
                                            )}
                                        </div>

                                        <div className="text-xs text-blue-400">
                                            {isBooker ? "BOOKER" : "MEMBER"}
                                        </div>
                                        <div>
                                            {isBooker && booking.bookingStatus !== "CANCELLED" && (
                                                <button
                                                    onClick={() => cancelMutation.mutate(booking.gameBookingId)}
                                                    className="text-xs text-red-400 hover:text-red-300">
                                                    Cancel
                                                </button>
                                            )}
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    </div>
                </main>
            </div>
        </div>
    )
}

export default UpcomingGameBookings