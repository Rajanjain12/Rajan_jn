/**
 * 
 */


function openAddStaffDiv(){
	
	$jquery('#addStaff').dialog({
		autoOpen: true,
		height: 500,
	    width: 800,
	    modal: true,
	    show: "clip",
	    hide: "clip",
	    dialogClass: "noTitleStuff"
  		
    });
	document.getElementById('addStaff').style.visibility = "visible";
	$jquery("#addStaff").css("height", "");
}
function getImage(val, row) {
	if(row.access=='1'){
		 return '<img class="editImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/edit.gif" style="width:20px;height:18px;cursor:pointer;" title="Edit" />'
		    + '<img class="deleteImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/delete.gif" style="width:20px;height:18px;cursor:pointer;" title="Toggle Active Status" />';
	}
	else{
		return '<img class="editImage" id=' + val + ' src="'+RSNT_GLOBALCONTEXTPATH +'/uiframework/images/edit.gif" style="width:20px;height:18px;cursor:pointer;" title="View" />';
	    
	}
	
   
}

function addSafeDataValidation() {
	$jquery.validator.addMethod("phoneUS", function(phone_number, element) {
	    phone_number = phone_number.replace(/\s+/g, ""); 
		return this.optional(element) || phone_number.length > 9 &&
			phone_number.match(/^(1-?)?(\([2-9]\d{2}\)|[2-9]\d{2})-?[2-9]\d{2}-?\d{4}$/);
	}, "Please specify a valid phone number");
	$jquery( "#addStaffForm").validate({
	  debug : true,
      rules: {
	      "addStaffForm:userNameEdit": { 
	    	required: true
	      },
	      "addStaffForm:password": { 
        	required: true
	      },
	      "addStaffForm:confirmPassword": { 
	        	required: true,
	        	equalTo: "#addStaffForm\\:password"
		      },
	      "addStaffForm:firstName": { 
        	required: true
	      },
	      "addStaffForm:lastName": { 
        	required: true
	      },
	      "addStaffForm:primaryContact": { 
	    	required: true,
        	phoneUS: true
	      },
	      "addStaffForm:alternateContact": { 
        	phoneUS: true
	      },
	      "addStaffForm:email": { 
        	required: true,
        	email: true
	      }
      },
      highlight: function(label) {
	   // $jquery(label).closest('.control-group').addClass('errorValidation');
	    
	  },
	  success: function(label) {
		  label
	      .addClass('validImg')
	      .closest('.control-group').addClass('success');
	    if($jquery("#addStaffForm\\:email").val()) $jquery("#addStaffForm\\:userNameEdit").val($jquery("#addStaffForm\\:email").val());
	    if($jquery("#addStaffForm\\:emailRd").val()) $jquery("#addStaffForm\\:userNameEdit").val($jquery("#addStaffForm\\:emailRd").val());
	  }
  });
}
function addStaff(mode) {
	if ($jquery("#addStaffForm").valid()) {
		console.log("valid is true");
		if(mode == 'add') {
			addStaffAction();
		} else {
			editStaffAction();
		}
	}
	return $jquery( "#addStaffForm").valid();
}
