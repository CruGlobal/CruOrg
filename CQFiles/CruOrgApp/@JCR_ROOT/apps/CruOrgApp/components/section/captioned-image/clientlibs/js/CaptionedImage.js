Cru.widgets.CaptionedImage = {

    WIDE_IMAGE_RENDITION_NAME : "CruExtraWide896x504",
    CAPTIONED_IMAGE_RENDITION_NAME : "CruCaptioned384x216",
    OUTSET_CLASS_NAME :"image--outset__center",

    updateRenditionField : function(container){
        var renditionWidget = container.findParentByType('dialog').getField("./imageRenditionName");
    	var style = container.getValue();
        renditionWidget.setValue(this.CAPTIONED_IMAGE_RENDITION_NAME);
    	if(style == this.OUTSET_CLASS_NAME){
        	renditionWidget.setValue(this.WIDE_IMAGE_RENDITION_NAME);
		}
    }
}