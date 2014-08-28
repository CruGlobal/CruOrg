var Cru = (function(v) {
    return v;
}(Cru || {}));
Cru.components = (function(v) {
    return v;
}(Cru.components || {}));

Cru.components.searchbox = {
    searchBoxClass: "form.primary-search",
    init: function(form) {
        //declare vars.
        var pathFields = form.find("ul.primary-search--dropdown li a");
        var searchlink_id = "cru_org";
        var give_link = 'https://give.cru.org/give/VirtualCommonLinks/process/search2';
        var cru_org_link = '/content/cru/us/en/search';

        //validate searchBox exist.
        if (!form) return;
        //at least one configured path.
        if (pathFields.size() === 0) return;

        //selectors of parameters.
        function processLink(link) {
            //get the link
            var a = $('<a>', {
                href: link
            })[0];
            var extension = a.pathname.split('.').pop();

            //in case that the pathName doesn't have any extension.
            if (a.pathname === extension) {
                extension = "html";
            }

            var path = a.pathname.replace('.' + extension, '');
            var queryField = getQueryField();


            if (queryField.val().length != 0) {
                if (searchlink_id == 'cru_org') {
                    //for internal links and selectors search.
                    return cru_org_link + "." + queryField.val() + "." + extension;
                } else {
                    return give_link + "?ssUserText=" + queryField.val() + "&Query=" + queryField.val();
                }
            }
        }

        function submitForm(link) {
            var processedLink = processLink(link);
            $(location).attr('href', processedLink);
        }

        function pathFieldsClick(e) {
            e.preventDefault();
            $('.searchlink--underline').removeClass('searchlink--underline');

            $(this).addClass('searchlink--underline');

            var link = $(this).attr("href");
            searchlink_id = $(this).attr('id');
        }

        function submitFormClick(e) {
            e.preventDefault();
            submitForm("");
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
        form.on("submit", submitFormClick);

        //if can catch a search query parameter, put it in the textfield.
        getQueryField().val(decodeURIComponent(getQueryParameter()));
    }
};

$(Cru.components.searchbox.searchBoxClass).ready(function() {
    Cru.components.searchbox.init($(Cru.components.searchbox.searchBoxClass));
});