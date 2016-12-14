(function() {




  $('#checkin-btn').click(function(){
    $('#popup-wrapper').fadeIn();
  });

  $('#close-btn').click(function() {
    $('#popup-wrapper').fadeOut();
    $('#crguestlbname').val('');
    $('#crguestlboptvalue').val('');
    $("#crguestlbpartof option[value='# of people in your party*']").prop('selected', true);
  });

  var nameField = $('#crguestlbname');
  var phoneEmail = $('#crguestlboptvalue');
  var noOfPeople = $('#crguestlbpartof');
  var noSelected = $('#crguestlbpartof option:selected');
  var nameTT = $('#name-tooltip');
  var phoneTT = $('#phone-tooltip');
  var numberTT = $('#number-tooltip');

  $('#submit-button').click(function() {
    if (!(nameField).val()) {
      nameTT.fadeIn();
    }
    if (!(phoneEmail).val()) {
    	phoneTT.fadeIn();
    }
    /*if ($('#crguestlbpartof').Text() == '# of people in your party*') {
    	numberTT.fadeIn();
    }*/
  });
                            
  nameField.focusin(function() {
  	nameTT.fadeOut();
    nameField.css('border-color', '#808080');
    nameField.attr("placeholder", "Name");
  });

  phoneEmail.focusin(function() {
  	phoneTT.fadeOut();
    phoneEmail.css('border-color', '#808080');
  });

  noOfPeople.focusin(function() {
  	numberTT.fadeOut();
    noOfPeople.css('border-color', '#808080');
  });

  nameField.focusout(function() {
    if (!(nameField).val()) {
    	nameTT.fadeIn();
      nameField.css('border-color', '#f00');
      nameField.attr("placeholder", "Please enter name");
    }
  });

  phoneEmail.focusout(function() {
  	if (!(phoneEmail).val()) {
    	phoneTT.fadeIn();
      phoneEmail.css('border-color', '#f00');
      phoneEmail.attr("placeholder", "Please provide phone # or e-mail");
    }
  });

  noOfPeople.focusout(function() {
  	if ($('#crguestlbpartof option:selected').text() == '# of people in your party*') {
    	numberTT.fadeIn();
      noOfPeople.css('border-color', '#f00');
    }
  });

})();