import React, { useEffect } from 'react'
import { useParams } from 'react-router-dom'
import { useUpdateJobReferral } from '../../hooks/jobReferral/useUpdateJobReferral';
import { useGetJobReferralById } from '../../hooks/jobReferral/useGetJobReferralById';
import { useForm } from 'react-hook-form';
import type { FetchAllJobReferrals } from '../../types/JobReferral';
import Loading from '../../components/Loading';
import Navbar from '../../components/Navbar';
import { Sidebar } from 'lucide-react';

const UpdateJobReferral = () => {

    const { jobReferralId } = useParams();

    const { mutate: updateJobReferral } = useUpdateJobReferral();

    const { data, isLoading } = useGetJobReferralById();

    const { register, formState: { errors }, handleSubmit, reset } = useForm<FetchAllJobReferrals>();

    const onSubmit = (data: FetchAllJobReferrals) => {
        if (!jobReferralId) {
            return <div> job Referral Id not found </div>
        }

        updateJobReferral({ jobReferralId, data });
    }

    useEffect(() => {
        if (data) {
            reset(data);
        }
    }, [data, reset]);

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
                <Sidebar />

                {/* Page Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Update Job Referral</h1>
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

                        {/* Short Note */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Short Note</label>
                            <textarea
                                {...register("shortNote")}
                                placeholder="Enter Short Note"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700 resize-none">
                            </textarea>
                        </div>

                        {/* Job Referral Status */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Job Referral Status <span className="text-rose-500">*</span></label>
                            <select {...register("jobReferralStatus", { required: true })} className="w-full p-2 rounded bg-neutral-800 border border-neutral-700">
                                <option value="" disabled>Select Job Referral Status</option>
                                <option value="NEW">NEW</option>
                                <option value="REVIEW">REVIEW</option>
                                <option value="INTERVIEW">INTERVIEW</option>
                                <option value="SELECTED">SELECTED</option>
                                <option value="REJECTED">REJECTED</option>
                                <option value="HOLD">HOLD</option>
                            </select>
                            {errors.jobReferralStatus && (<p className="text-rose-500 text-sm mt-1">Job Referral Status is required</p>)}
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2" >
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                Update Job Referral
                            </button>
                        </div>
                    </form>
                </main>
            </div>
        </div>
    )
}

export default UpdateJobReferral