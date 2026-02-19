import type { GetTravelExpenseByEmployee, TravelExpenseBase } from "../types/TravelExpense";
import api from "./axios";

export const createTravelExpenseApi = async(travelId: string , data: TravelExpenseBase) =>{
    const res = await api.post(`/api/expense/${travelId}`, data);
    return res.data;
}

// Fetch ALl documents based on travel id and employee id
export const employeeTravelExpenses = async (travelId: string, employeeId: string):
Promise<GetTravelExpenseByEmployee[]> =>{
    const res = await api.get(`/api/travel-expense/${travelId}/${employeeId}`);
    return res.data;
}