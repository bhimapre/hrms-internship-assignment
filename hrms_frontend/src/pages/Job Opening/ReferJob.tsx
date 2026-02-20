import React, { useState } from 'react'
import Navbar from '../../components/Navbar'
import { useForm } from 'react-hook-form'
import Sidebar from '../../components/Sidebar'
import type { AddJobReferral } from '../../types/JobReferral'
import { useAddJobReferral } from '../../hooks/jobReferral/useAddJobReferral'
import { useParams } from 'react-router-dom'
import { toast } from 'react-toastify'
import Loading from '../../components/Loading'


const ReferJob = () => {

    const [isCollapsed, setIsCollapsed] = useState(false);

    const { jobOpeningId } = useParams<{ jobOpeningId: string }>();
    if (!jobOpeningId) {
        toast.error("Job Opening Id not found");
        return;
    }

    const { register, formState: { errors }, handleSubmit } = useForm<AddJobReferral>();

    const { mutate: addRefer, isPending } = useAddJobReferral();

    const onSubmit = (data: AddJobReferral) => {
        const formData = new FormData;
        const file = data.file[0];

        const { file: _, ...jobReferralDto } = data;

        formData.append(
            "data",
            new Blob(
                [JSON.stringify(jobReferralDto)],
                { type: "application/json" }
            )
        );

        formData.append("file", file as File);
        addRefer({jobOpeningId, formData});
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
                <Sidebar isCollapsed={isCollapsed} setIsCollapsed={setIsCollapsed}/>

                {/* Page Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Refer Job Opening</h1>
                    </div>

                    <form
                        onSubmit={handleSubmit(onSubmit)}
                        className={`grid grid-cols-1 md:grid-cols-2 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>

                        {/* Name */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Name <span className="text-rose-500">*</span></label>
                            <input
                                {...register("name", { required: true })}
                                type="text"
                                placeholder="Enter Name"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.name && (<p className="text-rose-500 text-sm mt-1">Name is required</p>)}
                        </div>

                        {/* Email */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Email <span className="text-rose-500">*</span></label>
                            <input
                                {...register("email", { required: true })}
                                type="text"
                                placeholder="Enter Email"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.email && (<p className="text-rose-500 text-sm mt-1">Email is required</p>)}
                        </div>

                        {/* Phone Number */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Phone Number <span className="text-rose-500">*</span></label>
                            <input
                                {...register("phoneNumber", { required: true })}
                                type="text"
                                placeholder="Enter Phone Number"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.phoneNumber && (<p className="text-rose-500 text-sm mt-1">Phone Number is required</p>)}
                        </div>

                        {/* CV File */}
                        <div>
                            <label className="block mb-1 text-sm font-medium">
                                CV File
                            </label>
                            <input
                                {...register("file", { required: true })}
                                type="file"
                                accept=".pdf,.doc,.docx,.jpg,.jpeg,.png"
                                className="w-full p-1.5 rounded bg-neutral-800 border border-neutral-700 text-neutral-200
                                           file:h-8.5 file:px-3 file:rounded file:border-0 file:bg-purple-800 file:text-white"/>
                            {errors.file && <p className="text-red-500 text-xs">CV file is required</p>}
                        </div>

                        {/* Short Note */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Short Note</label>
                            <textarea
                                {...register("shortNote")}
                                placeholder="Enter Short Note"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700 resize-none">
                            </textarea>
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2" >
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                Send Job Referral
                            </button>
                        </div>
                    </form>
                </main>
            </div>
        </div>
    )
}

export default ReferJob