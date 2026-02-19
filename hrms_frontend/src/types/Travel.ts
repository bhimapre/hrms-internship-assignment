export interface TravelBase{
    travelTitle: string;
    travelDateFrom: string;
    travelDateTo: string;
    travelLocation: string;
    travelDetails: string;
}

export interface CreateTravelRequest{
    travelTitle: string;
    travelDateFrom: string;
    travelDateTo: string;
    travelLocation: string;
    travelDetails: string;
    employeeIds: string[];
}

export interface UpdateTravelRequest extends TravelBase{
    travelId: string;
}

export interface PageResponse<T> {
    content: T[];
    totalPages: number;
    number: number;
    size: number;
    first: boolean;
    last: boolean;
}

export interface GetAllTravels extends TravelBase{
    travelId: string;
}