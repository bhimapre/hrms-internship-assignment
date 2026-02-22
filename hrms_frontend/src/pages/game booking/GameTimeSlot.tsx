import { Box, Button, Paper, Typography, Stack, Grid, Dialog, DialogActions, DialogContent, DialogTitle } from "@mui/material";
import { useState } from "react";
import { getSlotStyles } from "../../theme/getSlotStyles";
import { useGetTimeSlotToady } from "../../hooks/time slot/useGetTimeSlotToday";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { useJoinWaitingQueue } from "../../hooks/waiting queue/useJoinWaitingQueue";
import Navbar from "../../components/Navbar";
import Sidebar from "../../components/Sidebar";

export default function GameTimeSlot() {

    const navigate = useNavigate();
    const { gameId } = useParams<{ gameId: string }>();
    if (!gameId) {
        toast.error("Game not found");
        return;
    }

    const [showSlots, setShowSlots] = useState(true);
    const [selectedSlotId, setSelectedSlotId] = useState<string | null>(null);
    const [isCollapsed, setIsCollapsed] = useState(false);

    const { data: timeSlots } = useGetTimeSlotToady(gameId);
    const [openQueueDialog, setOpenQueueDialog] = useState(false);
    const { mutate: joinQueue } = useJoinWaitingQueue();

    const selectedSlot = timeSlots?.find(
        (slot) => slot.timeSlotId === selectedSlotId
    );

    const handleJoinWaitingQueue = () => {
        if (!selectedSlotId) return;

        joinQueue({ timeSlotId: selectedSlotId, gameId });
    };


    return (
        <div className="flex flex-col h-screen">
            {/* Navbar */}
            <Navbar />

            {/* Main Layout */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                <Sidebar isCollapsed={isCollapsed} setIsCollapsed={setIsCollapsed} />

                {/* Page Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <Box p={4}>
                        {showSlots && (
                            <Paper sx={{ p: 3, mt: 4 }}>
                                <Typography
                                    variant="h6"
                                    mb={2}>
                                    Today's Time Slots
                                </Typography>
                                {timeSlots?.length === 0 && (<Typography>No Time Slot Found For Today</Typography>)}
                                <Grid container spacing={2}>
                                    {timeSlots?.map((slot) => {
                                        const isSelected =
                                            selectedSlotId === slot.timeSlotId;
                                        return (
                                            <Grid
                                                key={slot.timeSlotId}
                                                size={{ xs: 4, sm: 3, md: 2 }}>
                                                <Button
                                                    fullWidth
                                                    disabled={slot.status === "PAST"}
                                                    sx={{
                                                        height: 48,
                                                        fontWeight: "bold",
                                                        ...getSlotStyles(
                                                            slot.status,
                                                            isSelected
                                                        ),
                                                    }}
                                                    onClick={() => setSelectedSlotId(slot.timeSlotId)}>
                                                    {slot.startTime}
                                                </Button>
                                            </Grid>
                                        );
                                    })}
                                </Grid>

                                {selectedSlotId && (
                                    <Stack
                                        direction="row"
                                        justifyContent="flex-end"
                                        mt={3}>
                                        {selectedSlot?.status === "AVAILABLE" && (
                                            <Button onClick={() => navigate(`/game-booking/game/${gameId}/time-slot/${selectedSlotId}`)} variant="contained"> Confirm Booking </Button>
                                        )}

                                        {(selectedSlot?.status === "AVAILABLE" ||
                                            selectedSlot?.status === "BOOKED") && (
                                                <Button
                                                    variant="outlined"
                                                    color="warning"
                                                    onClick={() => setOpenQueueDialog(true)}>
                                                    Join Waiting Queue
                                                </Button>
                                            )}
                                    </Stack>
                                )}
                            </Paper>
                        )}
                        <Dialog
                            open={openQueueDialog}
                            onClose={() => setOpenQueueDialog(false)}>
                            <DialogTitle>Join Waiting Queue</DialogTitle>

                            <DialogContent>
                                <Typography> Are you sure you want to join the waiting queue for this time slot? </Typography>
                            </DialogContent>

                            <DialogActions>
                                <Button
                                    onClick={() => setOpenQueueDialog(false)}
                                    color="inherit">
                                    Cancel
                                </Button>

                                <Button
                                    onClick={() => {
                                        handleJoinWaitingQueue();
                                        setOpenQueueDialog(false);
                                    }}
                                    variant="contained"
                                    color="warning" >
                                    Confirm
                                </Button>
                            </DialogActions>
                        </Dialog>
                    </Box>
                </main>
            </div>
        </div>
    );
}
