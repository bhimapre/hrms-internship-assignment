import React from 'react'
import ProfilePictureUpload from './ProfilePictureUpload';

type Props = {
  employee: {
    employeeId: string;
    name: string;
    email: string;
    role: string;
    profilePictureFileUrl?: string;
  };
};

const ProfileCard = ({ employee }: Props) => {
    return (
        <div className="bg-neutral-900 rounded-xl p-6 flex items-center gap-6">

            <img
                src={employee.profilePictureFileUrl || "/avatar.jpg"}
                alt="Profile"
                className="w-24 h-24 rounded-full object-cover border border-neutral-700"/>

            <div className="flex-1">
                <h2 className="text-xl font-semibold">{employee.name}</h2>
                <p className="text-neutral-400">{employee.email}</p>
                <p className="text-sm text-purple-400 mt-1">{employee.role}</p>

                <div className="mt-4">
                    <ProfilePictureUpload />
                </div>
            </div>
        </div>
    )
}

export default ProfileCard