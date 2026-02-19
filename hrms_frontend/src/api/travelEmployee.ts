import type { AssignEmployeeTravel } from "../types/Employee";
import api from "./axios";

export const fetchAssignEmployeeApi = async(travelId: string):
Promise<AssignEmployeeTravel[]> => {
    const res = await api.get(`api/travel/employees/${travelId}`);
    return res.data;
}