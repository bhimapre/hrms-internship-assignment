import React from 'react'
import { useForm } from "react-hook-form";
import { useLogin } from '../auth/useLogin';
import { useAuth } from '../auth/useAuth';
import { data, useNavigate } from 'react-router-dom';

interface LoginForm {
  username: string;
  password: string;
}

const Login = () => {

  const { register, handleSubmit } = useForm<LoginForm>();
  const loginMuatation = useLogin();
  const { login } = useAuth();
  const navigate = useNavigate();

  const onSubmit = (data: LoginForm) => {
    loginMuatation.mutate(data, {
      onSuccess: (respose) => {
        login(respose.accessToken);
        navigate("/dashboard");
      },
    });
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="max-w-xl py-6 px-8 h-80 mt-20 bg-white rounded shadow-xl">
          <div className="mb-6">
            <label className="block text-gray-800 font-bold">Username:</label>
            <input {...register("username", {required: true})} placeholder="Enter UserName" className="w-full border border-gray-300 py-2 pl-3 rounded mt-2 outline-none focus:ring-indigo-600 :ring-indigo-600" />
          </div>
          <div>
            <label className="block text-gray-800 font-bold">Password:</label>
            <input {...register("password", {required: true})} type="password" placeholder="Enter Password" className="w-full border border-gray-300 py-2 pl-3 rounded mt-2 outline-none focus:ring-indigo-600 :ring-indigo-600" />
            <a href="#" className="text-sm font-thin text-gray-800 hover:underline mt-2 inline-block hover:text-indigo-600">Forget Password</a>
          </div>
          <button type='submit' className="cursor-pointer py-2 px-4 block mt-6 bg-indigo-500 text-white font-bold w-full text-center rounded">Login</button>
          {loginMuatation.isError && <p>Something went wrong</p>}
      </div>
    </form>
  )
}

export default Login