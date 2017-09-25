package de.greyshine.spielwiese.thymeleaf.springmvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationController {
	
	@Autowired
	private SomeService service;
	
	@RequestMapping("/thymeleaf.do")
	public String thymeleafDo( HttpServletRequest inReq,  Model inModel) {
		
		inModel.addAttribute("name", "World");
		inModel.addAttribute( "persons", service.getPersons());
		inModel.addAttribute( "items", service.getItems());		
		
		// other examples run with different ITemplateResolver
		return "templates/thymeleafed.html";
	}
}
