$(document).ready(function() {
    var $container = $('.tile-container-parsys');
    $container.imagesLoaded( function(){
        setTimeout(function() {
            $container.masonry({
                itemSelector : '.grid__item'
            });
        }, 200);

    });
});