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
                $('.slider0').addClass('temp').removeClass('slider0'); // $('.slider1').addClass('slider0').removeClass('slider1');
                $('.slider1').addClass('slider0').removeClass('slider1');
                $('.slider2').addClass('slider1').removeClass('slider2');
                $('.slider3').addClass('slider2').removeClass('slider3');
                if (slide_count === 4) {
                    $('.temp').addClass('slider4').removeClass('temp'); // $('.slider0').addClass('slider5').removeClass('slider0');
                    $('.slider4').addClass('slider3').removeClass('slider4'); // $('.slider5').addClass('slider4').removeClass('slider5');
                }
                if (slide_count === 5) {
                    $('.slider4').addClass('slider3').removeClass('slider4');
                    $('.temp').addClass('slider4').removeClass('temp');
                }
                if (slide_count === 6) {
                    $('.slider4').addClass('slider3').removeClass('slider4');
                    $('.slider5').addClass('slider4').removeClass('slider5');
                    $('.temp').addClass('slider5').removeClass('temp');
                }
                if (slide_count === 7) {
                    $('.slider4').addClass('slider3').removeClass('slider4');
                    $('.slider5').addClass('slider4').removeClass('slider5');
                    $('.slider6').addClass('slider5').removeClass('slider6');
                    $('.temp').addClass('slider6').removeClass('temp');
                }
                if (slide_count === 8) {
                    $('.slider4').addClass('slider3').removeClass('slider4');
                    $('.slider5').addClass('slider4').removeClass('slider5');
                    $('.slider6').addClass('slider5').removeClass('slider6');
                    $('.slider7').addClass('slider6').removeClass('slider7');
                    $('.temp').addClass('slider7').removeClass('temp');
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
                $('.slider7').addClass('temp').removeClass('slider7');
                $('.slider6').addClass('slider7').removeClass('slider6');
                $('.slider5').addClass('slider6').removeClass('slider5');
                $('.slider4').addClass('slider5').removeClass('slider4');
                $('.slider3').addClass('slider4').removeClass('slider3');
            }
            if (slide_count === 7) {
                $('.slider6').addClass('temp').removeClass('slider6');
                $('.slider5').addClass('slider6').removeClass('slider5');
                $('.slider4').addClass('slider5').removeClass('slider4');
                $('.slider3').addClass('slider4').removeClass('slider3');
            }
            if (slide_count === 6) {
                $('.slider5').addClass('temp').removeClass('slider5');
                $('.slider4').addClass('slider5').removeClass('slider4');
                $('.slider3').addClass('slider4').removeClass('slider3');
            }
            if (slide_count === 5) {
                $('.slider4').addClass('temp').removeClass('slider4');
                $('.slider3').addClass('slider4').removeClass('slider3');
            }
            if (slide_count === 4) {
                $('.slider3').addClass('temp').removeClass('slider3'); // $('.slider4').addClass('slider0').removeClass('slider4');
            }

            $('.slider2').addClass('slider3').removeClass('slider2');
            $('.slider1').addClass('slider2').removeClass('slider1');
            $('.slider0').addClass('slider1').removeClass('slider0');
            $('.temp').addClass('slider0').removeClass('temp');

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
        if (index === slide_count-1) {
            return "slider0";
        } else {
            return "slider" + (index+1);
        }
    });
});

//$( ".slider ul li").last().addClass('slider0');
//return "slider" + (index+1);