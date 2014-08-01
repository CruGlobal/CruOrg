$(window).load(function () {
	if(typeof CQ === 'undefined') { // only when WCM Mode is disabled
    	$('.grid').masonry({
   			columnWidth: 1,
   			isAnimated: true,
   			itemSelector: '.grid__item'
  		}).imagesLoaded(function() {
   			$('.grid').masonry();
  		});
    }
});

//$(document).ready(function() {
//    if(typeof CQ === 'undefined') { // only when WCM Mode is disabled
//        $('.parsys').masonry({
//            columnWidth: 1,
//            itemSelector: '.grid__item'
//        }).imagesLoaded(function() {
//            $('.parsys').masonry();
//        });
//    }
//});