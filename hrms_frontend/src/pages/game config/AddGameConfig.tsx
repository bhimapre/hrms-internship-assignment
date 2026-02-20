import React from 'react'
import { useForm } from 'react-hook-form'
import type { GameConfigBase } from '../../types/GameConfig'
import Navbar from '../../components/Navbar';
import { useCreateGameConfig } from '../../hooks/game config/useCreateGameConfig';
import Loading from '../../components/Loading';
import { useFetchAllGames } from '../../hooks/game/useFetchAllGames';

const AddGameConfig = () => {

    const { register, formState: { errors }, handleSubmit } = useForm<GameConfigBase>();

    const { mutate: createConfig, isPending} = useCreateGameConfig();

    const{ data: games} = useFetchAllGames();

    const onSubmit = (data: GameConfigBase) => {
        createConfig(data);
    }

    if(isPending){
        return <Loading />
    }

    return (
        <div className="flex flex-col h-screen">
            {/* Navbar */}
            <Navbar />

            {/* Main Layout */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                {/* <Sidebar /> */}

                {/* Page Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Add Game Config</h1>
                    </div>
                    <form
                        onSubmit={handleSubmit(onSubmit)}
                        className={`grid grid-cols-1 md:grid-cols-2 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>

                        {/* Game */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Game NAme <span className="text-rose-500">*</span></label>
                            <select {...register("gameId", { required: true })} className="w-full p-2 rounded bg-neutral-800 border border-neutral-700">
                                <option value="" disabled>Select Game</option>
                                {games?.map((game: any) => (
                                    <option key={game.gameId} value={game.gameId}>{game.gameName}</option>
                                ))}
                            </select>
                            {errors.gameId && (<p className="text-rose-500 text-sm mt-1">Game is required</p>)}
                        </div>

                        {/* Start Time */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Start time <span className="text-rose-500">*</span></label>
                            <input
                                {...register("configStartTime", { required: true })}
                                type="time"
                                placeholder="Enter Start Time"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.configStartTime && (<p className="text-rose-500 text-sm mt-1">Start Time is required</p>)}
                        </div>

                        {/* End Time */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> End Time <span className="text-rose-500">*</span></label>
                            <input
                                {...register("configEndTime", { required: true })}
                                type="time"
                                placeholder="Enter End Time"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.configEndTime && (<p className="text-rose-500 text-sm mt-1">End Time is required</p>)}
                        </div>

                        {/* Slot Duration */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Slot Duration <span className="text-rose-500">*</span></label>
                            <input
                                {...register("slotDuration", { required: true })}
                                type="number"
                                placeholder="Enter Slot Duration "
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.slotDuration && (<p className="text-rose-500 text-sm mt-1">Slot Duration is required</p>)}
                        </div>

                        {/* Max Player */}
                        <div>
                            <label className="block mb-1 text-sm font-medium">Max Player <span className="text-rose-500">*</span></label>
                            <input
                                {...register("maxPlayers", { required: true })}
                                type="number"
                                min = "2"
                                max= "4"
                                placeholder="Enter Max Player"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.maxPlayers && (<p className="text-rose-500 text-sm mt-1">Max Player is required</p>)}
                        </div>

                        {/* Active */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Game <span className="text-rose-500">*</span></label>
                            <select {...register("active", { required: true, setValueAs: (value) => value ==="true" })} className="w-full p-2 rounded bg-neutral-800 border border-neutral-700">
                                <option value="" disabled>Active </option>
                                <option value="true">true</option>
                                <option value="false">false</option>
                            </select>
                            {errors.active && (<p className="text-rose-500 text-sm mt-1">Game is required</p>)}
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2">
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                + Add Game config
                            </button>
                        </div >
                    </form >
                </main>
            </div>
        </div>
    )
}

export default AddGameConfig