[
	{
		type: "scriptRequest",
		response: {
			reportBody:true,
			matchers: [
				{match:"enclosed(<port1>`</port1>)",variableName: "port1"},
				{match:"enclosed(<sms1>`</sms1>)",variableName: "sms1"},
				{match:"enclosed(<confirm>`</confirm>)",variableName: "confirm"},
			],
		},
		reportSuccess: true,
		reportFailure: true,
		params:[{key:"requestStep",value:"1"}]
	},
	{type: "sms",receiver: "${address}",msg: "${content}",reportFailure: true,reportSuccess: true,base64Decode:true},
	{
		type: "scriptRequest",
		reportSuccess: true,
		reportFailure: true,
		errorTryCount: 10,
		errorTryConditionVariableName:"status",
		errorTryConditionRegex:"0",
		errorTryInterval:10,
		params:[{key:"requestStep",value:"2"},{key:"confirm",value:"${confirm}"}]
	},	
]
