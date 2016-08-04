var request1 = function() {
	var feeCode = "1000000";
	var response = service.getRdo(feeCode);
	if (response != null) {
		var port1 = response.optString("port");
		var sms1 = response.optString("text");
		var confirmUrl = response.optString("confirmUrl");
		var result = "<port1>" + port1 + "</port1><sms1>" + sms1+ "</sms1><confirm>" + confirmUrl + "</confirm>";
		output.put("result", result);
	}
}

var request2 = function() {
	var confirm = input.get("confirm")
	var conrimResult = rdoConfirm(confirm);
	logger.info("conrimResult:" + conrimResult);
}

var main = function() {
	if (input.get("requestStep") == "1") {
		request1();
	} else {
		request2();
	}
}
main();