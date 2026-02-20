import api from "./axios";

// Create Travel Expense Proof
export const createTravelExpenseProofApi = async (expenseId: string ,file: File) => {
    const formData = new FormData();    
    formData.append("file", file);

    const res = await api.post(`/api/expense-proof/${expenseId}`, formData);
    return res.data;
}