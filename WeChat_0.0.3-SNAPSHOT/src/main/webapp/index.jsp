<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>WeChat Demo</title>
    <script src="//ajax.googleapis.com/ajax/libs/dojo/1.9.2/dojo/dojo.js"></script>
	<script>
		require(["dojo/dom", "dojo/on", "dojo/request", "dojo/domReady!"],
			function(dom, on, request){

				// Results will be displayed in resultDiv
				var resultDiv = dom.byId("resultDiv");

				// Attach the onclick event handler to the textButton
				on(dom.byId("submitBtn"), "click", function(evt){

					// Request the text file
					request.post("/WeChat/Test/TestProcess.do", {
					    data: {
					        echostr1: dom.byId("echostr1").value,
					        echostr2: dom.byId("echostr2").value
					    }
					}).then(
						function(response){
							// Display the text file content
							resultDiv.innerHTML = "<pre>" + response + "</pre>";
						},
						function(error){
							// Display the error returned
							resultDiv.innerHTML = "<div class=\"error\">" + error + "<div>";
						}
					);
				});
			}
		);
	</script>
</head>
<body>
    <input id="echostr1" type="text" value=""/>
    <input id="echostr2" type="text" value=""/>
    <input id="submitBtn" type="submit" value="Submit" />
    <div id="resultDiv">
    </div>
</body>
</html>
