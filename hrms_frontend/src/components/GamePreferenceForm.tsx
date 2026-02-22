import React from 'react'
import { Box, Button, Chip, FormControl, InputLabel, MenuItem, OutlinedInput, Select, Typography, } from "@mui/material";
import { Controller, useForm } from 'react-hook-form';
import { useFetchAllGames } from '../hooks/game/useFetchAllGames';
import { useUpdateGamePreferences } from '../hooks/employee/useUpdateGamePreferences';

type Props = {
    employeeId: string;
    defaultGames?: string[];
};

type FormValues = {
    gamePreferences: string[];
};

const GamePreferenceForm = ({
    employeeId,
    defaultGames = [],
}: Props) => {

    const { data: games = [] } = useFetchAllGames();
    const updateMutation = useUpdateGamePreferences(employeeId);

    const { control, handleSubmit } = useForm<FormValues>({
        defaultValues: {
            gamePreferences: defaultGames,
        },
    });

    const onSubmit = (data: FormValues) => {
        updateMutation.mutate(data.gamePreferences);
    };
    return (
        <Box sx={{ p: 3, borderRadius: 2, bgcolor: "background.paper" }}>
            <Typography variant="h6" mb={2}> Game Preferences </Typography>

            <form onSubmit={handleSubmit(onSubmit)}>
                <FormControl fullWidth>
                    <InputLabel>Select Games</InputLabel>

                    <Controller
                        name="gamePreferences"
                        control={control}
                        render={({ field }) => (
                            <Select
                                {...field}
                                multiple
                                input={<OutlinedInput label="Select Games" />}
                                renderValue={(selected) => (
                                    <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1 }}>
                                        {selected.map((id) => {
                                            const game = games.find((g: any) => g.gameId === id);
                                            return <Chip key={id} label={game?.gameName} />;
                                        })}
                                    </Box>
                                )}>
                                {games.map((game: any) => (
                                    <MenuItem key={game.gameId} value={game.gameId}>
                                        {game.gameName}
                                    </MenuItem>
                                ))}
                            </Select>
                        )} />
                </FormControl>

                <Box mt={3}>
                    <Button
                        type="submit"
                        variant="contained">
                        Save Preferences
                    </Button>
                </Box>
            </form>
        </Box>
    )
}

export default GamePreferenceForm