import React from 'react';
import { Navigate } from 'react-router-dom';
import { isLoggedIn } from '../auth';

export function PrivateRoute({ children }) {
    return isLoggedIn()
        ? children
        : <Navigate to="/login" replace />;
}