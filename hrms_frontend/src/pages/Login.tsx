import React, { useState } from 'react'
import { useForm } from "react-hook-form";
import { useLogin } from '../auth/useLogin';
import { useAuth } from '../auth/useAuth';
import { data, useNavigate } from 'react-router-dom';
import { LogIn, Eye, EyeOff  } from 'lucide-react';
import Loading from '../components/Loading';


interface LoginForm {
  username: string;
  password: string;
}

const Login = () => {

  const { register, handleSubmit, formState: {errors} } = useForm<LoginForm>();
  const loginMuatation = useLogin();
  const { login } = useAuth();
  const navigate = useNavigate();

  const [showPassword, setShowPassword] = useState(false);

  const onSubmit = (data: LoginForm) => {
    loginMuatation.mutate(data, {
      onSuccess: (respose) => {
        login(respose.accessToken, respose.role);
        navigate("/dashboard");
      },
    });
  }

  return (
    <div className="min-h-screen bg-neutral-950 flex items-center justify-center p-4">
            <div className="w-full max-w-md bg-neutral-900 rounded-xl shadow-xl p-8">

                {/* Page Content */}
                <div className="mb-8">
                    <div className="flex items-center gap-3 mb-4">
                        <div className="w-10 h-10 bg-purple-700 rounded-lg flex items-center justify-center">
                            <LogIn className="w-6 h-6 text-white" />
                        </div>
                        <h1 className="text-2xl font-bold text-white">Login Portal</h1>
                    </div>
                    <h2 className="text-xl font-semibold text-white mb-2">Welcome Back</h2>
                </div>

                {/* Form Details */}
                <form className="space-y-5" onSubmit={handleSubmit(onSubmit)}>
                    <div>

                        {/* Email */}
                        <label className="block text-sm font-medium text-neutral-300 mb-2">Email Address <span className="text-rose-500">*</span></label>
                        <input {...register("username", {required:true})}
                            type="email"
                            placeholder="Enter your email"
                            className="w-full px-4 py-3 bg-neutral-800 border border-neutral-700 rounded-lg text-white placeholder-neutral-400 focus:outline-none focus:ring-2 focus:ring-purple-500" />
                        {errors.username && (<p className="text-rose-500 text-sm mt-1">Username filed is required</p>)}
                    </div>

                    {/* Password */}
                    <div className="mb-4">
                        <label className="block text-sm font-medium text-neutral-300 mb-1">Password <span className="text-rose-500">*</span></label>
                        <div className="relative">
                            <input
                                {...register("password", {required:true})}
                                type={showPassword ? "text" : "password"}
                                placeholder="Enter Password"
                                className="w-full px-4 py-3 pr-12 bg-neutral-800 border border-neutral-700 rounded-lg text-white placeholder-neutral-400 focus:outline-none focus:ring-2 focus:ring-purple-500" />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                aria-label="Toggle password visibility"
                                className="absolute inset-y-0 right-3 flex items-center justify-center text-neutral-400 hover:text-white">
                                {showPassword ? (
                                    <EyeOff className="w-5 h-5" />
                                ) : (
                                    <Eye className="w-5 h-5" />
                                )}
                            </button>
                        </div>
                        {errors.password && (<p className="text-rose-500 text-sm mt-1">Password field is required</p>)}
                    </div>

                    {/* Forgot Password */}
                    <div className="text-right">
                        <button
                            type="button"
                            onClick={() => navigate("/update-password-mail")}
                            className="text-sm text-purple-400 hover:text-purple-300 transition-colors">
                            Forgot password?
                        </button>
                    </div>

                    {/* Submit */}
                    <button
                        type="submit"
                        className={`w-full bg-purple-700 hover:bg-purple-800 text-white font-medium py-3 rounded-lg transition duration-200 flex items-center justify-center gap-2`}>
                        <LogIn className="w-5 h-5" /> Login 
                    </button>
                </form>
            </div>
        </div>
  )
}

export default Login