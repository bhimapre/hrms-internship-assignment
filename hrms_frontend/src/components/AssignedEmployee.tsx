import { Card, Typography, Box, Button, Stack } from "@mui/material";
import type { AssignEmployeeTravel } from "../types/Employee";
import { useNavigate, useParams } from "react-router-dom";

interface Props {
  employees?: AssignEmployeeTravel[];
}

export default function AssignedEmployees({ employees = [] }: Props) {

  const role = localStorage.getItem("role");
  const navigate = useNavigate();

  const { travelId } = useParams<{travelId: string}>();

  return (
    <Card sx={{ p: 3, mb: 3, backgroundColor: "background.paper" }}>
      <Typography variant="h6" mb={2}>
        Assigned Employees
      </Typography>

      {employees.length === 0 && (<Typography variant="body2" color="text.secondary">No Employee Assign</Typography>)}
      <Box sx={{ maxHeight: 300, overflowY: "auto" }}>
        {employees.map((emp) => (
          <Card
            key={emp.employeeId}
            sx={{
              p: 2,
              mb: 2,
              backgroundColor: "#111111",
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              borderRadius: 2,
            }}>
            <Typography fontWeight={600}>{emp.name}</Typography>

            <Stack direction="row" spacing={1}>
              <Button onClick={() => navigate(`/expense/travel/${travelId}/employee/${emp.employeeId}`)} variant="contained">Expenses</Button>
              <Button onClick={() => navigate(`/travel/${travelId}/employee/${emp.employeeId}`)} variant="contained">Documents</Button>
            </Stack>
          </Card>
        ))}
      </Box>
    </Card>
  );
}