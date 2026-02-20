import React, { useState } from 'react'
import { Box, Button, MenuItem, Select, TextField, Typography, Table, TableHead, TableRow, TableCell, TableBody, CircularProgress, Dialog, DialogTitle, DialogContent, DialogActions } from "@mui/material";
import type { ExpenseFilterParams, TravelExpenseRow } from '../../types/TravelExpense';
import { useHrExpenseFilter } from '../../hooks/travelExpense/useHrExpenseFilter';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import { useApproveExpense } from '../../hooks/travelExpense/useApproveExpense';
import { useRejectExpense } from '../../hooks/travelExpense/useRejectExpense';

const AllExpenseForHR = () => {

  const [selectedExpenseId, setSelectedExpenseId] = useState<string | null>(null);
  const [remark, setRemark] = useState("");
  const [actionType, setActionType] = useState<"APPROVE" | "REJECT" | null>(null);

  const approveMutation = useApproveExpense();
  const rejectMutation = useRejectExpense();

  const { travelId } = useParams<{ travelId: string }>();
  if (!travelId) {
    toast.error("Travel Id not found");
    return;
  }

  const [filters, setFilters] = useState<ExpenseFilterParams>({
    travelId,
    page: 0,
    size: 5,
  });

  const { data, isLoading } = useHrExpenseFilter(filters);

  return (
    <Box p={4}>
      <Typography variant="h5" mb={3}>
        Travel Expense Filter
      </Typography>

      <Box display="flex" gap={2} mb={3} flexWrap="wrap">
        <TextField
          size="small"
          label="Employee Name"
          value={filters.employeeName || ""}
          onChange={(e) => setFilters({ ...filters, employeeName: e.target.value })} />

        <Select
          size="small"
          displayEmpty
          value={filters.expenseStatus || ""}
          onChange={(e) =>
            setFilters({
              ...filters,
              expenseStatus: e.target.value || undefined,
            })}>

          <MenuItem value="">All Status</MenuItem>
          <MenuItem value="SUBMITTED">Submitted</MenuItem>
          <MenuItem value="APPROVED">Approved</MenuItem>
          <MenuItem value="REJECTED">Rejected</MenuItem>
          <MenuItem value="CANCELLED">Cancelled</MenuItem>
          <MenuItem value="DRAFT">Draft</MenuItem>
        </Select>

        <TextField
          size="small"
          type="date"
          label="From Date"
          InputLabelProps={{ shrink: true }}
          onChange={(e) =>
            setFilters({ ...filters, fromDate: e.target.value })
          } />

        <TextField
          size="small"
          type="date"
          label="To Date"
          InputLabelProps={{ shrink: true }}
          onChange={(e) =>
            setFilters({ ...filters, toDate: e.target.value })
          } />

        <Button
          variant="contained"
          onClick={() => setFilters({ ...filters, page: 0 })}>
          Search
        </Button>
      </Box>

      {isLoading ? (
        <CircularProgress />
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Expense</TableCell>
              <TableCell>Employee</TableCell>
              <TableCell>Amount</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Date</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>

          <TableBody>
            {data?.content?.length === 0 && (
              <TableRow>
                <TableCell colSpan={6} align="center">
                  No Expenses Found
                </TableCell>
              </TableRow>
            )}

            {data?.content?.map((exp) => (
              <TableRow key={exp.travelExpenseId}>
                <TableCell>{exp.expenseName}</TableCell>
                <TableCell>{exp.employeeName}</TableCell>
                <TableCell>₹ {exp.expenseAmount}</TableCell>
                <TableCell>{exp.expenseStatus}</TableCell>
                <TableCell>
                  {exp.expenseDate}
                </TableCell>
                <TableCell align="right">
                  <Button size="small" variant="outlined">
                    View
                  </Button>
                </TableCell>
                <TableCell align="right">
                  {exp.expenseStatus === "SUBMITTED" && (
                    <>
                      <Button
                        size="small"
                        color="success"
                        variant='outlined'
                        sx={{ ml: 1 }}
                        onClick={() => {
                          setSelectedExpenseId(exp.travelExpenseId);
                          setActionType("APPROVE");
                        }}>
                        Approve
                      </Button>

                      <Button
                        size="small"
                        color="error"
                        variant='outlined'
                        sx={{ ml: 1 }}
                        onClick={() => {
                          setSelectedExpenseId(exp.travelExpenseId);
                          setActionType("REJECT");
                        }}>
                        Reject
                      </Button>
                    </>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}

      <Dialog open={!!actionType} onClose={() => setActionType(null)}>
        <DialogTitle>
          {actionType === "APPROVE" ? "Approve Expense" : "Reject Expense"}
        </DialogTitle>

        <DialogContent>
          <TextField
            fullWidth
            label="Remark"
            value={remark}
            onChange={(e) => setRemark(e.target.value)}
            multiline
            minRows={3}
            required />
        </DialogContent>

        <DialogActions>
          <Button onClick={() => setActionType(null)}>Cancel</Button>
          <Button
            variant="contained"
            color={actionType === "APPROVE" ? "success" : "error"}
            onClick={() => {
              if (!selectedExpenseId) return;

              if (actionType === "APPROVE") {
                approveMutation.mutate({
                  expenseId: selectedExpenseId,
                  remark,
                });
              } else {
                rejectMutation.mutate({
                  expenseId: selectedExpenseId,
                  remark,
                });
              }
              setRemark("");
              setSelectedExpenseId(null);
              setActionType(null);
            }}>
            Confirm
          </Button>
        </DialogActions>
      </Dialog>

      {data && (
        <Box mt={3} display="flex" gap={2}>
          <Button
            disabled={filters.page === 0}
            onClick={() => setFilters((prev) => ({ ...prev, page: (prev.page ?? 0) - 1 }))}>
            Previous
          </Button>

          <Typography>
            Page {data.number + 1} of {data.totalPages}
          </Typography>

          <Button
            disabled={data.number + 1 >= data.totalPages}
            onClick={() => setFilters((prev) => ({ ...prev, page: (prev.page ?? 0) + 1 }))}>
            Next
          </Button>
        </Box>
      )}
    </Box>
  )
}

export default AllExpenseForHR