(function(document, $) {
    "use strict";

    $(document).on("change", ".article-template-date-change", function(e) {

        var dateStr = e.target.value;
      	if(dateStr){
        	var dateFormat = getdateFormat(dateStr, "D MMMM YY");
            setDateText(e, dateFormat);

        }
    });


   	function setDateText(el, dateFormat){
        var dateText = el.delegateTarget.getElementById("article-template-dateText");
      	if(dateText){
			dateText.setAttribute("value", dateFormat);
      	}
   	}


  	function getdateFormat(dateStr, format){
		var date = new Date(dateStr);
		var dateMoment = new moment(date);
      	return dateMoment.format(format);
   	} 


})(document,Granite.$);