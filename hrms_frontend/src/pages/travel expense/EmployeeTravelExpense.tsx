import { Box, Card, Typography, Button, Stack, Chip } from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";
import { useFetchExpenseByTravelIdAndEmployeeId } from "../../hooks/travelExpense/useFetchExpenseByTravelIdAndEmployeeId";
import { useMyEmployeeId } from "../../hooks/employee/useMyEmployeeId";
import { useExpenseDelete } from "../../hooks/travelExpense/useExpenseDelete";

export default function EmployeeTravelExpenses() {

  const { travelId, employeeId } = useParams();
  const navigate = useNavigate();
  const role = localStorage.getItem("role");

  const { data: myEmployee } = useMyEmployeeId();
  const loginEmployeeId = myEmployee?.employeeId;

  const { handleDelete } = useExpenseDelete();

  const { data: expenses } = useFetchExpenseByTravelIdAndEmployeeId(travelId!, employeeId!);

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h6" mb={2}>
        Employee Travel Expenses
      </Typography>
      {loginEmployeeId === employeeId && (<Button variant="contained" size="small" onClick={() => navigate(`/expense/add/${travelId}`)}> Add </Button>)}

      {expenses?.length === 0 && (<Typography variant="body2" color="text.secondary">No Expense Found</Typography>)}
      {expenses?.map((exp) => {
        const canEdit =
          role === "HR" ||
          (role === "EMPLOYEE" &&
            loginEmployeeId === exp.createdBy);
        return (
          <Card
            key={exp.travelExpenseId}
            sx={{
              p: 2,
              mt: 2,
              mb: 2,
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              backgroundColor: "#111111",
              borderRadius: 2,
            }}>
            <Box>
              <Typography fontWeight={600}>
                {exp.expenseName}
              </Typography>

              <Typography
                variant="body2"
                color="text.secondary">
                Amount: ₹{exp.expenseAmount}
              </Typography>

              <Chip
                label={exp.expenseStatus}
                size="small"
                sx={{
                  mt: 1,
                  backgroundColor:
                    exp.expenseStatus === "APPROVED"
                      ? "#22c55e"
                      : "#f59e0b",
                  color: "#fff",
                }} />
            </Box>

            <Stack direction="row" spacing={1}>
              {canEdit && (
                <>
                  <Button
                    variant="contained"
                    size="small"
                    onClick={() =>
                      navigate(`/expense/update/${exp.travelExpenseId}`)
                    }>
                    Edit
                  </Button>
                  {exp.expenseStatus !== "CANCELLED" && exp.expenseStatus !== "APPROVED" && exp.expenseStatus !== "REJECTED" && (
                    <Button
                      variant="contained"
                      size="small"
                      color="error"
                      onClick={() => handleDelete(exp.travelExpenseId!)}>
                      Delete
                    </Button>
                  )}
                </>
              )}
            </Stack>
          </Card>
        );
      })}
    </Box>
  );
}