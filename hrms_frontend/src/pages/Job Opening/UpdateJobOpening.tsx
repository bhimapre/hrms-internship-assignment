import React, { useEffect } from 'react'
import Navbar from '../../components/Navbar'
import { useForm } from 'react-hook-form';
import type { JobOpeningInterface } from './AddJobOpening';
import Sidebar from '../../components/Sidebar';
import { data, useParams } from 'react-router-dom';
import { useUpdateJobOpening } from '../../hooks/useUpdateJobOpening';
import { toast } from 'react-toastify';
import { useGetJobOpeningByIdHr } from '../../hooks/useGetJobOpeningByIdHr';
import Loading from '../../components/Loading';

const UpdateJobOpening = () => {

    const { jobOpeningId } = useParams<{ jobOpeningId: string }>();

    const { mutate: updateJob } = useUpdateJobOpening();

    const{data: jobData, isLoading} = useGetJobOpeningByIdHr(jobOpeningId);

    const { register, formState: { errors }, handleSubmit, reset } = useForm<JobOpeningInterface>();

    const onSubmit = (data: JobOpeningInterface) => {
        if (!jobOpeningId) {
            return <div> job Opening Id not found </div>
        }

        updateJob({ jobOpeningId, data});
    }

    useEffect(() => {
        if(jobData){
            reset(jobData);
        }
    }, [jobData, reset]);

    if(isLoading){
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
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Update Job Opening</h1>
                    </div>
                    <form
                        onSubmit={handleSubmit(onSubmit)}
                        className={`grid grid-cols-1 md:grid-cols-2 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>

                        {/* Title */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Title <span className="text-rose-500">*</span></label>
                            <input
                                {...register("jobTitle", { required: true })}
                                type="text"
                                placeholder="Enter Title"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.jobTitle && (<p className="text-rose-500 text-sm mt-1">Job title is required</p>)}
                        </div>

                        {/* Location */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Location <span className="text-rose-500">*</span></label>
                            <input
                                {...register("jobLocation", { required: true })}
                                type="text"
                                placeholder="Enter Location"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.jobLocation && (<p className="text-rose-500 text-sm mt-1">Job location is required</p>)}
                        </div>

                        {/* No of Opening */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> No of Opening <span className="text-rose-500">*</span></label>
                            <input
                                {...register("noOfOpening", { required: true })}
                                type="number"
                                placeholder="Enter No of Opening"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.noOfOpening && (<p className="text-rose-500 text-sm mt-1">No of opening is required</p>)}
                        </div>

                        {/* Experience */}
                        <div>
                            <label className="block mb-1 text-sm font-medium">Experience <span className="text-rose-500">*</span></label>
                            <input
                                {...register("experience", { required: true })}
                                type="number"
                                placeholder="Enter Experience"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.experience && (<p className="text-rose-500 text-sm mt-1">Experience is required</p>)}
                        </div>

                        {/* Description */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Description <span className="text-rose-500">*</span></label>
                            <textarea
                                {...register("jobDescription", { required: true })}
                                placeholder="Enter Description"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700 resize-none">
                            </textarea>
                            {errors.jobDescription && (<p className="text-rose-500 text-sm mt-1">Job descrpition is required</p>)}
                        </div>

                        {/* Job Type */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Job Type <span className="text-rose-500">*</span></label>
                            <select {...register("jobType", { required: true })} className="w-full p-2 rounded bg-neutral-800 border border-neutral-700">
                                <option value="" disabled>Select Job Type</option>
                                <option value="FULLTIME">Full time job</option>
                                <option value="PARTTIME">Part time job</option>
                                <option value="INTERNSHIP">Internship</option>
                            </select>
                            {errors.jobType && (<p className="text-rose-500 text-sm mt-1">Job type is required</p>)}
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2">
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                Update Job Opening
                            </button>
                        </div>
                    </form >
                </main>
            </div>
        </div>
    )
}

export default UpdateJobOpening