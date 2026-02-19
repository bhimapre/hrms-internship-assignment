import { Box, Card, Typography, Button, Stack, Chip } from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";
import { useFetchExpenseByTravelIdAndEmployeeId } from "../../hooks/travelExpense/useFetchExpenseByTravelIdAndEmployeeId";

export default function EmployeeTravelExpenses() {

  const {travelId, employeeId} = useParams();
  const navigate = useNavigate();

  const role = localStorage.getItem("role");

  const {data: expenses} = useFetchExpenseByTravelIdAndEmployeeId(travelId!, employeeId!);

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h6" mb={2}>
        Employee Travel Expenses
      </Typography>
      {expenses?.length === 0 && (<Typography variant="body2" color="text.secondary">No Expense Found</Typography>)}
      {expenses?.map((exp) => (
        <Card
          key={exp.expenseId}
          sx={{
            p: 2,
            mb: 2,
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            backgroundColor: "#111111",
            borderRadius: 2,
          }}>
          <Box>
            <Typography fontWeight={600}>{exp.expenseName}</Typography>
            <Typography variant="body2" color="text.secondary">
              Amount: ₹{exp.expenseAmount}
            </Typography>

            <Chip
              label={exp.expenseStatus}
              size="small"
              sx={{
                mt: 1,
                backgroundColor:
                  exp.expenseStatus === "APPROVED" ? "#22c55e" : "#f59e0b",
                color: "#fff",
              }}/>
          </Box>

          <Stack direction="row" spacing={1}>
            <Button onClick={() => navigate(`travel/expense-proof/${exp.expenseId}`)}variant="contained" size="small">
              View
            </Button>

            {role === "EMPLOYEE" && (
              <>
                <Button variant="contained" size="small">
                  Edit
                </Button>
                <Button variant="contained" size="small" color="error">
                  Delete
                </Button>
              </>
            )}
          </Stack>
        </Card>
      ))}
    </Box>
  );
}