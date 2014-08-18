$(document).ready(function(){
	

		var interval = setInterval(slideForward, 5000);
	  	var sliding = false;
	  	var window_focus = true;
	
		/*
		 * When window loses focus stop animation from firing to prevent queue up issues
		 */

    if ($('html').hasClass('lt-ie9')) {
        alert('less than ie9')
    } else {
        alert('modern browser')
		$(window).focus(function() {
		  window_focus=true;
		}).blur(function() {
		  window_focus=false;
		});
    }

	/*
	 * Forward movement
	 */
	function slideForward() {
		if(!sliding){
			if(window_focus){
				sliding = true;
				$('.slider').removeClass('slider__reverse');
				$('.slider1').addClass('slider0').removeClass('slider1');
				$('.js-slider1').addClass('js-slider0').removeClass('js-slider1');
				$('.slider2').addClass('slider1').removeClass('slider2');
				$('.js-slider2').addClass('js-slider1').removeClass('js-slider2');
				$('.slider3').addClass('slider2').removeClass('slider3');
				$('.js-slider3').addClass('js-slider2').removeClass('js-slider3');
				$('.slider4').addClass('slider3').removeClass('slider4');
				$('.js-slider4').addClass('js-slider3').removeClass('js-slider4');
				$('.slider0').addClass('slider5').removeClass('slider0');
				$('.js-slider0').addClass('js-slider5').removeClass('js-slider0');
				$('.slider5').addClass('slider4').removeClass('slider5');
				$('.js-slider5').addClass('js-slider4').removeClass('js-slider5');
				setTimeout(function(){sliding=false}, 1200);
			}
		}
	}
	
	/*
	 * Reverse movement
	 */
	function slideReverse() {
		if(!sliding){
			sliding = true;

			$('.slider4').addClass('slider5').removeClass('slider4');
			$('.js-slider4').addClass('js-slider5').removeClass('js-slider4');
			$('.slider3').addClass('slider4').removeClass('slider3');
			$('.js-slider3').addClass('js-slider4').removeClass('js-slider3');
			$('.slider2').addClass('slider3').removeClass('slider2');
			$('.js-slider2').addClass('js-slider3').removeClass('js-slider2');
			$('.slider1').addClass('slider2').removeClass('slider1');
			$('.js-slider1').addClass('js-slider2').removeClass('js-slider1');
			$('.slider5').addClass('slider0').removeClass('slider5');
			$('.js-slider5').addClass('js-slider0').removeClass('js-slider5');
			$('.slider0').addClass('slider1').removeClass('slider0');
			$('.js-slider0').addClass('js-slider1').removeClass('js-slider0');
			
			setTimeout(function(){sliding=false}, 1200);
			}
	}



	/*
	 * Pause the slider on hover
	 */
    $('.slider__item').hover(function() {
        clearInterval(interval);
    }, function() {
        interval = setInterval(slideForward, 5000);	
    });
	
    $('.slider-control').hover(function() {
        clearInterval(interval);
    }, function() {
        interval = setInterval(slideForward, 5000);	
    });
	
	
	
	/*
	 * Move the slide forward when the user clicks the next button
	 */
	 $('.slider-control__next').click(function(e) {
		 e.preventDefault();
		 
		 $('.slider').removeClass('slider__reverse');
		 slideForward();
	 });
	
	
	
	/*
	 * Reverse the slider when the user clicks the previous button
	 */
	 $('.slider-control__prev').click(function(e) {
		 e.preventDefault();
		 
		 $('.slider').addClass('slider__reverse');
 		  slideReverse();
	 });

	 /*
	  * Count the slides - debugging purposes only
	  */
		//var slide_count = ($( ".slider li" ).length);
	  	//alert(slide_count);

	 /*
	  * Add slider# class to slides, starting with 1
	  */

	  $( ".slider ul li" ).addClass(function( index ) {
			return "slider" + (index+1);
	  });

	 /*
	  * Add js-slider# class to slides, starting with 1
	  */
	  $( ".slider li" ).addClass(function( index ) {
			return "js-slider" + (index+1);
	  });


});