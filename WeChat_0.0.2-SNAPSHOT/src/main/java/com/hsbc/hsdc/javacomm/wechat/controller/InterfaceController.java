package com.hsbc.hsdc.javacomm.wechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/Interface")
public class InterfaceController {

	@RequestMapping(value = "/Process", method = RequestMethod.GET)
	public String processRequest() {
		return "Success";
	}
}
