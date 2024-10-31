import React, { useContext, useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import './Dashboard.css';
import CreateTask from './CreateTask';
import { UserContext } from '../UserContext';

const Dashboard = () => {
    const { userId } = useParams();
    const [user, setUser] = useState({});
    const [tasks, setTasks] = useState([]);
    const [recommendedResources, setRecommendedResources] = useState([]);
    const [isTaskCreatorOpen, setIsTaskCreatorOpen] = useState(false);
    const navigate = useNavigate();

    // Set userId in UserContext if required by other components
    const { setUserId } = useContext(UserContext);
    useEffect(() => {
        setUserId(userId);  // Set userId in the context for global access
    }, [userId, setUserId]);

    // Fetch user, tasks, and recommended resources data
    useEffect(() => {
        axios.get(`http://localhost:8080/api/users/${userId}`)
            .then(response => setUser(response.data))
            .catch(error => console.error("Error fetching user data:", error));

        axios.get(`http://localhost:8080/api/tasks/user/${userId}`)
            .then(response => setTasks(response.data))
            .catch(error => console.error("Error fetching tasks:", error));

        axios.get(`http://localhost:8080/api/users/${userId}/suggestedTasks`)
            .then(response => setRecommendedResources(response.data))
            .catch(error => console.error("Error fetching recommended resources:", error));
    }, [userId]);

    // Open and close the Create Task modal
    const openTaskCreator = () => setIsTaskCreatorOpen(true);
    const closeTaskCreator = () => setIsTaskCreatorOpen(false);

    // Handle task assignment
    const assignTask = async (taskId) => {
        try {
            await axios.put(`http://localhost:8080/api/users/${userId}/assignTask/${taskId}`);
            alert("Task successfully assigned to you!");
        } catch (error) {
            console.error("Error assigning task:", error);
            alert("Failed to assign task.");
        }
    };
     const handleLogout = () => {
            // Clear user context or any authentication tokens
            setUserId(null); // clear user ID from context
            localStorage.removeItem('authToken'); // if you’re using local storage for token
            navigate('/login'); // redirect to login page
        };

    // Handle solving task - navigate to SolveTask component with userId in state
    const handleSolveTask = (taskId) => {
        navigate(`/tasks/${taskId}/solve`, { state: { userId } });
    };

    return (
        <div className="dashboard-container">
            <h2>Welcome, {user.username}!</h2>

            <div className="profile-section section">
                <h3>Your Profile</h3>
                <p>Email: {user.email}</p>
                <p>GitHub: <a href={user.github} target="_blank" rel="noopener noreferrer">{user.github}</a></p>
                <p>LinkedIn: <a href={user.linkedin} target="_blank" rel="noopener noreferrer">{user.linkedin}</a></p>
                <h4>Skills:</h4>
                <ul>
                    {user.skills && user.skills.map(skill => (
                        <li key={skill.id}>{skill.skillName}</li>
                    ))}
                </ul>
            </div>

            <div className="tasks-section section">
                <h3>Your Tasks</h3>
                <button className="create-task-button" onClick={openTaskCreator}>Create New Task</button>
                {tasks.length > 0 ? (
                    tasks.map(task => (
                        <div key={task.id} className="task">
                            <h4>{task.title}</h4>
                            <p>Difficulty: {task.difficulty}</p>
                            <button onClick={() => handleSolveTask(task.id)}>Solve</button>
                        </div>
                    ))
                ) : (
                    <p>No tasks available yet.</p>
                )}
            </div>


            <div className="recommended-tasks-section section">
                <h3>Recommended Tasks</h3>
                {recommendedResources.length > 0 ? (
                    recommendedResources.map(task => (
                        <div key={task.id} className="task">
                            <h4>{task.title}</h4>
                            <p>Description: {task.description}</p>
                            <button onClick={() => assignTask(task.id)}>Select Task</button>
                        </div>
                    ))
                ) : (
                    <p>No recommended tasks available yet.</p>
                )}
            </div>

            {isTaskCreatorOpen && <CreateTask onTaskCreated={closeTaskCreator} onClose={closeTaskCreator} />}
            <div>
                        {/* other components */}
                        <button className="logout-button" onClick={handleLogout}>Logout</button>
                    </div>
        </div>
    );
};

export default Dashboard;
