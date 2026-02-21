import React, { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import type { FetchGame } from '../../types/Game'
import { useParams } from 'react-router-dom'
import { useUpdateGames } from '../../hooks/game/useUpdateGame'
import { useFetchGameById } from '../../hooks/game/useFetchGameById'
import Loading from '../../components/Loading'
import Navbar from '../../components/Navbar'
import Sidebar from '../../components/Sidebar'

const UpdateGame = () => {

    const [isCollapsed, setIsCollapsed] = useState(false);

    const { gameId } = useParams<{ gameId: string }>();

    const { mutate: updateGame } = useUpdateGames();

    const { data: gameDate, isLoading } = useFetchGameById(gameId);

    const { register, handleSubmit, formState: { errors }, reset } = useForm<FetchGame>();

    const onSubmit = (data: FetchGame) => {
        if (!gameId) {
            return <div> Game Id not found </div>
        }

        updateGame({ gameId, data });
    }

    useEffect(() => {
        if (gameDate) {
            reset(gameDate);
        }
    }, [gameDate, reset]);

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
                {/* Page Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Update Games</h1>
                    </div>
                    <form
                        onSubmit={handleSubmit(onSubmit)}
                        className={`grid grid-cols-1 md:grid-cols-2 gap-4 bg-neutral-900 p-4 sm:p-6 rounded-lg shadow-lg`}>

                        {/* Game Name */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Game Name <span className="text-rose-500">*</span></label>
                            <input
                                {...register("gameName", { required: true })}
                                type="text"
                                placeholder="Enter Game Name"
                                className="w-full p-2 rounded bg-neutral-800 border border-neutral-700" />
                            {errors.gameName && (<p className="text-rose-500 text-sm mt-1">Game Name is required</p>)}
                        </div>

                        {/* Game Status */}
                        <div>
                            <label className="block mb-1 text-sm font-medium"> Game status <span className="text-rose-500">*</span></label>
                            <select {...register("active", { required: true, setValueAs: (value) => value === "true" })} className="w-full p-2 rounded bg-neutral-800 border border-neutral-700">
                                <option value="" disabled>Select Status of game</option>
                                <option value="true">true</option>
                                <option value="false">false</option>
                            </select>
                            {errors.active && (<p className="text-rose-500 text-sm mt-1">Game status is required</p>)}
                        </div>

                        {/* Submit */}
                        <div className="md:col-span-2">
                            <button
                                type="submit"
                                className="w-full p-2 rounded font-medium transition bg-purple-600 hover:bg-purple-500">
                                + Update Games
                            </button>
                        </div >
                    </form >
                </main>
            </div>
        </div>
    )
}

export default UpdateGame