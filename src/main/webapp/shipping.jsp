<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Shipping</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
body {
	background: linear-gradient(135deg, #eef2f7, #ffffff);
	min-height: 100vh;
}

.card {
	border-radius: 18px;
}

.form-control, .form-select {
	border-radius: 12px;
}

.btn {
	border-radius: 12px;
}

/* ===== HEO RƠI ===== */
.pig-layer {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 0;              /* NẰM SAU */
    pointer-events: none;    /* KHÔNG CẢN CLICK */
    overflow: hidden;
}

.pig {
    position: absolute;
    font-size: 22px;
    opacity: 1.0;
    animation: pigFall linear forwards;
}

@keyframes pigFall {
    from {
        transform: translateY(-50px) rotate(0deg);
    }
    to {
        transform: translateY(110vh) rotate(360deg);
    }
}

/* ĐẨY NỘI DUNG LÊN TRÊN HEO */
.container {
    position: relative;
    z-index: 2;
}


</style>
</head>

<body>

<!-- 🐷 HEO RƠI -->
<div class="pig-layer" id="pigLayer"></div>


	<div class="container-xl py-5">


		<!-- ===== MENU + LOGOUT ===== -->
<div class="d-flex justify-content-between align-items-center mb-4">

    <!-- TAB -->
    <ul class="nav nav-tabs">
        <li class="nav-item">
            <button
                class="nav-link ${param.tab == null || param.tab == 'price' ? 'active' : ''}"
                data-bs-toggle="tab"
                data-bs-target="#tab-price">
                📊 Bảng giá cước
            </button>
        </li>

        <li class="nav-item">
            <button
                class="nav-link ${param.tab == 'order' ? 'active' : ''}"
                data-bs-toggle="tab"
                data-bs-target="#tab-order">
                🚚 Ghi nhận đơn
            </button>
        </li>
    </ul>

    <!-- LOGOUT -->
    <a href="${pageContext.request.contextPath}/logout"
       class="btn btn-outline-danger fw-semibold">
        🚪 Đăng xuất
    </a>

</div>



<!-- NÚT ĐĂNG XUẤT -->
    


		<div class="tab-content">

			<!-- ================= TAB BẢNG GIÁ ================= -->
			<div
				class="tab-pane fade
				${param.tab == null || param.tab == 'price' ? 'show active' : ''}"
				id="tab-price">

				<!-- THÊM KHÁCH MỚI -->
				<div class="card shadow mb-4">
					<div class="card-body p-4">
						<c:if test="${param.error == 'duplicateCustomer'}">
							<div class="alert alert-danger mb-3">❌ Khách hàng này đã
								tồn tại!</div>
						</c:if>

						<h5 class="fw-bold mb-3">➕ Thêm khách hàng mới</h5>

						<form method="post"
							action="${pageContext.request.contextPath}/customer"
							class="row g-3 align-items-end">
							<div class="col-md-8">
								<label class="form-label fw-semibold">Tên khách hàng</label> <input
									type="text" name="name" class="form-control"
									placeholder="VD: A Bình Mộc Bài" required>
							</div>
							<div class="col-md-4 d-grid">
								<button class="btn btn-primary fw-semibold">➕ Thêm
									khách</button>
								
									 <!-- NÚT HỦY -->
    <a href="${pageContext.request.contextPath}/shipping"
       class="btn btn-secondary fw-semibold mt-2">
        ❌ Hủy
    </a>
							</div>
						</form>
					</div>
				</div>

				<!-- THÊM BẢNG GIÁ -->
				<div class="card shadow mb-4">
					<div class="card-body p-4">
						<h5 class="fw-bold mb-3">➕ Thêm bảng giá cho khách</h5>
						<c:if test="${param.error == 'duplicateKg'}">
							<div class="alert alert-danger mb-3">❌ Khách hàng này đã có
								giá cho mức kg này rồi!</div>
						</c:if>

						<form method="post"
							action="${pageContext.request.contextPath}/shipping-price"
							class="row g-3 align-items-end">

							<div class="col-md-4">
								<label class="form-label fw-semibold">Khách hàng</label> <select
									name="customer_id" class="form-select" required>
									<option value="">-- Chọn khách --</option>
									<c:forEach items="${customers}" var="c">
										<option value="${c.id}">${c.name}</option>
									</c:forEach>
								</select>
							</div>

							<div class="col-md-3">
								<label class="form-label fw-semibold">Số kg (có thể bỏ
									trống)</label> <input type="number" name="kg" class="form-control"
									placeholder="VD: 40">
							</div>

							<div class="col-md-3">
								<label class="form-label fw-semibold">Giá tiền (VNĐ)</label> <input
									type="number" name="price" class="form-control" required>
							</div>

							<div class="col-md-2 d-grid">
								<button class="btn btn-success fw-semibold">➕ Thêm giá</button>
								
								<!-- NÚT HỦY -->
    <a href="${pageContext.request.contextPath}/shipping"
       class="btn btn-secondary fw-semibold mt-2">
        ❌ Hủy
    </a>
							</div>
						</form>
					</div>
				</div>

				<!-- TÌM KIẾM -->
				<form method="get"
					action="${pageContext.request.contextPath}/shipping"
					class="d-flex gap-2 mb-3">
					<input type="text" name="keyword" class="form-control"
						placeholder="Nhập tên khách (VD: ánh, dương...)"
						value="${keyword}">
					<button class="btn btn-primary">Tìm</button>
				</form>

				<!-- BẢNG GIÁ -->
				<div class="card shadow mb-4">
					<div class="card-body p-4">
						<h5 class="fw-bold mb-3">📊 Bảng giá theo khách hàng</h5>

						<div class="table-responsive">
							<table class="table table-bordered align-middle">
								<thead class="table-dark">
									<tr>
										<th>Khách hàng</th>
										<th>Số kg</th>
										<th>Giá tiền (VNĐ)</th>
										<th>Hành động</th>
									</tr>
								</thead>
								<tbody>

									<c:if test="${empty priceTable}">
										<tr>
											<td colspan="4" class="text-center text-muted">Không tìm
												thấy khách hàng phù hợp</td>
										</tr>
									</c:if>

									<c:forEach items="${priceTable}" var="entry">
										<c:set var="customer" value="${entry.key}" />
										<c:set var="prices" value="${entry.value}" />

										<c:forEach items="${prices}" var="p" varStatus="st">
											<tr>
												<c:if test="${st.first}">
													<td rowspan="${fn:length(prices)}" class="fw-semibold">

														<div
															class="d-flex justify-content-between align-items-center">
															<span>${customer}</span>

															<!-- LẤY ID KHÁCH -->
															<c:set var="customerId" value="0" />
															<c:forEach items="${customers}" var="c">
																<c:if test="${c.name eq customer}">
																	<c:set var="customerId" value="${c.id}" />
																</c:if>
															</c:forEach>

															<div class="btn-group btn-group-sm ms-2">
																<!-- SỬA -->
																<button type="button" class="btn btn-warning"
																	onclick="editCustomer(${customerId}, '${customer}')">
																	Sửa</button>



															</div>
														</div>

													</td>
												</c:if>


												<td><c:choose>
														<c:when test="${p.kg == null}">0 kg</c:when>
														<c:otherwise>${p.kg} kg</c:otherwise>
													</c:choose></td>

												<td class="text-danger fw-semibold">${p.price}</td>

												<td>
													<form method="post"
														action="${pageContext.request.contextPath}/shipping-price"
														style="display: inline">
														<input type="hidden" name="action" value="delete">
														<input type="hidden" name="id" value="${p.id}">
														<button class="btn btn-sm btn-danger"
															onclick="return confirm('Xoá giá này?')">Xoá</button>
													</form>
												</td>
											</tr>
										</c:forEach>
									</c:forEach>

								</tbody>
							</table>
						</div>
					</div>
				</div>

			</div>

			<!-- ================= TAB GHI NHẬN ĐƠN ================= -->
			<div
				class="tab-pane fade
${param.tab == 'order' ? 'show active' : ''}"
				id="tab-order">


				<div class="text-center mb-4">
					
					<form method="get"
						action="${pageContext.request.contextPath}/shipping"
						class="d-flex align-items-center gap-2 mb-4">

						<input type="hidden" name="tab" value="order"> <label
							class="fw-semibold">📅 Ngày:</label> <input type="date"
							name="date" class="form-control" style="max-width: 180px"
							value="${selectedDateValue}">


						<button class="btn btn-outline-primary">Xem</button>
					</form>
				</div>

				<!-- FORM -->
				<div class="card shadow mb-4">
					<div class="card-body p-4">
						<form method="post"
							action="${pageContext.request.contextPath}/shipping?tab=order"
							class="row g-4 align-items-end">

							<input type="hidden" name="date" value="${selectedDateValue}">
<input type="hidden" name="id" value="${editLog.id}">

<!-- KHÁCH HÀNG -->
<div class="col-md-4">
    <label class="form-label fw-semibold">Khách hàng</label>
    <select id="customerSelect" name="customer_id" class="form-select" required>
        <option value="">-- Chọn khách --</option>
        <c:forEach items="${customers}" var="c">
            <option value="${c.id}"
                ${editLog.customerId == c.id ? "selected" : ""}>
                ${c.name}
            </option>
        </c:forEach>
    </select>
</div>

<!-- KG -->
<div class="col-md-2">
    <label class="form-label fw-semibold">Số kg</label>
    <select id="kgSelect" name="kg" class="form-select">
        <option value="">-- Không có kg --</option>
    </select>
</div>

<!-- TIỀN SHIP -->
<div class="col-md-3">
    <label class="form-label fw-semibold">Tiền ship (VNĐ)</label>
    <input id="priceInput"
           name="price"
           class="form-control"
           readonly
           required
           value="${editLog.price}">
</div>

<!-- THU SHIP -->
<div class="col-md-3">
    <label class="form-label fw-semibold">Thu ship</label>
    <div class="form-check mt-2">
        <input class="form-check-input"
               type="checkbox"
               name="paid"
               ${editLog.paid ? "checked" : ""}>
        <label class="form-check-label">Đã thu</label>
    </div>
</div>

<!-- CHI PHÍ PHÁT SINH -->
<div class="col-md-3">
    <label class="form-label fw-semibold">Chi phí phát sinh</label>
    <input type="number"
           name="extraCost"
           class="form-control"
           placeholder="VD: 100000"
           value="${editLog.extraCost}">
</div>

<!-- GHI CHÚ -->
<div class="col-md-5">
    <label class="form-label fw-semibold">Ghi chú</label>
    <input type="text"
           name="note"
           class="form-control"
           placeholder="VD: Trả tiền hàng"
           value="${editLog.note}">
</div>

<!-- NÚT LƯU -->
<div class="col-md-4 d-grid align-self-end">
    <button class="btn btn-primary fw-semibold">
        💾 ${editLog != null ? "Cập nhật" : "Lưu"}
    </button>
    
    <!-- HỦY -->
    <a href="${pageContext.request.contextPath}/shipping?tab=order"
       class="btn btn-secondary fw-semibold mt-2">
        ❌ Hủy
    </a>
</div>

						</form>
					</div>
				</div>

				<!-- LỊCH SỬ -->
				<div class="card shadow">
					<div class="card-body p-4">
						<h5 class="fw-bold mb-3">📋 Lịch sử giao hàng</h5>

						<div class="table-responsive">
							<table class="table table-bordered table-hover align-middle">
								<thead class="table-dark">
    <tr>
        <th>STT</th>
        <th>Khách hàng</th>
        <th>Kg</th>
        <th>Tiền ship</th>
        <th>Thu ship</th>
        <th>Chi phí phát sinh</th>
        <th>Ghi chú</th>
        <th>Thời gian</th>
        <th>Hành động</th>
    </tr>
</thead>

								<tbody>
									<c:forEach items="${logs}" var="l" varStatus="st">
										<tr>
											<td>${st.index + 1}</td>
											<td>${l.customer}</td>
											<td>${l.kg}</td>

<td class="text-danger fw-semibold">
    ${l.price}
</td>

<td>
    <c:choose>
        <c:when test="${l.paid}">
            <span class="badge bg-success">Đã thu</span>
        </c:when>
        <c:otherwise>
            <span class="badge bg-secondary">Chưa thu</span>
        </c:otherwise>
    </c:choose>
</td>

<td class="text-warning fw-semibold">
    <c:if test="${l.extraCost > 0}">
        ${l.extraCost} VNĐ
    </c:if>
</td>

<td>
    <c:out value="${l.note}" />
</td>

<td>${l.time}</td>

											<td><a class="btn btn-sm btn-warning"
												href="${pageContext.request.contextPath}/shipping?action=edit&id=${l.id}&tab=order">
													Sửa </a> <a class="btn btn-sm btn-danger"
												href="${pageContext.request.contextPath}/shipping?action=delete&id=${l.id}&tab=order"
												onclick="return confirm('Xoá chuyến giao này?')"> Xoá </a></td>
										</tr>
									</c:forEach>
									<c:if test="${empty logs}">
										<tr>
											<td colspan="6" class="text-center text-muted">Chưa có
												chuyến giao nào</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</div>


				<div class="card shadow mt-3">
					<div
						class="card-body d-flex justify-content-between align-items-center">
						<span class="fw-semibold fs-5"> 💰 Tổng tiền giao hàng ngày
							<span class="text-primary">${selectedDate}</span>
						</span> <span class="fs-4 fw-bold text-danger"> ${totalAmount} VNĐ
						</span>
					</div>
				</div>
				
				
				

<%-- 19h03 --%>
<c:if test="${extraTotal > 0}">
<div class="card shadow mt-2">
    <div class="card-body d-flex justify-content-between align-items-center">
        <span class="fw-semibold fs-5 text-warning">
            ⚠ Chi phí phát sinh ngày
            <span class="text-primary">${selectedDate}</span>
        </span>

        <span class="fs-4 fw-bold text-warning">
            ${extraTotal} VNĐ
        </span>
    </div>
</div>
</c:if>





<div class="card shadow mt-3">
    <div class="card-body d-flex justify-content-between align-items-center">
        <span class="fw-semibold fs-5">
            📊 Tổng tiền giao hàng tháng
            <span class="text-primary">
                ${fn:substring(selectedDate, 3, 10)}
            </span>
            (01 → ${selectedDate})
        </span>

        <span class="fs-4 fw-bold text-success">
            ${monthTotal} VNĐ
        </span>
    </div>
</div>


			</div>

		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

	<script>
    const editingKg = "${editLog.kg}";
</script>


	<script>
	const contextPath = "${pageContext.request.contextPath}";

	const customerSelect = document.getElementById("customerSelect");
	const kgSelect = document.getElementById("kgSelect");
	const priceInput = document.getElementById("priceInput");

	function loadKgAndSelect() {
		const customerId = customerSelect.value;
		kgSelect.innerHTML = '<option value="">-- Không có kg --</option>';
		priceInput.value = "";

		if (!customerId) return;

		fetch(contextPath + "/shipping-price/kg?customerId=" + customerId)
			.then(res => res.json())
			.then(data => {
				data.forEach(kg => {
					const opt = document.createElement("option");
					opt.value = kg ?? "";
					opt.textContent = kg == null ? "Không có kg" : kg + " kg";

					// ✅ AUTO SELECT KG KHI ĐANG SỬA
					if (editingKg !== "" && String(kg) === editingKg) {
						opt.selected = true;
					}

					kgSelect.appendChild(opt);
				});

				// nếu đang sửa → load luôn giá
				if (editingKg !== "") {
					kgSelect.dispatchEvent(new Event("change"));
				}
			});
	}

	customerSelect.addEventListener("change", loadKgAndSelect);

	kgSelect.addEventListener("change", function () {
		const customerId = customerSelect.value;
		const kg = this.value;

		if (!customerId) return;

		fetch(contextPath + "/shipping-price/value?customerId=" + customerId + "&kg=" + (kg === "" ? "null" : kg))
			.then(res => res.text())
			.then(price => priceInput.value = price);
	});

	// ✅ TRANG LOAD + ĐANG SỬA → AUTO LOAD KG
	if (editingKg !== "") {
		loadKgAndSelect();
	}
</script>



	<!-- MODAL SỬA KHÁCH -->
	<div class="modal fade" id="editCustomerModal" tabindex="-1">
		<div class="modal-dialog">
			<form method="post"
				action="${pageContext.request.contextPath}/customer-update"
				class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Sửa tên khách hàng</h5>
				</div>
				<div class="modal-body">
					<input type="hidden" name="id" id="editCustomerId"> <input
						type="text" name="name" id="editCustomerName" class="form-control"
						required>
				</div>
				<div class="modal-footer">
					<button class="btn btn-warning">Lưu</button>
				</div>
			</form>
		</div>
	</div>

	<script>
function editCustomer(id, name) {
    document.getElementById("editCustomerId").value = id;
    document.getElementById("editCustomerName").value = name;
    new bootstrap.Modal(
        document.getElementById('editCustomerModal')
    ).show();
}
</script>


<script>
const pigLayer = document.getElementById("pigLayer");

function createPig() {
    const pig = document.createElement("div");
    pig.className = "pig";
    pig.innerText = "🐷"; // đổi 🐖 cũng được

    pig.style.left = Math.random() * 100 + "vw";
    pig.style.animationDuration = (6 + Math.random() * 6) + "s";

    pigLayer.appendChild(pig);

    // xoá khi rơi xong
    setTimeout(() => {
        pig.remove();
    }, 12000);
}

// mỗi 1.2s rơi 1 con
setInterval(createPig, 800);
</script>



</body>
</html>
