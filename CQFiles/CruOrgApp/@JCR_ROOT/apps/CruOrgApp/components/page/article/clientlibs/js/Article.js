Cru.widgets.Article = {

    /*
     *  get date in String Format
     * */
    getdateFormatProperty: function(value, format){

        var date = new Date(value);
        var dateFormat = date.dateFormat(format);
        return dateFormat;
    }

}
