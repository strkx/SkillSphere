import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./SignUp.css";

const SignUp = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [github, setGithub] = useState("");
    const [linkedin, setLinkedin] = useState("");
    const navigate = useNavigate();

    const handleSignUp = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/auth/signup", {
                username,
                email,
                password,
                github,
                linkedin,
            });

            const userId = response.data.userId; // Assume `userId` is in the response
            alert("Signup successful!");
            navigate(`/select-skill/${userId}`); // Redirect to skill selection with userId
        } catch (error) {
            console.error("Error during signup:", error);
            alert("Error registering user!");
        }
    };

    return (
        <div className="signup-container">
            <h2>Sign Up</h2>
            <form onSubmit={handleSignUp}>
                <div className="form-group">
                    <label>Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>GitHub (optional):</label>
                    <input
                        type="url"
                        value={github}
                        onChange={(e) => setGithub(e.target.value)}
                    />
                </div>
                <div className="form-group">
                    <label>LinkedIn (optional):</label>
                    <input
                        type="url"
                        value={linkedin}
                        onChange={(e) => setLinkedin(e.target.value)}
                    />
                </div>
                <button type="submit" className="btn-primary">Sign Up</button>
            </form>
            <p className="footer-text">Empowering education, one sign-up at a time.</p>
        </div>
    );
};

export default SignUp;
