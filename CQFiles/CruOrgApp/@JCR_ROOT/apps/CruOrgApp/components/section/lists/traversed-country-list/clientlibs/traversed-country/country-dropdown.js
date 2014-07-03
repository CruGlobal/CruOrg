$(function(){
      // bind change event to select
      $('.locations__select select').bind('change', function () {
          var url = $(this).val(); // get selected value
          if (url) { // require a URL
              window.location = url; // redirect
        }
        return false;
    });
});