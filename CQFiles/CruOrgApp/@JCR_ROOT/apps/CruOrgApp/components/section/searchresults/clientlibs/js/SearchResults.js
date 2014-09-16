var Cru = (function(v) {
    return v;
}(Cru || {}));

Cru.components = (function(v) {
    return v;
}(Cru.components || {}));

Cru.components.searchresults = {
    className : "div.primary-search__results",
    refresh: function(link) {
        function injectResponse(data) {
            var searchResultsObj = Cru.components.searchresults;
            var searchResults = $(searchResultsObj.className);
            var data_ = $(data).children().unwrap();
            searchResults.html(data_);

            //reevaluate the pagination links.
            searchResultsObj.init();
        }

        $.ajax({
            url: link,
            dataType: "html",
            success: injectResponse
        });
    },
    init: function() {
        var pagination = $(this.className + " .pagination");

        //function when some pagination link is clicked.
        function click(e) {
            //prevent all default clicks.
            //e.preventDefault();

            if (this.href.indexOf("_jcr_content") === -1 && this.href.indexOf("jcr:content") === -1) {
                var asynchronousPath = $(Cru.components.searchresults.className).data("asynchronous-path");
                var a = $('<a>', { href:asynchronousPath } )[0];
                var path = a.href.split("/jcr:content")[0];
                var selectors = this.href.split(path).pop();

                Cru.components.searchresults.refresh(asynchronousPath + selectors);
            } else {
                Cru.components.searchresults.refresh(this.href);
            }
        }

        //manage the pagination when use ajax calls.
        if (pagination) {
            var paginationLinks = pagination.find("a");
            paginationLinks.on("click", click);
        }
    }
};

$(Cru.components.searchresults.className).ready(function () {
    Cru.components.searchresults.init();
});
