
$jquery(document).ready(function(){ 
	  
	 addSafeDataValidation();
	 
	  $jquery("#alertTypeSelect").multiselect({
			 noneSelectedText: 'Select..',
		     selectedList:5 

	 });

	 $jquery("#alertTypeSelect").bind("multiselectclose",function(){
		 var checkedValues = $jquery.map($jquery(this).multiselect("getChecked"), function( input ){
	            return input.value;
	        });
		
		 updatePlanAlertData(checkedValues.join(','));			
	 });
	
	 $jquery("#reportTypeSelect").multiselect({
		 noneSelectedText: 'Select..',
	     selectedList:5 

	 });

	 $jquery("#reportTypeSelect").bind("multiselectclose",function(){
		 var checkedValues = $jquery.map($jquery(this).multiselect("getChecked"), function( input ){
	            return input.value;
	        });
		
		 updatePlanReportData(checkedValues.join(','));			
	 });
	
	 
	 var selectedAlertVar = $jquery("#addPlanForm\\:planAlertDataHidden").val();
	 if(selectedAlertVar){
		 var tempArray = selectedAlertVar.split(",");
		 for ( i=0; i<tempArray.length; i++ ){
			 $jquery("#alertTypeSelect").find('option[value="'+tempArray[i]+'"]').attr("selected", "selected");
		 }

		 $jquery("#alertTypeSelect").multiselect("refresh");
		 	
	 }
	 
	 var selectedReportVar = $jquery("#addPlanForm\\:planReportDataHidden").val();
	 if(selectedReportVar){
		 var tempArray = selectedReportVar.split(",");
		 for ( i=0; i<tempArray.length; i++ ){
			 $jquery("#reportTypeSelect").find('option[value="'+tempArray[i]+'"]').attr("selected", "selected");
		 }

		 $jquery("#reportTypeSelect").multiselect("refresh");
		 	
	 }
	 
	 updateActiveMenuItem('#spnAccnts');
	// updateSecondaryMenuItem();
	
	 
});



function validateMandatoryData() {
	var isCostValid=true;
	
	$jquery("#customErrMsg").find('.inner').html("");
	$jquery("#customErrMsg").hide();
	$jquery("#richErrMsg").hide();
	
	var costvalidMsg="Please provide correct cost and ad details";
	
	if(($jquery("#addPlanForm\\:monthlyCost").val()!="") && ($jquery("#addPlanForm\\:monthlyCurrency").val()=="0")){
		
		isCostValid=false;
	}
	if(($jquery("#addPlanForm\\:monthlyCost").val()!="") && ($jquery("#addPlanForm\\:monthlyPerAd").val()=="")){
		
		isCostValid=false;
	}
	if(($jquery("#addPlanForm\\:monthlyCost").val()!="") && ($jquery("#addPlanForm\\:monthlyFreeAds").val()=="")){
		
		isCostValid=false;
	}
	
	
	/*if(($jquery("#addPlanForm\\:yearlyCost").val()!="") && ($jquery("#addPlanForm\\:yearlyCurrency").val()=="0")){
		
		isCostValid=false;
	}
	if(($jquery("#addPlanForm\\:yearlyCost").val()!="") && ($jquery("#addPlanForm\\:yearlyPerAd").val()=="")){
		
		isCostValid=false;
	}
	if(($jquery("#addPlanForm\\:yearlyCost").val()!="") && ($jquery("#addPlanForm\\:yearlyFreeAds").val()=="")){
		
		isCostValid=false;
	}
	
	if(($jquery("#addPlanForm\\:dailyCost").val()!="") && ($jquery("#addPlanForm\\:dailyCurrency").val()=="0")){
		
		isCostValid=false;
	}
	
	if(($jquery("#addPlanForm\\:dailyCost").val()!="") && ($jquery("#addPlanForm\\:dailyPerAd").val()=="")){
		
		isCostValid=false;
	}
	if(($jquery("#addPlanForm\\:dailyCost").val()!="") && ($jquery("#addPlanForm\\:dailyFreeAds").val()=="")){
	
		isCostValid=false;
	}
	*/
	if($jquery("#addPlanForm\\:monthlyCost").val()==""){
		
		isCostValid=false;
		costvalidMsg= "Please specify cost detail";
	}
	

	if (!isCostValid) {
		$jquery("#customErrMsg").find(".inner").html("");
		$jquery("#customErrMsg").show();
		costvalidMsg = '<p><span>Error</span><br>'+costvalidMsg+'</p>';
		$jquery("#customErrMsg").find(".inner").html(costvalidMsg);
		document.getElementById('topLink').click();
		return false;
		
	}
	return true;

}
function hideAlertDiv(){
	 $jquery("#alertsDiv").hide();
}

function getImage(val) {
    return '<img class="editImage" id=' + val + '  src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/edit.gif" style="width:20px;height:18px;cursor:pointer;" title="Edit" />';
}
function reloadPlanData() {
	planDetailsTable.fnReloadAjax();
}
function addSafeDataValidation() {
	$jquery.validator.addMethod("valueNotEquals", function(selectValue, element) {
		return selectValue != "0";
	}, "Please select a Valid Value");
	
	$jquery( "#addPlanForm").validate({
	  debug : true,
      rules: {
	      "addPlanForm:title1": { 
	    	required: true
	      }
	     
	      
      },
      highlight: function(label) {
	    //$jquery(label).closest('.control-group').addClass('error');
	  },
	  success: function(label) {
		  label
	      .addClass('validImg')
	      .closest('.control-group').addClass('success');
	  }
  });
}
function savePlanJS() {
	
	if ($jquery("#addPlanForm").valid() && validateMandatoryData()) {
			savePlanAction();
	}
	return $jquery( "#addPlanForm").valid();
}


