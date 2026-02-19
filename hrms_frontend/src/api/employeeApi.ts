import axios from "axios";
import type { MultipleActiveEmployee } from "../types/Employee";
import api from "./axios";

// Fetch Active Employee
export const fetchAllActiveEmployee = async():
Promise<MultipleActiveEmployee[]> => {
    const res = await api.get(`/api/employee/active`);
    console.log("api response",res.data);
    return res.data;
}