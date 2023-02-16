package com.tokio.agentes.siniestros.portlet.command;

import java.io.PrintWriter;
import java.util.List;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.Gson;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.agentes.siniestros.portlet.constans.SiniestrosPortletKeys;
import com.tokio.cotizador.CotizadorService;
import com.tokio.cotizador.Bean.CotizacionResponse;
import com.tokio.cotizador.Bean.SiniestralidadResponse;
import com.tokio.cotizador.Bean.Siniestro;
import com.tokio.cotizador.Bean.SiniestrosResponse;

@Component(
		immediate = true, 
		property = { "javax.portlet.name=" + SiniestrosPortletKeys.PORTLET_NAME,
					 "mvc.command.name=/siniestro/llenaTablaSiniestro" },
		service = MVCResourceCommand.class
)

public class GetSiniestroTableMVCResourceCommand extends BaseMVCResourceCommand{

	@Reference
	private CotizadorService _CotizadorService;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		
		
		HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));

		String poliza = ParamUtil.getString(httpReq, "poliza");
		String asegurado = ParamUtil.getString(httpReq, "asegurado");
		String idAsegurado = ParamUtil.getString(httpReq, "txtIdAsegurado");
		idAsegurado = Validator.isNull(asegurado) ? "" : idAsegurado;  
		String fechaIn = ParamUtil.getString(httpReq, "inicio");
		String fechaFin = ParamUtil.getString(httpReq, "fin");
		String agente = ParamUtil.getString(httpReq, "agente");
		String siniestro = ParamUtil.getString(httpReq, "endoso");
		ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String usuario = themeDisplay.getUser().getFullName();
		int rowNum = ParamUtil.getInteger(resourceRequest, "rowNum");
		String tipTransaccion = "B";
		String pantalla = "Siniestros";
		
		try{
			SiniestrosResponse respuesta = _CotizadorService.getSiniestros(rowNum, tipTransaccion, agente, poliza, idAsegurado, siniestro, fechaIn, fechaFin, usuario, pantalla);
			
			if( respuesta.getCode() == 0 ){
				Gson gson = new Gson();
				String stringJson = gson.toJson(respuesta);
				resourceResponse.getWriter().write(stringJson);
				SessionMessages.add(resourceRequest, "consultaExitosa");
			}else{
				String jsonString = "{ \"code\" : " + respuesta.getCode() + ", \"msj\" : \"" + respuesta.getMsg() + "\"}"; 
				PrintWriter writer = resourceResponse.getWriter();
				writer.write(jsonString);
			}
		}catch(Exception e){
			e.printStackTrace();
			String jsonString = "{ \"code\" : 5, \"msj\" : \"Error al consultar la información\"}"; 
			PrintWriter writer = resourceResponse.getWriter();
			writer.write(jsonString);
		}	
		
	}

}
