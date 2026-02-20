export interface TravelExpenseBase {
    expenseName: string;
    expenseDate: string;
    expenseAmount: number;
    expenseCategory: "FOOD" | "TRANSPORT"
}

export interface GetTravelExpenseByEmployee extends TravelExpenseBase {
    travelExpenseId?: string;
    expenseStatus?: "APPROVED" | "REJECTED" | "SUBMITTED" | "CANCELLED" | "DRAFT";
    createdBy: string;
}

export interface UpdateTravelExpense extends TravelExpenseBase {
    travelExpenseId?: string;
    expenseStatus?: "APPROVED" | "REJECTED" | "SUBMITTED" | "CANCELLED" | "DRAFT";
}

// Travel Expense row for tabel
export interface TravelExpenseRow {
    travelExpenseId: string;
    expenseName: string;
    employeeName: string;
    expenseAmount: number;
    expenseStatus: "APPROVED" | "REJECTED" | "SUBMITTED" | "CANCELLED" | "DRAFT";
    createdAt: string;
    expenseDate: string;
}

// Match with Filter params
export interface ExpenseFilterParams {
    travelId: string;
    employeeName?: string;
    travelTitle?: string;
    expenseStatus?: "APPROVED" | "REJECTED" | "SUBMITTED" | "CANCELLED" | "DRAFT";
    fromDate?: string;
    toDate?: string;
    page?: number;
    size?: number;
}

// Filter pagination
export interface PageResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}