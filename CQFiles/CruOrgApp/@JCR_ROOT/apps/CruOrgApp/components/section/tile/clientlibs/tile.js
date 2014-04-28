
var Cru = (function(v) {
    return v;
}(Cru || {}));

Cru.components = (function(v) {
    return v;
}(Cru.components || {}));


Cru.components.tile = {
    afterRender : function(container){
				var cqGeneratedDiv = container.element;
        		var gridItemDiv = cqGeneratedDiv.firstElementChild;
                var tileClass = $(gridItemDiv).attr("class");
                $(cqGeneratedDiv).addClass(tileClass)
                $(gridItemDiv).removeClass(tileClass) 

		if(Xumak.Utils.elementExists($(".tile-container-parsys"))){

			$( "<div style='clear:both;'></div>" ).insertAfter($(".tile-container-parsys .parbase.section").last());
        }


    }
}
