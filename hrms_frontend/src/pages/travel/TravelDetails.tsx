
import { Box, Card, Typography } from "@mui/material";
import AssignedEmployees from "../../components/AssignedEmployee";
import TravelDocuments from "../../components/TravelDocuments";
import { useParams } from "react-router-dom";
import { useGetTravelById } from "../../hooks/travel/useGetTravelById";
import { useAssignEmployeesTravel } from "../../hooks/employee/useGetAssignEmployeesTravel";
import { useFetchAllBasedOnTravelAndHR } from "../../hooks/travel document/useFetchAllBasedOnTravelAndHR";
import { useAssignEmployees } from "../../hooks/employee/useAssignEmployees";

export default function TravelDetails() {

  const { travelId } = useParams<{travelId: string}>();
  if(!travelId){
    return <Typography color="error">Invalid travel</Typography>
  }

  const travelQuery = useGetTravelById(travelId!);

  const assignEmployeesTravel = useAssignEmployeesTravel(travelId!);

  const hrDocumentsQuery = useFetchAllBasedOnTravelAndHR(travelId!);


  return (
    <Box sx={{ p: 4 }}>
      {/* Travel Details */}
      <Card sx={{ p: 3, mb: 3, backgroundColor: "background.paper" }}>
        <Typography variant="h6" mb={2}>
          Travel Details
        </Typography>

        <Typography><b>Title:</b> {travelQuery.data?.travelTitle}</Typography>
        <Typography><b>Location:</b> {travelQuery.data?.travelLocation}</Typography>
        <Typography><b>Date:</b> {travelQuery.data?.travelDateFrom} → {travelQuery.data?.travelDateTo}</Typography>

        <Typography mt={2}>
          <b>Description:</b><br />
          {travelQuery.data?.travelDetails}
        </Typography>
      </Card>

      {/* Assigned Employees */}
      <AssignedEmployees employees={assignEmployeesTravel.data ?? []} />

      {/* Travel Documents */}
      {hrDocumentsQuery.isLoading ? (<Typography> Loading....</Typography>) : (
        <TravelDocuments documents={hrDocumentsQuery.data ?? []} />
      )}
    </Box>
  );
}
