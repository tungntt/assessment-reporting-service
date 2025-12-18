<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Work Hours Report - XperienceHR</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --card-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
        }
        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar {
            background: var(--primary-gradient) !important;
            box-shadow: var(--card-shadow);
        }
        .card {
            border: none;
            border-radius: 12px;
            box-shadow: var(--card-shadow);
        }
        .card-header {
            background: var(--primary-gradient);
            color: white;
            border-radius: 12px 12px 0 0 !important;
            font-weight: 600;
        }
        .btn-primary {
            background: var(--primary-gradient);
            border: none;
        }
        .btn-primary:hover {
            opacity: 0.9;
            background: var(--primary-gradient);
        }
        .table thead th {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
        }
        .badge-admin { background-color: #dc3545; }
        .badge-employee { background-color: #28a745; }
        .stats-card {
            background: white;
            border-radius: 12px;
            padding: 1.5rem;
            text-align: center;
            box-shadow: var(--card-shadow);
        }
        .stats-card h3 {
            color: #667eea;
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }
    </style>
</head>
<body>
<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark mb-4">
    <div class="container">
        <a class="navbar-brand" href="#">
            <i class="bi bi-clock-history me-2"></i>XperienceHR Time Tracker
        </a>
        <div class="d-flex align-items-center">
                <span class="text-white me-3">
                    <i class="bi bi-person-circle me-1"></i>${username}
                    <c:choose>
                        <c:when test="${isAdmin}">
                            <span class="badge badge-admin ms-2">ADMIN</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-employee ms-2">EMPLOYEE</span>
                        </c:otherwise>
                    </c:choose>
                </span>
            <form action="/logout" method="post" class="d-inline">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="btn btn-outline-light btn-sm">
                    <i class="bi bi-box-arrow-right me-1"></i>Logout
                </button>
            </form>
        </div>
    </div>
</nav>

<div class="container">
    <!-- Date Range Filter Card -->
    <div class="card mb-4">
        <div class="card-header">
            <i class="bi bi-funnel me-2"></i>Filter Report
        </div>
        <div class="card-body">
            <form method="get" action="/report/time-record" class="row g-3 align-items-end">
                <div class="col-md-4">
                    <label for="startDate" class="form-label">Start Date & Time</label>
                    <input type="datetime-local" class="form-control" id="startDate" name="startDate" value="${startDateValue}">
                </div>
                <div class="col-md-4">
                    <label for="endDate" class="form-label">End Date & Time</label>
                    <input type="datetime-local" class="form-control" id="endDate" name="endDate" value="${endDateValue}">
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="bi bi-search me-2"></i>Generate Report
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Summary Stats -->
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="stats-card">
                <h3>${reportData.size()}</h3>
                <p class="text-muted mb-0">Total Records</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stats-card">
                <h3>
                    <c:set var="totalHours" value="0"/>
                    <c:forEach var="record" items="${reportData}">
                        <c:set var="totalHours" value="${totalHours + record.totalHours}"/>
                    </c:forEach>
                    <fmt:formatNumber value="${totalHours}" maxFractionDigits="2"/>
                </h3>
                <p class="text-muted mb-0">Total Hours</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stats-card">
                <c:if test="${isAdmin}">
                    <h3><i class="bi bi-eye-fill text-success"></i></h3>
                    <p class="text-muted mb-0">Viewing All Employees</p>
                </c:if>
                <c:if test="${!isAdmin}">
                    <h3><i class="bi bi-person-fill text-primary"></i></h3>
                    <p class="text-muted mb-0">Viewing Your Records Only</p>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Report Table Card -->
    <div class="card">
        <div class="card-header">
            <i class="bi bi-table me-2"></i>Work Hours Report
        </div>
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty reportData}">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col"><i class="bi bi-person me-1"></i>Employee Name</th>
                                <th scope="col"><i class="bi bi-folder me-1"></i>Project Name</th>
                                <th scope="col"><i class="bi bi-clock me-1"></i>Total Hours</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="record" items="${reportData}" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td>${record.employeeName}</td>
                                    <td>${record.projectName}</td>
                                    <td>
                                                <span class="badge bg-info text-dark">
                                                    ${record.formattedHours} hrs
                                                </span>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-5">
                        <i class="bi bi-inbox display-4 text-muted"></i>
                        <p class="text-muted mt-3">No records found for the selected date range.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Footer -->
    <footer class="text-center mt-4 mb-3 text-muted">
        <small>&copy; 2024 XperienceHR - Time Tracking System</small>
    </footer>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Set default dates if inputs are empty
    document.addEventListener('DOMContentLoaded', function() {
        const startInput = document.getElementById('startDate');
        const endInput = document.getElementById('endDate');

        if (!startInput.value) {
            const oneMonthAgo = new Date();
            oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);
            startInput.value = oneMonthAgo.toISOString().slice(0, 16);
        }

        if (!endInput.value) {
            endInput.value = new Date().toISOString().slice(0, 16);
        }
    });
</script>
</body>
</html>

