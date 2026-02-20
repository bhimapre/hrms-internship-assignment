import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider } from './auth/AuthContext.tsx';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Login from './pages/Login.tsx';
import { ToastContainer } from 'react-toastify';
import AddJobOpening from './pages/Job Opening/AddJobOpening.tsx';
import ShareJob from './pages/Job Opening/ShareJob.tsx';
import ReferJob from './pages/Job Opening/ReferJob.tsx';
import HRJobOpening from './pages/Job Opening/HRJobOpening.tsx';
import UpdateJobOpening from './pages/Job Opening/UpdateJobOpening.tsx';
import UpdateJDFileJobOpening from './pages/Job Opening/UpdateJDFileJobOpening.tsx';
import AddTravel from './pages/travel/AddTravel.tsx';
import AllJobReferral from './pages/Job Opening/AllJobReferral.tsx';
import UpdateJobReferral from './pages/Job Opening/UpdateJobReferral.tsx';
import UpdateJobReferralCV from './pages/Job Opening/UpdateJobReferralCV.tsx';
import { ThemeProvider } from '@emotion/react';
import { theme } from './theme/theme.ts';
import { CssBaseline } from '@mui/material';
import TravelList from './pages/travel/TravelList.tsx';
import TravelDetails from './pages/travel/TravelDetails.tsx';
import EmployeeTravelExpenses from './pages/travel expense/EmployeeTravelExpense.tsx';
import EmployeeTravelDocuments from './pages/travel documents/EmployeeTravelDocuments.tsx';
import UpdateTravel from './pages/travel/UpdateTravel.tsx';
import AddTravelDocument from './pages/travel documents/AddTravelDocument.tsx';
import UpdateTravelDocumentFile from './pages/travel documents/UpdateTravelDocumentFile.tsx';
import AddTravelExpense from './pages/travel expense/AddTravelExpense.tsx';
import AddGames from './pages/games/AddGames.tsx';
import AllGames from './pages/games/AllGames.tsx';
import AddExpenseProof from './pages/expense proofs/AddExpenseProof.tsx';
import JobOpenings from './pages/Job Opening/JobOpenings.tsx';
import Dashboard from './pages/Dashboard.tsx';
import UpdateExpense from './pages/travel expense/UpdateExpense.tsx';
import AllExpenseForHR from './pages/travel expense/AllExpenseForHR.tsx';
import AllGameConfig from './pages/game config/AllGameConfig.tsx';
import AddGameConfig from './pages/game config/AddGameConfig.tsx';
import UpdateGameConfig from './pages/game config/UpdateGameConfig.tsx';
import UpdateGame from './pages/games/UpdateGame.tsx';
import ShowGamesForTimeSlot from './pages/games/ShowGamesForTimeSlot.tsx';

const router = createBrowserRouter([
  {
    path: "/job-openings",
    element: <JobOpenings />
  },
  {
    path: "/dashboard",
    element: <Dashboard />
  },
  {
    path: "/login",
    element: <Login />
  },
  {
    path: "/job-opening/add",
    element: <AddJobOpening />
  },
  {
    path: "/job-opening/share/:jobOpeningId",
    element: <ShareJob />
  },
  {
    path: "/job-opening/referral/:jobOpeningId",
    element: <ReferJob />
  },
  {
    path: "/hr/job-opening",
    element: <HRJobOpening />
  },
  {
    path: "/hr/job-opening/update/:jobOpeningId",
    element: <UpdateJobOpening />
  },
  {
    path: "/hr/job-opening/update-file/:jobOpeningId",
    element: <UpdateJDFileJobOpening />
  },
  {
    path: "/hr/travel-opening/add",
    element: <AddTravel />
  },
  {
    path: "/hr/job-referrals",
    element: <AllJobReferral />
  },
  {
    path: "/job-referral/update/:jobReferralId",
    element: <UpdateJobReferral />
  },
  {
    path: "/job-referral/update-cv/:jobReferralId",
    element: <UpdateJobReferralCV />
  },
  {
    path: "/hr/job-opening",
    element: <HRJobOpening />
  },
  {
    path: "/travel/:travelId",
    element: <TravelDetails />
  },
  {
    path: "/travel-list",
    element: <TravelList />
  },
  {
    path: "/expense/travel/:travelId/employee/:employeeId",
    element: <EmployeeTravelExpenses />
  },
  {
    path: "/employee-document",
    element: <EmployeeTravelDocuments />
  },
  {
    path: "/travel/:travelId/employee/:employeeId",
    element: <EmployeeTravelDocuments />
  },
  {
    path: "/travel/update/:travelId",
    element: <UpdateTravel />
  },
  {
    path: "/hr/travel-documents/:travelId",
    element: <AddTravelDocument />
  },
  {
    path: "/travel-documents/update-file/:travelDocumentId",
    element: <UpdateTravelDocumentFile />
  },
  {
    path: "/travel-expense/:travelId",
    element: <AddTravelExpense />
  },
  {
    path: "/expense/add/:travelId",
    element: <AddTravelExpense />
  },
  {
    path: "/travel/expense-proof/:expenseId",
    element: <AddExpenseProof />
  },
  {
    path: "/travel-documents/add/:travelId",
    element: <AddTravelDocument />
  },
  {
    path: "/expense/update/:expenseId",
    element: <UpdateExpense />
  },
  {
    path: "/hr/all-expenses/:travelId",
    element: <AllExpenseForHR />
  },
  {
    path: "/hr/game/add",
    element: <AddGames />
  },
  {
    path: "/hr/game", 
    element: <AllGames />
  },
  {
    path: "/hr/game/update/:gameId",
    element: <UpdateGame />
  },
  {
    path: "/hr/game-config",
    element: <AllGameConfig />
  },
  {
    path: "/hr/game-config/add",
    element: <AddGameConfig />
  },
  {
    path: "/hr/game-config/update/:configId",
    element: <UpdateGameConfig />
  },
  {
    path: "/games/booking",
    element: <ShowGamesForTimeSlot />
  }
]);

const queryClient = new QueryClient();

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ToastContainer position='top-right' autoClose={2000} theme='colored' />
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <ThemeProvider theme={theme}>
          <CssBaseline />
          <RouterProvider router={router} />
          <App />
        </ThemeProvider>
      </AuthProvider>
    </QueryClientProvider>
  </StrictMode>
)
