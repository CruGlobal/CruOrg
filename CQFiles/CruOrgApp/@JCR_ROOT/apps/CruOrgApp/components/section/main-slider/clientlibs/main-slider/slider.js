$(document).ready(function(){


    var interval = setInterval(slideForward, 50000000); // Animation delay interval
    var sliding = false; // Used to prevent stacking animation functions
    var window_focus = true; // Used to check for window focus before animation runs

    /*
     * When browser window loses focus stop animation from firing to prevent queue up issues
     */
    $(window).focus(function() {
        window_focus=true;
    }).blur(function() {
        window_focus=false;
    });


    /*
     * Forward movement
     */
    function slideForward() {
        if(!sliding){
            if(window_focus){
                sliding = true;
                $('.slider').removeClass('slider__reverse');
                $('.slider1').addClass('slider0').removeClass('slider1');
                $('.slider2').addClass('slider1').removeClass('slider2');
                $('.slider3').addClass('slider2').removeClass('slider3');
                $('.slider4').addClass('slider3').removeClass('slider4');
                if (slide_count === 4) {
                    $('.slider0').addClass('slider5').removeClass('slider0');
                    $('.slider5').addClass('slider4').removeClass('slider5');
                }
                if (slide_count === 5) {
                    $('.slider5').addClass('slider4').removeClass('slider5');
                    $('.slider0').addClass('slider5').removeClass('slider0');
                }
                if (slide_count === 6) {
                    $('.slider5').addClass('slider4').removeClass('slider5');
                    $('.slider6').addClass('slider5').removeClass('slider6');
                    $('.slider0').addClass('slider6').removeClass('slider0');
                }
                if (slide_count === 7) {
                    $('.slider5').addClass('slider4').removeClass('slider5');
                    $('.slider6').addClass('slider5').removeClass('slider6');
                    $('.slider7').addClass('slider6').removeClass('slider7');
                    $('.slider0').addClass('slider7').removeClass('slider0');
                }
                if (slide_count === 8) {
                    $('.slider5').addClass('slider4').removeClass('slider5');
                    $('.slider6').addClass('slider5').removeClass('slider6');
                    $('.slider7').addClass('slider6').removeClass('slider7');
                    $('.slider8').addClass('slider7').removeClass('slider8');
                    $('.slider0').addClass('slider8').removeClass('slider0');
                }


                setTimeout(function(){sliding=false;}, 1200);
            }
        }
    }

    /*
     * Reverse movement
     */
    function slideReverse() {
        if(!sliding){
            sliding = true;
            if (slide_count === 8) {
                $('.slider8').addClass('slider0').removeClass('slider8');
                $('.slider7').addClass('slider8').removeClass('slider7');
                $('.slider6').addClass('slider7').removeClass('slider6');
                $('.slider5').addClass('slider6').removeClass('slider5');
                $('.slider4').addClass('slider5').removeClass('slider4');
            }
            if (slide_count === 7) {
                $('.slider7').addClass('slider0').removeClass('slider7');
                $('.slider6').addClass('slider7').removeClass('slider6');
                $('.slider5').addClass('slider6').removeClass('slider5');
                $('.slider4').addClass('slider5').removeClass('slider4');
            }
            if (slide_count === 6) {
                $('.slider6').addClass('slider0').removeClass('slider6');
                $('.slider5').addClass('slider6').removeClass('slider5');
                $('.slider4').addClass('slider5').removeClass('slider4');
            }
            if (slide_count === 5) {
                $('.slider5').addClass('slider0').removeClass('slider5');
                $('.slider4').addClass('slider5').removeClass('slider4');
            }
            if (slide_count === 4) {
                $('.slider4').addClass('slider0').removeClass('slider4');
            }

            $('.slider3').addClass('slider4').removeClass('slider3');
            $('.slider2').addClass('slider3').removeClass('slider2');
            $('.slider1').addClass('slider2').removeClass('slider1');
            $('.slider0').addClass('slider1').removeClass('slider0');

            setTimeout(function(){sliding=false;}, 1200);
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
    var slide_count = ($( ".slider li" ).length);
    //alert(slide_count);

    /*
     * Add slider# class to slides, starting with 1
     */

    $( ".slider ul li" ).addClass(function( index ) {
        return "slider" + (index+1);
    });

});
