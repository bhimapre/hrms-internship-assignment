import React, { use, useState } from 'react'
import EmployeeCard from './EmployeeCard';
import { useMyOrgChart } from '../../hooks/org chart/useMyOrgChart';
import { useOrgChartBySearch } from '../../hooks/org chart/useOrgChartBySearch';
import Loading from '../../components/Loading';
import { useSearchEmployee } from '../../hooks/org chart/useSearchEmployee';

const OrgChart = () => {

    const [query, setQuery] = useState("");
    const [selectEmployee, setSelectEmployee] = useState<string | null>(null);

    const { data: searchResults } = useSearchEmployee(query);

    const { data: myOrgChart, isLoading } = useMyOrgChart();

    const { data: selectedOrgChart } = useOrgChartBySearch(selectEmployee!);

    const orgChart = selectEmployee ? selectedOrgChart : myOrgChart;

    if (isLoading) {
        return <Loading />
    }

    return (
        <div className="min-h-screen bg-neutral-900 text-white px-4 py-6">
            <div className="max-w-4xl mx-auto space-y-10">

                {/* Search */}
                <div className="relative">
                    <input
                        className="w-full rounded-lg bg-neutral-800 border border-neutral-700 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        placeholder="Search employee..."
                        value={query}
                        onChange={(e) => setQuery(e.target.value)} />

                    {/* Search Dropdown */}
                    {query && searchResults?.length > 0 && (
                        <div className="absolute z-20 mt-2 w-full bg-neutral-800 rounded-lg shadow-lg border border-neutral-700">
                            {searchResults.map((emp: any) => (
                                <div
                                    key={emp.employeeId}
                                    onClick={() => {
                                        setSelectEmployee(emp.employeeId);
                                        setQuery("");
                                    }}
                                    className="px-4 py-2 hover:bg-neutral-700 cursor-pointer">
                                    {emp.name}
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {/* Manager Chain */}
                {orgChart?.managerChain?.length > 0 && (
                    <div className="flex flex-col items-center space-y-4">
                        {orgChart.managerChain.map((emp: any, index: number) => (
                            <div key={emp.employeeId} className="flex flex-col items-center">
                                <EmployeeCard
                                    employee={emp}
                                    small
                                    onClick={() => setSelectEmployee(emp.employeeId)} />
                                {index !== orgChart.managerChain.length - 1 && (
                                    <div className="w-px h-6 bg-neutral-600" />
                                )}
                            </div>
                        ))}
                    </div>
                )}

                {/* Selected Employee */}
                {orgChart?.selectedEmployee && (
                    <div className="flex flex-col items-center space-y-3">
                        <div className="text-lg font-semibold">Selected Employee</div>

                        <EmployeeCard
                            employee={orgChart.selectedEmployee}
                            highlighted />
                        <div className="w-px h-8 bg-neutral-500" />
                    </div>
                )}

                {/* Direct Reports */}
                {orgChart?.directReports?.length > 0 && (
                    <div>
                        <h2 className="text-lg font-semibold mb-4 text-center"> Direct Reports </h2>

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            {orgChart.directReports.map((emp: any) => (
                                <EmployeeCard
                                    key={emp.employeeId}
                                    employee={emp}
                                    onClick={() => setSelectEmployee(emp.employeeId)} />
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}

export default OrgChart