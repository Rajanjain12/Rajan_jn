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
//Added By Ruchi for removing adding of numbers in field on arrow keys in input type=number        
var number = document.getElementById('number');

  number.onkeydown = function(e) {
     if(!((e.keyCode > 95 && e.keyCode < 106)
       || (e.keyCode > 47 && e.keyCode < 58) 
       || e.keyCode == 8)) {
         return false;
     }
 }
        
  
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

/*
<!-- Initialize the plugin: -->

    $(document).ready(function() {
         $('#example-multiple-selected').multiselect({
            buttonWidth: '100%',
            dropRight: false
        });
    });*/


 $(document).ready(function() {
    $('#example-multiple-selected').multiselect({
      buttonClass: 'btn-multiselect',
      buttonWidth: '100%',
      buttonText: function(options) {
        if (options.length == 0) {
          return 'Select...';
        }
        else if (options.length > 6) {
          return options.length + ' selected  ';
        }
        else {
          var selected = '';
          options.each(function() {
            selected += $(this).text() + ', ';
          });
          return selected.substr(0, selected.length -2) + ' ';
        }
      },
    });
  });




 
$(document).ready(function(){

    $('.clickpopup').click(function(){
        $('#showpopup').simplePopup();
    });
        
});



/*********************** Number Count in pluse Minues ***********************/



$('.btn-number').click(function(e){
    e.preventDefault();
    
    fieldName = $(this).attr('data-field');
    type      = $(this).attr('data-type');
    var input = $("input[name='"+fieldName+"']");
    var currentVal = parseInt(input.val());
    if (!isNaN(currentVal)) {
        if(type == 'minus') {
            
            if(currentVal > input.attr('min')) {
                input.val(currentVal - 1).change();
            } 
            if(parseInt(input.val()) == input.attr('min')) {
                $(this).attr('disabled', true);
            }

        } else if(type == 'plus') {

            if(currentVal < input.attr('max')) {
                input.val(currentVal + 1).change();
            }
            if(parseInt(input.val()) == input.attr('max')) {
                $(this).attr('disabled', true);
            }

        }
    } else {
        input.val(0);
    }
});
$('.input-number').focusin(function(){
   $(this).data('oldValue', $(this).val());
});
$('.input-number').change(function() {
    
    minValue =  parseInt($(this).attr('min'));
    maxValue =  parseInt($(this).attr('max'));
    valueCurrent = parseInt($(this).val());
    
    name = $(this).attr('name');
    if(valueCurrent >= minValue) {
        $(".btn-number[data-type='minus'][data-field='"+name+"']").removeAttr('disabled')
    } else {
        alert('Sorry, the minimum value was reached');
        $(this).val($(this).data('oldValue'));
    }
    if(valueCurrent <= maxValue) {
        $(".btn-number[data-type='plus'][data-field='"+name+"']").removeAttr('disabled')
    } else {
        alert('Sorry, the maximum value was reached');
        $(this).val($(this).data('oldValue'));
    }
    
    
});
$(".input-number").keydown(function (e) {
        // Allow: backspace, delete, tab, escape, enter and .
        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 190]) !== -1 ||
             // Allow: Ctrl+A
            (e.keyCode == 65 && e.ctrlKey === true) || 
             // Allow: home, end, left, right
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        // Ensure that it is a number and stop the keypress
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    });




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
