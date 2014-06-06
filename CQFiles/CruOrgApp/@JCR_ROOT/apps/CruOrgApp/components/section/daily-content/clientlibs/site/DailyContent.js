
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

    },

    changeNormalDate : function(normalDate, excludeYear){
        var startDate = normalDate.getComponent("startDate");
        var endDate = normalDate.getComponent("endDate");
        var startDateExcludeYear = excludeYear.getComponent("startDateExcludeYear").getValue();
       	var endDateExcludeYear = excludeYear.getComponent("endDateExcludeYear").getValue();
        startDate.setValue(startDateExcludeYear);
		endDate.setValue(endDateExcludeYear);
    },


    changeDateExcludeYear : function(normalDate, excludeYear){
        var endDate = normalDate.getComponent("endDate").getValue();
        var startDateExcludeYear = excludeYear.getComponent("startDateExcludeYear");
        var startDate = normalDate.getComponent("startDate").getValue();
        var endDateExcludeYear = excludeYear.getComponent("endDateExcludeYear");
        startDateExcludeYear.setValue(startDate);
        endDateExcludeYear.setValue(endDate);
    },

    selectionChangedExcludeYear : function(container, isChecked){

        var panel = container.findParentByType("panel");
		var normalDate = panel.getComponent("normalDate");
        var dateExcludeYear = panel.getComponent("dateExcludeYear");
        if(!isChecked) {
			dateExcludeYear.hide();
            normalDate.show();
            Cru.components.DailyContent.DisplayPeriodically.changeNormalDate(normalDate, dateExcludeYear);
        }else {
            normalDate.hide();
			dateExcludeYear.show();
            Cru.components.DailyContent.DisplayPeriodically.changeDateExcludeYear(normalDate, dateExcludeYear);
        }
    },

    loadContentExcludeYear : function(field,record){
		var panel = field.findParentByType("panel");
        var normalDate = panel.getComponent("normalDate");
        var dateExcludeYear = panel.getComponent("dateExcludeYear");

        if(field.getValue()[0]){
			normalDate.hide();
			dateExcludeYear.show();
        }else {
			dateExcludeYear.hide();
            normalDate.show();
        }

    }



}
