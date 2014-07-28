/*
* Add selected class to dropdown item that has been previously selected
*/

$(".site--select").change(function() {
    window.location = $(this).find("option:selected").val();
});

/* 
 * Desktop Nav Tab Stays Dark When Hovering Over Related Subnav
 */

$("div.dropdown").hover(
    function() {
        $(this).prev("a").addClass("is-active")
    }, function() {
        $(this).prev("a").removeClass("is-active");
    });

/*
* Desktop active class added to main nav item to inicate you are here
*/

$(function() {
 $('nav a[href^="' + location.pathname + '"]').addClass('primary-link-active');
});

