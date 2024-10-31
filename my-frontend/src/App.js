import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SignUp from './components/SignUp';
import SkillSelection from './components/SkillSelection';
import Dashboard from './components/Dashboard';
import './App.css';
import SolveTask from './components/SolveTask';
import { UserProvider } from './UserContext';
import Welcome from './components/Welcome';
import Login from './components/Login';
import AdminDashboard from './components/AdminDashboard';
import TaskReview from './components/TaskReview';

function App() {
  return (
    <UserProvider>
                <Router>
                    <Routes>
                        <Route path="/" element={<Welcome />} />
                        <Route path="/signup" element={<SignUp />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/admin" element={<AdminDashboard />} />
                         <Route path="/admin/*" element={<AdminDashboard />} /> {/* Admin Dashboard route */}
                        <Route path="/select-skill/:userId" element={<SkillSelection />} />
                        <Route path="/dashboard/:userId" element={<Dashboard />} />
                        <Route path="/tasks/:taskId/solve" element={<SolveTask />} />
                        <Route path="/task-review/:taskId" element={<TaskReview />} />

                    </Routes>
                </Router>
            </UserProvider>

  );
}

export default App;
