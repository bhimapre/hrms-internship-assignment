import api from "./axios";

// Create Travel Expense Proof
export const createTravelExpenseProofApi = async (expenseId: string, file: File) => {
    const formData = new FormData();
    formData.append("file", file);

    const res = await api.post(`/api/expense-proof/${expenseId}`, formData);
    return res.data;
}

// Fetch Expense Proof By Id
export const fetchExpenseProof = async (expenseId: string) => {
    const res = await api.get(`/api/expense-proof/${expenseId}`);
    return res.data;
}

// Update Expense Proof
export const updateExpenseProof = async (expenseProofId: string, file: File) => {
    const formData = new FormData();
    formData.append("file", file);

    const res = await api.put(`/api/expense-proof/${expenseProofId}`, formData);
    return res.data;
}