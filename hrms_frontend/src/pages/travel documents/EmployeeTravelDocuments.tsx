import { Box, Card, Typography, Button, Chip, Stack } from "@mui/material";
import { useFetchDocumentsBasedOnTravelAndEmployee } from "../../hooks/travel document/useFetchDocumentsBasedOnTravelAndEmployee";
import { useNavigate, useParams } from "react-router-dom";
import { useMyEmployeeId } from "../../hooks/employee/useMyEmployeeId";
import { toast } from "react-toastify";
import Navbar from "../../components/Navbar";
import Sidebar from "../../components/Sidebar";
import { useState } from "react";

export default function EmployeeTravelDocuments() {

  const [isCollapsed, setIsCollapsed] = useState(false);
  const { travelId, employeeId } = useParams();
  if (!travelId || !employeeId) {
    toast.error("Travel Id or Employeee Id missing");
    return;
  }

  const navigate = useNavigate();
  const role = localStorage.getItem("role");
  const { data: myEmployee } = useMyEmployeeId();
  const loginEmployeeId = myEmployee?.employeeId;

  const { data: documents } = useFetchDocumentsBasedOnTravelAndEmployee(travelId!, employeeId!);

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
              Employee Travel Documents
            </Typography>
            {loginEmployeeId == employeeId && (<Button variant="contained" onClick={() => navigate(`/travel-documents/add/${travelId}`)} size="small">Add</Button>)}

            {documents?.length === 0 && (<Typography variant="body2" color="text.secondary">No Documents Found</Typography>)}
            {documents?.map((doc) => {
              const canEdit =
                role === "HR" ||
                (role === "EMPLOYEE" &&
                  loginEmployeeId === doc.uploadedBy);
              console.log("Login", loginEmployeeId);
              console.log("Uploaded", doc.uploadedBy);
              return (
                <Card
                  key={doc.travelDocumentId}
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
                    <Typography fontWeight={600}>{doc.documentName}</Typography>
                    <Chip
                      label={doc.ownerType}
                      size="small"
                      sx={{
                        mt: 1,
                        backgroundColor: "#22c55e",
                        color: "#fff",
                      }} />
                  </Box>

                  <Stack direction="row" spacing={1}>
                    <Button onClick={() => window.open(doc.travelDocumentFileUrl)} variant="contained" size="small">
                      View
                    </Button>

                    {canEdit && (
                      <>
                        <Button onClick={() => navigate(`/travel-documents/update-file/${doc.travelDocumentId}`)} variant="contained" size="small">
                          Edit
                        </Button>
                      </>
                    )}
                  </Stack>
                </Card>
              )
            })}
          </Box>
        </main>
      </div>
    </div>
  );
}