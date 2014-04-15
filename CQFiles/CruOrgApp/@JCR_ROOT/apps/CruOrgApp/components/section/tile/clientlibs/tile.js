
var Cru = (function(v) {
    return v;
}(Cru || {}));

Cru.components = (function(v) {
    return v;
}(Cru.components || {}));


Cru.components.tile = {
    afterRender : function(container){
        //debugger;
				var cqGeneratedDiv = container.element;
        		var gridItemDiv = cqGeneratedDiv.firstChild;
                var tileClass = $(gridItemDiv).attr("class");
                $(cqGeneratedDiv).addClass(tileClass)
                $(gridItemDiv).removeClass(tileClass) 

		if(Xumak.Utils.elementExists($(".tile"))){
			$( "<div style='clear:both;'></div>" ).insertAfter($(".tile").last());
        }
    }
}
