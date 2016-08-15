package com.PublicacionySuscripcion.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {

	@RequestMapping(value = { "/", "/welcome**","/hello" }, method = RequestMethod.GET)
	public ModelAndView welcomePage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Bienvenido al Sistema Centralizado de");
		model.addObject("message", "Publicación y Suscripción de Tiendas ");
		model.setViewName("hello");
		return model;

	}

	@RequestMapping(value = {"/admin**"}, method = RequestMethod.GET)
	public ModelAndView adminPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", " Login Form");
		model.addObject("message", "Pagina protegida!");
		model.setViewName("admin");

		return model;

	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalido username o password!");
		}

		if (logout != null) {
			model.addObject("msg", "Salida de sesion exitoso.");
		}
		model.setViewName("login");

		return model;

	}

}