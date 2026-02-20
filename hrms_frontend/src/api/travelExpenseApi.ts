import type { ExpenseFilterParams, GetTravelExpenseByEmployee, PageResponse, TravelExpenseBase, TravelExpenseRow, UpdateTravelExpense } from "../types/TravelExpense";
import api from "./axios";

export const createTravelExpenseApi = async (travelId: string, data: TravelExpenseBase) => {
    const res = await api.post(`/api/expense/${travelId}`, data);
    return res.data;
}

// Fetch ALl documents based on travel id and employee id
export const employeeTravelExpenses = async (travelId: string, employeeId: string):
    Promise<GetTravelExpenseByEmployee[]> => {
    const res = await api.get(`/api/travel-expense/${travelId}/${employeeId}`);
    return res.data;
}

// Submit expense
export const submitExpenseApi = async (expenseId: string) => {
    const res = await api.post(`/api/expense/submit/${expenseId}`);
    return res.data;
}

// Update Travel Expense Details
export const updateTravelExpenseApi = async (expenseId: string, data: UpdateTravelExpense) => {
    const res = await api.put(`/api/expense/${expenseId}`, data);
    return res.data;
}

// Fetch Travel Expense By Id
export const fetchExpenseById = async (expenseId: string):
    Promise<GetTravelExpenseByEmployee> => {
    const res = await api.get(`/api/expense/${expenseId}`);
    return res.data;
}

// Soft Delete Travel Expense
export const deleteExpenseApi = async (expenseId: string) => {
    const res = await api.put(`/api/expense/cancel/${expenseId}`);
    return res.data;
}

// Fetch Expense For HR 
export const fetchHrExpenses = async (
    filters: ExpenseFilterParams
): Promise<PageResponse<TravelExpenseRow>> => {
    const { data } = await api.get("/api/hr/expense/filter", {
        params: {
            travelId: filters.travelId,
            employeeName: filters.employeeName || undefined,
            travelTitle: filters.travelTitle || undefined,
            status: filters.expenseStatus || undefined,
            fromDate: filters.fromDate || undefined,
            toDate: filters.toDate || undefined,
            page: filters.page ?? 0,
            size: filters.size ?? 5,
        },
    });
    return data;
}

// Approve Travel Expense
export const approveTravelExpenseApi = async (expenseId: string, remark: string) => {
    const res = await api.put(`/api/hr/expense/approve/${expenseId}`, null,{
        params: {remark},
    });
    return res.data;
}

// Reject Travel Expense
export const rejectTravelExpenseApi = async (expenseId: string, remark: string) => {
    const res = await api.put(`/api/hr/expense/reject/${expenseId}`, null,{
        params: {remark},
    });
    return res.data;
}