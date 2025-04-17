import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ token, user, requiredRole, children }) => {
  if (!token) return <Navigate to="/login" state={{ message: "Please log in." }} />;
  if (requiredRole && !user?.roles.includes(requiredRole)) {
    return <Navigate to="/" state={{ message: "Access denied." }} />;
  }
  return children;
};

export default ProtectedRoute;
