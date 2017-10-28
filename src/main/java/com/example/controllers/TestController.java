package com.example.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ModelAndView getTestView() {
		
		return new ModelAndView("testVM");
	}
	
	@RequestMapping(value = "/gems", method = RequestMethod.GET)
	public ModelAndView getAccountView() {
		
		return new ModelAndView("gem");
	}
}
