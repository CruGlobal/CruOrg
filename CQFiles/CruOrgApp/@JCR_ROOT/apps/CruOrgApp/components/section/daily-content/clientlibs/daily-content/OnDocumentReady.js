$(document).ready(function(){
    var dailyContentContainer = $(".post-article.daily-content");
    if(Cru.widgets.Util.elementExists(dailyContentContainer)) {
        var servletPath = dailyContentContainer.data("servletpath");
        if (typeof servletPath != 'undefined'){
            var servletResponse = $.ajax({
                type: "GET",
                url: servletPath,
                async: false
            });

            if(servletResponse.status == 200){
                var paths = servletResponse.responseJSON;
                console.log("paths: ", paths)
                if(paths.contentServletPath){
                    var wcmModeDisabled = "";
                    if(typeof CQ != 'undefined'){
                        if(CQ.WCM.isEditMode()){
                            wcmModeDisabled = "?wcmmode=disabled";
                        }
                    }
                    var parsysPath = paths.today + "/jcr:content/post-body-parsys.html" + wcmModeDisabled;
                    $.get(paths.contentServletPath, function(data){
                        var header = $("header.daily-content");
                        Cru.components.DailyContent.elements.buildHeader(header, data);
                        var figure = $(".daily-content figure");
                        Cru.components.DailyContent.elements.buildFigure(figure, data);
                    });
                    //debugger;
                    $(".daily-content-container").load(parsysPath);
                } else {
                    $(".post__body.daily-content").html("");
                    $("header.daily-content").append(Cru.components.DailyContent.elements.createTitle("Daily content"));
                    $("header.daily-content").append(Cru.components.DailyContent.elements.createSubtitle("No valid content for today."));

                }
            }
        }
    }
});

