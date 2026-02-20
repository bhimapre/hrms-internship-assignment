import React from 'react'
import { useForm } from 'react-hook-form'
import type { CreateTimeSlot } from '../../types/TimeSlot'
import Loading from '../../components/Loading';
import { useCreateTimeSlot } from '../../hooks/time slot/useCreateTimeSlot';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import Navbar from '../../components/Navbar';

const TimeSlotCreation = () => {

    const { gameId } = useParams<{ gameId: string }>();

    const { register, handleSubmit, formState: { errors } } = useForm<CreateTimeSlot>();

    const { mutate: createGame, isPending } = useCreateTimeSlot();

    const onSubmit = (data: CreateTimeSlot) => {
        if (!gameId) {
            toast.error("Game Id is not found for Creation of time slot");
            return;
        }

        createGame({gameId, data});
    }

    if (isPending) {
        return <Loading />
    }

    return (
        <div className="flex flex-col h-screen">
            {/* Navbar */}
            <Navbar />

            {/* Main Layout */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                {/* <Sidebar /> */}

                {/* Page Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Add Time Slot</h1>
                    </div>
                    <form
                        onSubmit={handleSubmit(onSubmit)}
                        className={`grid grid-cols-1 md:grid-cols-2 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>

                        {/* Start Date */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Start Date <span className="text-rose-500">*</span></label>
                            <input
                                {...register("fromDate", { required: true })}
                                type="date"
                                placeholder="Enter Start Date"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.fromDate && (<p className="text-rose-500 text-sm mt-1">Start Date is required</p>)}
                        </div>

                        {/* End Date */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> End Date <span className="text-rose-500">*</span></label>
                            <input
                                {...register("toDate", { required: true })}
                                type="date"
                                placeholder="Enter End Date"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.toDate && (<p className="text-rose-500 text-sm mt-1">End Date is required</p>)}
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2">
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                + Add Time Slot
                            </button>
                        </div >
                    </form >
                </main>
            </div>
        </div>
    )
}

export default TimeSlotCreation