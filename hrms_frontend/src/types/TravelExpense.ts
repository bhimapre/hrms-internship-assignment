export interface TravelExpenseBase{
    expenseName: string;
    expenseDate: string;
    expenseAmount: number;
    expenseCategory: "FOOD" | "TRANSPORT"
}

export interface GetTravelExpenseByEmployee extends TravelExpenseBase{
    expenseId?: string;
    expenseStatus: "APPROVED" | "REJECTED" | "SUBMITTED" | "CANCELLED" | "DRAFT";
}