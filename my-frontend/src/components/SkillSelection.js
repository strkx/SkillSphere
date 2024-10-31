import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import './SkillSelection.css';

const SkillSelection = () => {
    const [skills, setSkills] = useState([]);
    const [selectedSkills, setSelectedSkills] = useState([]);
    const { userId } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        axios.get("http://localhost:8080/api/skills/all")
            .then((response) => {
                setSkills(response.data);
            })
            .catch((error) => {
                console.error("Error fetching skills:", error);
            });
    }, []);

    const handleSelectSkill = (skillId) => {
        axios.put(`http://localhost:8080/api/users/${userId}/addSkill/${skillId}`)
            .then(() => {
                setSelectedSkills((prevSelected) => [...prevSelected, skillId]);
            })
            .catch((error) => {
                console.error("Error assigning skill:", error);
            });
    };

    // Function to handle "Next" button click
    const handleNext = () => {
        navigate(`/dashboard/${userId}`);
    };

    return (
        <div className="skill-selection-container">
            <h2>Select Skills</h2>
            <div className="skills-list">
                {skills.length > 0 ? (
                    skills.map((skill) => (
                        <button
                            key={skill.id}
                            onClick={() => handleSelectSkill(skill.id)}
                            className={`skill-button ${selectedSkills.includes(skill.id) ? "selected" : ""}`}
                        >
                            {skill.skillName}
                        </button>
                    ))
                ) : (
                    <p>No skills available to display</p>
                )}
            </div>
            {/* Show Next button only if at least one skill is selected */}
            {selectedSkills.length > 0 && (
                <button className="next-button" onClick={handleNext}>Next</button>
            )}
        </div>
    );
};

export default SkillSelection;
