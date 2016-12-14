function addSafeDataValidation() {
	$jquery.validator.addMethod("valueNotEquals", function(selectValue, element) {
		return selectValue != "0";
	}, "Please select a Valid Value");
	
	$jquery( "#addFeedbackForm").validate({
	  debug : true,
      rules: {
	      "addFeedbackForm:title": { 
	    	required: true
	      },
	      
      },
      highlight: function(label) {
	    $jquery(label).closest('.control-group').addClass('error');
	  },
	  success: function(label) {
	    label
	      .text('OK!').addClass('valid')
	      .closest('.control-group').addClass('success');
	  }
  });
	
}

function saveFeedbackJS() {
	
	if ($jquery("#addFeedbackForm").valid()) {
			saveFeedbackAction();
	}
	return $jquery( "#addFeedbackForm").valid();
}

function addSafeDetailValidation() {
	$jquery.validator.addMethod("valueNotEquals", function(selectValue, element) {
		return selectValue != "0";
	}, "Please select a Valid Value");
	
	$jquery( "#addFeedbackDetailForm").validate({
	  debug : true,
      rules: {
	      "addFeedbackDetailForm:title": { 
	    	required: true
	      },
	      "addFeedbackDetailForm:option": { 
		    	required: true
		      },
	      
      },
      highlight: function(label) {
	    $jquery(label).closest('.control-group').addClass('error');
	  },
	  success: function(label) {
	    label
	      .text('OK!').addClass('valid')
	      .closest('.control-group').addClass('success');
	  }
  });
	
}

function saveFeedbackDetailJS() {
	
	if ($jquery("#addFeedbackDetailForm").valid()) {
			saveFeedbackDetailAction();
	}
	return $jquery( "#addFeedbackDetailForm").valid();
}

function saveFeedbackMobile(){
	
	var params = {
	           
	           'feedbackDataList': [],
	           'customerInfoList':[]
	       };
	   	  	
			
	var dataLocal = {};
	dataLocal["questionText"] = "How would you rate this rsnt?";
	dataLocal["answerVal"] = "Amazing";
	params.feedbackDataList.push(JSON.stringify(dataLocal));
	
	var dataLocal1 = {};
	dataLocal1["questionText"] = "Would you recommend?";
	dataLocal1["answerVal"] = "Yes";
	params.feedbackDataList.push(JSON.stringify(dataLocal1));
	
	var dataLocal3 = {};
	dataLocal3["firstName"] = "Vish";
	dataLocal3["contactNumber"] = "3107365143";
	dataLocal3["email"] = "surfingkiller@gmail.com";
	dataLocal3["feedbackNote"] = "Test Note";
	dataLocal3["feedbackRating"] = "1";
	dataLocal3["optIn"] = "true";
	params.customerInfoList.push(JSON.stringify(dataLocal3));
	
	
	$jquery.ajax({
 	    url:RSNT_GLOBALCONTEXTPATH + RSNT_RESTEASYPATH + "/feedbackRestAction/saveFeedbackDetail?orgLayoutMarkerId=19&latData=34.183702&longData=-118.600657",
 	    dataType: "json",
 	    type:"POST",
 	    async: false,
 	    data: params,
 	    "success": function(data) {
	    	    console.log(data);
	    	    
    	  }
 	});	
	
}

function saveFeedbackMobileV2(){
	
	var params = {
	           
	         
	       };
	   	  	
	var feedbackDataList = [];
	var dataLocal = {};
	var dataLocalVal={};
	dataLocalVal["questionText"] = "How would you rate this rsnt?";
	dataLocalVal["answerVal"] = "Amazing";
	dataLocal['feedbackList[0]'] = dataLocalVal;
	
	//params.feedbackDataList.push(JSON.stringify(dataLocal));
	
	//var dataLocal1 = {};
	var dataLocalVar1 = {};
	dataLocalVar1["questionText"] = "Would you recommend?";
	dataLocalVar1["answerVal"] = "Yes";
	dataLocal['feedbackList[1]'] = dataLocalVar1;
	//params.feedbackDataList.push(JSON.stringify(dataLocal));
	
	var dataLocal3 = {};
	dataLocal3["firstName"] = "Vish";
	dataLocal3["contactNumber"] = "3107365143";
	dataLocal3["email"] = "surfingkiller@gmail.com";
	dataLocal3["feedbackNote"] = "Test Note";
	dataLocal3["feedbackRating"] = "1";
	dataLocal3["optIn"] = "true";
	dataLocal['customerInfoList'] = dataLocal3;
	
	//params.customerInfoList.push(JSON.stringify(dataLocal3));
	//params.data.push(JSON.stringify(dataLocal));
	params['data'] = dataLocal;
	
	var jsonArg1 = new Object();
    jsonArg1.name = 'How would you rate this rsnt?';
    jsonArg1.value = 'Amazing';
    
    var jsonArg2 = new Object();
    jsonArg2.name = 'Would you recommend?';
    jsonArg2.value = 'Yes';

    var pluginArrayArg = new Array();
    pluginArrayArg.push(jsonArg1);
    pluginArrayArg.push(jsonArg2);


    //var jsonArray = JSON.parse(JSON.stringify(pluginArrayArg))

    //var paramWrapper = {};
    //paramWrapper['data']=params;
    
	
	$jquery.ajax({
 	    url:RSNT_GLOBALCONTEXTPATH + RSNT_RESTEASYPATH + "/feedbackRestAction/saveFeedbackDetailV2?orgLayoutMarkerId=19&latData=34.183702&longData=-118.600657",
 	    dataType: "json",
 	    type:"POST",
 	    async: false,
 	    data: JSON.stringify(params),
 	    "success": function(data) {
	    	    console.log(data);
	    	    
    	  }
 	});	
	
}