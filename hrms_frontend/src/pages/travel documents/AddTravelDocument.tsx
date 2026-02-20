import React from 'react'
import { useForm } from 'react-hook-form'
import type { AddTravelDocumentBase } from '../../types/TravelDocuments'
import Navbar from '../../components/Navbar';
import { useAddTravelDocument } from '../../hooks/travel document/useAddTravelDocument';
import Loading from '../../components/Loading';
import { useParams } from 'react-router-dom';

const AddTravelDocument = () => {

    const { travelId } = useParams<{ travelId: string }>();
    if (!travelId) {
        return <div>Travel Id not found</div>
    }
    const { register, handleSubmit, formState: { errors } } = useForm<AddTravelDocumentBase>();

    const { mutate, isPending } = useAddTravelDocument();

    const onSubmit = (data: AddTravelDocumentBase) => {
        const formData = new FormData();
        const file = data.file[0];

        const { file: _, ...travelDocumentDto } = data;

        formData.append(
            "data",
            new Blob(
                [JSON.stringify(travelDocumentDto)],
                { type: "application/json" }
            )
        );

        // Add File 
        formData.append("file", file as File);
        mutate({ travelId, formData });
    }

    // Loading
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
                            {errors.file && <p className="text-red-500 text-xs">Document file is required</p>}
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2">
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                + Add Documents
                            </button>
                        </div >
                    </form >
                </main>
            </div>
        </div>
    )
}

export default AddTravelDocument