/*for active menu*/
$('.activeIcon li a').click(function() {
  $('li a').removeClass("activeState");
  $(this).addClass("activeState");
  localStorage.setItem('activeState', $(this).parent().index());
});

var ele = localStorage.getItem('activeState');
$('.activeIcon li:eq(' + ele + ')').find('a').addClass('activeState');
