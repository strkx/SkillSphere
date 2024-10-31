import React from 'react';
import { Link, Route, Routes } from 'react-router-dom';
import TaskManagement from './TaskManagement';
import './AdminDashboard.css';

const AdminDashboard = () => {
    return (
        <div className="admin-dashboard">
            <nav className="admin-nav">
                <Link to="/admin/task-management">Task Management</Link>
                <Link to="/admin/user-management">User Management</Link>
                <Link to="/admin/skill-management">Skill Management</Link>
                <Link to="/admin/analytics">Analytics</Link>
            </nav>
            <div className="admin-content">
                <Routes>
                    <Route path="task-management" element={<TaskManagement />} />
                    {/* Add other routes here */}
                </Routes>
            </div>
        </div>
    );
};

export default AdminDashboard;
