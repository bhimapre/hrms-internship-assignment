import React, { useState } from 'react'
import { useForm } from 'react-hook-form'
import Navbar from '../../components/Navbar';
import { useUpdateExpenseProof } from '../../hooks/travel expense proof/useUpdateExpenseProof';
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import Loading from '../../components/Loading';
import Sidebar from '../../components/Sidebar';

type FileForm = {
    file: FileList;
}

const UpdateExpenseProof = () => {

    const [isCollapsed, setIsCollapsed] = useState(false);

    const { expenseProofId } = useParams<{ expenseProofId: string }>();
    if (!expenseProofId) {
        toast.error("Expense Proof Id not found");
        return;
    }

    const navigate = useNavigate();
    const { register, handleSubmit } = useForm<FileForm>();
    const { mutate: updateProof, isPending } = useUpdateExpenseProof();

    const onSubmit = (data: FileForm) => {
            if(!expenseProofId){
                toast.error("docuemnt Id not found");
                return;
            }
    
            const file = data.file[0];
            updateProof({expenseProofId, file});
        }

    if(isPending){
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
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Update Expense Proof</h1>
                    </div>
                    <form
                        onSubmit={handleSubmit(onSubmit)}
                        className={`grid grid-cols-1 md:grid-cols-2 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>

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
                                + Update Expense Proof
                            </button>
                        </div>
                    </form >
                </main>
            </div>
        </div>
    )
}

export default UpdateExpenseProof