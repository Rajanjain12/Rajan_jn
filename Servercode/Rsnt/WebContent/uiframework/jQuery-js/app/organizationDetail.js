function addSafeDataValidation() {
	$jquery.validator.addMethod("phoneUS", function(phone_number, element) {
	    phone_number = phone_number.replace(/\s+/g, ""); 
		return this.optional(element) || phone_number.length > 9 &&
			phone_number.match(/^(1-?)?(\([2-9]\d{2}\)|[2-9]\d{2})-?[2-9]\d{2}-?\d{4}$/);
	}, "Please specify a valid phone number");
	
	$jquery.validator.addMethod("zipUS", function(value, element) {
	    return /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(value);
	},	 "Please specify a valid US zip code.");
	
	$jquery.validator.addMethod("valueNotEquals", function(selectValue, element) {
		return selectValue != "0";
	}, "Please select a Valid Value");
	
	
	$jquery( "#organizationForm").validate({
	  debug : true,
      rules: {
	     
	      "organizationForm:password": { 
        	required: true
	      },
	      "organizationForm:confirmPassword": { 
	        	required: true,
	        	 equalTo: "#organizationForm\\:password"
		      },
	      "organizationForm:firstName": { 
        	required: true
	      },
	      "organizationForm:lastName": { 
        	required: true
	      },
	      "organizationForm:primaryContact": { 
        	phoneUS: true
	      },
	      "organizationForm:alternateContact": { 
        	phoneUS: true
	      },
	      "organizationForm:email": { 
        	required: true,
        	email: true
	      },
	      "organizationForm:title": { 
	        	required: true
	      },
	      "organizationForm:primaryPhone": { 
	        	required: true,
	        	phoneUS: true
	      },
	      "organizationForm:secondaryPhone": { 
	        	phoneUS: true
	      },
	      "organizationForm:orgEmail": { 
	        	required: true,
	        	email:true
	      },
	      "organizationForm:addLine1": { 
	        	required: true
	      },
	      "organizationForm:city": { 
	        	required: true
	      },
	      "organizationForm:state": { 
	        	required: true
	      },
	      "organizationForm:zip": { 
	        	required: true,
        	    zipUS: true
	      },
	      "organizationForm:country": { 
	        	required: true
	      },
	      "organizationForm:selectedPlanId": { 
	        	valueNotEquals: true
	      },
	      "organizationForm:unit": { 
	        	valueNotEquals: true
	      },
	      "organizationForm:unitsCount": { 
	    	  required: true,
      	    number: true
	      }
      },
      highlight: function(label) {
	    //$jquery(label).closest('.control-group').addClass('error');
	  },
	  success: function(label) {
		  label
	      .addClass('validImg')
	      .closest('.control-group').addClass('success');
	    $jquery("#organizationForm\\:userName").val($jquery("#organizationForm\\:email").val());
	  },
	  messages :{
		  confirmPassword : {
			  equalTo: "password fields have to match"
		  }
	  }
  });
}

function addSafePaymentDataValidation() {
	
	$jquery("#paymentDetailForm").validate({
	  debug : true,
      rules: {
	      "paymentDetailForm:expDate": { 
	    	required: true,
	      },
	      
	      "paymentDetailForm:ccNo" : {
	    	  required:true
	      },
	      "paymentDetailForm:cvv" : {
	    	  required:true
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
	
	$jquery("#paymentDetailForm\\:expDate").datepicker({
	    changeMonth: true,
	    changeYear: true,
	    dateFormat: 'mm-yy',
	    showButtonPanel: false,
	    

        onClose: function(dateText, inst) {
            var month = $jquery("#ui-datepicker-div .ui-datepicker-month :selected").val();
            var year = $jquery("#ui-datepicker-div .ui-datepicker-year :selected").val();
            $jquery(this).val($jquery.datepicker.formatDate('mm-yy', new Date(year, month, 1)));
            setExpDateJS($jquery(this).val());
        },
	
	  });
	
	 $jquery("#paymentDetailForm\\:expDate").focus(function () {
		 $jquery(".ui-datepicker-calendar").hide();
		 $jquery("#ui-datepicker-div").position({
	            my: "center top",
	            at: "center bottom",
	            of: $jquery(this)
	        });
	    });
	 
}

function validateUserId(){
	$jquery('#organizationDiv').find('.DTE_Field_Error').text('');
	$jquery("#organizationDiv").find(".DTE_Field_Error").hide();

	$jquery.ajax({
		
		type: "GET",
        url:  RSNT_GLOBALCONTEXTPATH+RSNT_RESTEASYPATH+"/staffManagerRestAction/checkDuplicateUserId",
        dataType: "json",
        data:  {userId : $jquery("#organizationForm\\:userName").val()},
        async: false,
		success: function(data) {
			
            if (data == '1')
            {
            	console.log('Inside loop for 1');
            	$jquery('#organizationDiv').find('.DTE_Field_Error').text('User Id already exists');
            	$jquery("#organizationDiv").find(".DTE_Field_Error").show();
            	
            	return false;
            }
            else{
            	console.log('Inside loop for 0');
            	return true;
            }
		}
	});
}

function addOrganizationDataValidation(){
	$jquery.validator.addMethod("phoneUS", function(phone_number, element) {
		phone_number = phone_number.replace(/\s+/g, ""); 
		return this.optional(element) || phone_number.length > 9 &&
		phone_number.match(/^(1-?)?(\([2-9]\d{2}\)|[2-9]\d{2})-?[2-9]\d{2}-?\d{4}$/);
	}, "Please specify a valid phone number");

	$jquery.validator.addMethod("zipUS", function(value, element) {
		return /(^\d{5}$)|(^\d{5}-\d{4}$)/.test(value);
	},	 "Please specify a valid US zip code.");

	$jquery( "#organizationForm").validate({
		debug : true,
		rules: {
			"organizationForm:title": { 
				required: true
			},
			"organizationForm:primaryPhone": { 
				required: true,
				phoneUS: true
			},
			"organizationForm:secondaryPhone": { 
				phoneUS: true
			},
			"organizationForm:orgEmail": { 
				required: true,
				email:true
			},
			"organizationForm:addLine1": { 
				required: true
			},
			"organizationForm:city": { 
				required: true
			},
			"organizationForm:state": { 
				required: true
			},
			"organizationForm:zip": { 
				required: true,
				zipUS: true
			},
			"organizationForm:country": { 
				required: true
			},
			highlight: function(label) {
				//$jquery(label).closest('.control-group').addClass('error');
			},
			success: function(label) {
				label
				.addClass('validImg')
				.closest('.control-group').addClass('success');
			}
		}
	});


}

function addOrganizationStep1() {
	
	if($jquery( "#organizationForm").valid()){
		createOrganizationStep1Action();
	}
	document.getElementById('topLink').click();
	return $jquery( "#organizationForm").valid();
	
}

function validateOrgForm(){
	var isAdValid=true;
	var orgvalidMsg="";
	
	if(($jquery("#organizationForm\\:checkInSplFlag").attr('checked'))){
		if(($jquery("#organizationForm\\:checkInSpecialText").val()!="")){
			if (($jquery("#organizationForm\\:checkInSpecialText").val().length > 44)){
				orgvalidMsg='Please input at most 44 characters for Check In Special Note';
				isAdValid = false;
			}
		}
	}

	if (!isAdValid) {
		$jquery("#customErrMsg").find(".inner").html("");
		$jquery("#customErrMsg").show();
		orgvalidMsg = '<p><span>Error</span><br>'+orgvalidMsg+'</p>';
		$jquery("#customErrMsg").find(".inner").html(orgvalidMsg);
		return false;
	}
	return true;

}

function updateOrganizationProfileJS() {
	
	
	
	if(validateOrgForm() && $jquery( "#organizationForm").valid()){
		$jquery("#customErrMsg").find('.inner').html("");
		$jquery("#customErrMsg").hide();
		$jquery("#richErrMsg").hide();
		updateOrganizationProfile();
	}
	return $jquery( "#organizationForm").valid();
	
}

function addOrganizationStep2() {
	if ($jquery("#paymentDetailForm").valid()) {
		createOrganizationStep2ActionScs();
	}
	return $jquery( "#paymentDetailForm").valid();
}
function setUnitsForFreePlan(){
	
	console.log($jquery('#organizationForm\\:selectedPlanId').val());
	if($jquery('#organizationForm\\:selectedPlanId').val() == $jquery('#organizationForm\\:freePlanIdHidden').val()){
		$jquery('#organizationForm\\:unitsCount').attr('readonly','readonly');
	}
	
}







$jquery(document).ready(function(){

	setupLeftMenu();

	setSidebarHeight();
	
	$jquery("#cuisineTypeSelect").multiselect({
		 noneSelectedText: 'Select..',
	     selectedList:5 

	 });
	 $jquery("#cuisineTypeSelect").bind("multiselectclose",function(){
		 var checkedValues = $jquery.map($jquery(this).multiselect("getChecked"), function( input ){
	            return input.value;
	        });
		
	      updateOrgCuisineData(checkedValues.join(','));			
	 });

	 $jquery("#dishTypeSelect").multiselect({
		 noneSelectedText: 'Select..',
	     selectedList:5 

	 });
	 $jquery("#dishTypeSelect").bind("multiselectclose",function(){
		 var checkedValues = $jquery.map($jquery(this).multiselect("getChecked"), function( input ){
	            return input.value;
	        });
		
	      updateOrgDishData(checkedValues.join(','));			
	 });


	 $jquery("#rsntTypeSelect").multiselect({
		 noneSelectedText: 'Select..',
	     selectedList:5 

	 });
	 $jquery("#rsntTypeSelect").bind("multiselectclose",function(){
		 var checkedValues = $jquery.map($jquery(this).multiselect("getChecked"), function( input ){
	            return input.value;
	        });
		
	      updateOrgRsntTypeData(checkedValues.join(','));			
	 });

	 $jquery("#seatingTypeSelect").multiselect({
		 noneSelectedText: 'Select..',
	     selectedList:5 

	 });
	 $jquery("#seatingTypeSelect").bind("multiselectclose",function(){
		 var checkedValues = $jquery.map($jquery(this).multiselect("getChecked"), function( input ){
	            return input.value;
	        });
		
		 updateOrgRsntSeatData(checkedValues.join(','));			
	 });

	 var selectedCuisneVar = $jquery("#organizationForm\\:orgCuisineDataHidden").val();
	 if(selectedCuisneVar){
		 var tempArray = selectedCuisneVar.split(",");
		 for ( i=0; i < tempArray.length; i++ ){
			 $jquery("#cuisineTypeSelect").find('option[value="'+tempArray[i]+'"]').attr("selected", "selected");
		 }

		 $jquery("#cuisineTypeSelect").multiselect("refresh");
		 
		 	
	 }

	 var selectedDishVar = $jquery("#organizationForm\\:orgDishDataHidden").val();
	 if(selectedDishVar){
		 var tempArray = selectedDishVar.split(",");
		 for ( i=0; i < tempArray.length; i++ ){
			 $jquery("#dishTypeSelect").find('option[value="'+tempArray[i]+'"]').attr("selected", "selected");
		 }

		 $jquery("#dishTypeSelect").multiselect("refresh");
		 	
	 }

	 var selectedRsntTypeVar = $jquery("#organizationForm\\:orgRsntTypeDataHidden").val();
	 if(selectedRsntTypeVar){
		 var tempArray = selectedRsntTypeVar.split(",");
		 for ( i=0; i < tempArray.length; i++ ){
			 $jquery("#rsntTypeSelect").find('option[value="'+tempArray[i]+'"]').attr("selected", "selected");
		 }

		 $jquery("#rsntTypeSelect").multiselect("refresh");
		 	
	 }
	 
	 var selectedRsntSeatTypeVar = $jquery("#organizationForm\\:orgRsntSeatTypeDataHidden").val();
	 if(selectedRsntSeatTypeVar){
		 var tempArray = selectedRsntSeatTypeVar.split(",");
		 for ( i=0; i < tempArray.length; i++ ){
			 $jquery("#seatingTypeSelect").find('option[value="'+tempArray[i]+'"]').attr("selected", "selected");
		 }

		 $jquery("#seatingTypeSelect").multiselect("refresh");
		 	
	 }
	 updateActiveMenuItem('#spnAccnts');
	 updateActiveMenuItem('#spnOrg');
});
