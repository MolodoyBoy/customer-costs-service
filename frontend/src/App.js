import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import { PrivateRoute } from './components/PrivateRoute';
import { PublicRoute } from './components/PublicRoute';

import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';
import AnalyticsPeriod from './pages/AnalyticsPeriod';
import {isLoggedIn} from "./auth";
import AnalyticsDetail from "./pages/AnalyticsDetail";

export default function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route
                    path="/login"
                    element={
                        <PublicRoute>
                            <Login />
                        </PublicRoute>
                    }
                />
                <Route
                    path="/register"
                    element={
                        <PublicRoute>
                            <Register />
                        </PublicRoute>
                    }
                />

                <Route
                    path="/"
                    element={
                        <PrivateRoute>
                            <Home />
                        </PrivateRoute>
                    }
                />

                <Route
                    path="/analytics/period"
                    element={
                        <PrivateRoute>
                            <AnalyticsPeriod />
                        </PrivateRoute>
                    }
                />

                <Route
                    path="/analytics/period/detail"
                    element={
                        <PrivateRoute>
                            <AnalyticsDetail />
                        </PrivateRoute>
                    }
                />

                <Route
                    path="*"
                    element={
                        isLoggedIn()
                            ? <Navigate to="/" replace />
                            : <Navigate to="/login" replace />
                    }
                />
            </Routes>
        </BrowserRouter>
    );
}
