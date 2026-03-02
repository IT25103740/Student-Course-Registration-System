<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SLIIT Eduall | Central Registration Portal</title>

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        :root {
            --theme-blue: #0F2239;    /* Eduall Dark Blue */
            --theme-orange: #FF5E14;  /* Eduall Orange */
            --theme-gray: #6B7385;
            --bg-color: #F8F4F0;      /* Eduall Light Background */
        }

        body { font-family: 'Poppins', sans-serif; background-color: var(--bg-color); overflow-x: hidden; }

        /* --- Eduall Topbar & Navbar --- */
        .topbar { background-color: var(--theme-blue); color: white; padding: 10px 0; font-size: 14px; }
        .topbar i { color: var(--theme-orange); margin-right: 8px; }
        .navbar-custom { background-color: white; padding: 15px 0; box-shadow: 0 4px 15px rgba(0,0,0,0.03); }
        .navbar-brand { font-weight: 800; color: var(--theme-blue) !important; font-size: 26px; }
        .navbar-brand span { color: var(--theme-orange); }
        .nav-link { color: var(--theme-blue); font-weight: 500; margin: 0 10px; transition: 0.3s; }
        .nav-link:hover { color: var(--theme-orange); }

        /* --- Hero Section (Exact Clone) --- */
        .hero-section { padding: 80px 0; position: relative; }
        .hero-badge { display: inline-block; background: rgba(255, 94, 20, 0.1); color: var(--theme-orange); font-weight: 600; padding: 8px 20px; border-radius: 30px; margin-bottom: 20px; font-size: 14px; }
        .hero-title { font-size: 3.8rem; font-weight: 800; color: var(--theme-blue); line-height: 1.2; margin-bottom: 25px; }
        .hero-title span { color: var(--theme-orange); position: relative; }
        .hero-title span::after { content: ''; position: absolute; left: 0; bottom: 5px; width: 100%; height: 8px; background-color: rgba(255, 94, 20, 0.2); z-index: -1; }
        .hero-text { color: var(--theme-gray); font-size: 1.1rem; margin-bottom: 35px; line-height: 1.7; max-width: 90%; }

        .btn-theme-primary { background-color: var(--theme-orange); color: white; padding: 15px 35px; border-radius: 8px; font-weight: 600; border: none; transition: 0.3s; }
        .btn-theme-primary:hover { background-color: var(--theme-blue); color: white; transform: translateY(-3px); }

        /* Hero Image & Floating Badges */
        .hero-img-box { position: relative; text-align: right; z-index: 1; }
        .hero-img { width: 90%; border-radius: 20px; box-shadow: 0 20px 40px rgba(0,0,0,0.1); }

        .float-badge-1 { position: absolute; bottom: 10%; left: -30px; background: white; padding: 15px 25px; border-radius: 12px; box-shadow: 0 15px 30px rgba(0,0,0,0.1); display: flex; align-items: center; gap: 15px; animation: float 4s ease-in-out infinite; }
        .avatar-group img { width: 40px; height: 40px; border-radius: 50%; border: 3px solid white; margin-left: -15px; }
        .avatar-group img:first-child { margin-left: 0; }

        .float-badge-2 { position: absolute; top: 15%; right: 20px; background: white; padding: 15px 25px; border-radius: 12px; box-shadow: 0 15px 30px rgba(0,0,0,0.1); border-left: 5px solid var(--theme-orange); animation: float 5s ease-in-out infinite reverse; }

        @keyframes float { 0% { transform: translateY(0); } 50% { transform: translateY(-15px); } 100% { transform: translateY(0); } }

        /* --- Portals Section (Features) --- */
        .portal-section { padding: 60px 0 100px; }
        .section-title { text-align: center; margin-bottom: 50px; }
        .section-title h5 { color: var(--theme-orange); font-weight: 600; }
        .section-title h2 { color: var(--theme-blue); font-weight: 800; font-size: 2.5rem; }

        .feature-card { background: white; padding: 40px 30px; border-radius: 15px; text-align: left; box-shadow: 0 5px 20px rgba(0,0,0,0.03); border: 1px solid #f0f0f0; transition: all 0.4s ease; height: 100%; position: relative; overflow: hidden; }
        .feature-card:hover { transform: translateY(-10px); box-shadow: 0 20px 40px rgba(0,0,0,0.08); border-color: rgba(255, 94, 20, 0.3); }

        .feature-icon { width: 70px; height: 70px; background: rgba(255, 94, 20, 0.1); color: var(--theme-orange); font-size: 30px; display: flex; align-items: center; justify-content: center; border-radius: 12px; margin-bottom: 25px; transition: 0.3s; }
        .feature-card:hover .feature-icon { background: var(--theme-orange); color: white; }

        .feature-card h4 { color: var(--theme-blue); font-weight: 700; margin-bottom: 15px; font-size: 1.3rem; }
        .feature-card p { color: var(--theme-gray); font-size: 0.95rem; margin-bottom: 25px; line-height: 1.6; }

        .btn-link-custom { color: var(--theme-blue); font-weight: 600; text-decoration: none; border-bottom: 2px solid transparent; padding-bottom: 2px; transition: 0.3s; background: none; border: none; padding: 0; }
        .btn-link-custom:hover { color: var(--theme-orange); border-bottom-color: var(--theme-orange); }

        /* Secure Badge overlay */
        .secure-badge { position: absolute; top: 20px; right: 20px; color: #ccc; font-size: 1.2rem; }

        /* Modal Customization */
        .modal-content { border-radius: 15px; border: none; }
        .modal-header { background-color: var(--theme-blue); color: white; }
    </style>
</head>
<body>

<div class="topbar d-none d-lg-block">
    <div class="container">
        <div class="row">
            <div class="col-md-8">
                <span class="me-4"><i class="fas fa-envelope"></i> admin@sliit.lk</span>
                <span><i class="fas fa-phone-alt"></i> +94 11 241 3900</span>
            </div>
            <div class="col-md-4 text-end">
                <span class="me-3"><i class="fas fa-map-marker-alt"></i> New Kandy Rd, Malabe</span>
            </div>
        </div>
    </div>
</div>

<nav class="navbar navbar-expand-lg navbar-custom sticky-top">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">SLIIT<span>.Eduall</span></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mx-auto">
                <li class="nav-item"><a class="nav-link" href="#">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Modules</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Admissions</a></li>
            </ul>
            <a href="#portals" class="btn btn-theme-primary px-4 py-2" style="border-radius: 5px;">Get Started</a>
        </div>
    </div>
</nav>

<section class="hero-section">
    <div class="container">
        <div class="row align-items-center">

            <div class="col-lg-6 mb-5 mb-lg-0">
                <span class="hero-badge"><i class="fas fa-award me-2"></i> Semester 2026 Registration</span>
                <h1 class="hero-title">Centralized Module <br><span>Registration System</span></h1>
                <p class="hero-text">Welcome to the EduAll-powered academic gateway. Seamlessly manage module enrollments, waitlists, and system analytics powered by advanced Queue and Sorting algorithms.</p>
                <div>
                    <a href="course-enrollment.jsp" class="btn btn-theme-primary me-3">Enroll Now <i class="fas fa-arrow-right ms-2"></i></a>
                    <button onclick="openAuthModal('admin')" class="btn" style="background: white; border: 2px solid var(--theme-blue); color: var(--theme-blue); padding: 13px 30px; border-radius: 8px; font-weight: 600;">Admin Login</button>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="hero-img-box">
                    <img src="https://images.unsplash.com/photo-1523240795612-9a054b0db644?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80" alt="Students" class="hero-img">

                    <div class="float-badge-1">
                        <div class="avatar-group">
                            <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="user">
                            <img src="https://randomuser.me/api/portraits/men/32.jpg" alt="user">
                            <img src="https://randomuser.me/api/portraits/women/68.jpg" alt="user">
                        </div>
                        <div>
                            <h6 class="mb-0 fw-bold" style="color: var(--theme-blue);">1,245+</h6>
                            <small class="text-muted">Enrolled Students</small>
                        </div>
                    </div>

                    <div class="float-badge-2">
                        <div class="d-flex align-items-center gap-3">
                            <i class="fas fa-satellite-dish fs-3" style="color: var(--theme-orange);"></i>
                            <div>
                                <h6 class="mb-0 fw-bold" style="color: var(--theme-blue);">Live Queue</h6>
                                <small class="text-muted">Radar Active</small>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

        </div>
    </div>
</section>

<section id="portals" class="portal-section">
    <div class="container">
        <div class="section-title">
            <h5>EXPLORE PORTALS</h5>
            <h2>Choose Your Workspace</h2>
        </div>

        <div class="row g-4">

            <div class="col-lg-3 col-md-6">
                <div class="feature-card">
                    <div class="feature-icon"><i class="fas fa-user-graduate"></i></div>
                    <h4>Student Portal</h4>
                    <p>Access your dashboard to register for modules and check enrollment status.</p>
                    <a href="course-enrollment.jsp" class="btn-link-custom">Enter Portal <i class="fas fa-arrow-right ms-1"></i></a>
                </div>
            </div>

            <div class="col-lg-3 col-md-6">
                <div class="feature-card">
                    <i class="fas fa-lock secure-badge"></i>
                    <div class="feature-icon"><i class="fas fa-user-shield"></i></div>
                    <h4>Admin Panel</h4>
                    <p>Approve pending requests and manage capacities using Insertion Sort.</p>
                    <button onclick="openAuthModal('admin')" class="btn-link-custom">Secure Login <i class="fas fa-lock ms-1"></i></button>
                </div>
            </div>

            <div class="col-lg-3 col-md-6">
                <div class="feature-card">
                    <i class="fas fa-lock secure-badge"></i>
                    <div class="feature-icon"><i class="fas fa-layer-group"></i></div>
                    <h4>Queue Reader</h4>
                    <p>Monitor live FIFO queues and automated Waitlist Radar systems.</p>
                    <button onclick="openAuthModal('radar')" class="btn-link-custom">Secure Login <i class="fas fa-lock ms-1"></i></button>
                </div>
            </div>

            <div class="col-lg-3 col-md-6">
                <div class="feature-card" style="border-bottom: 4px solid var(--theme-orange);">
                    <i class="fas fa-key secure-badge" style="color: var(--theme-orange);"></i>
                    <div class="feature-icon" style="background: var(--theme-blue); color: white;"><i class="fas fa-chart-pie"></i></div>
                    <h4>HOD Analytics</h4>
                    <p>View demand forecasting and predictive module allocation charts.</p>
                    <button onclick="openAuthModal('hod')" class="btn-link-custom" style="color: var(--theme-orange);">Authorize Access <i class="fas fa-arrow-right ms-1"></i></button>
                </div>
            </div>

        </div>
    </div>
</section>

<div class="modal fade" id="authModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header border-0 pb-0">
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-header d-flex flex-column align-items-center border-0 pt-2 pb-4">
                <div class="feature-icon bg-white mt-2 mb-3" style="width: 60px; height: 60px; font-size: 25px;"><i class="fas fa-shield-alt"></i></div>
                <h4 class="modal-title fw-bold" id="loginRoleTitle">Staff Authentication</h4>
                <small class="text-white-50">Private Access Only. Students are restricted.</small>
            </div>

            <div class="modal-body p-4 text-center">
                <form id="loginForm">
                    <input type="hidden" id="targetPortal">

                    <div class="input-group mb-3">
                        <span class="input-group-text bg-light border-end-0"><i class="fas fa-envelope text-muted"></i></span>
                        <input type="email" class="form-control bg-light border-start-0" id="userEmail" placeholder="Institutional Email" required>
                    </div>

                    <div class="input-group mb-4">
                        <span class="input-group-text bg-light border-end-0"><i class="fas fa-key text-muted"></i></span>
                        <input type="password" class="form-control bg-light border-start-0" id="userPassword" placeholder="Password" required>
                    </div>

                    <button type="button" onclick="verifyCredentials()" class="btn btn-theme-primary w-100 py-2 fs-6">
                        Login securely <i class="fas fa-sign-in-alt ms-2"></i>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    const authModal = new bootstrap.Modal(document.getElementById('authModal'));

    function openAuthModal(portal) {
        document.getElementById('targetPortal').value = portal;
        document.getElementById('userEmail').value = '';
        document.getElementById('userPassword').value = '';

        let title = "Admin Login";
        if(portal === 'radar') title = "Queue Reader Login";
        if(portal === 'hod') title = "HOD Restricted Login";

        document.getElementById('loginRoleTitle').innerText = title;
        authModal.show();
    }

    function verifyCredentials() {
        const email = document.getElementById('userEmail').value;
        const password = document.getElementById('userPassword').value;
        const target = document.getElementById('targetPortal').value;

        if(!email || !password) {
            Swal.fire({ icon: 'warning', title: 'Empty Fields', text: 'Please enter your email and password.', confirmButtonColor: '#FF5E14' });
            return;
        }

        // --- STRICT SECURITY CHECK ---
        let isAuthenticated = false;
        let redirectUrl = "";

        // 1. Admin & Queue Reader Logic
        if ((target === 'admin' || target === 'radar') && email === 'admin@sliit.lk' && password === 'admin123') {
            isAuthenticated = true;
            redirectUrl = target === 'admin' ? 'admin-dashboard.jsp' : 'waitlist-status.jsp';
        }
        // 2. HOD Logic
        else if (target === 'hod' && email === 'hod@sliit.lk' && password === 'hod123') {
            isAuthenticated = true;
            redirectUrl = 'hod-analytics.jsp';
        }

        // Results
        if (isAuthenticated) {
            authModal.hide();
            Swal.fire({
                icon: 'success',
                title: 'Access Granted',
                text: 'Routing to secure dashboard...',
                showConfirmButton: false,
                timer: 1500,
                timerProgressBar: true
            }).then(() => {
                window.location.href = redirectUrl;
            });
        } else {
            // Blocks students and wrong passwords
            Swal.fire({
                icon: 'error',
                title: 'Access Denied',
                text: 'Invalid credentials. Private use only. Students are strictly prohibited from accessing this area.',
                confirmButtonColor: '#0F2239'
            });
        }
    }
</script>
</body>
</html>