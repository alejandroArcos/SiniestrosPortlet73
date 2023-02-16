/**
 * ========================== fechas Versiones a produccion ============================
 * >>>>>>>>> 01/11/2018
 */

package com.tokio.agentes.siniestros.portlet;

/**
 * Liberacion a produccion, 14 septiembre 18
 */

import java.io.IOException;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.PortalUtil;
import com.tokio.cotizador.CotizadorService;
import com.tokio.cotizador.Bean.Persona;

@Component(immediate = true, property = { 
		"com.liferay.portlet.display-category=category.sample", 
		"com.liferay.portlet.instanceable=true", 
		"javax.portlet.display-name=SiniestrosPortlet Portlet",
		"javax.portlet.init-param.template-path=/", 
		"javax.portlet.init-param.view-template=/view.jsp", 
		"javax.portlet.resource-bundle=content.Language", 
		"javax.portlet.security-role-ref=power-user,user",
		"com.liferay.portlet.private-session-attributes=false", 
		"com.liferay.portlet.requires-namespaced-parameters=false",
		"com.liferay.portlet.private-request-attributes=false" }, service = Portlet.class)
public class SiniestrosportletmvcportletPortlet extends MVCPortlet {

	@Reference
	CotizadorService _CotizadorService;

	@SuppressWarnings("unchecked")
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		try {
			
			HttpServletRequest originalRequest = PortalUtil
					.getOriginalServletRequest(PortalUtil.getHttpServletRequest(renderRequest));

			Integer idUsuario = (Integer)originalRequest.getSession().getAttribute("idUsuario");
			List<Persona> catRespAgente = (List<Persona>) originalRequest.getSession().getAttribute("listaAgentes");
			
			//catRespAgente = catRespAgente.subList(0, 1);
			
			int auxTamList = 0;
			if (catRespAgente.size() > 1){
				auxTamList = catRespAgente.size();
			}
			
			
			renderRequest.setAttribute("catRespAgente", catRespAgente);
			renderRequest.setAttribute("auxTamList", auxTamList);

			
			super.doView(renderRequest, renderResponse);

		} catch (Exception e) {
			e.printStackTrace();
			SessionErrors.add(renderRequest, "errorDesconocido");
			SessionMessages.add(renderRequest, PortalUtil.getPortletId(renderRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		}

	}
}