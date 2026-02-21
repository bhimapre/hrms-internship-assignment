const EmployeeCard = ({ employee, onClick, small, highlighted }: any) => {
    return (
        <div
            onClick={onClick}
            className={`cursor-pointer rounded-xl bg-neutral-800 border border-neutral-700
                        transition hover:bg-neutral-700
                        ${small ? "px-4 py-2 text-sm" : "px-6 py-4"}
                        ${highlighted ? "ring-2 ring-blue-500" : ""}`}>
                            
            <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-full bg-neutral-600 flex items-center justify-center font-bold">
                    {employee.name?.[0]}
                </div>

                <div>
                    <div className="font-semibold">{employee.name}</div>
                    <div className="text-sm text-neutral-400">{employee.designation}</div>
                </div>
            </div>
        </div>
    )
}

export default EmployeeCard