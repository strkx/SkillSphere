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
                    <button className="welcome-button disabled" disabled>
                        Admin Login (Coming Soon)
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Welcome;
