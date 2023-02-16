var finInput;
var finPicker;
var inicioInput;
var inicioPicker;
var tablaModal = null;

$(document).ready(function() {
	
	
	customModalTable = $('.custom-data-table').DataTable({
		responsive : true,
		dom : 'fBrltip',
		buttons : [ {
			extend : 'excelHtml5',
			text : '<a class="btn-floating btn-sm teal waves-effect waves-light py-2 my-0">XLS</a>',
			titleAttr : 'Exportar XLS',
			className : "btn-unstyled",
			exportOptions : {
				columns : ':not(:last)',
				format : {
					body : function(data, row, column, node) {
						return data.replace(/[$,]/g, '');
					}
				}

			}
		} ],
		columnDefs : [ {
			targets : '_all',
			className : "py-2"
		} ],
		lengthChange : true,
		language : {
			"sProcessing" : "Procesando...",
			"sLengthMenu" : "Mostrando _MENU_ registros por página",
			"sInfo" : "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
			"sInfoEmpty" : "Mostrando registros del 0 al 0 de un total de 0 registros",
			"sInfoFiltered" : "(filtrado de un total de _MAX_ registros)",
			"sSearch" : "Filtrar:",
			"sLoadingRecords" : "Cargando...",
			"oPaginate" : {
				"sFirst" : "<i class='fa fa-angle-double-left'>first_page</i>",
				"sLast" : "<i class='fa fa-angle-double-right'>last_page</i>",
				"sNext" : "<i class='fa fa-angle-right' aria-hidden='false'></i>",
				"sPrevious" : "<i class='fa fa-angle-left' aria-hidden='false'></i>"
			},
		},
		lengthMenu : [ [ 5, 10, 15 ], [ 5, 10, 15 ] ],
		order : [ [ 0, "asc" ] ],
		pageLength : 10
	});

	var myDate = new Date();

	finInput = $('#creationDateFin').pickadate({
		format : 'dd/mm/yyyy',
		formatSubmit : 'dd/mm/yyyy'
	});

	finPicker = finInput.pickadate('picker');

	inicioInput = $('#creationDateInicio').pickadate({
		format : 'dd/mm/yyyy',
		formatSubmit : 'dd/mm/yyyy'
	});

	inicioPicker = inicioInput.pickadate('picker');

	$("#modal-archives").on('shawn.bs.modal', function() {
		$.fn.dataTable.tables({
			visible : true,
			api : true
		}).responsive.recalc();
	});

	$("#aseguradoSiniestro").autocomplete({
		minLength : 3,
		source : $('#txtJSAutoCompletadoAsegurado').val(),
		focus : function(event, ui) {
			$("#aseguradoSiniestro").val(ui.item.nombrepersona);
			return false;
		},
		select : function(event, ui) {

			console.log(ui.item)
			$("#aseguradoSiniestro").val(ui.item.nombre + " " + ui.item.appMaterno + " " + ui.item.appPaterno);
			$('#txtIdAsegurado').val(ui.item.idPersona);
			return false;
		}

	}).autocomplete("instance")._renderItem = function(ul, item) {

		return $("<li class=\"list-group-item\">").append("<div>" + item.nombre + " " + item.appPaterno + " " + item.appMaterno + "</div>").appendTo(ul);
	};

	$(".fecha").each(function() {
		value = $(this).text();
		if (value == "") {
			$(this).text("");
			return;
		}
		value = new Date(parseInt(value.replace("/Date(", "").replace(")/", ""), 10));
		month = value.getMonth() + 1;

		if (month < 9) {
			month = "0" + month;
		}
		$(this).text(value.getDate() + "/" + month + "/" + value.getFullYear())
	});
});

/*fin document ready*/

$("#creationDateInicio").change(function() {
	var fechaIn = $("#creationDateInicio").val();

	$("#creationDateFin").val("");
	finPicker.set('min', inicioPicker.get('select'));

	var nf = fechaIn.split("/");

	var otra = new Date(nf[1] + "/" + nf[0] + "/" + nf[2]);
	otra.setDate(otra.getDate() + 90);

	finPicker.set('max', otra);

});

$("#search-form").submit(function(e) {
	showLoader();
	if (!validaCampos()) {
		e.preventDefault();
	}

});

function validaCampos() {

	return true;
}

function obtieneSiniestro(poliza, asegurado, siniestro, fechaIn, fechaFin) {
	console.log("obtiene siniestro");
	$('#siniestroSeleccionado').text(siniestro);
	showLoader();
	customModalTable.clear().draw();
	/* url: "${infSiniestralidad}", */
	$.ajax({
		url : $('#txtJSInfSiniestralidad').val(),
		type : 'POST',
		data : {
			poliza : poliza,
			asegurado : asegurado,
			siniestro : siniestro,
			fechaIn : fechaIn,
			fechaFin : fechaFin
		},
		success : function(data) {
			var comentario = JSON.parse(data);
			/* $("#tablaSiniestros tbody").html(""); */
			console.log(comentario.lista);
			$.each(comentario.lista, function(i, stringJson) {
				console.log(stringJson);

				customModalTable.row.add(
						[ valida(stringJson.consecutivo), valida(stringJson.cobertura), valida(stringJson.ramo), setCoinFormat(stringJson.monto), valida(stringJson.tipoTransaccion),
								valida(stringJson.Moneda), valida(stringJson.fechaTransaccion), valida(stringJson.estado) ]).draw();

			});

			hideLoader();
			$('#modal-archives').modal('show')
		}
	});
}

function valida(valor) {

	if (typeof valor === 'undefined') {
		return "";
	} else {
		valor = "" + valor;
/*		if (valor.includes("Date")) {*/
	    if (valor.indexOf("Date") != -1  ){
			console.log("calcula la fecha");
			return calculaFecha(valor);
		} else {
			return valor;
		}
	}

}

function calculaFecha(value) {

	value = new Date(parseInt(value.replace("/Date(", "").replace(")/", ""), 10));
	month = value.getMonth() + 1;
	console.log(month);
	if (month < 9) {
		month = "0" + month;
	}
	return value.getDate() + "/" + month + "/" + value.getFullYear();

}

function setCoinFormat(num) {

	if (isNaN(num)) {
		console.log("no es un numero" + num);
		return num;
	}
	var isNegative = '';
	num = "" + num;

/*	if (num.includes('-')){*/
    if (num.indexOf("-") != -1  ){
		isNegative = '-';
		num = num.replace('-', '');
	}
	
	arraySplit = num.split(".");
	izq = arraySplit[0];
	der = "00";
/*	if (num.includes(".")) {*/
    if (num.indexOf(".") != -1  ){
		der = arraySplit[1];
	}
	izq.replace(/ /g, "");
	izq.replace(/$/g, "");
	izq.replace(/,/g, "");

	var izqAux = "";
	var j = 0;
	for (i = izq.length - 1; i >= 0; i--) {
		if (j != 0 && j % 3 == 0) {
			izqAux += ",";
		}
		j++;
		izqAux += izq[i];
	}
	izq = "";
	for (i = izqAux.length - 1; i >= 0; i--) {
		izq += izqAux[i];
	}
	der = der.substring(0, 2);
	if (der.length < 2) {
		der += "0";
	}
	
	return  isNegative + "$" + izq + "." + der;
}


$('#tablaSiniestro').on('draw.dt', function(e) {
	var ultimoPaginador = $(this).siblings().find('li').last().prev();
	if (ultimoPaginador.hasClass('active')){
		generaTablas(
			'#search-form', /* id del formulario a enviar al resources comand */
			$('#txtJSLlenaTablaSiniestro').val(), /* Liga del resources comand */
			'#tablaSiniestro', /* tabla que se le van a agregar los registros */
			listaLlavesJson, /* Lista de nombres de atributos a pegar emn la tabla */ 
			'lista', /* Nombre de la propiedad del json donde estan las listas de objetos a pegar a la tabla */
			btnHtml /* btn */
		);
	}
});


var listaLlavesJson =  [
	{
		"nombre" : "poliza",
		"tipo" : 0,
		"attrCelda" : ""
	},
	{
		"nombre" : "siniestro",
		"tipo" : 0,
		"attrCelda" : ""
	},
	{
		"nombre" : "asegurado",
		"tipo" : 0,
		"attrCelda" : ""
	},
	{
		"nombre" : "fechaSiniestro",
		"tipo" : 1,
		"attrCelda" : ""
	},
	{
		"nombre" : "reservaPendiente",
		"tipo" : 3,
		"attrCelda" : "class=\"number\""
	},
	{
		"nombre" : "montoPagado",
		"tipo" : 3,
		"attrCelda" : "class=\"number\""
	},
	{
		"nombre" : "moneda",
		"tipo" : 0,
		"attrCelda" : ""
	},
	{
		"nombre" : "estatus",
		"tipo" : 0,
		"attrCelda" : ""
	},
	{
		"nombre" : "bienDanado",
		"tipo" : 0,
		"attrCelda" : ""
	},
	{
		"nombre" : "causa",
		"tipo" : 0,
		"attrCelda" : ""
	},
	{
		"nombre" : "fechaReclamo",
		"tipo" : 1,
		"attrCelda" : ""
	},
	{
		"nombre" : "fechaPago",
		"tipo" : 1,
		"attrCelda" : ""
	}
];


var infoHtml = 
	'<div class="actions-container dropleft"> ' +
		'<button type="button" class="btn btn-outline-pink dropdown-menu-right px-3 py-2 waves-effect waves-light" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> ' +
		    '<i class="fa fa-ellipsis-v" aria-hidden="true"></i>' +
		   '</button> ' +
		'<div class="dropdown-menu animated fadeIn">' +
			'<form id="formCargaArchivo¿?" action="' + $("#txtJSInfSiniestralidad").val() + '" method="POST"> ' +
		    	'<a class="dropdown-item" data-id="Siniestro 2" data-toggle="modal" href="#modal-archives" onclick="obtieneSiniestro(\'¿?\', \'¿?\', \'¿?\', \'¿?\', \'¿?\');"> ' +
		    		'<i class="far fa-file-alt mr-2"></i> ' +
		    		'<span>' + $('#txtJSTituloBtnModalDetalle').val() + '</span> ' +
		    	'</a>' +
		    '</form>' +
		'</div>' +
	'</div>';
	
	
	var btnHtml ={
			"requerido" : true,
			"html" :  infoHtml,
			"listaRemplazo" : ["poliza","poliza", "asegurado", "siniestro", "fechaSiniestro", "fechaReclamo"]
	};
	
	