/*for active menu*/
$(document).on('click', '.activeIcon li a', function (e) {
  $('li a').removeClass("activeState");
  $(this).addClass("activeState");
  localStorage.setItem('activeState', $(this).parent().index());
});
var ele = localStorage.getItem('activeState');
$('.activeIcon li:eq(' + ele + ')').find('a').addClass('activeState');

/*checkbox inside dropdown*/
var expanded = false;
function showCheckboxes() {
  var checkboxes = document.getElementById("checkboxes");
  if (!expanded) {
    checkboxes.className = "showDropdown";
    expanded = true;
  } else {
    checkboxes.className = "hideDropdown";
    expanded = false;
  }
}
/* Filter Button Activate */
$(document).on('click', '#tableColumnModalBtn', function (e) {
  $(this).addClass("active-filter");
});
$('#tableColumnModal').click(function() {
  $('#tableColumnModalBtn').removeClass("active-filter");
});

/*show hide password*/
$(document).on('click', '.toggle-password', function (e) {
$(this).children('i').toggleClass("flaticon-eye-1 flaticon-eye");
var input = $($(this).attr("toggle"));
if (input.attr("type") == "password") {
input.attr("type", "text");
} else {
input.attr("type", "password");
}
});

/*hide sidebar*/
$(document).on('click', '.sidehide', function (e) {
  $("#sidebar-wrapper").css("animation-name","slid-Left");
  $("#asidetogglebtn").delay(100000).click();
  $("#sidebar-wrapper").css("animation-name","slid-right");
});

/*verification code auto focus previous and next*/
function moveOnNext(field) { 
   var id = field.id;
   var arr = id.split("_");
   var incr = parseInt(arr[1]) + 1;
   var nextFieldID = "code_"+ incr;
   if (field.value.length >= field.maxLength) { 
    document.getElementById(nextFieldID).focus(); 
  } 
  else{
    moveOnPrevious(field)
  }
}
function moveOnPrevious(field) { 
  var id = field.id;
  var arr = id.split("_");
  var decr = parseInt(arr[1]) - 1;
  var prviousFieldID = "code_" + decr;
  if (field.value.length <= 0) { 
    document.getElementById(prviousFieldID).focus(); 
  } 
}
