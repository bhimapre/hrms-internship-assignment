import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider } from './auth/AuthContext.tsx';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Dashboard from './pages/Dashboard.tsx';
import Login from './pages/Login.tsx';
import { ToastContainer } from 'react-toastify';
import AddJobOpening from './pages/Job Opening/AddJobOpening.tsx';
import JobOpeningCard from './components/JobOpeningCard.tsx';
import ShareJob from './pages/Job Opening/ShareJob.tsx';
import ReferJob from './pages/Job Opening/ReferJob.tsx';
import HRJobOpening from './pages/Job Opening/HRJobOpening.tsx';
import JobOpenings from './pages/Job Opening/JobOpenings.tsx';
import UpdateJobOpening from './pages/Job Opening/UpdateJobOpening.tsx';
import UpdateJDFileJobOpening from './pages/Job Opening/UpdateJDFileJobOpening.tsx';
import AddTravel from './pages/travel/AddTravel.tsx';
import AllJobReferral from './pages/Job Opening/AllJobReferral.tsx';
import UpdateJobReferral from './pages/Job Opening/UpdateJobReferral.tsx';
import UpdateJobReferralCV from './pages/Job Opening/UpdateJobReferralCV.tsx';
import TravelDetailsCard from './components/TravelDetailsCard.tsx';
import { ThemeProvider } from '@emotion/react';
import { theme } from './theme/theme.ts';
import { CssBaseline } from '@mui/material';
import TravelList from './pages/travel/TravelList.tsx';
import TravelDetails from './pages/travel/TravelDetails.tsx';
import EmployeeTravelExpenses from './pages/travel expense/EmployeeTravelExpense.tsx';
import EmployeeTravelDocuments from './pages/travel documents/EmployeeTravelDocuments.tsx';


const router = createBrowserRouter([
  {
    path: "/dashboard",
    element: <Dashboard />
  },
  {
    path: "/login",
    element: <Login />
  },
  {
    path: "/job-opening",
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
