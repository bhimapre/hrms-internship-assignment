import React, { useEffect, useState } from 'react'
import type { UpdateTravelExpense } from '../../types/TravelExpense';
import { useForm } from 'react-hook-form';
import { useParams } from 'react-router-dom';
import { updateTravelExpenseApi } from '../../api/travelExpenseApi';
import { useGetTravelById } from '../../hooks/travel/useGetTravelById';
import { useUpdateTravelExpnese } from '../../hooks/travelExpense/useUpdateTravelExpense';
import { useGetTravelExpense } from '../../hooks/travelExpense/useGetTravelExpense';
import Loading from '../../components/Loading';
import Navbar from '../../components/Navbar';
import Sidebar from '../../components/Sidebar';

const UpdateExpense = () => {

    const [isCollapsed, setIsCollapsed] = useState(false);

    const { expenseId } = useParams<{ expenseId: string }>();

    const { mutate: updateExpense } = useUpdateTravelExpnese();

    const { data: expenseData, isLoading } = useGetTravelExpense(expenseId);
    const { register, handleSubmit, formState: { errors }, reset } = useForm<UpdateTravelExpense>();

    const onSubmit = (data: UpdateTravelExpense) => {
        if (!expenseId) {
            return <div> Travel Id not found </div>
        }

        updateExpense({ expenseId, data });
    }

    useEffect(() => {
        if (expenseData) {
            reset(expenseData);
        }
    }, [expenseData, reset]);

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
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Update Expense</h1>
                    </div>
                    <form
                        onSubmit={handleSubmit(onSubmit)}
                        className={`grid grid-cols-1 md:grid-cols-2 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>

                        {/* Expense Name */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Expense Name <span className="text-rose-500">*</span></label>
                            <input
                                {...register("expenseName", { required: true })}
                                type="text"
                                placeholder="Enter Expense Name"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.expenseName && (<p className="text-rose-500 text-sm mt-1">Expense Name is required</p>)}
                        </div>

                        {/* Expense Date */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Expense Date <span className="text-rose-500">*</span></label>
                            <input
                                {...register("expenseDate", { required: true })}
                                type="date"
                                placeholder="Enter Expense Date"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.expenseDate && (<p className="text-rose-500 text-sm mt-1">Expense Date is required</p>)}
                        </div>

                        {/* Amount */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Amount <span className="text-rose-500">*</span></label>
                            <input
                                {...register("expenseAmount", { required: true })}
                                type="number"
                                placeholder="Enter Amount"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.expenseAmount && (<p className="text-rose-500 text-sm mt-1">Amount is required</p>)}
                        </div>

                        {/* Expense Category */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Expense Category <span className="text-rose-500">*</span></label>
                            <select {...register("expenseCategory", { required: true })} className="w-full p-2 rounded bg-neutral-800 border border-neutral-700">
                                <option value="" disabled>Select Expense Category</option>
                                <option value="FOOD">FOOD</option>
                                <option value="TRANSPORT">TRANSPORT</option>
                            </select>
                            {errors.expenseCategory && (<p className="text-rose-500 text-sm mt-1">Expense Category is required</p>)}
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2">
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                + Update Travel Expense
                            </button>
                        </div >
                    </form >
                </main>
            </div>
        </div>
    )
}

export default UpdateExpense