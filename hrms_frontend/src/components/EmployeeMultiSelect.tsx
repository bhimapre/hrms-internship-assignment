import React from 'react'
import type { MultipleActiveEmployee } from '../types/Employee'
import { Controller, type Control } from 'react-hook-form';
import type { CreateTravelRequest } from '../types/Travel';

interface Props {
  employees: MultipleActiveEmployee[];
  control: Control<CreateTravelRequest>;
  error?: string;
}

const EmployeeMultiSelect = ({ employees, control, error }: Props) => {
  return (
    <div>
      <label className="block mb-1 text-sm font-medium">
        Assign Employees <span className="text-rose-500">*</span>
      </label>

      <Controller
        name="employeeIds"
        control={control}
        rules={{ required: true }}
        render={({ field }) => {
          const selectedIds: string[] = field.value || [];

          const toggleEmployee = (id: string) => {
            let updated: string[];

            if (selectedIds.includes(id)) {
              updated = selectedIds.filter(empId => empId !== id);
            }
            else {
              updated = [...selectedIds, id];
            }

            field.onChange(updated);
          };

          return (
            <div className="border border-neutral-700 rounded bg-neutral-800 max-h-64 overflow-y-auto p-3 space-y-2">
              {employees.map(emp => (
                <label
                  key={emp.employeeId}
                  className="flex items-center gap-3 p-2 rounded hover:bg-neutral-700 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={selectedIds.includes(emp.employeeId)}
                    onChange={() => toggleEmployee(emp.employeeId)}
                    className="accent-purple-600"/>
                  <div className='flex flex-col'>
                    <span className='text-sm font-medium text-white'>{emp.name}</span>
                    <span className='text-sm font-medium text-white'>{emp.employeeId}</span>
                  </div>
                </label>
              ))}
            </div>
          );
        }}/>

      {error && (
        <p className="text-rose-500 text-sm mt-1">
          {error}
        </p>
      )}
    </div>
  );
}

export default EmployeeMultiSelect