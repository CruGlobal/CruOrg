Cru.widgets.Article = {

    /*
     *  get date in String Format
     * */
    getdateFormatProperty: function(value, format){
        var pagePath = CQ.WCM.getPagePath();
        var completePath = pagePath + "/jcr:content.json"; //TODO refactor using xcqb approach
        var date = new Date(value);
        var dateFormat = date.dateFormat(format);
        return dateFormat;
    }

}
