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
  $("#asidetogglebtn").click();
});

/*input mask for mobile number*/
document.getElementById('phone').addEventListener('input', function (e) {
  var x = e.target.value.replace(/\D/g, '').match(/(\d{0,3})(\d{0,3})(\d{0,4})/);
  e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
});
