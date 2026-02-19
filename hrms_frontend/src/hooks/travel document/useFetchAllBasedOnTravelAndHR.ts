import { useQuery } from "@tanstack/react-query"
import { fetchAllDocumentsBasedOnTravelAndHRApi } from "../../api/travelDocumentApi"


export const useFetchAllBasedOnTravelAndHR = (travelId:string) =>{
    return useQuery({
        queryKey: ["travel-documents-hr", travelId],
        queryFn: () => fetchAllDocumentsBasedOnTravelAndHRApi(travelId),
        enabled: !!travelId,
    })
}


