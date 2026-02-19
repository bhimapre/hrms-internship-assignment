import React, { useState } from 'react'
import { Eye, Edit, Trash2, File } from "lucide-react"
import { useNavigate } from 'react-router-dom'
import { useHrAllJobOpenings } from '../../hooks/useHrAllJobOpenings'
import Loading from '../../components/Loading'
import type { GetJobOpening, PageResponse } from '../../types/JobOpening'
import Navbar from '../../components/Navbar'
import Sidebar from '../../components/Sidebar'
import { useDeleteJobOpening } from '../../hooks/useDeleteJobOpening'

const HRJobOpening = () => {

    // Navigate
    const navigate = useNavigate();

    // Pagination State
    const [page, setPage] = useState(0);

    // Fetch Data
    const { data, isLoading, isError } = useHrAllJobOpenings(page, 6);

    // Handle Delete
    const { handleDelete } = useDeleteJobOpening();

    const jobs = data?.content ?? [];
    const isFirst = data?.first ?? true;
    const isLast = data?.last ?? true;
    const totalPages = data?.totalPages ?? 0;

    // Loading
    if (isLoading) {
        return <Loading />
    }

    // Error Handling
    if (isError) {
        return <div>Failed to fetch data</div>
    }

    console.log(jobs);

    return (
        <div className="flex flex-col h-screen bg-neutral-950 text-neutral-100">
            {/* Navbar */}
            <Navbar />

            {/* Main Layout */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                {/* <Sidebar /> */}

                {/* Main Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">HR Job Opening</h1>
                    </div>

                    <div className="flex-1 overflow-y-auto p-3 sm:p-4 lg:p-6 max-w-7xl mx.auto w-full min-w-0">

                        {/* Main Layout */}
                        <div className="flex flex-col sm:flex-row gap-2 sm:gap-3 mb-4 sm:justify-end">
                            <button
                                className="px-3 py-1 bg-emerald-700 hover:bg-emerald-600 rounded text-sm w-full sm:w-auto">
                                + Add Job Opening
                            </button>
                        </div>

                        {/* Card View Data */}
                        <div className="space-y-2">
                            {jobs.map((job: GetJobOpening) => (
                                <div
                                    key={job.jobOpeningId}
                                    className="bg-neutral-900 border border-neutral-700 rounded-md p-3 sm:p-4 shadow-sm hover:shadow-md transition flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 text-sm">
                                    <div className="flex items-start sm:items-center gap-3 flex-1 min-w-0">
                                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-x-4 sm:gap-x-6 gap-y-1 flex-1 min-w-0">
                                            <p className="break-all"><span className="font-medium text-purple-300">Job Opening Id:</span>{job.jobOpeningId}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Title:</span>{job.jobTitle}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">No of Openings:</span>{job.noOfOpening}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Location:</span>{job.jobLocation}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Experience:</span>{job.experience}</p>
                                        </div>
                                    </div>

                                    {/* View Button */}
                                    <div className="flex flex-col sm:flex-row gap-2 sm:ml-4 w-full sm:w-auto">
                                        <button
                                            className="flex items-center gap-1 px-2 py-1 bg-purple-800 hover:bg-purple-700 rounded text-xs w-full sm:w-auto">
                                            <Eye size={14} /> View
                                        </button>

                                        {/* Update & Delete Button */}
                                        <>
                                            <button
                                                onClick={() => navigate(`/hr/job-opening/update/${job.jobOpeningId}`)}
                                                className="flex items-center gap-1 px-2 py-1 bg-amber-700 hover:bg-amber-600 rounded text-xs w-full sm:w-auto">
                                                <Edit size={14} /> Update
                                            </button>
                                            <button
                                                onClick={() => navigate(`/hr/job-opening/update-file/${job.jobOpeningId}`)}
                                                className="flex items-center gap-1 px-2 py-1 bg-amber-700 hover:bg-amber-600 rounded text-xs w-full sm:w-auto">
                                                <File size={14} /> File Update
                                            </button>
                                            <button
                                                onClick={() => handleDelete(job.jobOpeningId)}
                                                className="flex items-center gap-1 px-2 py-1 bg-rose-800 hover:bg-rose-700 rounded text-xs w-full sm:w-auto">
                                                <Trash2 size={14} /> Delete
                                            </button>
                                        </>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                    {/* Pagination */}
                    <div className="flex items-center justify-center gap-4 mt-10">
                        <button
                            disabled={isFirst}
                            onClick={() => setPage((p) => p - 1)}
                            className={`px-4 py-1 rounded-full text-sm
                            ${isFirst
                                    ? "bg-neutral-700 text-neutral-400 cursor-not-allowed"
                                    : "bg-purple-700 hover:bg-purple-800 text-white"
                                }`}>
                            Prev
                        </button>

                        <span className="text-neutral-300 text-sm">
                            Page <span className="text-white font-semibold">{page + 1}</span> of{" "}
                            <span className="text-white font-semibold">
                                {totalPages}
                            </span>
                        </span>

                        <button
                            disabled={isLast}
                            onClick={() => setPage((p) => p + 1)}
                            className={`px-4 py-1 rounded-full text-sm
                            ${isLast
                                    ? "bg-neutral-700 text-neutral-400 cursor-not-allowed"
                                    : "bg-purple-700 hover:bg-purple-800 text-white"
                                }`}>
                            Next
                        </button>
                    </div>
                </main>
            </div>
        </div>
    )
}

export default HRJobOpening