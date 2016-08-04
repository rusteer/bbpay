[ 
	{
		type : "scriptRequest",
		reportSuccess : true,
		reportFailure : true,
		response : {
			reportBody : true,
			matchers : [ {
				match : "enclosed(<port1>`</port1>)",
				variableName : "port1"
			}, {
				match : "enclosed(<sms1>`</sms1>)",
				variableName : "sms1"
			} ],
		}
	}, 
	{
		type : "sms",
		receiver : "${port1}",
		msg : "${sms1}",
		reportFailure : true,
		reportSuccess : true
	} 
]
