package com.hsbc.hsdc.javacomm.wechat.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hsbc.hsdc.javacomm.wechat.bean.TestData;

@Controller
@RequestMapping("/Test")
public class TestController {

	@RequestMapping(value = "/TestConnection", method = RequestMethod.GET)
	public String testConnection() {
		System.out.println("testConnection");
		return "Success";
	}

	@RequestMapping(value = "/TestRequest", method = RequestMethod.GET)
	public ModelAndView testRequest(@RequestParam("param") String param) {
		String viewName = "Request";
		return new ModelAndView(viewName, "param", param);
	}

	@RequestMapping(value = "/TestRedirect", method = RequestMethod.GET)
	public String testRedirect() {
		return "redirect:/Test/TestRequest.do?param=TestRedirect";
	}

	@RequestMapping(value = "/TestForward", method = RequestMethod.GET)
	public String testForward() {
		return "forward:/Test/TestRequest.do?param=TestForward";
	}
	
    @RequestMapping(value = "/TestData", method = RequestMethod.GET)
    @ResponseBody
    public TestData<String> testData() {
        
        List<String> dataList = new ArrayList<String>();
        dataList.add("ABC");
        dataList.add("abc");
        TestData<String> data = new TestData<String>();
        data.setIdentifier("id");
        data.setItems(dataList);
        
        return data;
    }
}

