import React, { useState } from 'react'
import { useMyEmployeeId } from '../../hooks/employee/useMyEmployeeId';
import ProfileCard from '../../components/ProfileCard';
import GamePreferenceForm from '../../components/GamePreferenceForm';
import Loading from '../../components/Loading';
import { toast } from 'react-toastify';
import Navbar from '../../components/Navbar';
import Sidebar from '../../components/Sidebar';

const MyProfilePage = () => {

    const { data: employee, isLoading } = useMyEmployeeId();
    const [isCollapsed, setIsCollapsed] = useState(false);

    if (isLoading) {
        return <Loading />
    }

    if (!employee) {
        toast.error("employee id not found");
        return;
    }

    return (
        <div className="flex flex-col h-screen">
            {/* Navbar */}
            <Navbar />

            {/* Main Layout */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                <Sidebar isCollapsed={isCollapsed} setIsCollapsed={setIsCollapsed} />

                {/* Page Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="max-w-4xl mx-auto p-6 space-y-6 text-white">
                        <h1 className="text-2xl font-semibold">My Profile</h1>

                        <ProfileCard employee={employee} />

                        <GamePreferenceForm
                            employeeId={employee.employeeId}
                            defaultGames={employee.gamePreferences || []} />
                    </div>
                </main>
            </div>
        </div>
    )
}

export default MyProfilePage