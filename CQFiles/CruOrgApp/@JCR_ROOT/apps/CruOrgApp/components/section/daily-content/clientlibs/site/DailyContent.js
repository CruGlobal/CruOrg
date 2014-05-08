
var Cru = (function(v) {
    return v;
}(Cru || {}));

Cru.components = (function(v) {
    return v;
}(Cru.components || {}));


Cru.components.DailyContent = (function(v) {
    return v;
}(Cru.components.DailyContent || {}));

Cru.components.DailyContent.DisplayPeriodically = {
    selectionChanged : function(container, isChecked){

        var panel = container.findParentByType("panel");
		var periodicityFieldSet = panel.getComponent("periodicity");
        if(!isChecked) {
			periodicityFieldSet.hide();
        }else {
            periodicityFieldSet.show();
        }
    },

    loadContent : function(field,record){
		var panel = field.findParentByType("panel");
        var periodicityFieldSet = panel.getComponent("periodicity");
        if(field.getValue()[0]){
			periodicityFieldSet.show();
        }else {
			periodicityFieldSet.hide();
        }
        
    }
}
