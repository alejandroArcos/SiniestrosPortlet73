<%@ include file="./init.jsp"%>

<portlet:actionURL var="buscaSiniestro" name="/siniestro/buscaSiniestro" />
<portlet:resourceURL id="/siniestro/siniestralidad"
	var="infSiniestralidad" />
<portlet:resourceURL id="/siniestro/autoCompletado"
	var="autoCompletadoAsegurado" />
<portlet:resourceURL id="/siniestro/llenaTablaSiniestro"
	var="llenaTablaSiniestro" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/main.css?v=${version}">

<liferay-ui:success key="exitoBusqueda" message="MsjExito" />
<liferay-ui:success key="exitoConocido" message="${errorMsg}" />
<liferay-ui:error key="error" message="MjsError" />
<liferay-ui:error key="errorConocido" message=" ${errorMsg}" />
<section>
	<div class="container">
		<div class="section-heading">
			<h1 class="title text-left">
				<liferay-ui:message
					key="siniestrosportlet_Siniestrosportletmvcportlet.tituloPrincipal" />
			</h1>
			<br />
			<c:set var="versionPublicar" scope="session" value="V. 1.40" />
			<%-- 			<c:out value="${versionPublicar}" /> --%>
		</div>
		<p></p>
		<div class="form-wrapper">
			<form action="${buscaSiniestro}" method="post" id="search-form"
				class="mb-4">
				<div class="row">
					<div class="col-sm-6 col-lg-3">
						<div class="md-form form-group">
							<input type="text" name="poliza" class="form-control"
								id="polizaSiniestro" value="${polizaBus}"> <label
								for="polizaSiniestro"> <liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloFormularioPoliza" />
							</label>
						</div>
					</div>
					<div class="col-sm-6 col-lg-3">
						<div class="md-form form-group">
							<input type="text" name="endoso" class="form-control"
								id="endosoSiniestro" value="${siniestroBus}"> <label
								for="endosoSiniestro"> <liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloFormularioSiniestro" />
							</label>
						</div>
					</div>
					<div class="col-sm-6 col-lg-6">
						<div class="md-form form-group">
							<input type="text" name="asegurado" class="form-control"
								id="aseguradoSiniestro" value="${aseguradoBus}"> <label
								for="aseguradoSiniestro"> <liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloFormularioAsegurado" />
							</label> <input type="hidden" id="txtIdAsegurado" name="txtIdAsegurado"
								value="${txtIdAsegurado}">
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 col-lg-6">
						<div class="md-form form-group">
							<div class="row">
								<label for="inicio" class="active"> <liferay-ui:message
										key="Siniestrosportletmvcportlet.tituloFormularioFechas" />
								</label>
								<div class="col">
									<input name="inicio" placeholder="Desde" type="text"
										id="creationDateInicio" class="form-control datepicker"
										value="${fechaInBus}">
								</div>
								<div class="col">
									<input name="fin" placeholder="Hasta" type="text"
										id="creationDateFin" class="form-control datepicker"
										value="${fechaFinBus}">
								</div>
							</div>
						</div>
					</div>
					<div class="col-sm-6 col-lg-6">
						<div class="md-form form-group">
							<select name="agente" class="mdb-select"
								searchable='<liferay-ui:message key="Siniestrosportletmvcportlet.buscar" />'>
								<c:if test="${fn:length(catRespAgente) gt 1}">
									<option value="0">Todos</option>
								</c:if>

								<c:set var="estatusAnterior" value="" />
								<c:forEach items="${catRespAgente}" var="agente">
									<c:if test="${agente.idPersona == agenteBus}">
										<c:set var="agenteAnterior" value="selected" />
									</c:if>
									<option value="${agente.idPersona}" ${agenteAnterior }>${agente.nombre}</option>
									<c:set var="agenteAnterior" value="" />
								</c:forEach>
							</select>
							<label for="agente" class="mdb-main-label">
								<liferay-ui:message key="Siniestrosportletmvcportlet.tituloFormularioAgente" />
							</label>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<button class="btn btn-pink float-right">
							<liferay-ui:message
								key="Siniestrosportletmvcportlet.tituloFormularioBtnBuscar" />
						</button>
					</div>
				</div>
			</form>
		</div>
		<c:if test="${not empty tablaSin}">
			<div class="table-wrapper">
				<table class="table data-table table-striped table-bordered"
					style="width: 100%;" id="tablaSiniestro">
					<thead>
						<tr>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloPoliza" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloSiniestro" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloAsegurado" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloFechaSiniestro" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloReservaPendiente" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloMontoPagado" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloMoneda" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloEstatus" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloBienDanado" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloCausaSiniestro" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloFechaReclamo" /></th>
							<th><liferay-ui:message
									key="Siniestrosportletmvcportlet.tituloFechaPago" /></th>
							<th class="all" data-orderable="false"></th>
							<%-- 			                <th><liferay-ui:message key="Siniestrosportletmvcportlet.tituloTelAjustador"/></th> --%>
							<%-- 			                <th><liferay-ui:message key="Siniestrosportletmvcportlet.tituloEstado"/></th> --%>
							<%-- 			                <th><liferay-ui:message key="Siniestrosportletmvcportlet.tituloAjustador"/></th> --%>
							<%-- 			                <th><liferay-ui:message key="Siniestrosportletmvcportlet.tituloMonto"/></th>                                     --%>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${tablaSin}" var="sin">
							<tr>
								<td>${sin.poliza}</td>
								<td>${sin.siniestro}</td>
								<td>${sin.asegurado}</td>
								<td class="fecha">${sin.fechaSiniestro}</td>
								<td class="number">$<fmt:formatNumber
										value="${fn:trim(sin.reservaPendiente)}" type="number"
										minFractionDigits="2" maxFractionDigits="2" />
								</td>
								<td class="number">$<fmt:formatNumber
										value="${fn:trim(sin.montoPagado)}" type="number"
										minFractionDigits="2" maxFractionDigits="2" />
								</td>
								<td>${sin.moneda}</td>
								<td>${sin.estatus}</td>
								<td>${sin.bienDanado}</td>
								<td>${sin.causa}</td>
								<td class="fecha">${sin.fechaReclamo}</td>
								<td class="fecha">${sin.fechaPago}</td>
								<%-- 			                    <td>${sin.estado}</td> --%>
								<%-- 			                    <td>${sin.ajustador}</td> --%>
								<%-- 			                    <td>${sin.telAjustador}</td> --%>
								<%-- 			                    <td>$<fmt:formatNumber value = "${sin.montoTotal}" type = "number" minFractionDigits="2" maxFractionDigits="2"/></td> --%>
								<td>
									<div class="actions-container dropleft">
										<button type="button"
											class="btn btn-outline-pink dropdown-menu-right px-3 py-2 waves-effect waves-light"
											data-toggle="dropdown" aria-haspopup="true"
											aria-expanded="false">
											<i class="fa fa-ellipsis-v" aria-hidden="true"></i>
										</button>
										<div class="dropdown-menu animated fadeIn">
											<form id="formCargaArchivo${sin.poliza}"
												action="${infSiniestralidad}" method="POST">
												<a class="dropdown-item" data-id="Siniestro 2"
													data-toggle="modal" href="#modal-archives"
													onclick="obtieneSiniestro('${sin.poliza}','${sin.asegurado}','${sin.siniestro}','${sin.fechaSiniestro}','${sin.fechaReclamo}');">
													<i class="far fa-file-alt mr-2"></i> <span> <liferay-ui:message
															key="Siniestrosportletmvcportlet.tituloBtnModalDetalle" />
												</span>
												</a>
											</form>
										</div>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>
	</div>
	<!-- Modal Ver Archivos -->
	<div class="modal" id="modal-archives" tabindex="-1" role="dialog"
		aria-labelledby="archivesLabel" aria-hidden="true">
		<div class="modal-dialog modal-fluid modal-dialog-centered"
			role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="#archivesLabel">
						Detalle del Siniestro <span id="siniestroSeleccionado"></span>
					</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="table-wrapper">
						<table
							class="table custom-data-table table-striped table-bordered"
							style="width: 100%;" id="tablaSiniestros">
							<thead>
								<tr>
									<th><liferay-ui:message
											key="Siniestrosportletmvcportlet.tituloTablaModalConsecutivo" />
									</th>
									<th><liferay-ui:message
											key="Siniestrosportletmvcportlet.tituloTablaModalCobertura" />
									</th>
									<th><liferay-ui:message
											key="Siniestrosportletmvcportlet.tituloTablaModalRamo" /></th>
									<th><liferay-ui:message
											key="Siniestrosportletmvcportlet.tituloTablaModalMontoTotal" />
									</th>
									<th><liferay-ui:message
											key="Siniestrosportletmvcportlet.tituloTablaModalTipoTransaccion" />
									</th>
									<th><liferay-ui:message
											key="Siniestrosportletmvcportlet.tituloTablaModalMoneda" />
									</th>
									<th><liferay-ui:message
											key="Siniestrosportletmvcportlet.tituloTablaModalFechaTransaccion" />
									</th>
									<th><liferay-ui:message
											key="Siniestrosportletmvcportlet.tituloTablaModalEstado" />
									</th>
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- END Modal Ver archivos -->
</section>

<form id="divInfoAuxiliar" hidden="true">

	<!-- portlet resourceURL -->

	<input type="hidden" id="txtJSBuscaSiniestro" value="${buscaSiniestro}">
	<input type="hidden" id="txtJSInfSiniestralidad"
		value="${infSiniestralidad}"> <input type="hidden"
		id="txtJSAutoCompletadoAsegurado" value="${autoCompletadoAsegurado}">
	<input type="hidden" id="txtJSLlenaTablaSiniestro"
		value="${llenaTablaSiniestro}"> <input type="hidden"
		id="txtRowTotalNum" value="${rowTotalNum}"> <input
		type="hidden" id="txtJSTituloBtnModalDetalle"
		value="<liferay-ui:message key="Siniestrosportletmvcportlet.tituloBtnModalDetalle" />">
	<input type="hidden" id="txtJSErrorCampoRequerido"
		value="<liferay-ui:message key="Siniestrosportletmvcportlet.errorCampoRequerido" />">

	<script src="<%=request.getContextPath()%>/js/jquery-ui.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/js/siniestrosScript.js?v=${ versionPublicar }"></script>
</form>

