<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SLIIT | Centralized CourseWeb</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        :root {
            --sliit-navy: #1e1b4b;
            --sliit-blue: #3730a3;
            --sliit-orange: #f59e0b;
            --bg-body: #f4f7fe;
            --bg-card: #ffffff;
            --text-main: #0f172a;
            --text-muted: #64748b;
        }

        body {
            font-family: 'Poppins', sans-serif;
            background-color: var(--bg-body);
            color: var(--text-main);
            overflow-x: hidden;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        /* --- Sleek Navbar --- */
        .top-navbar {
            background-color: var(--bg-card);
            padding: 15px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 20px rgba(0,0,0,0.02);
        }
        .nav-brand {
            font-size: 24px;
            font-weight: 800;
            color: var(--sliit-navy);
            text-decoration: none;
            display: flex;
            align-items: center;
        }
        .nav-brand i { color: var(--sliit-orange); margin-right: 10px; font-size: 28px; }

        .btn-system-login {
            background-color: var(--bg-body);
            color: var(--sliit-navy);
            border: 1px solid var(--border-light);
            font-weight: 600;
            padding: 10px 25px;
            border-radius: 12px;
            transition: 0.3s;
            font-size: 14px;
        }
        .btn-system-login:hover {
            background-color: #e0e7ff;
            color: var(--sliit-blue);
        }

        /* --- Main Layout --- */
        .portal-wrapper { flex: 1; padding: 60px 0; }

        /* --- The "New Student" Clarification Banner --- */
        .admission-banner {
            background: white;
            border: 1px dashed var(--sliit-blue);
            border-radius: 16px;
            padding: 20px 25px;
            margin-bottom: 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 10px 30px rgba(55, 48, 163, 0.05);
        }
        .admission-info { display: flex; align-items: center; gap: 15px; }
        .admission-icon {
            width: 50px; height: 50px;
            background: #e0e7ff; color: var(--sliit-blue);
            border-radius: 14px;
            display: flex; align-items: center; justify-content: center;
            font-size: 20px;
        }
        .btn-admission {
            background: var(--sliit-blue); color: white;
            padding: 10px 20px; border-radius: 10px; font-weight: 600;
            border: none; font-size: 13px; transition: 0.3s;
        }
        .btn-admission:hover { background: var(--sliit-navy); transform: translateY(-2px); }

        /* --- Hero Section --- */
        .hero-badge {
            background: white; color: var(--sliit-orange);
            padding: 8px 20px; border-radius: 30px;
            font-weight: 600; font-size: 13px; display: inline-block;
            margin-bottom: 20px; border: 1px solid #fef3c7;
            box-shadow: 0 4px 15px rgba(245, 158, 11, 0.1);
        }
        .hero-title { font-size: 48px; font-weight: 800; line-height: 1.2; margin-bottom: 20px; color: var(--sliit-navy); }
        .hero-title span { color: var(--sliit-orange); }
        .hero-desc { color: var(--text-muted); font-size: 15px; margin-bottom: 40px; line-height: 1.6; max-width: 90%; }

        /* --- Modern Module Cards --- */
        .module-card {
            background: var(--bg-card);
            border-radius: 24px;
            padding: 35px 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.03);
            transition: 0.3s;
            height: 100%;
            border: 1px solid rgba(255,255,255,0.5);
            cursor: pointer;
            text-decoration: none;
            display: block;
            position: relative;
        }
        .module-card:hover { transform: translateY(-10px); box-shadow: 0 20px 40px rgba(30, 27, 75, 0.08); }

        .icon-box {
            width: 60px; height: 60px; border-radius: 16px;
            display: flex; align-items: center; justify-content: center;
            font-size: 24px; margin-bottom: 20px;
        }

        .card-enroll .icon-box { background: #fef3c7; color: var(--sliit-orange); }
        .card-admin .icon-box { background: #e0e7ff; color: var(--sliit-blue); }
        .card-waitlist .icon-box { background: #dcfce7; color: #16a34a; }

        .module-card h4 { font-size: 18px; font-weight: 700; color: var(--text-main); margin-bottom: 10px; }
        .module-card p { font-size: 13px; color: var(--text-muted); margin-bottom: 0; }

        .secure-badge { position: absolute; top: 25px; right: 25px; color: var(--text-muted); font-size: 14px; }

        /* --- Secure Login Modal --- */
        .modal-content { border-radius: 24px; border: none; box-shadow: 0 20px 50px rgba(30, 27, 75, 0.15); }
        .modal-header { border-bottom: none; padding: 25px 25px 0 25px; }
        .modal-body { padding: 25px; }
        .form-control { border-radius: 12px; padding: 14px 18px; background-color: var(--bg-body); border: 1px solid var(--border-light); font-size: 14px; }
        .form-control:focus { background-color: white; border-color: var(--sliit-blue); box-shadow: 0 0 0 4px rgba(55, 48, 163, 0.1); }
        .btn-login-submit { background-color: var(--sliit-navy); color: white; border-radius: 12px; padding: 14px; font-weight: 600; width: 100%; border: none; transition: 0.3s; }
        .btn-login-submit:hover { background-color: var(--sliit-blue); }

        footer { text-align: center; padding: 20px; color: var(--text-muted); font-size: 13px; }
    </style>
</head>
<body>

<nav class="top-navbar">
    <a class="nav-brand" href="#">
        <i class="fa-solid fa-layer-group"></i> SLIIT<span style="font-weight: 400; color: #64748b; margin-left:5px;">Portal</span>
    </a>
    <button class="btn-system-login d-none d-md-block" onclick="openLoginModal('${pageContext.request.contextPath}/register')">
        <i class="fa-solid fa-shield-halved me-2 text-success"></i> System Admin
    </button>
</nav>

<div class="portal-wrapper">
    <div class="container">

        <div class="row mb-4">
            <div class="col-12">
                <div class="admission-banner flex-column flex-md-row text-center text-md-start">
                    <div class="admission-info mb-3 mb-md-0">
                        <div class="admission-icon"><i class="fa-solid fa-user-plus"></i></div>
                        <div>
                            <h6 class="mb-1 fw-bold" style="color: var(--sliit-navy); font-size: 15px;">New Student Admissions</h6>
                            <p class="mb-0 text-muted" style="font-size: 13px;">Are you looking to join SLIIT for the first time? Apply via the Admissions Portal.</p>
                        </div>
                    </div>
                    <button class="btn-admission" onclick="showAdmissionAlert()">
                        Apply for 2026 Intake <i class="fa-solid fa-external-link-alt ms-1"></i>
                    </button>
                </div>
            </div>
        </div>

        <div class="row align-items-center mt-4">

            <div class="col-lg-5 mb-5 mb-lg-0 pe-lg-5 text-center text-lg-start">
                <div class="hero-badge">
                    <i class="fa-solid fa-code-branch me-2"></i> Active Student Portal V2.0
                </div>
                <h1 class="hero-title">Semester Module<br><span>Registration</span><br>System.</h1>
                <p class="hero-desc">
                    Strictly for currently enrolled students. Manage your academic modules for the upcoming semester. Features real-time FIFO Queues and Backend File Management.
                </p>
                <div class="d-flex align-items-center justify-content-center justify-content-lg-start mt-4">
                    <div style="width: 45px; height: 45px; border-radius: 50%; background: white; display: flex; align-items: center; justify-content: center; box-shadow: 0 4px 10px rgba(0,0,0,0.05); margin-right: 15px;">
                        <i class="fa-solid fa-check text-success"></i>
                    </div>
                    <div style="font-size: 13px; font-weight: 600; color: var(--text-main);">
                        Academic Year: <span style="color: var(--sliit-orange);">2025/2026</span>
                    </div>
                </div>
            </div>

            <div class="col-lg-7">
                <div class="row g-4">

                    <div class="col-md-6">
                        <a href="course-enrollment.jsp" class="module-card card-enroll">
                            <div class="icon-box"><i class="fa-solid fa-pen-to-square"></i></div>
                            <h4>Module Enrollment</h4>
                            <p>Access the student workspace to select and register for semester modules.</p>
                            <div class="mt-4" style="font-size: 13px; font-weight: 700; color: var(--sliit-orange);">
                                Enter Workspace <i class="fa-solid fa-arrow-right ms-1"></i>
                            </div>
                        </a>
                    </div>

                    <div class="col-md-6">
                        <div class="module-card card-admin" onclick="openLoginModal('${pageContext.request.contextPath}/register')">
                            <div class="secure-badge"><i class="fa-solid fa-lock"></i></div>
                            <div class="icon-box"><i class="fa-solid fa-users-gear"></i></div>
                            <h4>Admin Dashboard</h4>
                            <p>Backend access to view and manage Insertion Sorted student data.</p>
                            <div class="mt-4" style="font-size: 13px; font-weight: 700; color: var(--sliit-blue);">
                                Secure Login <i class="fa-solid fa-arrow-right ms-1"></i>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6 offset-md-3">
                        <div class="module-card card-waitlist" onclick="openLoginModal('waitlist-status.jsp')">
                            <div class="secure-badge"><i class="fa-solid fa-lock"></i></div>
                            <div class="icon-box"><i class="fa-solid fa-hourglass-half"></i></div>
                            <h4>System Waitlist</h4>
                            <p>Management view of the strict FIFO Queue structure for full capacity modules.</p>
                            <div class="mt-4" style="font-size: 13px; font-weight: 700; color: #16a34a;">
                                Secure Login <i class="fa-solid fa-arrow-right ms-1"></i>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>

<footer>
    <div class="container">
        &copy; 2026 Course Management System. Designed by Umesh Tharuka.
    </div>
</footer>

<div class="modal fade" id="adminLoginModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title fw-bold" style="color: var(--sliit-navy);"><i class="fa-solid fa-shield-halved text-warning me-2"></i> System Authentication</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">

                <div id="loginError" class="alert alert-danger" style="display: none; font-size: 13px; border-radius: 12px; padding: 10px 15px;">
                    <i class="fa-solid fa-circle-exclamation me-2"></i> Invalid credentials provided.
                </div>

                <p class="text-muted" style="font-size: 13px; margin-bottom: 25px;">
                    Restricted area. Please enter your administrator credentials to proceed.
                </p>

                <div class="mb-3">
                    <label class="form-label" style="font-size: 13px; font-weight: 600;">Administrator Email</label>
                    <input type="email" id="adminEmail" class="form-control" placeholder="Enter email address">
                </div>

                <div class="mb-4">
                    <label class="form-label" style="font-size: 13px; font-weight: 600;">Secure Password</label>
                    <input type="password" id="adminPassword" class="form-control" placeholder="Enter password">
                </div>

                <button type="button" class="btn-login-submit" onclick="attemptLogin()">
                    Authenticate System <i class="fa-solid fa-arrow-right-to-bracket ms-1"></i>
                </button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
    // --- Modal Logic ---
    let targetPageUrl = '';

    function openLoginModal(pageUrl) {
        targetPageUrl = pageUrl;
        document.getElementById('loginError').style.display = 'none';
        document.getElementById('adminEmail').value = '';
        document.getElementById('adminPassword').value = '';
        var loginModal = new bootstrap.Modal(document.getElementById('adminLoginModal'));
        loginModal.show();
    }

    function attemptLogin() {
        let email = document.getElementById('adminEmail').value;
        let pass = document.getElementById('adminPassword').value;

        // Password checks
        if(email === 'admin@sliit.lk' && pass === 'admin123') {
            window.location.href = targetPageUrl;
        } else {
            document.getElementById('loginError').style.display = 'block';
        }
    }

    // --- Viva Clarification Logic (Admissions Alert) ---
    function showAdmissionAlert() {
        Swal.fire({
            icon: 'info',
            title: 'University Admissions System',
            text: 'Admissions for new students are handled by a separate university system (apply.sliit.lk). This portal is strictly designed for Active Student Semester Module Registrations only.',
            confirmButtonColor: '#3730a3',
            confirmButtonText: 'Understood'
        });
    }
</script>

</body>
</html>
