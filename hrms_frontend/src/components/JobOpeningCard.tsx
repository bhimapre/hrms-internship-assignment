import React from 'react'
import type { JobOpeningInterface } from '../pages/Job Opening/AddJobOpening'

export interface GetAllJobOpening {
    jobOpeningId: string;
    jobTitle: string;
    jobLocation: string;
    jobDescription: string;
    noOfOpening: number;
    experience: number;
    jobType: "FULLTIME" | "PARTTIME" | "INTERNSHIP";
}

interface Props {
    job: GetAllJobOpening;
}

const JobOpeningCard = ({ job }: Props) => {

    return (
        <>
            <div className="bg-neutral-800 rounded-xl shadow-lg hover:shadow-xl transition">
                <div className="px-6 py-4">
                    <h3 className="text-xl font-bold text-white mb-2">
                        {job.jobTitle}
                    </h3>

                    <p className="text-neutral-300 text-sm mb-3 line-clamp-3">
                      jobDescription:  {job.jobDescription}
                    </p>

                    <div className="space-y-1 text-sm text-neutral-300">
                        <p>Job Location: {job.jobLocation}</p>
                        <p>Experience: {job.experience} years</p>
                        <p>Openings: {job.noOfOpening}</p>
                        <p>{job.jobType}</p>
                    </div>
                </div>

                <div className="px-6 pb-4 pt-2 flex justify-between">
                    <button className="bg-purple-700 hover:bg-purple-800 text-white text-sm px-4 py-1 rounded-full">Share Job</button>
                    <button className="bg-purple-700 hover:bg-purple-800 text-white text-sm px-4 py-1 rounded-full">Refer Job</button>
                </div>
            </div>
        </>
    )
}

export default JobOpeningCard