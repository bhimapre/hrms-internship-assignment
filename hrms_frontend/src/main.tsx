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
    path: "/all-job-opening",
    element: <JobOpenings />
  },
  {
    path: "/share-job",
    element: <ShareJob />
  },
  {
    path: "/refer-job",
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
    path: "hr/job-opening/update-file/:jobOpeningId",
    element: <UpdateJDFileJobOpening />
  }
]);

const queryClient = new QueryClient();

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ToastContainer position='top-right' autoClose={2000} theme='colored' />
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <RouterProvider router={router} />
          <App />
      </AuthProvider>
    </QueryClientProvider>
  </StrictMode>
)
