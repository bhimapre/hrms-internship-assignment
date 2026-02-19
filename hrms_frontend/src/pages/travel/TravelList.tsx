import { Box, Card, Typography, Button, Stack } from "@mui/material";
import { useContext, useState } from "react";
import AuthContext from "../../auth/AuthContext";
import { useFetchAllTravels } from "../../hooks/travel/useFetchAllTravels";
import Loading from "../../components/Loading";
import { useNavigate } from "react-router-dom";

export default function TravelList() {

    const role = localStorage.getItem("role");
    const [page, setPage] = useState(0);
    const navigate = useNavigate();

    const { data, isLoading } = useFetchAllTravels(page, 5);

    if (isLoading) {
        return <Loading />
    }

    return (
        <Box sx={{ p: 4 }}>
            <Typography variant="h5" mb={3}>
                Travel Plans
            </Typography>

            <Stack spacing={2}>
                {data?.content.map((travel) => (
                    <Card
                        key={travel.travelId}
                        sx={{
                            p: 3,
                            backgroundColor: "background.paper",
                            borderRadius: 2,
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                        }}>
                        <Box>
                            <Typography fontWeight={600}>{travel.travelTitle}</Typography>
                            <Typography color="text.secondary">
                                {travel.travelLocation} | {travel.travelDateFrom} → {travel.travelDateTo}
                            </Typography>
                        </Box>

                        <Stack direction="row" spacing={1}>
                            <Button onClick={() => navigate(`/travel/${travel.travelId}`)} variant="contained">View</Button>

                            {role === "HR" && (<>
                                <Button onClick={() => navigate(`/travel/update/${travel.travelId}`)} variant="contained">Update</Button>
                                <Button variant="contained" color="error">Delete</Button>
                            </>)}
                        </Stack>
                    </Card>
                ))}
            </Stack>

            {/* Pagination */}
            <div className="flex items-center justify-center gap-4 mt-10">
                <button
                    disabled={data?.first}
                    onClick={() => setPage((p) => p - 1)}
                    className={`px-4 py-1 rounded-full text-sm
                            ${data?.first
                            ? "bg-neutral-700 text-neutral-400 cursor-not-allowed"
                            : "bg-purple-700 hover:bg-purple-800 text-white"
                        }`}>
                    Prev
                </button>

                <span className="text-neutral-300 text-sm">
                    Page <span className="text-white font-semibold">{page + 1}</span> of{" "}
                    <span className="text-white font-semibold">
                        {data?.totalPages}
                    </span>
                </span>

                <button
                    disabled={data?.last}
                    onClick={() => setPage((p) => p + 1)}
                    className={`px-4 py-1 rounded-full text-sm
                            ${data?.last
                            ? "bg-neutral-700 text-neutral-400 cursor-not-allowed"
                            : "bg-purple-700 hover:bg-purple-800 text-white"
                        }`}>
                    Next
                </button>
            </div>
        </Box>
    );
}