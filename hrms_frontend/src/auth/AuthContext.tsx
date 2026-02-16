import React, { Children, createContext, useState } from 'react'

interface AuthContextType{
    token: string | null;
    login: (token: string) => void;
    logout: () => void;
    isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({children}: {children: React.ReactNode}) =>{
    const[token, setToken] = useState<string | null>(
        localStorage.getItem("accessToken")
    );

    const login = (token: string) =>{
        localStorage.setItem("accessToken", token);
        setToken(token);
    }
    
    const logout = () => {
        localStorage.removeItem("accessToken");
        setToken(null);
    };
    
    return(
        <AuthContext.Provider
        value={{
            token, login, logout, isAuthenticated: !!token
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export default AuthContext