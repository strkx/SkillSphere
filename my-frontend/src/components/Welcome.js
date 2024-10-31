import React from 'react';
import { Link } from 'react-router-dom';
import './Welcome.css';

const Welcome = () => {
    return (
        <div className="welcome-container">
            <div className="overlay"></div> {/* New Overlay */}
            <div className="welcome-card">
                <h1 className="welcome-title">Welcome to SkillSphere</h1>
                <p className="welcome-subtitle">Your Ultimate Micro-Task Platform</p>

                <div className="button-group">
                    <Link to="/signup">
                        <button className="welcome-button">Sign Up</button>
                    </Link>
                    <Link to="/login">
                        <button className="welcome-button">Login</button>
                    </Link>
                    <Link to="/admin">
                          <button className="admin-button">Admin Login</button>
                    </Link>

                </div>
            </div>
        </div>
    );
};

export default Welcome;
