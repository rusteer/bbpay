var request1 = function() {
	var staticCmd = "MzCjSdNVI0ZQIKfjMdQ=";
	var key = "ABCJHZQ1Y3K4P5627NKFZ9TCACSFERH95M419831001DGH7R"
	var response = service.getComicDynamicCmd(staticCmd, key);
	logger.info("getComicDynamicCmd result:" + response);
	if (response != null) {
		var port1 = "1065843110";
		var sms1 = response;
		var result = "<port1>" + port1 + "</port1><sms1>" + sms1 + "</sms1>";
		output.put("result", result);
	}
}
request1();