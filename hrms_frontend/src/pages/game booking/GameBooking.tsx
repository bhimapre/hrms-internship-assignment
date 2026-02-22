import React, { useState } from 'react'
import EmployeeMultiSelect from '../../components/EmployeeMultiSelect';
import { toast } from 'react-toastify';
import type { CreateGameBooking } from '../../types/GameBooking';
import { useForm } from 'react-hook-form';
import { useNavigate, useParams } from 'react-router-dom';
import { useCreateGameBooking } from '../../hooks/game booking/useCreateGameBooking';
import { useGetActiveEmployee } from '../../hooks/employee/useGetActiveEmployee';
import Navbar from '../../components/Navbar';
import Sidebar from '../../components/Sidebar';

const GameBooking = () => {
    const { gameId, timeSlotId } = useParams<{
        gameId: string;
        timeSlotId: string;
    }>();

    const { mutate: createBooking } = useCreateGameBooking();
    const [isCollapsed, setIsCollapsed] = useState(false);

    const navigate = useNavigate();

    const { data: employees = [] } = useGetActiveEmployee();

    if (!gameId || !timeSlotId) {
        toast.error("Invalid booking request");
        return null;
    }

    const {
        control,
        handleSubmit,
        formState: { errors }, } = useForm<CreateGameBooking>({
            defaultValues: {
                gameId,
                timeSlotId,
                memberIds: [],
            },
        });

    const onSubmit = (data: CreateGameBooking) => {
        if (data.memberIds.length === 0) {
            toast.error("Please select at least one player");
            return;
        }
        createBooking(data)
    };

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
                    <div className="max-w-2xl mx-auto p-6">
                        <div className="bg-neutral-900 border border-neutral-800 rounded-xl p-6">
                            <h1 className="text-xl font-semibold text-white">
                                Book Game
                            </h1>
                            <p className="text-sm text-neutral-400 mt-1">
                                Select players to join this game
                            </p>

                            <div className="mt-5">
                                <EmployeeMultiSelect
                                    employees={employees}
                                    name="memberIds"
                                    control={control}
                                    error={errors.memberIds?.message} />
                            </div>

                            <div className="flex justify-end gap-3 mt-6">
                                <button
                                    type="button"
                                    onClick={() => navigate(-1)}
                                    className="px-4 py-2 rounded-md border border-neutral-700 text-neutral-300 hover:bg-neutral-800 transition">
                                    Cancel
                                </button>

                                <button
                                    onClick={handleSubmit(onSubmit)}
                                    className="px-5 py-2 rounded-md bg-indigo-600 hover:bg-indigo-500 text-white font-medium transition">
                                    Confirm Booking
                                </button>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    );
};

export default GameBooking