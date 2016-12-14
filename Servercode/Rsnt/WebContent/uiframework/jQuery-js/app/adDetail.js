function addSafeDataValidation() {
	$jquery.validator.addMethod("valueNotEquals", function(selectValue, element) {
		return selectValue != "0";
	}, "Please select a Valid Value");
	
	$jquery.validator.addMethod("tagcheck", function(value, element) { 
		  return value && value.split(/\s+/).length < 8;
		}, "Please input at most 7 words for Title.");
	
	$jquery.validator.addMethod("greaterThan", 
			function(value, element, params) {
				if (!/Invalid|NaN/.test(new Date(value))) {
			        return new Date(value) > new Date($jquery(params).val());
			    }
			    return isNaN(value) && isNaN($jquery(params).val()) 
			        || (parseFloat(value) > parseFloat($jquery(params).val())); 
			},'Must be greater than {0}.');
	
	$jquery( "#addAdForm").validate({
	  debug : true,
      rules: {
	      "addAdForm:title": { 
		    	required: true,
		    	tagcheck: true
		   },
		   "addAdForm:description": { 
		    	required: true,
		   },
		   "addAdForm:originalPriceDetail": { 
		    	number: true
		   },
		   "addAdForm:discountPriceDetail": { 
		    	number: true
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
	
	addDatePickers();
}

function addDatePickers(){
	
	$jquery('#addAdForm\\:startDatePicker').datepicker({
		onClose: function(dateText, inst) {
			console.log(dateText);
			updateStartDate(dateText);
			var startDateTmp = $jquery('#addAdForm\\:startDatePicker').datepicker('getDate');
			if(startDateTmp){
				$jquery('#addAdForm\\:endDatePicker').datepicker("option", 'minDate',startDateTmp);
			}
		}
	});

	$jquery('#addAdForm\\:endDatePicker').datepicker({
		onClose: function(dateText, inst) {
			console.log(dateText);
			updateEndDate(dateText);
		}
	});
	
	$jquery('#addAdForm\\:startTimePicker').timepicker({
		
		timeFormat: "HH:mm",
		
		onClose: function(dateText, inst) {
			console.log(dateText);
			updateStartTime(dateText);
			var startTimeTmp = $jquery('#addAdForm\\:startTimePicker').timepicker('getDate');
			console.log('startTimeTmp: '+startTimeTmp);
			if(startTimeTmp){
				//$jquery('#addAdForm\\:endTimePicker').timepicker('option','hourMin',14);
			}
		}


	});
	$jquery('#addAdForm\\:endTimePicker').timepicker({
		
		timeFormat: "HH:mm",
		
		onClose: function(dateText, inst) {
			console.log(dateText);
			updateEndTime(dateText);
		}


	});
	
}

function validateMandatoryData(){
	
	var isAdValid=true;
	var weeklyCheckValid=false;
	var advalidMsg="";
	$jquery("#customErrMsg").find('.inner').html("");
	$jquery("#customErrMsg").hide();
	$jquery("#richErrMsg").hide();
	addSafeDataValidation();
	if(($jquery("#addAdForm\\:title").val()=="") || ($jquery("#addAdForm\\:description").val()=="")){
		
		advalidMsg='Please enter all Mandatory values.';
		isAdValid = false;
	}
	
	if(($jquery("#addAdForm\\:title").val()!="") && ($jquery("#addAdForm\\:title").val().split(/\s+/).length > 7)){
		advalidMsg='Please input at most 7 words for Title.';
		isAdValid = false;
	}
	
	if(($jquery("#addAdForm\\:description").val()!="") && ($jquery("#addAdForm\\:description").val().length > 500)){
		advalidMsg='Please input at most 500 characters for description.';
		isAdValid = false;
	}
	
	
	if (!$jquery('#addAdForm\\:runTillCredit').attr('checked'))
	{
		if(($jquery("#addAdForm\\:startDatePicker").val()=="") || ($jquery("#addAdForm\\:endDatePicker").val()=="") 
				|| ($jquery("#addAdForm\\:startTimePicker").val()=="") || ($jquery("#addAdForm\\:endTimePicker").val()=="")){
			
			isAdValid=false;
			advalidMsg="Please provide Valid Week Start and End Run details";
		}
		else{
			 // var starDateStr = $jquery("#addAdForm\\:startDatePicker").val().split('/');
			 // var endDateStr = $jquery("#addAdForm\\:endDatePicker").val().split('/');
			 // var starTimeStr = $jquery("#addAdForm\\:startTimePicker").val().split('/');
			 // var endTimeStr = $jquery("#addAdForm\\:endTimePicker").val().split('/');
			  // new Date(year, month [, date [, hours[, minutes[, seconds[, ms]]]]]) // months are 0-based
			  var startDateVar = getDateObj($jquery("#addAdForm\\:startDatePicker").val()+" "+$jquery("#addAdForm\\:startTimePicker").val()); 
			  var endDateVar = getDateObj($jquery("#addAdForm\\:endDatePicker").val()+" "+$jquery("#addAdForm\\:endTimePicker").val()); 
			 
			 if(startDateVar > endDateVar){
				 isAdValid=false;
				 advalidMsg="Start Date cannot be after End Date";
			 }
			 
			  var startTimeVar = $jquery("#addAdForm\\:startTimePicker").val(); 
			  //alert(startTimeVar);
			  var endTimeVar = $jquery("#addAdForm\\:endTimePicker").val(); 
			  //alert(endTimeVar);
			  if(startTimeVar > endTimeVar){
					 isAdValid=false;
					 advalidMsg="Start Time cannot be after End Time";
			  }
		}
		
		
		
		$jquery('div#weeklyRunDiv input[type=checkbox]').each(function() {
			   if ($jquery(this).attr('checked')) {
				   weeklyCheckValid=true;
			   }
		});
	}
	else{
		 weeklyCheckValid=true;
	}
	
	
	if (!isAdValid || !weeklyCheckValid) {
		$jquery("#customErrMsg").find(".inner").html("");
		$jquery("#customErrMsg").show();
		advalidMsg = '<p><span>Error</span><br>'+advalidMsg+'</p>';
		$jquery("#customErrMsg").find(".inner").html(advalidMsg);
		return false;
	}
	return true;
	
}

function getDateObj(dateTimeVar){
	
	var dateReg = /(\d{1,2})\/(\d{1,2})\/(\d{4})\s*(\d{1,2}):(\d{2})\s*(AM|PM)/;
	
    var year, month, day, hour, minute,
        result = dateReg.exec(dateTimeVar);
    if (result) {
        year = +result[3];
        month = +result[1]-1;
        day = +result[2];
        hour = +result[4];
        minute = +result[5];
        if (result[6] === 'pm' && hour !== 12) {
            hour += 12;
        }       
    }
    return new Date(year, month, day, hour, minute);
	
}
function saveAdJS() {
	
	if (validateMandatoryData() && $jquery("#addAdForm").valid()) {
			$jquery("#customErrMsg").find(".inner").html('');
			saveAdAction();
	}
	document.getElementById('topLink').click();
	return $jquery( "#addAdForm").valid();
}


function getBannerImage(val, row) {
	 return '<img  src='+val +' style="width:84px;height:35px;" title="Banner" />';
}