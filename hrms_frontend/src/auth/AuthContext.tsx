import React, { Children, createContext, useState } from 'react'

interface AuthContextType{
    token: string | null;
    login: (token: string, role: AuthContextType["role"]) => void;
    logout: () => void;
    isAuthenticated: boolean;
    role: "HR" | "EMPLOYEE" | "MANAGER" | null;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({children}: {children: React.ReactNode}) =>{
    const[token, setToken] = useState<string | null>(
        localStorage.getItem("accessToken")
    );

    const[role, setRole] = useState<AuthContextType["role"]>(
        localStorage.getItem("role") as AuthContextType["role"]
    );

    const login = (token: string, role: AuthContextType["role"]) =>{
        localStorage.setItem("accessToken", token);
        localStorage.setItem("role", role ?? "");
        setToken(token);
        setRole(role);
    }
    
    const logout = () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("role");
        setToken(null);
        setRole(null);
    };
    
    return(
        <AuthContext.Provider
        value={{
            token, role, login, logout, isAuthenticated: !!token
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export default AuthContext