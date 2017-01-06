/*
 * Template Name: CalAg - Custom Script
 * Developer: Jaimin 

*/


    /* Preloader */

		$(window).on('load', function() { // makes sure the whole site is loaded 
			$('#status').fadeOut(); // will first fade out the loading animation 
            $('#preloader').delay(3000).fadeOut('slow'); // will fade out the white DIV that covers the website. 
            $('body').delay(3000).css({'overflow':'visible'});
		})

   /* Nav tp fix  */

/*        
var offset = $( ".header-bg" ).offset();
var sticky = document.getElementById("header-bg")

$(window).scroll(function() {
    
    if ( $('body').scrollTop() > offset.top){
        $('.header-bg').addClass('nav-fix');
    } else {
         $('.header-bg').removeClass('nav-fix');
    }
    

});
  
  */ 
        
        
        
  
$(window).load(function(){
  $("#mynav").sticky({ topSpacing: 0 });

});

    
    
$(document).ready( function() {
  $('.respmenu').addClass( 'navbar-toggle-mob' );
} );
  
/*
        
$(document).ready(function(){        
  $(window).scroll(function() {
    var myNav = document.getElementById('mynav');
    if (document.body.scrollTop >= 100 ) {
            myNav.classList.add("nav-fix");
            myNav.classList.remove("nav-top-fix");
            $(".head-contact").css("display", "none");
        } 
        else {
            myNav.classList.add("nav-top-fix");
            myNav.classList.remove("nav-fix");
            $(".head-contact").css("display", "block");
        }
  });
});


*/




/*********************** Number Count in pluse Minues ***********************/








/******Back Bottom to top Script********/

$(document).ready(function(){

$(function(){
 
    $(document).on( 'scroll', function(){
 
    	if ($(window).scrollTop() > 100) {
			$('.scroll-top-wrapper').addClass('show');
		} else {
			$('.scroll-top-wrapper').removeClass('show');
		}
	});
 
	$('.scroll-top-wrapper').on('click', scrollToTop);
});
 
function scrollToTop() {
	verticalOffset = typeof(verticalOffset) != 'undefined' ? verticalOffset : 0;
	element = $('body');
	offset = element.offset();
	offsetTop = offset.top;
	$('html, body').animate({scrollTop: offsetTop}, 500, 'linear');
}

});
