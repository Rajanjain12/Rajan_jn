/*for active menu*/
$('.activeIcon a').click( function() {
  $(this).addClass('active').siblings().removeClass('active');
  // $('.activeIcon li').addClass('border-right').siblings().removeClass('border-right');
});
// $('#sidebar .sidebar-nav a').on('click', function () {
// 	$('#sidebar .sidebar-nav').find('li.active').removeClass('active');
// 	$( this ).parent('li').addClass('active');
// });