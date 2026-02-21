import React, { useState } from 'react'
import { Box, Card, Typography, Button, Stack } from "@mui/material";
import { useNavigate, useParams } from 'react-router-dom';
import { useMyEmployeeId } from '../../hooks/employee/useMyEmployeeId';
import { useGetExpenseProof } from '../../hooks/travel expense proof/useGetTravelExpenseProof';
import Navbar from '../../components/Navbar';
import Sidebar from '../../components/Sidebar';

const ExpenseProof = () => {
    const [isCollapsed, setIsCollapsed] = useState(false);
    const { expenseId } = useParams();
    const navigate = useNavigate();
    const role = localStorage.getItem("role");

    const { data: myEmployee } = useMyEmployeeId();
    const loginEmployeeId = myEmployee?.employeeId;

    const { data: proof } = useGetExpenseProof(expenseId!);

    const canEdit =
        role === "HR" ||
        (role === "EMPLOYEE" && loginEmployeeId === proof?.uploadedBy);

    return (
        <div className="flex flex-col h-screen">
            {/* Navbar */}
            <Navbar />

            {/* Main Layout */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                <Sidebar isCollapsed={isCollapsed} setIsCollapsed={setIsCollapsed} />

                {/* Page Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <Box sx={{ p: 4 }}>
                        <Typography variant="h6" mb={2}>
                            Expense Proof
                        </Typography>

                        <Card sx={{ p: 3, backgroundColor: "#111111", borderRadius: 2 }}>
                            <Typography fontWeight={600}> Expense Proof File </Typography>

                            {!proof && (
                                <>
                                    <Typography mt={2} color="text.secondary">No proof uploaded</Typography>

                                    {canEdit && (
                                        <Button
                                            sx={{ mt: 2 }}
                                            variant="contained"
                                            onClick={() => navigate(`/travel/expense-proof/${expenseId}`)}>
                                            Add Proof
                                        </Button>
                                    )}
                                </>
                            )}

                            {proof && (
                                <>
                                    <Stack direction="row" spacing={2} mt={2}>
                                        <Button
                                            variant="contained"
                                            onClick={() => window.open(proof.expenseFileUrl, "_blank")}>
                                            View Proof
                                        </Button>

                                        {canEdit && (
                                            <Button
                                                variant="outlined"
                                                onClick={() => navigate(`/expense-proof/update/${proof.expenseProofId}`)}>
                                                Update
                                            </Button>
                                        )}
                                    </Stack>
                                </>
                            )}
                        </Card>
                    </Box>
                </main>
            </div>
        </div>
    )
}

export default ExpenseProof