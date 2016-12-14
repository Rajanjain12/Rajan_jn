var gCurrentIndex = 0; 
var ACCORDION_PANEL_COUNT = 3;
var wizard;

$jquery(document).ready(function(){ 
	  
	wizard = $jquery("#accordion").accordion({
       event: false,
       active: 0,
       autoheight: true,
       animated: "bounceslide",
       icons: { 'header': 'ui-icon-plus', 'headerSelected': 'ui-icon-minus' },
       change: function (event, ui) { gCurrentIndex = $jquery(this).find("h3").index(ui.newHeader[0]); }
       
	});

	setupLeftMenu();
	setSidebarHeight();
		
	updateActiveMenuItem('#spnAccnts');
	updateSecondaryMenuItem();
	
	 
});

function changeAccdn(navVar, cnt){
	
	var index = 0;
	if(!cnt) cnt= 1;
	
	for(var i=1; i<=cnt; i++){
		if(navVar=='next'){
			 index = gCurrentIndex + 1+index;
	       if (index > ACCORDION_PANEL_COUNT ) {
	               index = ACCORDION_PANEL_COUNT;
	       }
		}
		else {
	      index = gCurrentIndex - 1-index;
	      if (index < 0) {
	             index = 0;
	 		}
	 }
	}
	
  wizard.accordion("activate", index);
}

function addPlanPaymentDataValidation(checkedAttr) {
	console.log('CheckedAttr: '+checkedAttr);
	
	
	if(checkedAttr){
		
		$jquery("#planPaymentDetailForm").validate({
		  debug : true,
		  rules: {
		      "planPaymentDetailForm:expDate": { 
			required: true,
		  },
		  
		  "planPaymentDetailForm:ccNo" : {
			  required:true
		  },
		  "planPaymentDetailForm:cvv" : {
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
		
		$jquery("#planPaymentDetailForm\\:expDate").datepicker({
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
		
		 $jquery("#planPaymentDetailForm\\:expDate").focus(function () {
			 $jquery(".ui-datepicker-calendar").hide();
			 $jquery("#ui-datepicker-div").position({
		            my: "center top",
		            at: "center bottom",
		            of: $jquery(this)
		        });
		    });
	}
	
	 
}
function addPaymentValidationAcdn(){
	addPlanPaymentDataValidation($jquery("#planPaymentDetailForm\\:useDiffCard").attr("checked"));
}

function checkValidCardDetails(module){
	if ($jquery("#planPaymentDetailForm").valid()) {
		if(module=='ads'){
			confirmAds2();
		}else{
			submitPlanUpgrade();
		}
		
	}
	document.getElementById('topLink').click();
	return $jquery( "#planPaymentDetailForm").valid();
	
}

function checkUpgradeValidation(){
	
	if ($jquery("#upgradeForm").valid()) {
		startUpgradeFn();
	}
	document.getElementById('topLink').click();
	return $jquery( "#upgradeForm").valid();
		
}

function addNewPlanDataValidation(){
	
	$jquery.validator.addMethod("valueNotEquals", function(selectValue, element) {
		return selectValue != "0";
	}, "Please select a Valid Value");
	
	$jquery("#upgradeForm").validate({
		  debug : true,
	      rules: {
		      "upgradeForm:selectedPlanId": { 
		    	  valueNotEquals: true
		      },
		      
		      "upgradeForm:unitsCount" : {
		    	  required:true
		      },
		      "upgradeForm:unit" : {
		    	  valueNotEquals: true
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

function checkBuyAdValidation(){
	
	if ($jquery("#buyAdForm").valid()) {
		confirmBuyAdsFn();
	}
	document.getElementById('topLink').click();
	return $jquery( "#buyAdForm").valid();
		
}

function buyAdsDataValidation(){
	
	$jquery.validator.addMethod("valueNotEquals", function(selectValue, element) {
		return selectValue != "0";
	}, "Please select a Valid Value");
	
	$jquery("#buyAdForm").validate({
		  debug : true,
	      rules: {
		      "buyAdForm:adsCount": { 
		    	  valueNotEquals: true,
		    	  required: true
		      },
	      },
	      highlight: function(label) {
		   // $jquery(label).closest('.control-group').addClass('error');
		  },
		  success: function(label) {
			  label
		      .addClass('validImg')
		      .closest('.control-group').addClass('success');
		  }
	  });
}

function setUnitsForFreePlan(){
	
	console.log($jquery('#upgradeForm\\:selectedPlanId').val());
	if($jquery('#upgradeForm\\:selectedPlanId').val() == $jquery('#upgradeForm\\:freePlanIdHidden').val()){
		$jquery('#upgradeForm\\:unitsCount').attr('readonly','readonly');
	}
	
}