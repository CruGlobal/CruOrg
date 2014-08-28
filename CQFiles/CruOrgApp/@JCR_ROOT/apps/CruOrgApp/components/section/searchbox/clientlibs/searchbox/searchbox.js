var Cru = (function(v) {
    return v;
}(Cru || {}));
Cru.components = (function(v) {
    return v;
}(Cru.components || {}));


//$(document).ready(function () {
//    // finds and replaces underlines on search scope click
//    $('.searchlink').click(function (element) {
//        $('.searchlink--underline').removeClass('searchlink--underline');
//        $(this).addClass('searchlink--underline');
//
//        var searchlink_id = $(this).attr('id');
//        return false;
//    });
//});


Cru.components.searchbox = {
    searchBoxClass : "form.primary-search",
    init: function(form) {
        //declare vars.
        var pathFields = form.find("ul.primary-search--dropdown li a");
        var searchlink_id = 'cru_org';

        //if (searchlink_id == 'cru_org') {alert(searchlink_id);}

        //validate searchBox exist.
        if (!form) return;
        //at least one configured path.
        if (pathFields.size() === 0) return;

        //declare functions
//        function isInternalLink(searchlink_id) {
//            if (searchlink_id == 'cru_org') {
//                alert('true');
//                return true;
//            } else {
//                alert('false');
//                return false;
//            }
//        }
        //look for an internal of external link and process it with
        //selectors of parameters.
        function processLink(link, searchlink_id) {
            //get the link
            var a = $('<a>', { href:link } )[0];
            var extension = a.pathname.split('.').pop();

            //in case that the pathName doesn't have any extension.
            if (a.pathname === extension) {
                extension = "html";
            }

            var path = a.pathname.replace('.' + extension, '');
            var queryField = getQueryField();


            alert('searchlink_id' + searchlink_id);

            if (queryField.val().length != 0) {
                if (searchlink_id == 'cru_org') {
                    //for internal links and selectors search.
                    return path + "." + queryField.val() + "." + extension;
                } else {
                    return link + "?ssUserText=" + queryField.val() + "&Query=" + queryField.val();
                }
            }
        }

        function submitForm(link) {
            var processedLink = processLink(link, searchlink_id);
            $(location).attr('href', processedLink);
        }

        function pathFieldsClick(e) {
            $('.searchlink--underline').removeClass('searchlink--underline');

            $(this).addClass('searchlink--underline');

            var link = $(this).attr("href");
            var searchlink_id = $(this).attr('id');

            //alert(link);
            //alert(searchlink_id);

            processLink(link, searchlink_id);
            e.preventDefault();
        }
        function submitFormClick(e) {
            var link = e.data.link.href;
            submitForm(link);
            e.preventDefault();
        }
        function getQueryField() {
            return form.find("input.search-field");
        }
        function getQueryParameter() {
            var reParameters = new RegExp("Query=([^&#=]*)");
            var reSelectors = new RegExp("\\.([^\\.&#=]*)\\.html");
            var results = reSelectors.exec(window.location.pathname);

            //prioritizing on selectors,
            results = results || reParameters.exec(window.location.search);
            if (results && results.length > 1) {
                return results[1];
            }

            return "";
        }

        //process
        pathFields.on("click", pathFieldsClick);
        form.on("submit", { "link" : pathFields[0] }, submitFormClick);

        //if can catch a search query parameter, put it in the textfield.
        getQueryField().val(decodeURIComponent(getQueryParameter()));
    }
};

$(Cru.components.searchbox.searchBoxClass).ready(function () {
    Cru.components.searchbox.init($(Cru.components.searchbox.searchBoxClass));
});
