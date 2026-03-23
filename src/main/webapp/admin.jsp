<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%
if (!"admin".equals(session.getAttribute("role"))) {
    response.sendRedirect("index.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin - Quản lý người dùng</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">
<div class="container mt-5">

<div class="card shadow">
<div class="card-body">

<h3 class="mb-4">👑 Quản lý người dùng</h3>

<!-- FORM THÊM / SỬA -->
<form method="post"
      action="${pageContext.request.contextPath}/admin/users"
      class="row g-2 mb-4">

    <input type="hidden" name="action"
           value="${editUser != null ? 'update' : 'add'}">
    <input type="hidden" name="id" value="${editUser.id}">

    <div class="col-md-3">
        <input name="username"
               class="form-control"
               placeholder="Tên đăng nhập"
               value="${editUser.username}"
               ${editUser != null ? "readonly" : ""}
               required>
    </div>

    <div class="col-md-2">
        <input name="password"
               class="form-control"
               placeholder="Mật khẩu"
               ${editUser != null ? "disabled" : ""}>
    </div>

    <div class="col-md-3">
        <input name="position"
               class="form-control"
               placeholder="Vị trí"
               value="${editUser.position}">
    </div>

    <div class="col-md-2">
        <select name="role" class="form-select">
            <option value="user" ${editUser.role == 'user' ? 'selected' : ''}>User</option>
            <option value="admin" ${editUser.role == 'admin' ? 'selected' : ''}>Admin</option>
        </select>
    </div>

    <div class="col-md-2 d-flex gap-2">
        <button class="btn btn-primary w-100">
            ${editUser != null ? "Cập nhật" : "Thêm"}
        </button>
    </div>
</form>

<!-- BẢNG USER -->
<table class="table table-hover table-bordered align-middle">
<thead class="table-dark">
<tr>
    <th>STT</th>

    <th>User</th>
    <th>Password</th>
    <th>Role</th>
    <th>Vị trí</th>
    <th>Ngày tạo</th>
    <th>Hành động</th>
</tr>
</thead>

<tbody>
<c:forEach items="${users}" var="u" varStatus="st">

<tr>
    <td>${st.index + 1}</td>

    <td>${u.username}</td>
    <td>${u.password}</td>
    <td>${u.role}</td>
    <td>${u.position}</td>
    <td>${u.created}</td>

    <td>
        <a class="btn btn-sm btn-warning"
           href="${pageContext.request.contextPath}/admin/users?action=edit&id=${u.id}">
            Sửa
        </a>

        <c:if test="${u.role != 'admin'}">
            <form method="post"
                  action="${pageContext.request.contextPath}/admin/users"
                  style="display:inline">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="${u.id}">
                <button class="btn btn-sm btn-danger"
                        onclick="return confirm('Xoá user này?')">
                    Xoá
                </button>
            </form>
        </c:if>
    </td>
</tr>
</c:forEach>
</tbody>
</table>

<!-- NÚT HUỶ + ĐĂNG XUẤT -->
<div class="mt-3 d-flex gap-2">
    <a href="${pageContext.request.contextPath}/admin/users"
       class="btn btn-secondary">
        Huỷ
    </a>

    <a href="${pageContext.request.contextPath}/logout"
       class="btn btn-danger">
        Đăng xuất
    </a>
</div>

</div>
</div>
</div>
</body>
</html>
