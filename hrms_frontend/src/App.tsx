import { useState } from 'react'
import { ToastContainer } from 'react-toastify';
import { BrowserRouter as Router, Routes, Route, BrowserRouter } from "react-router-dom";
import './App.css'
import Login from "./pages/Login";
import Dashboard from './pages/Dashboard';
import AddJobOpening from './pages/Job Opening/AddJobOpening';
function App() {

  return (
    <>
      <ToastContainer position='top-right' autoClose={2000} theme='colored' />
      <BrowserRouter>
        <Routes>
          <Route path='/login' element={<Login />} />
          <Route path='/dashboard' element={<Dashboard />} />
          <Route path='/add-job-opening' element={<AddJobOpening />} />
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
