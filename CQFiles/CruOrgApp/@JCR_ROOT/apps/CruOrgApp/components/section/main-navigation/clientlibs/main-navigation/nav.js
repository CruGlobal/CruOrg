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

$(function () {
    var url = location.pathname; // get current URL
    $('.nav a[href*="' + url + '"]').addClass('primary-link-active-current');
    $('>a',$('.primary-link-active-current').parents('li.nav__item')).addClass('primary-link-active');
});

