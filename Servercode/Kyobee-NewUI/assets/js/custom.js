/*for active menu*/
$('.activeIcon li a').click(function() {
  $('li a').removeClass("activeState");
  $(this).addClass("activeState");
  localStorage.setItem('activeState', $(this).parent().index());
});

var ele = localStorage.getItem('activeState');
$('.activeIcon li:eq(' + ele + ')').find('a').addClass('activeState');

var expanded = false;
/*checkbox inside dropdown*/
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
/*input mask for mobile number*/
document.getElementById('phone').addEventListener('input', function (e) {
  var x = e.target.value.replace(/\D/g, '').match(/(\d{0,3})(\d{0,3})(\d{0,4})/);
  e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
});
/*multiselect*/
$('.selectpicker').selectpicker();