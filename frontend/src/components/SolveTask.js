import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import './SolveTask.css';

const SolveTask = () => {
    const { taskId } = useParams();
    const location = useLocation();
    const navigate = useNavigate();

    const [task, setTask] = useState({});
    const [solutionText, setSolutionText] = useState('');
    const [solutionFile, setSolutionFile] = useState(null);
    const [userId, setUserId] = useState(null);

    // Retrieve userId from navigation state
    useEffect(() => {
        if (location.state && location.state.userId) {
            setUserId(location.state.userId);
        }
    }, [location.state]);

    // Fetch task details
    useEffect(() => {
        axios.get(`http://localhost:8080/api/tasks/${taskId}`)
            .then(response => setTask(response.data))
            .catch(error => console.error("Error fetching task data:", error));
    }, [taskId]);

    const handleFileChange = (e) => {
        setSolutionFile(e.target.files[0]);
    };

    const handleSubmitSolution = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('solutionText', solutionText);
        if (solutionFile) formData.append('solutionFile', solutionFile);

        try {
            await axios.post(`http://localhost:8080/api/tasks/${taskId}/solution`, formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            alert('Solution submitted successfully!');

            // Navigate back to dashboard with userId
            if (userId) {
                navigate(`/dashboard/${userId}`);
            } else {
                console.warn("UserId not available for navigation");
            }
        } catch (error) {
            console.error("Error submitting solution:", error);
            alert('Failed to submit solution.');
        }
    };

    return (
        <div className="solve-task-container">
            <h2>Solve Task</h2>

            <div className="task-details">
                <p><strong>Description:</strong> {task.description}</p>
                <p><strong>Difficulty:</strong> {task.difficulty}</p>
                <p><strong>Skills Required:</strong> {task.skillsRequired && task.skillsRequired.map(skill => skill.skillName).join(', ')}</p>

                {task.fileUrl && (
                    <div className="file-section">
                        <strong>File:</strong>
                        <a href={`http://localhost:8080/api/tasks/${taskId}/file`} target="_blank" rel="noopener noreferrer">
                            Download File
                        </a>
                    </div>
                )}
            </div>

            <form onSubmit={handleSubmitSolution}>
                <label className="solution-label" htmlFor="solutionText">Solution Text</label>
                <textarea
                    id="solutionText"
                    className="solution-input"
                    placeholder="Write your solution here..."
                    value={solutionText}
                    onChange={(e) => setSolutionText(e.target.value)}
                    required
                />

                <div className="file-upload">
                    <label htmlFor="solutionFile">Attach Solution File (optional)</label>
                    <input
                        type="file"
                        id="solutionFile"
                        onChange={handleFileChange}
                    />
                </div>

                <div className="button-section">
                    <button type="submit" className="submit-button">Submit Solution</button>
                </div>
            </form>
        </div>
    );
};

export default SolveTask;
