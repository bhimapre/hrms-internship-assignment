import React from 'react'
import { Eye, Trash2, Edit } from 'lucide-react'
import Navbar from '../../components/Navbar'
import Sidebar from '../../components/Sidebar'

const AllJobReferral = () => {
    return (
        <div className="flex flex-col h-screen bg-neutral-950 text-neutral-100">
            {/* Navbar */}
            <Navbar />

            {/* Main Layout */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                <Sidebar />

                {/* Main Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Job Referrals</h1>
                    </div>

                    <div className="flex-1 overflow-y-auto p-3 sm:p-4 lg:p-6 max-w-7xl mx.auto w-full min-w-0">

                        {/* Card View Data */}
                        <div className="space-y-2">
                            <div
                                className="bg-neutral-900 border border-neutral-700 rounded-md p-3 sm:p-4 shadow-sm hover:shadow-md transition flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 text-sm">
                                <div className="flex items-start sm:items-center gap-3 flex-1 min-w-0">
                                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-x-4 sm:gap-x-6 gap-y-1 flex-1 min-w-0">
                                        <p className="wrap-break-word"><span className="font-medium text-purple-300">Name:</span> Name</p>
                                        <p className="wrap-break-word"><span className="font-medium text-purple-300">Email:</span> Email</p>
                                        <p className="wrap-break-word"><span className="font-medium text-purple-300">Phone Number:</span> Phone Number</p>
                                        <p className="wrap-break-word"><span className="font-medium text-purple-300">Status:</span> Status</p>
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
                                            className="flex items-center gap-1 px-2 py-1 bg-amber-700 hover:bg-amber-600 rounded text-xs w-full sm:w-auto">
                                            <Edit size={14} /> Update
                                        </button>
                                        <button className="flex items-center gap-1 px-2 py-1 bg-rose-800 hover:bg-rose-700 rounded text-xs w-full sm:w-auto">
                                            <Trash2 size={14} /> Delete
                                        </button>
                                    </>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    )
}

export default AllJobReferral