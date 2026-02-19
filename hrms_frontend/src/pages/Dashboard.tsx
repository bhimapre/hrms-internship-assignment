import React from 'react'
import Navbar from '../components/Navbar'
import Sidebar from '../components/Sidebar'
import TravelAssignEmployee from '../components/TravelAssignEmployee'
import DocumentsByHRCard from '../components/DocumentsByHRCard'
import TravelDetailsCard from '../components/TravelDetailsCard'

const Dashboard = () => {
  return (
    <>
      <div className="flex flex-col h-screen bg-neutral-950 text-neutral-100">Dashboard
        <div className='flex flex-col m-7 items-center'>
          <div>
          <DocumentsByHRCard />
          </div>
          <div className='m-8'>
            <TravelDetailsCard />
          </div>
        </div>
      </div>
    </>
  )
}

export default Dashboard