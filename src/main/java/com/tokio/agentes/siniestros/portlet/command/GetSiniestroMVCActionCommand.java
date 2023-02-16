package com.tokio.agentes.siniestros.portlet.command;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.agentes.siniestros.portlet.constans.SiniestrosPortletKeys;
import com.tokio.cotizador.CotizadorService;
import com.tokio.cotizador.Bean.Siniestro;
import com.tokio.cotizador.Bean.SiniestrosResponse;

@Component(immediate = true, property = { "javax.portlet.name=" + SiniestrosPortletKeys.PORTLET_NAME, "mvc.command.name=/siniestro/buscaSiniestro" }, service = MVCActionCommand.class)

public class GetSiniestroMVCActionCommand extends BaseMVCActionCommand {
	private static Log _log = LogFactoryUtil.getLog(GetSiniestroMVCActionCommand.class);

	@Reference
	private CotizadorService _CotizadorService;

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		// TODO Auto-generated method stub
		_log.info("GetSiniestroMVCActionCommand....");

		HttpServletRequest httpReq = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(actionRequest));

		String poliza = ParamUtil.getString(httpReq, "poliza");
		String asegurado = ParamUtil.getString(httpReq, "asegurado");
		String idAsegurado = ParamUtil.getString(httpReq, "txtIdAsegurado");
		String fechaIn = ParamUtil.getString(httpReq, "inicio");
		String fechaFin = ParamUtil.getString(httpReq, "fin");
		String agente = ParamUtil.getString(httpReq, "agente");
		String siniestro = ParamUtil.getString(httpReq, "endoso");
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String usuario = themeDisplay.getUser().getFullName();
		int rowNum = 0;
		String tipTransaccion = "B";
		String pantalla = "Siniestros";
		System.out.println("idAsegurado : " + idAsegurado);
		
		SiniestrosResponse sinistroResp = null;
		List<Siniestro> tablaSin = null;

		System.out.println("asegurado : " +  asegurado);
		try {
			
			idAsegurado = Validator.isNull(asegurado) ? "" : idAsegurado;  
			
			sinistroResp = _CotizadorService.getSiniestros(rowNum, tipTransaccion, agente, poliza, idAsegurado, siniestro, fechaIn, fechaFin, usuario, pantalla);
			tablaSin = sinistroResp.getLista();
			System.out.println("entre aqui");
			if (tablaSin.size() > 0) {
				actionRequest.setAttribute("tablaSin", tablaSin);
				actionRequest.setAttribute("rowTotalNum", sinistroResp.getRowTotalNum());
				SessionMessages.add(actionRequest, "exitoBusqueda");
			} else {
				String msjError =  sinistroResp.getMsg();
				actionRequest.setAttribute("errorMsg", msjError);
				SessionMessages.add(actionRequest, "exitoConocido");
			}				
		} catch (Exception e) {
			e.getStackTrace();
			SessionErrors.add(actionRequest, "error");
		} finally {
			SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		}

		actionRequest.setAttribute("polizaBus", poliza);
		actionRequest.setAttribute("aseguradoBus", asegurado);
		actionRequest.setAttribute("fechaInBus", fechaIn);
		actionRequest.setAttribute("fechaFinBus", fechaFin);
		actionRequest.setAttribute("agenteBus", agente);
		actionRequest.setAttribute("siniestroBus", siniestro);
		actionRequest.setAttribute("txtIdAsegurado", idAsegurado);

	}

}
