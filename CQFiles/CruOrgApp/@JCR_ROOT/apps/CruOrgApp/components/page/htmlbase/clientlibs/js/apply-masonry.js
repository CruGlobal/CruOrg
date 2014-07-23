$(document).ready(function() {
    if(typeof CQ === 'undefined') { // only when WCM Mode is disabled
        $('.parsys').masonry({
            columnWidth: 1,
            itemSelector: '.grid__item'
        }).imagesLoaded(function() {
            $('.parsys').masonry('reload');
        });
    }
});