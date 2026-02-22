import React from 'react'
import { useForm } from 'react-hook-form';
import { useUploadProfilePicture } from '../hooks/employee/useUploadProfilePicture';

type FormValues = {
  file: FileList;
};

const ProfilePictureUpload = () => {

    const { register, handleSubmit, reset } = useForm<FormValues>();
    const uploadMutation = useUploadProfilePicture();

    const onSubmit = (data: FormValues) => {
        if (!data.file || data.file.length === 0) return;
        uploadMutation.mutate(data.file[0]);
        reset();
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)} className="flex items-center gap-3">
            <input
                type="file"
                accept="image/*"
                {...register("file", { required: true })}
                className="text-sm text-neutral-300
                file:bg-purple-600 file:border-0
                  file:px-3 file:py-1.5 file:rounded
                file:text-white hover:file:bg-purple-500"/>

            <button
                type="submit"
                className="px-4 py-2 rounded bg-purple-600 hover:bg-purple-500 transition">
                Upload
            </button>
        </form>
    )
}

export default ProfilePictureUpload