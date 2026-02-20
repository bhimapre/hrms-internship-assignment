export interface TravelDocumentsBase{
    documentName: string;
}

export interface AddTravelDocumentBase extends TravelDocumentsBase{
    file: FileList;
}

export interface UpdateTravelDocumentDetails extends TravelDocumentsBase{
    travelDocumentId: string;
}

export interface TravelDocuments{
    travelDocumentId: string;
    documentName: string;
    travelDocumentFileUrl: string;
    ownerType: "HR" | "EMPLOYEE";
    travelId: string;
    uploadedBy: string;
}