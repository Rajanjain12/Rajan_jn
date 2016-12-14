(function() {

  $('#checkin-btn').click(function(){
	openAddGuestPopup();
  });

  $('#close-btn').click(function() {
	  addPopupReset();
  });

})();

function addPopupReset(){
	  $('#block').fadeOut();
	    $('#popup-wrapper').fadeOut();
	    $("#crguestlbname").val('');
	    $("#crguestlboptvalue").val('');
	    $("#crguestlbpartof option[value='# of people in your party *']").prop('selected', true);
	    
	    $(':checkbox:checked').each(function(i){
	    	var id = this.id;
			if(id == 41){
				$("#"+id).prop('checked', true);
			}else{
				
				$("#"+id).prop('checked', false);
			}
		});
	    
	    $("#crguestlboptin").prop('checked', false);
	    $('#name-tooltip').fadeOut();
	    $('#phone-tooltip').fadeOut();
	    $('#number-tooltip').fadeOut();
	    $("#crguestlbpartof").css('border-color', '#7f7f7f');
	    $("#crguestlboptvalue").css('border-color', '#7f7f7f');
	    $("#crguestlbname").css('border-color', '#7f7f7f');
	    
}
function openAddGuestPopup(){
    $('#popup-wrapper').fadeIn();
     $('#block').fadeIn();
    $(':checkbox:checked').each(function(i){
    	var id = this.id;
		if(id == 41){
			$("#"+id).prop('checked', true);
		}else{
			
			$("#"+id).prop('checked', false);
		}
	});
	}; 