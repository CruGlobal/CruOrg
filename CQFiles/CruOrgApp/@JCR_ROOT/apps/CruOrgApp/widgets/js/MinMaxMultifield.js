
CQ.form.MinMaxMultiField = CQ.Ext.extend(CQ.form.MultiField, {
//CQ.Ext.override(CQ.form.MultiField, {
    minItems: 0,    
    getValue: function() {
		var noItems = this.items.length;	
        if ((noItems != null) && (noItems != "")) {
            if (noItems <= this.minItems) {
                for (var i = noItems; i <= this.minItems; i++) {
                	this.addItem("");
                }            	
            }
        }

        var value = new Array();
        this.items.each(function(item, index/*, length*/) {
            if (item instanceof CQ.form.MinMaxMultiField.Item) {
                value[index] = item.getValue();
                index++;
            }
        }, this);
        return value;
    }
});
CQ.Ext.reg("minmaxmultifield", CQ.form.MinMaxMultiField);

CQ.form.MinMaxMultiField.Item = CQ.Ext.extend(CQ.Ext.Panel, {

//CQ.Ext.override(CQ.form.MultiField.Item, {
    constructButtonConfig: function(items, fieldConfig) {
        var item = this;
        this.field = CQ.Util.build(fieldConfig, true);
        items.push({
            "xtype":"panel",
            "border":false,
            "cellCls":"cq-multifield-itemct",
            //"width": 100,
            "items":item.field
        });

        if (!fieldConfig.readOnly) {
            if (fieldConfig.orderable) {
                items.push({
                    "xtype": "panel",
                    "border": false,
                    "items": {
                        "xtype": "button",
                        "iconCls": "cq-multifield-up",
                        "template": new CQ.Ext.Template('<span><button class="x-btn" type="{0}"></button></span>'),
                        "handler": function(){
                            var parent = item.ownerCt;
                            var index = parent.items.indexOf(item);

                            if (index > 0) {
                                item.reorder(parent.items.itemAt(index - 1));
                            }
                        }
                    }
                });
                items.push({
                    "xtype": "panel",
                    "border": false,
                    "items": {
                        "xtype": "button",
                        "iconCls": "cq-multifield-down",
                        "template": new CQ.Ext.Template('<span><button class="x-btn" type="{0}"></button></span>'),
                        "handler": function(){
                            var parent = item.ownerCt;
                            var index = parent.items.indexOf(item);

                            if (index < parent.items.getCount() - 1) {
                                item.reorder(parent.items.itemAt(index + 1));
                            }
                        }
                    }
                });
            }

            items.push({
                "xtype":"panel",
                "border":false,
                "items":{
                    "xtype":"button",
                    "iconCls": "cq-multifield-remove",
                    "template": new CQ.Ext.Template('<span><button class="x-btn" type="{0}"></button></span>'),
                    "handler":function() {
                        var parent = item.ownerCt;
                        var minItems = parent.minItems;
                        var noItems = parent.items.length - 1;
                        if(noItems <= minItems){
                            var msg = CQ.I18n.getMessage("Is required to have a minimum of ") + minItems + CQ.I18n.getMessage(" items.");           
                            CQ.Ext.Msg.show({
                                "title": CQ.I18n.getMessage("Create New Item"),
                                "msg": msg,
                                icon:CQ.Ext.MessageBox.ERROR,                               
                                "buttons": CQ.Ext.Msg.OK
                            });                             
                        }else{
                            var parent = item.ownerCt;
                            parent.remove(item);
                            parent.fireEvent("removeditem", parent);                            
                        }
                    }
                }
            });
        }
    }
});


CQ.Ext.reg("minmaxmultifielditem", CQ.form.MinMaxMultiField.Item);


CQ.Ext.override(CQ.form.MinMaxMultiField, {
    maxItems: 50,
    addItem: function(value) {
        if( this.items.getCount() > this.maxItems ) {
            var msg = CQ.I18n.getMessage("You can only create a maximum of ") + this.maxItems + CQ.I18n.getMessage(" items.");
            CQ.Ext.Msg.show({
                "title": CQ.I18n.getMessage("Create New Item"),
                "msg": msg,
                "buttons": CQ.Ext.Msg.OK
            });
        }else{
            var item = this.insert(this.items.getCount() - 1, {});
            var form = this.findParentByType("form");
            if (form)
                form.getForm().add(item.field);
            this.doLayout();

            if (item.field.processPath) item.field.processPath(this.path);
            if (value) {
                item.setValue(value);
            }

            if (this.fieldWidth < 0) {
                // fieldWidth is < 0 when e.g. the MultiField is on a hidden tab page;
                // do not set width but wait for resize event triggered when the tab page is shown
                return;
            }
            if (!this.fieldWidth) {
                this.calculateFieldWidth(item);
            }
            try {
                item.field.setWidth(this.fieldWidth);
            }
            catch (e) {
                CQ.Log.debug("CQ.form.MinMaxMultiField#addItem: " + e.message);
            }
        }
    }
});