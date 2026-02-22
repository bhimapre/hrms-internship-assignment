import React, { useState } from 'react'
import type { MultipleActiveEmployee } from '../types/Employee'
import { Controller, type Control, type FieldValues, type Path } from 'react-hook-form';
import type { CreateTravelRequest } from '../types/Travel';

interface Props<T extends FieldValues> {
  employees: MultipleActiveEmployee[];
  control: Control<T>;
  name: Path<T>;
  error?: string;
}

const EmployeeMultiSelect = <T extends FieldValues>({ employees, control, name, error }: Props<T>) => {

  const [search, setSearch] = useState("");

  const filteredEmployees = employees.filter(emp =>
    emp.name.toLocaleLowerCase().includes(search.toLocaleLowerCase()) ||
    emp.employeeId.toLocaleLowerCase().includes(search.toLocaleLowerCase())
  );

  return (
    <div>
      <label className="block mb-1 text-sm font-medium">
        Assign Employees <span className="text-rose-500">*</span>
      </label>

      <input type='text' placeholder='search employees...' value={search} onChange={(e) => setSearch(e.target.value)}
        className='w-full mb-3 rounded-md bg-neutral-900 border border-neutral-700 px-3 py-2 text-sm text-white
      focus:outtline-none focus:ring-1 focus:ring-purple-s00'/>

      <Controller
        name={name}
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
              {filteredEmployees.map(emp => (
                <label
                  key={emp.employeeId}
                  className="flex items-center gap-3 p-2 rounded hover:bg-neutral-700 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={selectedIds.includes(emp.employeeId)}
                    onChange={() => toggleEmployee(emp.employeeId)}
                    className="accent-purple-600" />
                  <div className='flex flex-col'>
                    <span className='text-sm font-medium text-white'>{emp.name}</span>
                    <span className='text-sm font-medium text-white'>{emp.employeeId}</span>
                  </div>
                </label>
              ))}
            </div>
          );
        }} />

      {error && (
        <p className="text-rose-500 text-sm mt-1">
          {error}
        </p>
      )}
    </div>
  );
}

export default EmployeeMultiSelect