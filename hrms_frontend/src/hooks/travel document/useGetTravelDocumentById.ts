import { useQuery } from "@tanstack/react-query";
import { fetchTravelDocument } from "../../api/travelDocumentApi";

export const useGetTravelDocumentById = (travelDocumentId?: string) =>{
    return useQuery({
        queryKey: ["travel-document", travelDocumentId],
        queryFn: () => fetchTravelDocument(travelDocumentId!),
        enabled: !!travelDocumentId
    });
}