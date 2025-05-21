import { isLoggedIn } from '../auth';

export function PublicRoute({ children }) {
    return isLoggedIn()
        ? children
        : children;
}