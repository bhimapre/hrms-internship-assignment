import axios from "axios";
import type { MultipleActiveEmployee } from "../types/Employee";
import api from "./axios";

// Fetch Active Employee
export const fetchAllActiveEmployee = async ():
    Promise<MultipleActiveEmployee[]> => {
    const res = await api.get(`/api/employee/active`);
    console.log("api response", res.data);
    return res.data;
}

// Fetch current employee Id
export const fetchMyEmployeeId = async () => {
    const res = await api.get(`/api/employee/me`);
    return res.data;
}

// Update Game Preference
export const updateGamePreferences = async (
    employeeId: string,
    gamePreferences: string[]
) => {
    return api.patch(`/api/employee/${employeeId}`, {
        gamePreferences,
    });
};

// Upload Profile Picture
export const uploadProfilePicture = async (file: File) => {
    const formData = new FormData();
    formData.append("file", file);

    const res = await api.post("api/employee/profile-picture", formData);
    return res.data;
};