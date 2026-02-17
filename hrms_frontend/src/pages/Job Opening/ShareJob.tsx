import React from 'react'
import { useForm } from 'react-hook-form'

export interface ShareEmails {
    email: string;
}

export interface ListShareEmails {
    emails: ShareEmails[];
}

const ShareJob = () => {

    const { register, formState: { errors } } = useForm<ListShareEmails>();

    return (
        <>
            <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                <div className="text-center mt-8 mb-8">
                    <h1 className="text-4xl font-bold text-center text-white mb-4">Add Job Opening</h1>
                </div>
                <form className="bg-neutral-800 shadow-md rounded px-8 pt-6 pb-8 mb-4 border-2 border-solid border-neutral-700">
                    <div className="mb-4">
                        <label className="block text-white text-sm font-bold mb-2"> Email Id </label>
                        <input {...register("emails", { required: true })} className="border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline" placeholder="Enter multiple Email Id use ',' for that" />
                    </div>
                    {errors.emails && (<p className="text-rose-500 text-sm mt-1">At least one Email is required</p>)}
                    <div className='flex justify-between'>
                        <button className="text-white bg-purple-800 hover:bg-purple-600 focus:ring-4 focus:outline-none focus:ring-purple-300 font-medium rounded-base text-sm px-4 py-2 text-center">Cancel</button>
                        <button type="submit" className="text-white bg-purple-800 hover:bg-purple-600 focus:ring-4 focus:outline-none focus:ring-purple-300 font-medium rounded-base text-sm px-4 py-2 text-center">Submit</button>
                    </div>
                </form>
            </main>
        </>
    )
}

export default ShareJob