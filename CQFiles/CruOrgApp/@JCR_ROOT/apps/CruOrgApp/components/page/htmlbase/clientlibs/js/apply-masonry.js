$(document).ready(function() {
    if(typeof CQ === 'undefined') { // only when WCM Mode is disabled
        $('.tile-masonry').masonry({
            columnWidth: 1,
            itemSelector: '.grid__item'
        }).imagesLoaded(function() {
            $('.tile-masonry').masonry(); // parsys
        });
    }
});