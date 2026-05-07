const API_URL = 'http://localhost:8080/api';
let currentUser = null;
let statusChart = null;

// UI Helpers
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast show ${type}`;
    setTimeout(() => {
        toast.className = 'toast';
    }, 3000);
}

function toggleAuth() {
    document.getElementById('auth-container').classList.toggle('active');
    document.getElementById('register-container').classList.toggle('active');
}

function showSection(sectionId, event) {
    document.querySelectorAll('.content-section').forEach(sec => sec.classList.remove('active'));
    document.getElementById(`${sectionId}-section`).classList.add('active');
    
    document.querySelectorAll('.nav-links li').forEach(li => li.classList.remove('active'));
    if (event) {
        event.currentTarget.classList.add('active');
    }

    if (sectionId === 'overview') updateDashboard();
    if (sectionId === 'users') fetchUsers();
    if (sectionId === 'logs') fetchLogs();
}

function checkAuth() {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
        currentUser = JSON.parse(savedUser);
        document.getElementById('auth-container').classList.remove('active');
        document.getElementById('register-container').classList.remove('active');
        document.getElementById('dashboard-container').classList.add('active');
        document.getElementById('current-user-display').textContent = `${currentUser.username} (${currentUser.role})`;
        
        if (currentUser.role === 'ADMIN') {
            showSection('overview');
        } else {
            document.querySelector('.nav-links').style.display = 'none';
            document.getElementById('users-tbody').innerHTML = '<tr><td colspan="5">Access Restricted to Administrators</td></tr>';
            showSection('users');
        }
    }
}

function logout() {
    localStorage.removeItem('currentUser');
    currentUser = null;
    document.getElementById('dashboard-container').classList.remove('active');
    document.getElementById('auth-container').classList.add('active');
    showToast('Logged out successfully');
}

// Format Data for sending via x-www-form-urlencoded
function toFormData(obj) {
    return Object.keys(obj).map(key => encodeURIComponent(key) + '=' + encodeURIComponent(obj[key])).join('&');
}

// Auth Actions
document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const u = document.getElementById('login-username').value;
    const p = document.getElementById('login-password').value;

    try {
        const res = await fetch(`${API_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: toFormData({ username: u, password: p })
        });
        
        if (res.ok) {
            const data = await res.json();
            localStorage.setItem('currentUser', JSON.stringify(data));
            showToast('Login successful!');
            checkAuth();
        } else {
            showToast('Invalid credentials or inactive account', 'error');
        }
    } catch (err) {
        showToast('Server error. Is the backend running?', 'error');
    }
});

document.getElementById('register-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const u = document.getElementById('reg-username').value;
    const p = document.getElementById('reg-password').value;
    const r = document.getElementById('reg-role').value;

    try {
        const res = await fetch(`${API_URL}/users`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: toFormData({ username: u, password: p, role: r })
        });
        
        if (res.ok) {
            showToast('Registration successful! Please login.');
            toggleAuth();
        } else {
            const data = await res.json();
            showToast(data.error || 'Registration failed', 'error');
        }
    } catch (err) {
        showToast('Server error.', 'error');
    }
});

// Dashboard Logic
async function updateDashboard() {
    if (currentUser?.role !== 'ADMIN') return;

    try {
        const [usersRes, logsRes] = await Promise.all([
            fetch(`${API_URL}/users`),
            fetch(`${API_URL}/logs`)
        ]);

        const users = await usersRes.json();
        const logs = await logsRes.json();

        // Update Stats
        document.getElementById('stat-total-users').textContent = users.length;
        document.getElementById('stat-active-users').textContent = users.filter(u => u.active).length;
        document.getElementById('stat-admin-users').textContent = users.filter(u => u.role === 'ADMIN').length;
        document.getElementById('stat-total-logs').textContent = logs.length;

        // Update Chart
        renderChart(users);
    } catch (err) {
        console.error('Dashboard update failed', err);
    }
}

function renderChart(users) {
    const ctx = document.getElementById('userStatusChart').getContext('2d');
    const activeCount = users.filter(u => u.active).length;
    const inactiveCount = users.length - activeCount;

    if (statusChart) statusChart.destroy();

    statusChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Active', 'Disabled'],
            datasets: [{
                data: [activeCount, inactiveCount],
                backgroundColor: ['#10b981', '#ef4444'],
                borderWidth: 0,
                hoverOffset: 10
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    position: 'right',
                    labels: { 
                        color: '#f8fafc', 
                        padding: 15, 
                        font: { size: 13, weight: '600' },
                        usePointStyle: true,
                        boxWidth: 8
                    }
                }
            },
            layout: {
                padding: {
                    top: 10,
                    bottom: 10,
                    left: 0,
                    right: 20
                }
            }
        }
    });
}

async function downloadCSV(type) {
    try {
        const res = await fetch(`${API_URL}/${type}`);
        const data = await res.json();
        if (!data || data.length === 0) {
            showToast('No data available to download', 'error');
            return;
        }

        const headers = Object.keys(data[0]).join(',');
        const rows = data.map(obj => Object.values(obj).join(',')).join('\n');
        const csvContent = "data:text/csv;charset=utf-8," + headers + "\n" + rows;
        
        const encodedUri = encodeURI(csvContent);
        const link = document.createElement("a");
        link.setAttribute("href", encodedUri);
        link.setAttribute("download", `${type}_report_${new Date().toISOString().split('T')[0]}.csv`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        showToast(`Downloaded ${type} report`);
    } catch (err) {
        showToast('Export failed', 'error');
    }
}

// Data Fetching
async function fetchUsers() {
    if (currentUser?.role !== 'ADMIN') {
        document.getElementById('users-tbody').innerHTML = `<tr><td colspan="5">Access Denied: Admins Only</td></tr>`;
        return;
    }
    
    try {
        const res = await fetch(`${API_URL}/users`);
        const users = await res.json();
        const tbody = document.getElementById('users-tbody');
        tbody.innerHTML = '';
        
        users.forEach(u => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td style="font-size: 0.8rem; color: var(--text-secondary);">${u.id.substring(0,8)}...</td>
                <td><strong>${u.username}</strong></td>
                <td><span class="badge ${u.role.toLowerCase()}">${u.role}</span></td>
                <td><span class="badge ${u.active ? 'active' : 'inactive'}">${u.active ? 'Active' : 'Disabled'}</span></td>
                <td class="action-btns">
                    <button class="btn secondary small" onclick="openEditModal('${u.id}', '${u.role}')" ${!u.active ? 'disabled' : ''}>Edit</button>
                    <button class="btn danger small" onclick="deleteUser('${u.id}')" ${!u.active ? 'disabled' : ''}>Disable</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        showToast('Failed to load users', 'error');
    }
}

async function fetchLogs() {
    if (currentUser?.role !== 'ADMIN') {
        document.getElementById('logs-tbody').innerHTML = `<tr><td colspan="3">Access Denied: Admins Only</td></tr>`;
        return;
    }

    try {
        const res = await fetch(`${API_URL}/logs`);
        const logs = await res.json();
        const tbody = document.getElementById('logs-tbody');
        tbody.innerHTML = '';
        
        logs.reverse().forEach(l => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong>${l.username}</strong></td>
                <td><span style="color: var(--accent)">${l.action}</span></td>
                <td style="font-size: 0.9rem; color: var(--text-secondary);">${l.timestamp}</td>
            `;
            tbody.appendChild(tr);
        });
    } catch (err) {
        showToast('Failed to load logs', 'error');
    }
}

// User Actions
function openEditModal(id, role) {
    document.getElementById('edit-id').value = id;
    document.getElementById('edit-role').value = role;
    document.getElementById('edit-password').value = '';
    document.getElementById('edit-modal').classList.add('active');
}

function closeModal() {
    document.getElementById('edit-modal').classList.remove('active');
}

document.getElementById('edit-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('edit-id').value;
    const password = document.getElementById('edit-password').value;
    const role = document.getElementById('edit-role').value;

    const payload = { id, role };
    if (password) payload.password = password;

    try {
        const res = await fetch(`${API_URL}/users`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: toFormData(payload)
        });
        
        if (res.ok) {
            showToast('User updated successfully');
            closeModal();
            fetchUsers();
        } else {
            showToast('Update failed', 'error');
        }
    } catch (err) {
        showToast('Server error', 'error');
    }
});

async function deleteUser(id) {
    if (!confirm('Are you sure you want to disable this account?')) return;
    
    try {
        const res = await fetch(`${API_URL}/users`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: toFormData({ id })
        });
        
        if (res.ok) {
            showToast('User disabled');
            fetchUsers();
        } else {
            showToast('Failed to disable user', 'error');
        }
    } catch (err) {
        showToast('Server error', 'error');
    }
}

async function clearLogs() {
    if (!confirm('Are you sure you want to clear ALL security logs? This cannot be undone.')) return;

    try {
        const res = await fetch(`${API_URL}/logs`, { method: 'DELETE' });
        if (res.ok) {
            showToast('Logs cleared successfully');
            fetchLogs();
            if (document.getElementById('overview-section').classList.contains('active')) {
                updateDashboard();
            }
        } else {
            showToast('Failed to clear logs', 'error');
        }
    } catch (err) {
        showToast('Server error', 'error');
    }
}

// Init
checkAuth();
