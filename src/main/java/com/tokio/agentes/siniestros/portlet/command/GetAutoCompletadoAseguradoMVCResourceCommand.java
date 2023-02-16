package com.tokio.agentes.siniestros.portlet.command;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.Gson;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.tokio.agentes.siniestros.portlet.constans.SiniestrosPortletKeys;
import com.tokio.cotizador.CotizadorService;
import com.tokio.cotizador.Bean.PersonaResponse;

@Component(
		immediate = true, 
		property = { "javax.portlet.name=" + SiniestrosPortletKeys.PORTLET_NAME,
					 "mvc.command.name=/siniestro/autoCompletado" },
		service = MVCResourceCommand.class
)

public class GetAutoCompletadoAseguradoMVCResourceCommand extends BaseMVCResourceCommand{
	
	@Reference
	private CotizadorService _CotizadorService;

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {
			
		String nombrecliente= ParamUtil.getString(resourceRequest, "term");
		
		
		int tipo = 1;
		User user = (User) resourceRequest.getAttribute(WebKeys.USER);
		String usuario = user.getScreenName();
		
		String pantalla = SiniestrosPortletKeys.PANTALLA;
		
		PersonaResponse personaRes = null;
		
		personaRes = _CotizadorService.getListaPersonas(nombrecliente, tipo, usuario, pantalla);
		
		Gson gson = new Gson();
		String stringJsonComen = gson.toJson(personaRes.getListaPersona());
		resourceResponse.getWriter().write(stringJsonComen);
	}

}
