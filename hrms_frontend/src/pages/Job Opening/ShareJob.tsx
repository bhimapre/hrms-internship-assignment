import { Sidebar } from 'lucide-react';
import React from 'react'
import { useFieldArray, useForm } from 'react-hook-form'
import Navbar from '../../components/Navbar';
import { useNavigate, useParams } from 'react-router-dom';
import { useCreateJobShare } from '../../hooks/jobShare/useCreateJobShare';
import { toast } from 'react-toastify';
import Loading from '../../components/Loading';

export interface ShareEmails {
    emails: { email: string }[];
}

const ShareJob = () => {

    const { jobOpeningId } = useParams<{ jobOpeningId: string }>();
    const navigate = useNavigate();

    const { mutate: shareJob, isPending } = useCreateJobShare();

    const {
        register,
        control,
        handleSubmit,
        formState: { errors },
    } = useForm<ShareEmails>({
        defaultValues: {
            emails: [{ email: "" }],
        },
    });

    const { fields, append, remove } = useFieldArray({
        control,
        name: "emails",
    });

    const onSubmit = (data: ShareEmails) => {
        if (!jobOpeningId) {
            toast.error("Job opening not found");
            return;
        }

        const emailList = data.emails
            .map((e) => e.email.trim())
            .filter(Boolean);

        if (emailList.length === 0) {
            toast.error("At least one email addres is required");
            return;
        }

        shareJob({
            jobOpening: jobOpeningId,
            jobShareEmailIds: emailList,
        });
    };

    if (isPending) {
        return <Loading />
    }

    return (
        <>
            <div className="flex flex-col h-screen bg-neutral-950 text-white">
                <Navbar />

                <div className="flex flex-1 overflow-hidden">
                    <Sidebar />

                    <main className="flex-1 overflow-y-auto p-6 max-w-3xl mx-auto">
                        <h1 className="text-3xl font-bold text-center mb-6">Share Job Opening</h1>

                        <form
                            onSubmit={handleSubmit(onSubmit)}
                            className="bg-neutral-900 p-6 rounded-md border border-neutral-700 space-y-4">
                            {fields.map((field, index) => (
                                <div key={field.id} className="flex gap-2">
                                    <input
                                        type="email"
                                        placeholder="Enter email address"
                                        {...register(`emails.${index}.email`, {
                                            required: "Email is required",
                                        })}
                                        className="flex-1 rounded px-3 py-2 text-white" />

                                    {fields.length > 1 && (
                                        <button
                                            type="button"
                                            onClick={() => remove(index)}
                                            className="bg-red-600 px-3 rounded text-white">
                                            Cancel
                                        </button>
                                    )}
                                </div>
                            ))}

                            {errors.emails && (
                                <p className="text-red-400 text-sm">At least one email is required</p>
                            )}

                            <button
                                type="button"
                                onClick={() => append({ email: "" })}
                                className="text-purple-400 text-sm">
                                + Add another email
                            </button>

                            <div className="flex justify-between pt-4">
                                <button
                                    type="button"
                                    onClick={() => navigate(-1)}
                                    className="px-4 py-2 rounded bg-neutral-700">
                                    Cancel
                                </button>

                                <button
                                    type="submit"
                                    className="px-6 py-2 rounded bg-purple-700 hover:bg-purple-800">
                                    Submit
                                </button>
                            </div>
                        </form>
                    </main>
                </div>
            </div>
        </>
    )
}

export default ShareJob