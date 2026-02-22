import { Card, Typography, Box, Button, Chip, Stack } from "@mui/material";
import type { TravelDocuments } from "../types/TravelDocuments";
import { useNavigate, useParams } from "react-router-dom";

interface Props {
  documents?: TravelDocuments[];
}

export default function TravelDocuments({ documents = [] }: Props) {

  const travelId = useParams<{travelId: string}>();  const role = localStorage.getItem("role");
  const navigate = useNavigate();

  return (
    <Card sx={{ p: 3, backgroundColor: "background.paper" }}>
      <Typography variant="h6" mb={2}>
        Travel Documents
      </Typography>

      {role === "HR" && (
        <>
          <Button onClick={() => navigate(`/hr/travel-documents/${travelId.travelId}`)} variant="contained">Add Documents</Button>
        </>
      )}

      {documents.length === 0 && (<Typography variant="body2" color="text.secondary">No Documents for HR found</Typography>)}
      <Box sx={{ maxHeight: 300, overflowY: "auto", mt: 2 }}>
        {documents.map((doc) => (
          <Card
            key={doc.travelDocumentId}
            sx={{
              p: 2,
              mb: 2,
              backgroundColor: "#111111",
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              borderRadius: 2,
            }}>
            <Box>
              <Typography fontWeight={600}>{doc.documentName}</Typography>
              <Chip
                label={doc.ownerType}
                size="small"
                sx={{
                  mt: 1,
                  backgroundColor: doc.ownerType === "HR" ? "#7c3aed" : "#22c55e",
                  color: "#fff",
                }} />
            </Box>

            <Stack direction="row" spacing={1}>
              <Button onClick={() => window.open(doc.travelDocumentFileUrl)} variant="contained">View</Button>

              {role === "HR" && (<>
                <Button onClick={() => navigate(`/travel-documents/update-file/${doc.travelDocumentId}`)} variant="contained" size="small">Update File</Button>
              </>)}
            </Stack>
          </Card>
        ))}
      </Box>
    </Card>
  );
}