package com.hsbc.hsdc.javacomm.wechat.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		System.out.println(param);
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

	@RequestMapping(value = "/TestString", method = RequestMethod.GET)
	@ResponseBody
	public String testString() {
		return "Test";
	}

	@RequestMapping(value = "/TestProcess", method = RequestMethod.POST)
	@ResponseBody
	public Object processTest(HttpServletRequest request, HttpServletResponse response) {

		Object result = null;
		String echostr1 = request.getParameter("echostr1");
		String echostr2 = request.getParameter("echostr2");

		if (echostr1 != null && "数据".equals(echostr1)) {

			List<String> dataList = new ArrayList<String>();
			dataList.add(echostr1);
			dataList.add(echostr2);
			TestData<String> data = new TestData<String>();
			data.setIdentifier("id");
			data.setItems(dataList);

			result = data;
		} else {
			result = echostr1 + "-" + echostr2;
		}

		return result;
	}
}
