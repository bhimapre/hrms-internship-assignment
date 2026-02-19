import React, { useState } from 'react'
import { useFetchAllTravels } from '../../hooks/travel/useFetchAllTravels';
import { useNavigate } from 'react-router-dom';
import { useTravelDelete } from '../../hooks/travel/useTravelDelete';
import Loading from '../../components/Loading';
import Navbar from '../../components/Navbar';
import type { GetAllTravels } from '../../types/Travel';
import { Eye, Trash2, Edit } from 'lucide-react'

const AllTravels = () => {

    const [page, setPage] = useState<number>(0);
    const { data, isLoading, isError } = useFetchAllTravels(page, 5);
    const navigate = useNavigate();
    const { handleDelete } = useTravelDelete();

    if (isLoading) {
        return <Loading />
    }

    if (isError) {
        return <div>Failed to fetch data</div>
    }

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
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Travels</h1>
                    </div>

                    <div className="flex-1 overflow-y-auto p-3 sm:p-4 lg:p-6 max-w-7xl mx.auto w-full min-w-0">

                        <div className="flex flex-col sm:flex-row gap-2 sm:gap-3 mb-4 sm:justify-end">
                                <button
                                    className="px-3 py-1 bg-emerald-700 hover:bg-emerald-600 rounded text-sm w-full sm:w-auto"
                                    onClick={() => navigate('hr/travel/add')}>
                                    + Add Travel
                                </button>
                        </div>

                        {/* Card View Data */}
                        <div className="space-y-2">
                            {data?.content?.length === 0 && (
                                <div>No Travel found</div>
                            )}
                            {data?.content?.map((referral: GetAllTravels) => (
                                <div
                                    key={referral.travelId}
                                    className="bg-neutral-900 border border-neutral-700 rounded-md p-3 sm:p-4 shadow-sm hover:shadow-md transition flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 text-sm">
                                    <div className="flex items-start sm:items-center gap-3 flex-1 min-w-0">
                                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-x-4 sm:gap-x-6 gap-y-1 flex-1 min-w-0">
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Title:</span>{referral.travelId}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Location:</span>{referral.travelLocation}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Start Date:</span>{referral.travelDateFrom}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">End Date:</span>{referral.travelDateTo}</p>
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
                                                onClick={() => navigate(`/job-referral/update/${referral.travelId}`)}
                                                className="flex items-center gap-1 px-2 py-1 bg-amber-700 hover:bg-amber-600 rounded text-xs w-full sm:w-auto">
                                                <Edit size={14} /> Update
                                            </button>
                                            <button
                                                onClick={() => handleDelete(referral.travelId)}
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
                            disabled={data?.first}
                            onClick={() => setPage((p) => p - 1)}
                            className={`px-4 py-1 rounded-full text-sm
                            ${data?.first
                                    ? "bg-neutral-700 text-neutral-400 cursor-not-allowed"
                                    : "bg-purple-700 hover:bg-purple-800 text-white"
                                }`}>
                            Prev
                        </button>

                        <span className="text-neutral-300 text-sm">
                            Page <span className="text-white font-semibold">{page + 1}</span> of{" "}
                            <span className="text-white font-semibold">
                                {data?.totalPages}
                            </span>
                        </span>

                        <button
                            disabled={data?.last}
                            onClick={() => setPage((p) => p + 1)}
                            className={`px-4 py-1 rounded-full text-sm
                            ${data?.last
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

export default AllTravels