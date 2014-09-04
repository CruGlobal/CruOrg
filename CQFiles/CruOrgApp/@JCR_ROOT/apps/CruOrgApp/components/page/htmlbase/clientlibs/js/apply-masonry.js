$(document).ready(function() {
    if(typeof CQ === 'undefined') { // only when WCM Mode is disabled
        $('.masonry-container').masonry({
            columnWidth: 1,
            itemSelector: '.masonry__item',
			stamp: ".masonry__ignore", //stamp is something masonry ignores
        }).imagesLoaded(function() {
            $('.masonry-container').masonry(); // parsys
        });
    }
});