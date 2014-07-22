$(document).ready(function() {
    if(typeof CQ === 'undefined') { // only when WCM Mode is disabled
        var $container = $('.parsys');
        $container.imagesLoaded( function(){
            setTimeout(function() {
                $container.masonry({
                    itemSelector : '.grid__item'
                });
            }, 200);
    
        });
    }
});