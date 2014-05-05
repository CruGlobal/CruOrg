Cru.components.searchbox = {
    searchBoxClass : "form.primary-search",
    init: function(form) {
        //declare vars.
        var pathFields = form.find("ul.primary-search--dropdown li a");

        //validate searchBox exist.
        if (!form) return;
        //at least one configured path.
        if (pathFields.size() === 0) return;

        //declare functions
        function isInternalLink(a) {
            return a.hostname === location.hostname;
        }
        function processLink(link) {
            //get the link
            var a = $('<a>', { href:link } )[0];
            var extension = a.pathname.split('.').pop();
            var path = a.pathname.replace('.' + extension, '');
            var queryField = form.find("input.search-field");

            if (isInternalLink(a)) {
                //for internal links and selectors search.
                return path + "." + queryField.val() + "." + extension;
            } else {
                return link;
            }
        }
        function submitForm(link) {
            var processedLink = processLink(link);
            $(location).attr('href', processedLink);
        }
        function pathFieldsClick(e) {
            var link = $(this).attr("href");
            submitForm(link);
            e.preventDefault();
        }
        function submitFormClick(e) {
            var link = e.data.link.href;
            submitForm(link);
            e.preventDefault();
        }

        //process
        pathFields.on("click", pathFieldsClick);
        form.on("submit", { "link" : pathFields[0] }, submitFormClick);
    }
};

$(Cru.components.searchbox.searchBoxClass).ready(function () {
    Cru.components.searchbox.init($(Cru.components.searchbox.searchBoxClass));
});
