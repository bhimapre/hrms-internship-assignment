import React from 'react'
import { useParams } from 'react-router-dom';
import { useUpdateTravelDocumentFile } from '../../hooks/travel document/useUpdateTravelDocumentFile';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import Loading from '../../components/Loading';
import Navbar from '../../components/Navbar';

type FileForm = {
    file: FileList;
}

const UpdateTravelDocumentFile = () => {

    const { documentId } = useParams<{documentId: string}>();

    const{ mutate: updateFile, isPending} = useUpdateTravelDocumentFile();

    const { register, formState: { errors }, handleSubmit } = useForm<FileForm>();

    const onSubmit = (data: FileForm) => {
        if(!documentId){
            toast.error("Job opening Id not found");
            return;
        }

        const file = data.file[0];
        updateFile({documentId, file});
    }

    if(isPending){
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
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Update Travel Document File</h1>
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

export default UpdateTravelDocumentFile