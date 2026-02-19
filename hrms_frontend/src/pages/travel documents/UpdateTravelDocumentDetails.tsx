import React, { useEffect } from 'react'
import { useParams } from 'react-router-dom';
import { useUpdateTravelDocumentDetails } from '../../hooks/travel document/useUpdateTravelDocumentDetails';
import { useGetTravelDocumentById } from '../../hooks/travel document/useGetTravelDocumentById';
import type { UpdateTravelDocumentDetails } from '../../types/TravelDocuments';
import { useForm } from 'react-hook-form';
import Loading from '../../components/Loading';
import Navbar from '../../components/Navbar';

const UpdateTravelDocumentDetails = () => {

    const { travelDocumentId } = useParams<{ travelDocumentId: string }>();

    const { mutate: updateDocument } = useUpdateTravelDocumentDetails();

    const { data: docs, isLoading } = useGetTravelDocumentById(travelDocumentId);

    const { register, formState: { errors }, handleSubmit, reset } = useForm<UpdateTravelDocumentDetails>();

    const onSubmit = (data: UpdateTravelDocumentDetails) => {
        if (!travelDocumentId) {
            return <div> Travel Document Id not found </div>
        }

        updateDocument({ travelDocumentId, data });
    }

    useEffect(() => {
        if (docs) {
            reset(docs);
        }
    }, [docs, reset]);

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
                {/* <Sidebar /> */}

                {/* Page Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Add Travel Documents</h1>
                    </div>
                    <form
                        onSubmit={handleSubmit(onSubmit)}
                        className={`grid grid-cols-1 md:grid-cols-2 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>

                        {/* Dcument Name */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Document Name <span className="text-rose-500">*</span></label>
                            <input
                                {...register("documentName", { required: true })}
                                type="text"
                                placeholder="Enter Document Name"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.documentName && (<p className="text-rose-500 text-sm mt-1">Document Name is required</p>)}
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2">
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                + Update Document Details
                            </button>
                        </div >
                    </form >
                </main>
            </div>
        </div>
    )
}

export default UpdateTravelDocumentDetails