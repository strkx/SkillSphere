import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Login.css';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();


    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                username,
                password
            });

            // Handle successful login
            alert('Login successful!');
            navigate(`/dashboard/${response.data.userId}`);
        } catch (error) {
            // Handle error
            console.error('Login failed:', error);
            alert('Login failed. Please check your credentials.');
        }
    };

    return (
        <div className="login-container">
            <div className="login-box">
                <h2>Login to Sphere</h2>
                <form onSubmit={handleLogin}>
                    <label htmlFor="username">Username</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        placeholder="Enter your username"
                    />
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        placeholder="Enter your password"
                    />
                    <button type="submit" className="login-button">Login</button>
                </form>
                <div className="login-footer">
                    <p>Don't have an account? <a href="/signup">Sign Up</a></p>
                    <p><a href="/forgot-password">Forgot Password?</a></p>
                </div>
            </div>
        </div>
    );
};

export default Login;
