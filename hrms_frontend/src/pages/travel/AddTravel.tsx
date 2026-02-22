import React, { useState } from 'react'
import Navbar from '../../components/Navbar'
import Sidebar from '../../components/Sidebar'
import { useForm } from 'react-hook-form'
import type { CreateTravelRequest } from '../../types/Travel'
import EmployeeMultiSelect from '../../components/EmployeeMultiSelect'
import { useGetActiveEmployee } from '../../hooks/employee/useGetActiveEmployee'
import { useCreateTravel } from '../../hooks/travel/useCreateTravel'
import Loading from '../../components/Loading'

const AddTravel = () => {

  const { register, formState: { errors }, control, handleSubmit } = useForm<CreateTravelRequest>({
    defaultValues: {
      employeeIds: []
    }
  });

  const [isCollapsed, setIsCollapsed] = useState(false);
  const { data: employees = [], isLoading } = useGetActiveEmployee();

  const { mutate: createTravel, isPending } = useCreateTravel();

  const onSubmit = (data: CreateTravelRequest) => {
    createTravel(data);
  }

  if (isPending || isLoading) {
    <Loading />
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
          <div className="text-center mt-8 mb-8">
            <h1 className="text-4xl font-bold text-center text-white mb-4">Add Travel</h1>
          </div>
          <form
            onSubmit={handleSubmit(onSubmit)}
            className={`grid grid-cols-1 md:grid-cols-2 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>

            {/* Title */}
            <div>
              <label className="block mb-1 text-sm font-medium"> Title <span className="text-rose-500">*</span></label>
              <input
                {...register("travelTitle", { required: true })}
                type="text"
                placeholder="Enter Title"
                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
              {errors.travelTitle && (<p className="text-rose-500 text-sm mt-1">Travel title is required</p>)}
            </div>

            {/* Start Date */}
            <div>
              <label className="block mb-1 text-sm font-medium"> Start Date <span className="text-rose-500">*</span></label>
              <input
                {...register("travelDateFrom", { required: true })}
                type="date"
                placeholder="Enter Start Date"
                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
              {errors.travelDateFrom && (<p className="text-rose-500 text-sm mt-1">Start date is required</p>)}
            </div>

            {/* End Date */}
            <div>
              <label className="block mb-1 text-sm font-medium"> End Date <span className="text-rose-500">*</span></label>
              <input
                {...register("travelDateTo", { required: true })}
                type="date"
                placeholder="Enter End Date"
                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
              {errors.travelDateTo && (<p className="text-rose-500 text-sm mt-1">End Date is required</p>)}
            </div>

            {/* Location */}
            <div>
              <label className="block mb-1 text-sm font-medium">Location <span className="text-rose-500">*</span></label>
              <input
                {...register("travelLocation", { required: true })}
                type="text"
                placeholder="Enter Location"
                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
              {errors.travelLocation && (<p className="text-rose-500 text-sm mt-1">Location is required</p>)}
            </div>

            {/* Details */}
            <div>
              <label className="block mb-1 text-sm font-medium"> Details <span className="text-rose-500">*</span></label>
              <textarea
                {...register("travelDetails", { required: true })}
                placeholder="Enter Details"
                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700 resize-none">
              </textarea>
              {errors.travelDetails && (<p className="text-rose-500 text-sm mt-1">Travel Details is required</p>)}
            </div>

            <div className='md:col-span-2'></div>
            <EmployeeMultiSelect employees={employees} name="employeeIds"
              control={control}
              error={errors.employeeIds && "Please select at least one Ids"} />

            {/* Submit */}
            <div className="md:col-span-2">
              <button
                type="submit"
                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                + Add Travel
              </button>
            </div >
          </form >
        </main>
      </div>
    </div>
  )
}

export default AddTravel