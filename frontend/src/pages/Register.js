import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { ApiClient, UserManagementApi, UserRegistration } from 'ccs-openapi-client';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import { authorize } from '../auth';

const API_BASE = process.env.REACT_APP_API_BASE_URL;
ApiClient.instance.basePath = API_BASE;

export default function Register() {
    const [email,    setEmail]    = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error,    setError]    = useState(null);
    const [loading,  setLoading]  = useState(false);
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        const model = new UserRegistration(email, username, password);
        new UserManagementApi().registration(model, (_err, _data, response) => {
            setLoading(false);
            if (_err) {
                let msg = 'Error occurred. Please try again later.';
                if (response?.body?.message) {
                    msg = response.body.message;
                }
                setError(msg);
            } else {
                authorize(response);
                navigate('/');
            }
        });
    };

    return (
        <div className="d-flex flex-column justify-content-between vh-100 bg-light">
            <header className="text-center mt-5">
                <h1 className="display-4 fw-bold text-primary">CoinKeeper</h1>
            </header>

            <main className="d-flex justify-content-center align-items-center flex-grow-1">
                <div className="card shadow-lg p-4" style={{ maxWidth: '420px', width: '100%' }}>
                    <div className="card-header bg-white border-0 text-center mb-3">
                        <h3 className="card-title">Registration</h3>
                    </div>

                    {error && (
                        <div className="alert alert-danger" role="alert">
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit} noValidate>
                        <div className="mb-3">
                            <label htmlFor="email" className="form-label">Email</label>
                            <input
                                id="email"
                                type="email"
                                className="form-control form-control-lg"
                                value={email}
                                onChange={e => setEmail(e.target.value)}
                                required
                            />
                        </div>

                        <div className="mb-3">
                            <label htmlFor="username" className="form-label">Username</label>
                            <input
                                id="username"
                                type="text"
                                className="form-control form-control-lg"
                                value={username}
                                onChange={e => setUsername(e.target.value)}
                                required
                            />
                        </div>

                        <div className="mb-4">
                            <label htmlFor="password" className="form-label">Password</label>
                            <input
                                id="password"
                                type="password"
                                className="form-control form-control-lg"
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                                required
                            />
                        </div>

                        <button
                            type="submit"
                            className="btn btn-primary btn-lg w-100 mb-3"
                            disabled={loading}
                        >
                            {loading ? 'Registering…' : 'Register'}
                        </button>

                        <div className="text-center">
                            <span className="me-1">Already have an account?</span>
                            <Link to="/login" className="text-decoration-none fw-medium">
                                Login here
                            </Link>
                        </div>
                    </form>
                </div>
            </main>

            <footer className="text-center py-3 bg-white border-top">
                <small className="text-muted">
                    &copy; {new Date().getFullYear()} CoinKeeper. All rights reserved.
                </small>
            </footer>
        </div>
    );
}
