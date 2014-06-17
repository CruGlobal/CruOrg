
var Cru = (function(v) {
    return v;
}(Cru || {}));

Cru.components = (function(v) {
    return v;
}(Cru.components || {}));


Cru.components.tile = {

    DEFAULT_RENDITION_VALUE : "CruHalf432x243", //used for the 1/2, 1/3 and 1/4 widths
    CRU_RENDITION_VALUE : "Cru704x396", //used for the 2/3 and whole grid widths
    CRU_WIDTHS_VALUES_ARRAY : [ //array of widths that should use the Cru704x396 rendition 
		"desk--one-whole",
       "desk--two-thirds"
    ],
	CRU_250_CHARACTERS_WIDTHS_ARRAY : [ //array of widths that should use the Cru704x396 rendition 
		"desk--one-whole",
        "desk--two-thirds",
        "desk--one-half"
    ],

    PARSYS_CLASSES_ARRAY : [
		".tile-container-parsys",
        ".post-body-parsys",
        ".content-parsys"
    ],

    afterRender : function(container){ //adds a div with clear:both style after the last tile in the container so it doesn't overlap with the parsys
        var cqGeneratedDiv = container.element;
        var gridItemDiv = cqGeneratedDiv.firstElementChild;
        var tileClass = $(gridItemDiv).attr("class");
        $(cqGeneratedDiv).addClass(tileClass) ;
        $(gridItemDiv).removeClass(tileClass);
        this.PARSYS_CLASSES_ARRAY.forEach(function(parsysClass) {
            if(Xumak.Utils.elementExists($(parsysClass))) {
                if($(parsysClass + " .divider").length == 0) {
                    $( "<div class='divider'></div>" ).insertAfter($(parsysClass + " .parbase.section").last());
                }
            }
        });
    },

    updateRenditionField : function(container) {

        var renditionWidget = container.findParentByType('dialog').getField("./imageRenditionName");
    	var width = container.getValue();
        renditionWidget.setValue(this.DEFAULT_RENDITION_VALUE);
    	if(this.CRU_WIDTHS_VALUES_ARRAY.indexOf(width) != -1){
        	renditionWidget.setValue(this.CRU_RENDITION_VALUE);
		}
    },

    validator : function(value, container) {
        var width = container.findParentByType('dialog').getField("./width").getValue();
        var characters = Cru.widgets.Util.richTextEditorCharacterCount(value);
        var limit = (this.CRU_250_CHARACTERS_WIDTHS_ARRAY.indexOf(width) == -1) ? 140 : 250;
        if( Cru.widgets.Util.richTextEditorCharacterCount(value) > limit ){
            return CQ.I18n.getMessage("The character count limit is " + limit);
        } else {
            return true;
        }

    }

}

