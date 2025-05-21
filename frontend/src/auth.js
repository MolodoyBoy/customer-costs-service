export function isLoggedIn() {
    return Boolean(localStorage.getItem('authToken'));
}

export function getAuthToken() {
    return localStorage.getItem('authToken');
}

export function authorize(response) {
    const authHeader = response.header?.authorization;
    if (authHeader && authHeader.startsWith('Bearer ')) {
        localStorage.setItem('authToken', authHeader)
    }
}

export function logout() {
    localStorage.removeItem('authToken');
}