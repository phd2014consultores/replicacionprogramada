package ve.gob.mercal.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.phdconsultores.ws.exception.ExcepcionServicio;

import ve.gob.mercal.app.models.UsuarioSession;
import ve.gob.mercal.app.services.WsQuery;
import ve.gob.mercal.app.services.WsUsuario;

@Controller
@Scope("request")
public class WebController {
	
	@Autowired
	public WsUsuario wsUsuario;
	
	@Autowired
	public UsuarioSession usuarioSession;
	
	@Autowired
	public WsQuery wsQuery;
	
	/*@Autowired
	public Tienda tienda;*/
	
	/*@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public ModelAndView getWelcomePage(){		
		ModelAndView model = new ModelAndView();
		model.setViewName("welcomePage");
		return model;		
	}*/
	
	@RequestMapping(value = {"/homePageAdmin"}, method = RequestMethod.GET)
	public ModelAndView getHomePageAdmin(){
		ModelAndView model = new ModelAndView();
		
		if(!usuarioSession.isInizializado()){
			usuarioSession.setId(wsUsuario.getId_usuario());
			usuarioSession.setUname(wsUsuario.getUsuario());
			usuarioSession.setUpassword(wsUsuario.getPassword());
			usuarioSession.setUrole(wsUsuario.getTipo_usuario());
			usuarioSession.setInizializado(true);
		}
		
		model.addObject("id", usuarioSession.getId());
		model.addObject("name", usuarioSession.getUname());
		model.setViewName("homePageAdmin");
		return model;
	}
	
	@RequestMapping(value = {"/pageAdmin"}, method = RequestMethod.GET)
	public ModelAndView getPageAdmin(){
		ModelAndView model = new ModelAndView();
		
		model.addObject("id", usuarioSession.getId());
		model.addObject("name", usuarioSession.getUname());
		model.setViewName("pageAdmin");
		return model;
	}
	
	@RequestMapping(value = {"/homePagePub"}, method = RequestMethod.GET)
	public ModelAndView getHomePagePub(){
		ModelAndView model = new ModelAndView();
		
		if(!usuarioSession.isInizializado()){
			usuarioSession.setId(wsUsuario.getId_usuario());
			usuarioSession.setUname(wsUsuario.getUsuario());
			usuarioSession.setUpassword(wsUsuario.getPassword());
			usuarioSession.setUrole(wsUsuario.getTipo_usuario());
			usuarioSession.setInizializado(true);
		}
		
		model.addObject("id", usuarioSession.getId());
		model.addObject("name", usuarioSession.getUname());
		
		model.setViewName("homePagePub");
		return model;
	}
	
	@RequestMapping(value = {"/pagePub"}, method = RequestMethod.GET)
	public ModelAndView getPagePub(){
		ModelAndView model = new ModelAndView();
		
		model.addObject("id", usuarioSession.getId());
		model.addObject("name", usuarioSession.getUname());
		
		model.setViewName("pagePub");
		return model;
	}
	
	@RequestMapping(value = {"/homePageSusc"}, method = RequestMethod.GET)
	public ModelAndView getHomePageSusc(){
		ModelAndView model = new ModelAndView();
		
		if(!usuarioSession.isInizializado()){
			usuarioSession.setId(wsUsuario.getId_usuario());
			usuarioSession.setUname(wsUsuario.getUsuario());
			usuarioSession.setUpassword(wsUsuario.getPassword());
			usuarioSession.setUrole(wsUsuario.getTipo_usuario());
			usuarioSession.setInizializado(true);
		}
		
		model.addObject("id", usuarioSession.getId());
		model.addObject("name", usuarioSession.getUname());
		
		model.setViewName("homePageSusc");
		return model;
	}
	
	@RequestMapping(value = {"/pageSusc"}, method = RequestMethod.GET)
	public ModelAndView getPageSusc(){
		ModelAndView model = new ModelAndView();
		
		model.addObject("id", usuarioSession.getId());
		model.addObject("name", usuarioSession.getUname());
		
		model.setViewName("pageSusc");
		return model;
	}
	
	@RequestMapping(value = {"/loginPage"}, method = RequestMethod.GET)
	public ModelAndView getLoginPage(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout
			){
		
		ModelAndView model = new ModelAndView();
		
		if(error != null){
			model.addObject("error", "Datos incorrectos");
		}
		
		if(logout != null){
			model.addObject("message", "Cierre de sesion completado");
		}
		model.setViewName("loginPage");
		return model;
	}
	
	
	@RequestMapping(value = {"/homeTienda"}, method = RequestMethod.GET)
	public ModelAndView getHomeTienda(){
		ModelAndView model = new ModelAndView();
		String s = "NULL";
		try {
			s = wsQuery.getConsulta("select * from tiendas;");
		} catch (ExcepcionServicio e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		model.addObject("tienda", s);		
		
		model.setViewName("homeTienda");
		return model;
	}

}
