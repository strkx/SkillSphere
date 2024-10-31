import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import './TaskReview.css';

const TaskReview = () => {
    const { taskId } = useParams();
    const [taskDetails, setTaskDetails] = useState(null);

    useEffect(() => {
        const fetchTaskDetails = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/tasks/${taskId}/details`);
                console.log("Task Details Response:", response.data); // Log the response to verify structure
                setTaskDetails(response.data);
            } catch (error) {
                console.error("Error fetching task details:", error);
            }
        };
        fetchTaskDetails();
    }, [taskId]);

    if (!taskDetails) return <p>Loading...</p>;

    return (
        <div className="task-review-container">
            <h2>{taskDetails.title}</h2>
            <p><strong>Description:</strong> {taskDetails.description}</p>
            <p><strong>Status:</strong> {taskDetails.status}</p>
            <p><strong>Difficulty:</strong> {taskDetails.difficulty}</p>
            <p><strong>Skills Required:</strong> {taskDetails.skillsRequired?.map(skill => skill.skillName).join(', ') || "N/A"}</p>
            <p><strong>Solution Text:</strong> {taskDetails.solutionText || "No solution text provided."}</p>

            {/* Display Task File Download Link */}
            {taskDetails.fileUrl ? (
                <p>
                    <strong>Task File:</strong>
                    <a href={`http://localhost:8080/api/tasks/${taskId}/file`} target="_blank" rel="noopener noreferrer">
                        Download Task File
                    </a>
                </p>
            ) : (
                <p><strong>Task File:</strong> Not available</p>
            )}

            {/* Display Solution File Download Link */}
            {taskDetails.solutionFileUrl ? (
                <p>
                    <strong>Solution File:</strong>
                    <a href={`http://localhost:8080/api/tasks/${taskId}/solution-file`} target="_blank" rel="noopener noreferrer">
                        Download Solution File
                    </a>
                </p>
            ) : (
                <p><strong>Solution File:</strong> Not available</p>
            )}

            <button className="approve-button">Approve</button>
            <button className="delete-button">Delete</button>
            <button className="reject-button">Reject</button>
        </div>
    );
};

export default TaskReview;
