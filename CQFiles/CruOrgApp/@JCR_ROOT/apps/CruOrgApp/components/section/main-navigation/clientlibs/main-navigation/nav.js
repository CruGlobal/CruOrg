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
  	