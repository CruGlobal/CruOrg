//$(document).ready(function(){
    var dailyContentContainer = $(".post-article.daily-content");
    if(Cru.widgets.Util.elementExists(dailyContentContainer)) {
        var servletPath = dailyContentContainer.data("servletpath");
    	if (typeof servletPath != 'undefined'){
			var servletResponse = $.ajax({
    			type: "GET",
    			url: servletPath,
    			async: false
			});
           // debugger;

            if(servletResponse.status == 200){
				var paths = servletResponse.responseJSON;
                if(paths.today){
                    var data = {};
                    if(typeof CQ != 'undefined'){
                        if(CQ.WCM.isEditMode()){
							data.wcmmode = "disabled";
                        }
                    }
                    $.get( paths.today + "/jcr:content/post-body-parsys.html", data, function( data ) {
                        $(".post__body.layout-single-column.daily-content").append(data);
					}, "html");
                }
            }
    	}

//    	debugger;    
    }
//});

