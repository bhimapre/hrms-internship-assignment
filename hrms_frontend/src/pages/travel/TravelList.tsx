import { Box, Card, Typography, Button, Stack } from "@mui/material";
import { useContext, useState } from "react";
import AuthContext from "../../auth/AuthContext";
import { useFetchAllTravels } from "../../hooks/travel/useFetchAllTravels";
import Loading from "../../components/Loading";
import { useNavigate } from "react-router-dom";
import { useTravelDelete } from "../../hooks/travel/useTravelDelete";
import { toast } from "react-toastify";
import { useAssignEmployees } from "../../hooks/employee/useAssignEmployees";
import { useGetManagerAssignedTravelApi } from "../../hooks/employee/useGetManagerAssignedTravelApi";
import Navbar from "../../components/Navbar";
import Sidebar from "../../components/Sidebar";

export default function TravelList() {

    const role = localStorage.getItem("role");
    const [isCollapsed, setIsCollapsed] = useState(false);

    const isHr = role === "HR";
    const isManager = role === "MANAGER";
    const [view, setView] = useState<"MY" | "TEAM">("MY");

    const { data: mamagerTravles } = useGetManagerAssignedTravelApi();

    const [page, setPage] = useState(0);
    const navigate = useNavigate();

    const { data: hrData, isLoading } = useFetchAllTravels(page, 5);

    const { handleDelete } = useTravelDelete();

    const { data: assignTravels } = useAssignEmployees();

    const travels = isHr ? hrData?.content
        : isManager ? view === "MY"
            ? assignTravels : mamagerTravles
            : assignTravels;

    const showPagination = isHr && hrData;

    if (isLoading) {
        return <Loading />
    }

    return (
        <div className="flex flex-col h-screen">
            {/* Navbar */}
            <Navbar />

            {/* Main Layout */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                <Sidebar isCollapsed={isCollapsed} setIsCollapsed={setIsCollapsed} />

                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <Box sx={{ p: 4 }}>
                        <Typography variant="h5" mb={3}>
                            Travel Plans
                        </Typography>

                        {role === "HR" && (
                            <Button onClick={() => navigate(`/hr/travel-opening/add`)} variant="contained">Add Travel</Button>
                        )}

                        {isManager && (
                            <Stack direction="row" spacing={2} mb={2}>
                                <Button variant={view === "MY" ? "contained" : "outlined"}
                                    onClick={() => setView("MY")}>
                                    My travels
                                </Button>

                                <Button variant={view === "TEAM" ? "contained" : "outlined"}
                                    onClick={() => setView("TEAM")}>
                                    Team travels
                                </Button>
                            </Stack>
                        )}

                        <Stack mt={2} spacing={2}>
                            {travels?.map((travel: any) => {
                                const travelId = travel.travelId;
                                if (!travelId) {
                                    console.warn("missing travel", travel);
                                    return null;
                                }

                                return <Card
                                    key={travelId}
                                    sx={{
                                        p: 3,
                                        backgroundColor: "background.paper",
                                        borderRadius: 2,
                                        display: "flex",
                                        justifyContent: "space-between",
                                        alignItems: "center",
                                    }}>
                                    <Box>
                                        <Typography fontWeight={600}>{travel.travelTitle || travel.travel.travelTitle}</Typography>
                                        <Typography color="text.secondary">
                                            {travel.travelLocation} | {travel.travelDateFrom} → {travel.travelDateTo}
                                        </Typography>
                                    </Box>

                                    <Stack direction="row" spacing={1}>
                                        <Button onClick={() => navigate(`/travel/${travelId}`)} variant="contained">View</Button>
                                        {role === "HR" && (<>
                                            <Button onClick={() => navigate(`/travel/update/${travel.travelId}`)} variant="contained">Update</Button>
                                            <Button onClick={() => handleDelete(travel.travelId)} variant="contained" color="error">Delete</Button>
                                            <Button onClick={() => navigate(`/hr/all-expenses/${travel.travelId}`)} variant="contained">Expense</Button>
                                        </>)}
                                    </Stack>
                                </Card>
                            })}
                        </Stack>

                        {/* Pagination */}
                        {showPagination && (<div className="flex items-center justify-center gap-4 mt-10">
                            <button
                                disabled={hrData?.first}
                                onClick={() => setPage((p) => p - 1)}
                                className={`px-4 py-1 rounded-full text-sm
                            ${hrData?.first
                                        ? "bg-neutral-700 text-neutral-400 cursor-not-allowed"
                                        : "bg-purple-700 hover:bg-purple-800 text-white"
                                    }`}>
                                Prev
                            </button>

                            <span className="text-neutral-300 text-sm">
                                Page <span className="text-white font-semibold">{page + 1}</span> of{" "}
                                <span className="text-white font-semibold">
                                    {hrData?.totalPages}
                                </span>
                            </span>

                            <button
                                disabled={hrData?.last}
                                onClick={() => setPage((p) => p + 1)}
                                className={`px-4 py-1 rounded-full text-sm
                            ${hrData?.last
                                        ? "bg-neutral-700 text-neutral-400 cursor-not-allowed"
                                        : "bg-purple-700 hover:bg-purple-800 text-white"
                                    }`}>
                                Next
                            </button>
                        </div>
                        )}
                    </Box>
                </main>
            </div>
        </div>
    );
}