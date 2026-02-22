import React, { useState } from 'react'
import JobOpenings from './Job Opening/JobOpenings'
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';
import DashboardNotifications from './employees/DashboardNotifications';
import DashboardUpcomingBookings from './employees/DashboardUpcomingBookings';

const Dashboard = () => {
  const [isCollapsed, setIsCollapsed] = useState(false);

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
          <div className="text-center mt-8 mb-8">
            <h1 className="text-4xl font-bold text-center text-white mb-4">Dashboard</h1>
            <div className='grid grid-cols-1 lg:grid-cols-2 gap-6'>
              <DashboardNotifications />
              <DashboardUpcomingBookings />
            </div>
          </div>
        </main>
      </div>
    </div>
  )
}

export default Dashboard