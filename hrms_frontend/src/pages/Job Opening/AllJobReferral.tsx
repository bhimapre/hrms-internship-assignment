import React, { useState } from 'react'
import { Eye, Trash2, Edit, File } from 'lucide-react'
import Navbar from '../../components/Navbar'
import Sidebar from '../../components/Sidebar'
import { useGetAllJobReferrals } from '../../hooks/jobReferral/useGetAllJobReferrals'
import Loading from '../../components/Loading'
import type { FetchAllJobReferrals } from '../../types/JobReferral'
import { useNavigate } from 'react-router-dom'
import { useDeleteJobReferral } from '../../hooks/jobReferral/useDeleteJobReferral'

const AllJobReferral = () => {

    const [isCollapsed, setIsCollapsed] = useState(false);

    const [page, setPage] = useState<number>(0);
    const { data, isLoading, isError } = useGetAllJobReferrals(page, 5);
    const navigate = useNavigate();
    const { handleDelete } = useDeleteJobReferral();

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
                <Sidebar isCollapsed={isCollapsed} setIsCollapsed={setIsCollapsed} />
                
                {/* Main Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Job Referrals</h1>
                    </div>

                    <div className="flex-1 overflow-y-auto p-3 sm:p-4 lg:p-6 max-w-7xl mx.auto w-full min-w-0">

                        {/* Card View Data */}
                        <div className="space-y-2">
                            {data?.content?.length === 0 && (
                                <div>No job Referrals found</div>
                            )}
                            {data?.content?.map((referral: FetchAllJobReferrals) => (
                                <div
                                    key={referral.jobReferralId}
                                    className="bg-neutral-900 border border-neutral-700 rounded-md p-3 sm:p-4 shadow-sm hover:shadow-md transition flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 text-sm">
                                    <div className="flex items-start sm:items-center gap-3 flex-1 min-w-0">
                                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-x-4 sm:gap-x-6 gap-y-1 flex-1 min-w-0">
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Name:</span>{referral.name}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Email:</span> {referral.email}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Phone Number:</span>{referral.phoneNumber}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Status:</span>{referral.jobReferralStatus}</p>
                                        </div>
                                    </div>

                                    <div className="flex flex-col sm:flex-row gap-2 sm:ml-4 w-full sm:w-auto">
                                        {/* Update & Delete Button */}
                                        <>
                                            <button
                                                onClick={() => navigate(`/job-referral/update/${referral.jobReferralId}`)}
                                                className="flex items-center gap-1 px-2 py-1 bg-amber-700 hover:bg-amber-600 rounded text-xs w-full sm:w-auto">
                                                <Edit size={14} /> Update
                                            </button>
                                            <button
                                                onClick={() => navigate(`/job-referral/update-cv/${referral.jobReferralId}`)}
                                                className="flex items-center gap-1 px-2 py-1 bg-amber-700 hover:bg-amber-600 rounded text-xs w-full sm:w-auto">
                                                <File size={14} /> Update CV
                                            </button>
                                            <button
                                                onClick={() => handleDelete(referral.jobReferralId)}
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

export default AllJobReferral