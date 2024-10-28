import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './CreateTask.css';

const CreateTask = ({ onTaskCreated, onClose }) => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [skills, setSkills] = useState([]);
    const [selectedSkills, setSelectedSkills] = useState([]);
    const [selectedProficiency, setSelectedProficiency] = useState('');
    const [taskDescription, setTaskDescription] = useState('');
    const [file, setFile] = useState(null);

    const proficiencyLevels = ['Beginner', 'Intermediate', 'Advanced'];

    useEffect(() => {
        axios.get('http://localhost:8080/api/skills/all')
            .then(response => setSkills(response.data))
            .catch(error => console.error("Error fetching skills:", error));
    }, []);

    const handleFileChange = (e) => setFile(e.target.files[0]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('title', title);
        formData.append('description', description);
        formData.append('status', 'pending');
        formData.append('difficulty', selectedProficiency);
        formData.append('file', file);
        selectedSkills.forEach((skill, index) => {
            formData.append(`skillsRequired[${index}].skillName`, skill.skillName);
            formData.append(`skillsRequired[${index}].proficiencyLevel`, selectedProficiency);
        });

        try {
            await axios.post('http://localhost:8080/api/tasks/addTaskWithFile', formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            alert('Task created successfully!');
            onTaskCreated(); // Close the form on success
        } catch (error) {
            console.error("Error creating task:", error);
            alert('Failed to create task.');
        }
    };

    return (
        <div className="create-task-container">
            <h2>Create New Task</h2>
            <form onSubmit={handleSubmit}>
                <label>Title</label>
                <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />

                <label>Proficiency Level</label>
                <select value={selectedProficiency} onChange={(e) => setSelectedProficiency(e.target.value)} required>
                    <option value="" disabled>Select a proficiency level</option>
                    {proficiencyLevels.map(level => (
                        <option key={level} value={level}>{level}</option>
                    ))}
                </select>

                <label>Skills Required</label>
                <select multiple onChange={(e) => {
                    const selectedOptions = Array.from(e.target.selectedOptions, option => ({
                        skillName: option.value,
                    }));
                    setSelectedSkills(selectedOptions);
                }}>
                    {skills.map(skill => (
                        <option key={skill.id} value={skill.skillName}>{skill.skillName}</option>
                    ))}
                </select>

                <label>Problem Description</label>
                <textarea value={taskDescription} onChange={(e) => setTaskDescription(e.target.value)} required />

                <label>Upload File</label>
                <input type="file" onChange={handleFileChange} />

                <div className="button-group">
                    <button type="submit">Create Task</button>
                    <button type="button" onClick={onClose}>Close</button> {/* Close button */}
                </div>
            </form>
        </div>
    );
};

export default CreateTask;
