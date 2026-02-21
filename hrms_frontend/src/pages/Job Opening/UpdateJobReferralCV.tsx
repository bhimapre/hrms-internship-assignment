import React, { useState } from 'react'
import Navbar from '../../components/Navbar'
import Sidebar from '../../components/Sidebar'
import { useParams } from 'react-router-dom';
import { useUpdateJobReferralCV } from '../../hooks/jobReferral/useUpdateJobReferralCV';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import Loading from '../../components/Loading';

type FileForm = {
    file: FileList;
}

const UpdateJobReferralCV = () => {

    const [isCollapsed, setIsCollapsed] = useState(false);

    const { jobReferralId } = useParams<{ jobReferralId: string }>();

    const { mutate: updateFile, isPending } = useUpdateJobReferralCV();

    const { register, formState: { errors }, handleSubmit } = useForm<FileForm>();

    const onSubmit = (data: FileForm) => {
        if (!jobReferralId) {
            toast.error("Job referral Id not found");
            return;
        }

        const file = data.file[0];
        updateFile({ jobReferralId, file });
    }

    if (isPending) {
        <Loading />
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
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Update Job Referral CV</h1>
                    </div>
                    <form
                        onSubmit={handleSubmit(onSubmit)}
                        className={`grid grid-cols-1 md:grid-cols-2 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>
                        {/* Job Referral CV */}
                        <div>
                            <label className="block mb-1 text-sm font-medium">
                                Job Referral CV
                            </label>
                            <input
                                {...register("file", { required: true })}
                                type="file"
                                accept=".pdf,.doc,.docx.jpg,.jpeg,.png"
                                className="w-full p-1.5 rounded bg-neutral-800 border border-neutral-700 text-neutral-200
                                           file:h-8.5 file:px-3 file:rounded file:border-0 file:bg-purple-600 file:text-white"/>
                            {errors.file && <p className="text-red-500 text-xs">Job Referral CV is required</p>}
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2">
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                Update Job Referral CV File
                            </button>
                        </div>
                    </form>
                </main>
            </div>
        </div>
    )
}

export default UpdateJobReferralCV