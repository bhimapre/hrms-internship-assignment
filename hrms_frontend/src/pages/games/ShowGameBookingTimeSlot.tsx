import React, { useState } from 'react'
import { Box, Button, Paper, Stack, Typography } from "@mui/material";
import { useNavigate } from 'react-router-dom';
import { useFetchAllGames } from '../../hooks/game/useFetchAllGames';
import Loading from '../../components/Loading';
import Navbar from '../../components/Navbar';
import Sidebar from '../../components/Sidebar';

const ShowGameBookingTimeSlot = () => {

    const [isCollapsed, setIsCollapsed] = useState(false);
    const navigate = useNavigate();
    const { data: games, isLoading, isError } = useFetchAllGames();
    const role = localStorage.getItem("role");

    if (isLoading) {
        return <Loading />
    }

    if (isError) {
        return <div>Failed to fetch data</div>
    }

    return (
        <div className="flex flex-col h-screen bg-neutral-950 text-neutral-100">
            {/* Navbar */}
            <Navbar />

            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                <Sidebar isCollapsed={isCollapsed} setIsCollapsed={setIsCollapsed} />

                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">

                    <Box p={4}>
                        <Typography variant="h4" align='center' fontWeight="bold" mb={3}> Games </Typography>

                        <Stack spacing={2}>
                            {games!.map((game) => (
                                <Paper
                                    key={game.gameId}
                                    sx={{
                                        p: 3,
                                        display: "flex",
                                        alignItems: "center",
                                        justifyContent: "space-between",
                                    }}>

                                    <Box>
                                        <Typography variant="h6" fontWeight="bold"> {game.gameName} </Typography>
                                    </Box>

                                    <Stack direction="row" spacing={2}>
                                        {role === "HR" && (
                                            <Button variant="outlined"
                                                onClick={() => navigate(`/hr/time-slot-creation/${game.gameId}`)}> Create Time Slot </Button>
                                        )}

                                        <Button
                                            variant="contained"
                                            onClick={() => navigate(`/time-slots/${game.gameId}`)}>
                                            Book Game
                                        </Button>
                                    </Stack>
                                </Paper>
                            ))}
                        </Stack>
                    </Box>
                </main>
            </div>
        </div>
    )
}

export default ShowGameBookingTimeSlot