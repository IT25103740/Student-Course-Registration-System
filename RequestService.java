body{
    margin:0;
    font-family: Arial, sans-serif;
    background:#f4f6f9;
}

.dashboard{
    display:flex;
}

/* SIDEBAR */

.sidebar{
    width:220px;
    background:#1e293b;
    color:white;
    height:100vh;
    padding:20px;
}

.sidebar h2{
    text-align:center;
}

.sidebar ul{
    list-style:none;
    padding:0;
}

.sidebar li{
    padding:12px;
    margin-top:10px;
    border-radius:5px;
}

.sidebar li.active,
.sidebar li:hover{
    background:#334155;
}

/* MAIN */

.main{
    flex:1;
    padding:20px;
}

h1{
    margin-bottom:20px;
}

/* CARDS */

.cards{
    display:flex;
    gap:20px;
}

.card{
    background:white;
    flex:1;
    padding:20px;
    border-radius:10px;
    box-shadow:0 2px 5px rgba(0,0,0,0.1);
}

.card p{
    font-size:28px;
    font-weight:bold;
}

/* TABLE */

.table-section{
    margin-top:30px;
    background:white;
    padding:20px;
    border-radius:10px;
    box-shadow:0 2px 5px rgba(0,0,0,0.1);
}

.table-header{
    display:flex;
    justify-content:space-between;
    align-items:center;
}

table{
    width:100%;
    border-collapse:collapse;
    margin-top:20px;
}

th, td{
    padding:12px;
    border-bottom:1px solid #ddd;
    text-align:center;
}

tr:hover{
    background:#f1f5f9;
}

/* STATUS */

.pending{
    background:orange;
    color:white;
    padding:5px 10px;
    border-radius:5px;
}

.approved{
    background:green;
    color:white;
    padding:5px 10px;
    border-radius:5px;
}

.rejected{
    background:red;
    color:white;
    padding:5px 10px;
    border-radius:5px;
}

/* BUTTONS */

.action-form{
    display:flex;
    gap:5px;
    justify-content:center;
}

button{
    border:none;
    padding:8px 12px;
    color:white;
    border-radius:5px;
    cursor:pointer;
}

.approve-btn{
    background:green;
}

.reject-btn{
    background:red;
}

.clear-btn{
    background:#0f172a;
}

button:hover{
    opacity:0.9;
}