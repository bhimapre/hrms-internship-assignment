import React, { useState } from 'react'
import { useJobOpening } from '../../hooks/useJobOpenings'
import Loading from '../../components/Loading';
import JobOpeningCard from '../../components/JobOpeningCard';
import type { JobOpeningInterface } from './AddJobOpening';
import Navbar from '../../components/Navbar';
import Sidebar from '../../components/Sidebar';

const JobOpenings = () => {
    const [page, setPage] = useState(0);
    const { data, isLoading, isError } = useJobOpening(page, 6);
    const [isCollapsed, setIsCollapsed] = useState(true);

    // Loading
    if (isLoading) {
        return <Loading />
    }   

    // Error Handling
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
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Job Opening</h1>
                    </div>
                    {/* Job Cards */}
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        {data?.content?.map((job) => (
                            <JobOpeningCard key={job.jobOpeningId} job={job} />
                        ))}
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

export default JobOpenings