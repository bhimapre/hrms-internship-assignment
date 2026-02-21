import api from "./axios";

// Current Employee Org Chart
export const fetchMyOrgChart = async () =>{
    const res = await api.get(`/api/org-chart/me`);
    return res.data;
}

// Get Org Chart employee by search 
export const fetchOrgChartBySearch = async (employeeId: string) =>{
    const res = await api.get(`/api/org-chart/${employeeId}`);
    return res.data;
}

// Search Employees
export const fetchSearchEmployees = async (query: string) =>{
    const res = await api.get(`/api/employees/search?query=${query}`);
    return res.data;
}