import type { AssignEmployeeTravel } from "../types/Employee";
import api from "./axios";

export const fetchAssignEmployeeApi = async(travelId: string):
Promise<AssignEmployeeTravel[]> => {
    const res = await api.get(`/api/travel/employees/${travelId}`);
    return res.data;
}

export const fetchAssignedTravelsApi = async():
Promise<AssignEmployeeTravel[]> => {
    const res = await api.get(`api/travel/assigned`);
    return res.data;
}

export const fetchManagerAssignedEmployee = async():
Promise<AssignEmployeeTravel[]> => {
    const res = await api.get(`/api/travel/manager/assigned`);
    return res.data;
}