import { Box, Button, Paper, Typography, Stack, Grid } from "@mui/material";
import { useState } from "react";
import { getSlotStyles } from "../../theme/getSlotStyles";
import { useGetTimeSlotToady } from "../../hooks/time slot/useGetTimeSlotToday";
import { useParams } from "react-router-dom";
import { toast } from "react-toastify";

export default function GameTimeSlot() {

    const { gameId } = useParams<{ gameId: string }>();
    if (!gameId) {
        toast.error("Game not found");
        return;
    }

    const [showSlots, setShowSlots] = useState(true);
    const [selectedSlotId, setSelectedSlotId] = useState<string | null>(null);

    const { data: timeSlots } = useGetTimeSlotToady(gameId);

    return (
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
                                        disabled={slot.status !== "AVAILABLE"}
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
                            <Button variant="contained"> Confirm Booking </Button>
                        </Stack>
                    )}
                </Paper>
            )}
        </Box>
    );
}
