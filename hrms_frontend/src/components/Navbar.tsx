import React, { useState } from 'react'
import logo from "../assets/ROIMA_LOGO.jpeg"
import { User, ChevronDown, LogOut } from "lucide-react"

const Navbar = () => {

    const [ProfileDropdown, setProfileDropdown] = useState<boolean>(false);

    return (
        <nav className="bg-neutral-900 border-b border-neutral-800 px-4 py-3">
            <div className="max-w-7xl mx-auto flex items-center justify-between">

                {/* Company Logo */}
                <div className="flex items-center">
                    <div className="w-12 h-12 rounded-lg overflow-hidden flex items-center justify-center bg-white">
                        <img
                            src={logo}
                            alt="Company Logo"
                            className="w-full h-full object-cover" />
                    </div>
                </div>

                {/* Right Section */}
                <div className="flex items-center gap-4">

                    {/* Profile Section */}
                    <div className="relative">
                        <button
                            onClick={() => setProfileDropdown(!ProfileDropdown)}
                            className="flex items-center gap-2 p-2 rounded-lg hover:bg-neutral-800 transition">
                            <div className="w-8 h-8 bg-neutral-700 rounded-full flex items-center justify-center">
                                <User className="w-5 h-5 text-neutral-300" />
                            </div>
                            <ChevronDown className="w-4 h-4 text-neutral-400" />
                        </button>

                        {/* Profile Dropdown Menu */}
                        {ProfileDropdown && (
                            <div className="absolute right-0 mt-2 w-56 bg-neutral-900 border border-neutral-800 rounded-lg shadow-lg z-50">
                                <div className="py-2">
                                    <a
                                        className="flex items-center gap-3 px-4 py-2 text-neutral-300 hover:bg-neutral-800 hover:text-white transition">
                                        <User className="w-4 h-4" />
                                        My Profile
                                    </a>
                                    {/* Logout */}
                                    <div className="border-t border-neutral-800 mt-2 pt-2">
                                        <button
                                            className="flex items-center gap-3 px-4 py-2 text-red-400 hover:bg-neutral-800 hover:text-red-300 transition w-full text-left">
                                            <LogOut className="w-4 h-4" />
                                            Logout
                                        </button>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </div>

            {/* Close Dropdown Overlay */}
            {ProfileDropdown && (
                <div
                    className="fixed inset-0 z-40"
                    onClick={() => setProfileDropdown(false)} />
            )}
        </nav>
    )
}

export default Navbar