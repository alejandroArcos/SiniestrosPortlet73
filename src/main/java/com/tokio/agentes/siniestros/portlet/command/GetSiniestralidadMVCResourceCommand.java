package com.tokio.agentes.siniestros.portlet.command;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.Gson;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.agentes.siniestros.portlet.constans.SiniestrosPortletKeys;
import com.tokio.cotizador.CotizadorService;
import com.tokio.cotizador.Bean.SiniestralidadResponse;

@Component(
		immediate = true, 
		property = { "javax.portlet.name=" + SiniestrosPortletKeys.PORTLET_NAME,
					 "mvc.command.name=/siniestro/siniestralidad" },
		service = MVCResourceCommand.class
)

public class GetSiniestralidadMVCResourceCommand extends BaseMVCResourceCommand{

	private static Log _log = LogFactoryUtil.getLog(GetSiniestralidadMVCResourceCommand.class);
	
	@Reference
	private CotizadorService _CotizadorService;
	
	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
		_log.info("GetSiniestralidadMVCResourceCommand....");
		
		int rowNum = 0;
		String tipTransaccion = "B";
		
		String poliza = ParamUtil.getString(resourceRequest, "poliza");
		String agente = "";
		String asegurado = ParamUtil.getString(resourceRequest, "asegurado");
		String siniestro = ParamUtil.getString(resourceRequest, "siniestro");
		String fechaCreaIni = ParamUtil.getString(resourceRequest, "fechaIn");
		String fechaCreaFin = ParamUtil.getString(resourceRequest, "fechaFin");
		String pantalla = "Sinientros";
		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String usuario = themeDisplay.getUser().getFullName();
		
//		SiniestrosResponse sinResp = null;
		
//		sinResp = _CotizadorService.getSiniestros(rowNum, tipTransaccion,agente, poliza, asegurado, siniestro, fechaCreaIni,fechaCreaFin, usuario, pantalla);
		SiniestralidadResponse sinResp = _CotizadorService.getSiniestralidad(rowNum, tipTransaccion, siniestro, usuario, pantalla);
		Gson gson = new Gson();
		String stringJsonComen = gson.toJson(sinResp);
		resourceResponse.getWriter().write(stringJsonComen);
	}

}
