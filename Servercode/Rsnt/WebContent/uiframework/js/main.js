jQuery(document).ready(function($){
	RSNT_GLOBALCONTEXTPATH = '/Rsnt';
	RSNT_RESTEASYPATH = '/seam/resource/restv1';
	
	//open popup
	$('#go').on('click', function(event){
		event.preventDefault();
		var orgId=$.query.get("orgid");
		 var guest = {};

	      guest.name= $("#crguestlbname").val();
	      guest.organizationID =  orgId;
	      
	      guest.noOfPeople= $("#crguestlbpartof").val();
	    
	     if($('input:radio[name=crguestlbemailopt]:checked').val() == 'email'){
	    	guest.email = $("#crguestlboptvalue").val();
	    	guest.prefType='EMAIL';
		     }else{
		    	guest.prefType='SMS';
		    	guest.sms=$("#crguestlboptvalue").val().replace(/()-/,'');
		    	guest.sms=guest.sms.replace(/\(|\)/g, '');
		    	guest.sms=guest.sms.replace(/ /g, '');
	 		     }
	     		guest.optin =$("#crguestlboptin").is(':checked');
	     		guest.status='CHECKIN';
	     		guest.noOfPeople= $("#crguestlbpartof option:selected" ).val();
	     		console.log(guest);
	    	
	    	
	    	$("#createErrorDiv").empty();
	    		var validation = true;
		    	if(!guest.name && guest.name.length<=0 ){
		    		validation = false;
		    		  $('#name-tooltip').fadeIn();
		    		  $("#crguestlbname").css('border-color', '#f00');
		  	    	  $("#crguestlbname").attr("placeholder", "Please enter name *");
 		    	}
		    	if( guest.noOfPeople == '# of people in your party *' ){
		    		validation = false;
		    		$('#number-tooltip').fadeIn();
 		    	}
		    	if(!$('input:radio[name=crguestlbemailopt]:checked').val() ){
		    		validation = false;
		    		$('#phone-tooltip').fadeIn();
 		    	}
		    	if($("#crguestlboptvalue").val().length<=0){
		    		validation = false;
		    		$('#phone-tooltip').fadeIn();
		    		$("#crguestlboptvalue").css('border-color', '#f00');
					$("#crguestlboptvalue").attr("placeholder", "Please provide phone # or e-mail *");
 		    	}
		    	if(guest.prefType && guest.prefType=='EMAIL' && guest.email && !isValidEmailAddress(guest.email)){  
		    		validation = false;
		    		$('#phone-tooltip').fadeIn();
		    		$("#crguestlboptvalue").css('border-color', '#f00');
					$("#crguestlboptvalue").attr("placeholder", "Please provide valid e-mail *");
 		    	}
		    	if(guest.prefType && guest.prefType=='SMS' && guest.sms && !isValidPhoneNumber(guest.sms)){  
		    		validation = false;
		    		$('#phone-tooltip').fadeIn();
		    		$("#crguestlboptvalue").css('border-color', '#f00');
					$("#crguestlboptvalue").attr("placeholder", "Please provide valid phone *");
 		    	}
		    	if( guest.noOfPeople == 0 ||  ($("#crguestlbpartof").val() == '# of people in your party *') ){ 
		    		validation = false; 
		    		//$("#createErrorDiv").append("<li>Please select # of guests.</li>"); 
			  		$('#number-tooltip').fadeIn();
		    		$("#crguestlbpartof").css('border-color', '#f00');
					$("#crguestlbpartof").attr("placeholder", "Please select # of guests.");
 		    	}

		    	if(validation) {
		    		var selections = [];
 		    		$(':checkbox:checked').each(function(i){
 		    			if(this.id != 'crguestlboptin'){
 		    				selections.push({prefValueId:this.id,prefValue:$(this).val()});
 		    			}
 		    		});
 		    		guest.guestPreferences =selections;
		    		
		    		$("#createErrorDiv").empty();
 		    		$("#createErrorDiv").removeClass("validation");
 		    		$.ajax({
 		               url: RSNT_GLOBALCONTEXTPATH + RSNT_RESTEASYPATH + "/waitlistRestAction/addGuest",
 		               data: JSON.stringify(guest),
 		               dataType: "json",
 		               contentType: "application/json; charset=utf-8",
 		               type: "POST",
 		               async: false,
 		               "success": function(data) {
 		               		//loadWaitlistGuests();
 		               		var twt = data.ORG_GUEST_COUNT;
 		               		var guestRank = data.guestRank;
 		               		$("#totalWaitTime").val(guestRank);
 		               		$("#guestRank").html(parseInt(data.guestRank));
 		               		$("#totalWaitTime").val(twt);
 				            $('#popup-wrapper').fadeOut();
 				   	    	$('#aftersave').fadeIn();
 		               }
 		    		
 		           });
 		    		

		    	}else { 
		    		$("#createErrorDiv").addClass("validation");
		    	}
	});
	
	$("#crguestlbname").focusin(function() {
		$('#name-tooltip').fadeOut();
	  	$("#crguestlbname").css('border-color', '#808080');
	  	$("#crguestlbname").attr("placeholder", "Name *");
	  });

	$("#crguestlboptvalue").focusin(function() {
		  $('#phone-tooltip').fadeOut();
		  $("#crguestlboptvalue").css('border-color', '#808080');
	  });

	$("#crguestlbpartof").focusin(function() {
		$('#number-tooltip').fadeOut();
		$("#crguestlbpartof").css('border-color', '#808080');
	  });

	
	$("#crguestlbname").focusout(function() {
	    if (!$("#crguestlbname").val() && $("#crguestlbname").val().length<=0) {
	    	$('#name-tooltip').fadeIn();
	    	$("#crguestlbname").css('border-color', '#f00');
	    	$("#crguestlbname").attr("placeholder", "Please enter name *");
	    }
	  });

	$("#crguestlboptvalue").focusout(function() {
		if($("#crguestlboptvalue").val().length<=0 || $("#crguestlboptvalue").val() == "(___) ___-____ *" || $("#crguestlboptvalue").val() == "(___) ___-____"){
			$('#phone-tooltip').fadeIn();
			$("#crguestlboptvalue").css('border-color', '#f00');
			$("#crguestlboptvalue").attr("placeholder", "Please provide phone # or e-mail *");
	    }
	  });

	$("#crguestlbpartof").focusout(function() {
	  	if ($("#crguestlbpartof").val() == '# of people in your party *') {
	  		$('#number-tooltip').fadeIn();
	  		$("#crguestlbpartof").css('border-color', '#f00');
	    }
	  });
	
	$('#update-info').on('click', function(event){
		event.preventDefault();
		 var guest = {};
	      guest.name= $("#crguestlbname").val();
	      guest.guestID = $("#guestIdVAL").val();
	      guest.OrganizationID = $("#orgId").val();
	      guest.noOfPeople= $("#crguestlbname").val();
	     if($('input:radio[name=crguestlbemailopt]:checked').val() == 'email'){
	    	guest.email = $("#crguestlboptvalue").val();
	    	guest.prefType='EMAIL';
		     }else{
		    	 	guest.prefType='SMS';
			    	guest.sms=$("#crguestlboptvalue").val().replace(/()-/,'');
			    	guest.sms=guest.sms.replace(/\(|\)/g, '');
			    	guest.sms=guest.sms.replace(/ /g, '');
	 		     }
	     guest.optin =$("#crguestlboptin").is(':checked');
	      guest.status='CHECKIN';
	    	guest.noOfPeople= $("#crguestlbpartof option:selected" ).val();
	    	console.log(guest);
	    	
	    	$("#createErrorDiv").empty();
    		var validation = true;
	    	if(!guest.name && guest.name.length<=0 ){
	    		validation = false;
	    		$("#createErrorDiv").append("<li>Please enter name.</li>");
	    		$("#crguestlbname").addClass('input-error');
		    	}
	    	if(!guest.noOfPeople && guest.noOfPeople.length<=0 ){
	    		validation = false;
	    		$("#createErrorDiv").append("<li>Please select # of guests.</li>");
	    		$("#crguestlbpartof").addClass('input-error');
		    	}
	    	if(!$('input:radio[name=crguestlbemailopt]:checked').val() ){
	    		validation = false;
	    		$("#createErrorDiv").append("<li>Please select SMS or E-mail.</li>");
	    		$("#crguestlboptvalue").addClass('input-error');
		    	}
	    	if($("#crguestlboptvalue").val().length<=0){
	    		validation = false;
	    		$("#createErrorDiv").append("<li>Please provide SMS or E-mail.</li>");
	    		$("#crguestlboptvalue").addClass('input-error');
		    	}
	    	if(guest.prefType && guest.prefType=='EMAIL' && guest.email && !isValidEmailAddress(guest.email)){  
	    		validation = false;
	    		$("#createErrorDiv").append("<li>Please provide valid E-mail.</li>");
	    		$("#crguestlboptvalue").addClass('input-error');
		    	}
	    	if(guest.prefType && guest.prefType=='SMS' && guest.sms && !isValidPhoneNumber(guest.sms)){  
	    		validation = false;
	    		$("#createErrorDiv").append("<li>Please provide valid phone.</li>");
	    		$("#crguestlboptvalue").addClass('input-error');
		    	}
	    	console.log(validation);
	    	if(validation) {
	    		$("#createErrorDiv").empty();
		    		$("#createErrorDiv").removeClass("validation");
		    		guest.guestPreferences=[];
		    		$("#crguestlbsearpref option:selected").each(function(){
	 		    		var prefObject = {};
	 		    		prefObject.prefValueId =parseInt($(this).val());
	 		    		prefObject.prefValue = $(this).text();
	 		    		var sameObjectPref = $.grep(selectedPrefObjects, function(item){
	 		    			      return item.prefValueId == prefObject.prefValueId;
	 		    			    });
		    			    if(sameObjectPref && sameObjectPref.length>0){ 
		    			    	prefObject.guestPrefId = sameObjectPref[0].guestPrefId;
 		    			    }
	 		    		guest.guestPreferences.push(prefObject);
 	 		           });
	    		$.ajax({
		    	    url:RSNT_GLOBALCONTEXTPATH + RSNT_RESTEASYPATH + "/waitlistRestAction/updateGuestInfo",
		    	    data: JSON.stringify(guest),
		            dataType: "json",
		            contentType: "application/json; charset=utf-8",
		            type: "POST",
		            async: true,
		    	    "success": function(data) {
		   	    	    console.log(data);
		   	    	 /*PUBNUB_RSNT.publish({
                         channel: 'RSNT_GUEST',
                         message: {"OP":"UPD","guestObj":data.guest.guestID,"FROM":"USER","ppwt":$("#ppwtime").val(),"orgid":$("#orgid").val()}
                     });*/
		   	    	 if (client.getIsConnected() == false) {
					        client.connect(appKey, authToken);
	                     }
	   	    		    var message = JSON.stringify({"OP":"UpdageGuestInfo","guestObj":data.updguest.guestID,"updguest":data.guest,"FROM":"USER","ppwt":$("#ppwtime").val(),"orgid":$("#orgid").val()});
	                    client.send(channel, message);
			            console.log('Sending from updateguest: ' + message + ' to channel: ' + channel);
		   	    	 $.ajax({
				    	    url:RSNT_GLOBALCONTEXTPATH + RSNT_RESTEASYPATH + "/waitlistRestAction/checkinnotify?guestid="+data.guest.guestID,
				    	    dataType: "json",
				    	    type:"GET",
				    	    async: true,
				    	    "success": function(data) {
                                
				       	  }
				    	});
		   	    	 
		   	    	$.ajax({
			    	    url:RSNT_GLOBALCONTEXTPATH + RSNT_RESTEASYPATH + "/waitlistRestAction/waitingListBackupUpdate?guestid="+data.guest.guestID,
			    	    dataType: "json",
			    	    type:"GET",
			    	    async: true,
			    	    "success": function(data) {
			   	    	    
			       	  }
			    	});
		   	    	
		   	    	 $('#aftersave').addClass('is-visible');
		       	  }
		    	});	
	    	}
	    	else { 
	    		$("#createErrorDiv").addClass("validation");
	    	}
	});
	
	//close popup
	$('#aftersave').on('click', function(event){
		if( $(event.target).is('.close-btn') || $(event.target).is('.close-btn') ) {
			event.preventDefault();
  	    	 $('#aftersave').fadeOut(); // Just close the Thank You Message Pop up upon click on OK button
  	    	 //reset the pop up of add guest detail
  	         addPopupReset();
  	         }
	});
	
	
	/*$('#delete').on('click', function(event){
			event.preventDefault();
			$.ajax({
	    	    url:RSNT_GLOBALCONTEXTPATH + RSNT_RESTEASYPATH + "/waitlistRestAction/deleteguest?guestid="+$("#guestIdVAL").val(),
	    	    type:"GET",
	    	    async: false,
	    	    "success": function(data) {
	   	    	    console.log(data);
		   	    	 
	   	    	 PUBNUB_RSNT.publish({
		   	    	    channel: 'RSNT_GUEST',
		   	    	    message: {"OP":"DEL","guestObj":$("#guestIdVAL").val(),"FROM":"USER","ppwt":$("#ppwtime").val(),"orgid":$("#orgid").val() }
		   	    	});
	   	    	 $('#deletemsg').addClass('is-visible');
	       	  }
	    	});	
	});*/
	
	

	$('#delete').on('click', function(event){
			event.preventDefault();
			$(function() {
			$("#delete-confirm").dialog({
			      height: 300,
			      width: 350,
			      modal: true,
			      buttons: {
			        "Yes": function() {
			        	var orgId =  $("#orgIdHidden").val();
						console.log(orgId);
						$.ajax({
							 url:RSNT_GLOBALCONTEXTPATH + RSNT_RESTEASYPATH + "/waitlistRestAction/deleteguest?guestid="+$("#guestIdVAL").val(),
					    	    type:"GET",
					    	    async: false,
					    	    "success": function(data) {
					   	    	    console.log(data);
						   	    	 
					   	    	/* PUBNUB_RSNT.publish({
						   	    	    channel: 'RSNT_GUEST',
						   	    	    message: {"OP":"DEL","guestObj":$("#guestIdVAL").val(),"FROM":"USER","ppwt":$("#ppwtime").val(),"orgid":$("#orgid").val() }
						   	    	});*/
					   	    	 if (client.getIsConnected() == false) {
								        client.connect(appKey, authToken);
				                     }
				   	    		    var message = JSON.stringify( {"OP":"DEL","guestObj":$("#guestIdVAL").val(),"FROM":"USER","ppwt":$("#ppwtime").val(),"orgid":$("#orgid").val() });
				                    client.send(channel, message);
						            console.log('Sending from deleteguest: ' + message + ' to channel: ' + channel);
					   	    	 $('#deletemsg').addClass('is-visible');
					       	  }
				    	});	
						$( this ).dialog( "close" );
			        },
			        "No": function() {
			        	$( this ).dialog( "close" );
			        }
			      }
			    });
			});
	
	});
	
	$('#deletemsg').on('click', function(event){
		if( $(event.target).is('.cd-popup-close') || $(event.target).is('.cd-popup') ) {
			event.preventDefault();
			$(this).removeClass('is-visible');
			 location.reload();
		}
	});
	$('#expired').on('click', function(event){
		if( $(event.target).is('.cd-popup-close') || $(event.target).is('.cd-popup') ) {
			event.preventDefault();
			$(this).removeClass('is-visible');
			 location.reload();
		}
	});
	
	//close popup when clicking the esc keyboard button
	$(document).keyup(function(event){
    	if(event.which=='27'){
    		$('.cd-popup').removeClass('is-visible');
	    }
    });
	
	var tockeniD=$.query.get("tid");
	var guest;
	var selectedPrefValues = [];
	var selectedPrefObjects = [];
	if(tockeniD){ 
		$.ajax({
    	    url:RSNT_GLOBALCONTEXTPATH + RSNT_RESTEASYPATH + "/waitlistRestAction/guestuuid?uuid="+tockeniD,
    	    type:"GET",
    	    async: false,
    	    "success": function(data) {
   	    	    console.log(data);
   	    	    if(data.guests){ 
   	    	    	guest = data.guests;
   	    	    	loadSeatPrefLookups(guest.organizationID);
   	    	    	$("#orgid").val(guest.organizationID);
   	    	    	$.ajax({
   	    	 	    url:RSNT_GLOBALCONTEXTPATH + RSNT_RESTEASYPATH + "/waitlistRestAction/usermetricks?guest="+guest.guestID+"&orgid="+guest.organizationID,
   	    	 	    dataType: "json",
   	    	 	    type:"GET",
   	    	 	    async: false,
   	    	 	    "success": function(data) {
   	    	     	    console.log(data);
   	    	 	    	 $("#numberofparties").html(data.GUEST_AHEAD_COUNT);
   	    	 	    	  $("#totalWaitTime").html(data.TOTAL_WAIT_TIME);
   	    	 	    	  $("#ppwtime").val(data.ORG_WAIT_TIME);
   	    	 	    	  var twt = data.TOTAL_WAIT_TIME;
   	    	 	    	  console.log(twt);
   	    		    	 var h = Math.floor(twt/60);	
   	    	 			 var hour = h.toString().length == 1 ? (0+h.toString()) : h ;
   	    	 			  var m = twt%60;
   	    		    	  var min = m.toString().length == 1 ? (0+m.toString()) : m ;
   	    	   	          $("#hour").html(hour);
   	    		    	  $("#min").html(min);
   	    		    	  $("#ppwtime").val(data.ORG_WAIT_TIME);
   	    				  $("#guestRankMin").html(data.GUEST_RANK_MIN);
   	    	    	  }
   	    	 	});
   	    	    	
   	 		    	$("#guestIdVAL").val(guest.guestID);
   	 		    $("#crguestlbname").val(guest.name);
   	 		$("#crguestlbpartof" ).val(guest.noOfPeople);
	 		     if(guest.prefType.toLocaleLowerCase() == 'email'){
	 		    	$("#crguestlboptvalue").val(guest.email);
	 		    	 $("input[name='crguestlbemailopt'][value='email']").attr("checked", true);
		 		     }else{
		 		    	$("#crguestlboptvalue").val(guest.sms);
		 		    	 $("input[name='crguestlbemailopt'][value='sms']").attr("checked", true);
			 		     }
	 		     console.log(guest.optin);
	 		     //alert(guest.optin);
	 		     if(guest.optin){
	 		    	$("#crguestlboptin").attr( 'checked', true );
	 		     }
	 		    
	 		    if(data.guests.guestPreferences) { 
						
						$(data.guests.guestPreferences).each(function(index,value){
							selectedPrefValues.push(value.prefValueId);
							selectedPrefObjects.push(value);
	 	 		           });
						$("#crguestlbsearpref").val(selectedPrefValues);
		 		    }
	 		    $("#crguestlbsearpref").multiselect({
	  				 noneSelectedText: 'Select..',
	  			     selectedList:5 

	  			 });
	  	 		 $("#crguestlbsearpref").multiselect("refresh");
   	    	    }else{
   	    	    	//document.getElementById("expired").click();
   	    	    	
   	    	     $('#expired').addClass('is-visible');
   	    	    	
   	    	    }
       	  }
    	});
	}
});