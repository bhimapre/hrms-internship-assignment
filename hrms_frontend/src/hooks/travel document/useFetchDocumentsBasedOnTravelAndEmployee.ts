import { useQuery } from "@tanstack/react-query"
import { employeeTravelDocuments } from "../../api/travelDocumentApi"

export const useFetchDocumentsBasedOnTravelAndEmployee = (travelId:string, employeeId:string) =>{
    return useQuery({
        queryKey: ["travel-documents-employee", travelId],
        queryFn: () => employeeTravelDocuments(travelId, employeeId),
        enabled: !!travelId,
    })
}