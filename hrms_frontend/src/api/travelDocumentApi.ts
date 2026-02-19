import type { TravelDocuments, TravelDocumentsBase } from "../types/TravelDocuments";
import api from "./axios";

// Add Travel Document
export const addTravelDocument = async (travelId: string, formData: FormData) => {
    const res = await api.post(`/api/travel-documents/${travelId}`, formData);
    return res.data;
}

// Fetch Travel Document By Id
export const fetchTravelDocument = async (documentId: string) => {
    const res = await api.get(`/api/travel-documents/${documentId}`);
    return res.data;
}

// Update Travel Document Details
export const updateTravelDocument = async (documentId: string, data: TravelDocumentsBase) => {
    const res = await api.put(`/api/travel-documents/${documentId}`, data);
    return res.data;
}

// Update Travel Document File
export const updateTravelDocumentFile = async (documentId: string, file: File) => {
    const formData = new FormData();
    formData.append("file", file);

    const res = await api.put(`/api/travel-documents/file/${documentId}`, formData);
    return res.data;
}

// Fetch All documents based on travel id and HR
export const fetchAllDocumentsBasedOnTravelAndHRApi = async (travelId: string):
Promise<TravelDocuments[]> =>{
    const res = await api.get(`/api/travel-documents/document-hr/${travelId}`);
    return res.data;
}


// Fetch ALl documents based on travel id and employee id
export const employeeTravelDocuments = async (travelId: string, employeeId: string):
Promise<TravelDocuments[]> =>{
    const res = await api.get(`/api/travel-documents/${travelId}/${employeeId}`);
    return res.data;
}
