$(document).ready(function(){
    var PARSYS_PATH_SUFFIX = "/jcr:content/post-body-parsys.html";
    var DAILY_CONTENT_LABEL = "Daily Content";
    var NO_VALID_CONTENT_LABEL = "No valid content for today.";
    var WCMMODE_DISABLED = "?wcmmode=disabled";

    var dailyContentContainer = $(".post-article.daily-content");
    var servletPath = dailyContentContainer.data("servletpath"); //path to the pages servlet e.g. /content/cru/us/en/daily-content/jcr:content/content-parsys/daily_content.pages.json
    if (typeof servletPath != 'undefined'){
        var servletResponse = $.ajax({ //synchronous request
            type: "GET",
            url: servletPath,
            async: false
        });

        if(servletResponse.status == 200){
            var paths = servletResponse.responseJSON; //JSON object containing today's, yesterday's and tomorrow's path
            if(paths.contentServletPath){
                var parsysPath = paths.today + PARSYS_PATH_SUFFIX + (Cru.components.DailyContent.elements.isEditMode() ? WCMMODE_DISABLED : "");
                $.get(paths.contentServletPath, function(data){
                    if(!(data instanceof Object)){//if data is a string, we parse it first
                        data = JSON.parse(data);
                    }
                    var header = $("header.daily-content");
                    Cru.components.DailyContent.elements.buildHeader(header, data); //build the header (title, subtitle, etc.)
                    var figure = $(".daily-content figure");
                    Cru.components.DailyContent.elements.buildFigure(figure, data); //build the image
                    $(".daily-content-container").load(parsysPath, function() {
                        //reloads the twitter api for reevaluate twitter widgets.
                        var twitterScript = $("script#twitter-wjs");
                        $.getScript(twitterScript[0].src).fail(function() {
                            console.error("Error reloading twitter platform, src: " + twitterScript[0].src);
                        });
                    }); //get the contents from the article parsys
                    var pagination = $(".grid.pagination");
                    Cru.components.DailyContent.elements.buildPagination(pagination, paths, data);
                });

            } else { //if the content doesn't exist
                $(".post__body.daily-content").html(""); //remove existing HTML (article share component and other stuff)
                if(Cru.components.DailyContent.elements.isEditMode()){
                    $("header.daily-content").append(Cru.components.DailyContent.elements.createTitle(DAILY_CONTENT_LABEL));
                    $("header.daily-content").append(Cru.components.DailyContent.elements.createSubtitle(NO_VALID_CONTENT_LABEL));
                }
            }
        }
    }
});

