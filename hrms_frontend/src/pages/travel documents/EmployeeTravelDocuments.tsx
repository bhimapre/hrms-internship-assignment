import { Box, Card, Typography, Button, Chip, Stack } from "@mui/material";
import { useFetchDocumentsBasedOnTravelAndEmployee } from "../../hooks/travel document/useFetchDocumentsBasedOnTravelAndEmployee";
import { useParams } from "react-router-dom";

export default function EmployeeTravelDocuments() {

  const {travelId, employeeId} = useParams();

  const role = localStorage.getItem("role");

  const {data: documents} = useFetchDocumentsBasedOnTravelAndEmployee(travelId!, employeeId!);

  return (
    <Box sx={{ p: 4 }}>
      <Typography variant="h6" mb={2}>
        Employee Travel Documents
      </Typography>
      
      {documents?.length === 0 && (<Typography variant="body2" color="text.secondary">No Documents Found</Typography>)}
      {documents?.map((doc) => (
        <Card
          key={doc.travelDocumentId}
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
            <Typography fontWeight={600}>{doc.documentName}</Typography>
            <Chip
              label={doc.ownerType}
              size="small"
              sx={{
                mt: 1,
                backgroundColor: "#22c55e",
                color: "#fff",
              }}/>
          </Box>

          <Stack direction="row" spacing={1}>
            <Button onClick={() => window.open(doc.travelDocumentFileUrl)} variant="contained" size="small">
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