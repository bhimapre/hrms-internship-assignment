import React from 'react'
import { Edit } from 'lucide-react'
import Navbar from '../../components/Navbar'
import { useFetchAllGameConfig } from '../../hooks/game config/useFetchAllGameConfig';
import { useNavigate } from 'react-router-dom';
import Loading from '../../components/Loading';

const AllGameConfig = () => {

    const { data, isLoading, isError } = useFetchAllGameConfig();
    const navigate = useNavigate();

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

            {/* Main Layout */}
            <div className="flex flex-1 overflow-hidden">
                {/* Sidebar */}
                {/* <Sidebar /> */}

                {/* Main Content */}
                <main className="flex-1 bg-neutral-950 text-white p-6 overflow-y-auto">
                    <div className="text-center mt-8 mb-8">
                        <h1 className="text-4xl font-bold text-center text-white mb-4">Games Config</h1>
                    </div>

                    <div className="flex-1 overflow-y-auto p-3 sm:p-4 lg:p-6 max-w-7xl mx.auto w-full min-w-0">
                        {/* Main Layout */}
                        <div className="flex flex-col sm:flex-row gap-2 sm:gap-3 mb-4 sm:justify-end">
                            <button
                                onClick={() => navigate("/hr/game-config/add")}
                                className="px-3 py-1 bg-emerald-700 hover:bg-emerald-600 rounded text-sm w-full sm:w-auto">
                                + Add Game Config
                            </button>
                        </div>

                        {/* Card View Data */}
                        <div className="space-y-2">
                            {data?.length === 0 && (
                                <div>No Game found</div>
                            )}
                            {data?.map((game) => (
                                <div
                                    key={game.gameId}
                                    className="bg-neutral-900 border border-neutral-700 rounded-md p-3 sm:p-4 shadow-sm hover:shadow-md transition flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 text-sm">
                                    <div className="flex items-start sm:items-center gap-3 flex-1 min-w-0">
                                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-x-4 sm:gap-x-6 gap-y-1 flex-1 min-w-0">
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Game Name:</span> {game.gameName}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Start time:</span> {game.configStartTime}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">End time:</span> {game.configEndTime}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Slot Duration:</span> {game.slotDuration}</p>
                                            <p className="wrap-break-word"><span className="font-medium text-purple-300">Max Player:</span> {game.maxPlayers}</p>
                                        </div>
                                    </div>
                                    <div className="flex flex-col sm:flex-row gap-2 sm:ml-4 w-full sm:w-auto">
                                        {/* Update Button */}
                                        <>
                                            <button
                                                onClick={() => navigate(`/hr/game-config/update/${game.configId}`)}
                                                className="flex items-center gap-1 px-2 py-1 bg-amber-700 hover:bg-amber-600 rounded text-xs w-full sm:w-auto">
                                                <Edit size={14} /> Update
                                            </button>
                                        </>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </main>
            </div>
        </div>
    )
}

export default AllGameConfig