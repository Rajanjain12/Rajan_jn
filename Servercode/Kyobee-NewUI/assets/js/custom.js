/*for active menu*/
$('.activeIcon li a').click(function() {
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
$('#tableColumnModalBtn').click(function() {
  $(this).addClass("active-filter");
});
$('#tableColumnModal').click(function() {
  $('#tableColumnModalBtn').removeClass("active-filter");
});

/*show hide password*/
$(".toggle-password").click(function() {
  $(this).toggleClass("flaticon-eye-1 flaticon-eye");
  var input = $($(this).attr("toggle"));
  if (input.attr("type") == "password") {
    input.attr("type", "text");
  } else {
    input.attr("type", "password");
  }
});

/*hide sidebar*/
$(".sidehide").click(function(){
  $("#sidebar-wrapper").css("animation-name","slid-Left");
  $("#asidetogglebtn").delay(100000).click();
  $("#sidebar-wrapper").css("animation-name","slid-right");
});

/*input mask for mobile number*/
document.getElementById('phone').addEventListener('input', function (e) {
  var x = e.target.value.replace(/\D/g, '').match(/(\d{0,3})(\d{0,3})(\d{0,4})/);
  e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
});
