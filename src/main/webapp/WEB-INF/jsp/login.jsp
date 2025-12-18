<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - XperienceHR</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.2);
            padding: 2.5rem;
            width: 100%;
            max-width: 400px;
        }
        .login-header {
            text-align: center;
            margin-bottom: 2rem;
        }
        .login-header h2 {
            color: #667eea;
            font-weight: 700;
        }
        .btn-login {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            padding: 12px;
            font-weight: 600;
        }
        .btn-login:hover {
            opacity: 0.9;
        }
        .credentials-info {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 1rem;
            margin-top: 1.5rem;
            font-size: 0.875rem;
        }
    </style>
</head>
<body>
    <div class="login-card">
        <div class="login-header">
            <h2>🕐 XperienceHR</h2>
            <p class="text-muted">Time Tracking System</p>
        </div>

        <c:if test="${param.error != null}">
            <div class="alert alert-danger" role="alert">
                Invalid username or password.
            </div>
        </c:if>

        <c:if test="${param.logout != null}">
            <div class="alert alert-success" role="alert">
                You have been logged out successfully.
            </div>
        </c:if>

        <form action="/login" method="post">
            <div class="mb-3">
                <label for="username" class="form-label">Username</label>
                <input type="text" class="form-control" id="username" name="username" required autofocus>
            </div>
            <div class="mb-4">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" required>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-primary btn-login w-100">Sign In</button>
        </form>

        <div class="credentials-info">
            <strong>Test Credentials:</strong><br>
            <code>tom / password</code> (Employee)<br>
            <code>jerry / password</code> (Employee)<br>
            <code>admin / admin123</code> (Admin)
        </div>
    </div>
</body>
</html>

