import React, { useState } from 'react'
import { useForm } from 'react-hook-form'
import { useNavigate, useParams } from 'react-router-dom'
import { useAddTravelExpenseProof } from '../../hooks/travel expense proof/useAddTravelExpenseProof';
import { toast } from 'react-toastify';
import Navbar from '../../components/Navbar';
import { useSubmitExpense } from '../../hooks/travelExpense/useSubmitExpense';
import Sidebar from '../../components/Sidebar';

type FormValues = {
    file: FileList;
}

const AddExpenseProof = () => {

    const [isCollapsed, setIsCollapsed] = useState(false);

    const params = useParams<{ expenseId: string }>();
    const expenseId = params.expenseId!;
    const navigate = useNavigate();

    const { mutateAsync: uploadProof } = useAddTravelExpenseProof();

    const { register, handleSubmit } = useForm<FormValues>();

    const submiitExpense = useSubmitExpense();

    const onUpload = async (data: FormValues) => {
        const file = data.file?.[0];

        if (!file) {
            toast.error("Please select file");
            return;
        }
        try {
            await uploadProof({ expenseId, file });
            toast.success("Expense Proof uploaded successfully");
        }
        catch {
            toast.error("Something went wrong");
        }
    }

    const onFinalSubmit = async () => {
        try {
            await submiitExpense.mutateAsync(expenseId);
            toast.success("Expense Added Successfully");
        }
        catch {
            toast.error("Somthing went wrong");
        }
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
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Add Expense Proof</h1>
                    </div>
                    <form
                        onSubmit={handleSubmit(onUpload)}
                        className={`grid grid-cols-1 md:grid-cols-1 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>

                        {/* Document File */}
                        <div>
                            <label className="block mb-1 text-sm font-medium">
                                Document File
                            </label>
                            <input
                                {...register("file", { required: true })}
                                type="file"
                                accept=".pdf,.doc,.docx"
                                className="w-full p-1.5 rounded bg-neutral-800 border border-neutral-700 text-neutral-200
                           file:h-8.5 file:px-3 file:rounded file:border-0 file:bg-purple-600 file:text-white"/>
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2">
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                + Add Expense Proof
                            </button>
                        </div>
                    </form >
                    <button
                        onClick={onFinalSubmit}
                        className="min-w-full py-2.5 rounded font-medium transition bg-green-600 hover:bg-green-500">
                        + Submit Expense
                    </button>
                </main>
            </div>
        </div>
    )
}

export default AddExpenseProof