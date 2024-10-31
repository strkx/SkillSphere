import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './TaskManagement.css';
import TaskReview from './TaskReview';
import { useNavigate } from 'react-router-dom';

const TaskManagement = () => {
    const [tasks, setTasks] = useState([]);
    const navigate = useNavigate();
    // In the parent component
    <TaskReview tasks={tasks} />


    useEffect(() => {
        axios.get('http://localhost:8080/api/tasks/all')
            .then(response => setTasks(response.data))
            .catch(error => console.error("Error fetching tasks:", error));
    }, []);
     const handleReviewClick = (taskId) => {
             navigate(`/task-review/${taskId}`);
         };

    return (
        <div className="task-management-container">
            <h2>Manage Tasks</h2>
            <div className="task-grid">
                {tasks.map(task => (
                    <div key={task.id} className="task-card">
                        <h3>{task.title}</h3>
                        <p><strong>Description:</strong> {task.description}</p>
                        <p><strong>Difficulty:</strong> {task.difficulty}</p>
                        <div className="task-actions">
                            <button className="review-btn" onClick={() => handleReviewClick(task.id)}>Review</button>
                            <button className="delete-btn">Delete</button>
                            <button className="reject-btn">Reject</button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default TaskManagement;
