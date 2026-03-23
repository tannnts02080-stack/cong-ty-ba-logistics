<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-dark d-flex justify-content-center align-items-center vh-100">

<div class="card shadow-lg" style="width:380px">
<div class="card-body p-4">
<h4 class="text-center mb-4">🔐 ĐĂNG NHẬP</h4>

<form action="login" method="post">
    <div class="mb-3">
        <input name="username" class="form-control" placeholder="Tên đăng nhập" required>
    </div>
    <div class="mb-3">
        <input type="password" name="password" class="form-control" placeholder="Mật khẩu" required>
    </div>
    <button class="btn btn-primary w-100">Đăng nhập</button>
</form>

<p class="text-danger mt-3 text-center">${error}</p>
</div>
</div>

</body>
</html>
