
CQ.form.MinMaxMultiField = CQ.Ext.extend(CQ.form.MultiField, {
//CQ.Ext.override(CQ.form.MultiField, {
    minItems: 0,   
    maxItems: 50,
    constructor: function(config) {
        console.log("ASDF");
        var list = this;

        if (typeof config.orderable === "undefined") {
            config.orderable = true;
        }

        if (!config.fieldConfig) {
            config.fieldConfig = {};
        }
        if (!config.fieldConfig.xtype) {
            config.fieldConfig.xtype = "textfield";
        }
        config.fieldConfig.name = config.name;
        config.fieldConfig.ownerCt = this;
//        config.fieldConfig.style = "width:95%;";
        config.fieldConfig.orderable = config.orderable;
        config.fieldConfig.dragDropMode = config.dragDropMode;
        if (!config.addItemLabel) {
            config.addItemLabel = CQ.I18n.getMessage("Add Item");
        }

        var items = new Array();

        if(config.readOnly) {
            //if component is defined as readOnly, apply this to all items
            config.fieldConfig.readOnly = true;
        } else {
            items.push({
                xtype: "toolbar",
                cls: "cq-multifield-toolbar",
                items: [
                    "->",
                    {
                        xtype: "textbutton",
                        text: config.addItemLabel,
                        style: "padding-right:6px",
                        handler:function() {
                            list.addItem();
                        }
                    },
                    {
                        xtype: "button",
                        iconCls: "cq-multifield-add",
                        template: new CQ.Ext.Template('<span><button class="x-btn" type="{0}"></button></span>'),
                        handler: function() {
                            list.addItem();
                        }
                    }
                ]
            });
        }

        this.hiddenDeleteField = new CQ.Ext.form.Hidden({
            "name":config.name + CQ.Sling.DELETE_SUFFIX
        });
        items.push(this.hiddenDeleteField);

        if (config.typeHint) {
            this.typeHintField = new CQ.Ext.form.Hidden({
                name: config.name + CQ.Sling.TYPEHINT_SUFFIX,
                value: config.typeHint + "[]"
            });
            items.push(this.typeHintField);
        }

        config = CQ.Util.applyDefaults(config, {
            "defaults":{
                "xtype":"minmaxmultifielditem",
                "fieldConfig":config.fieldConfig
            },
            "items":[
                {
                    "xtype":"panel",
                    "border":false,
                    "bodyStyle":"padding:" + this.bodyPadding + "px",
                    "items":items
                }
            ]
        });
        CQ.form.MultiField.superclass.constructor.call(this,config);
        if (this.defaults.fieldConfig.regex) {
            // somehow regex get broken in this.defaults, so fix it
            this.defaults.fieldConfig.regex = config.fieldConfig.regex;
        }
        this.addEvents(
            /**
             * @event change
             * Fires when the value is changed.
             * @param {CQ.form.MultiField} this
             * @param {Mixed} newValue The new value
             * @param {Mixed} oldValue The original value
             */
            "change",
            /**
             * @event removeditem
             * Fires when an item is removed.
             * @param {CQ.form.MultiField} this
             */
            "removeditem"
        );
    },

    initComponent: function() {
        CQ.form.MultiField.superclass.initComponent.call(this);

        this.on("resize", function() {
            // resize fields
            var item = this.items.get(0);
            this.calculateFieldWidth(item);
            if (this.fieldWidth > 0) {
                for (var i = 0; i < this.items.length; i++) {
                    try {
                        this.items.get(i).field.setWidth(this.fieldWidth);
                    }
                    catch (e) {
                        CQ.Log.debug("CQ.form.MultiField#initComponent: " + e.message);
                    }
                }
            }
        });

        this.on("disable", function() {
            this.hiddenDeleteField.disable();
            if (this.typeHintField) this.typeHintField.disable();
            this.items.each(function(item/*, index, length*/) {
                if (item instanceof CQ.form.MultiField.Item) {
                    item.field.disable();
                }
            }, this);
        });

        this.on("enable", function() {
            this.hiddenDeleteField.enable();
            if (this.typeHintField) this.typeHintField.enable();
            this.items.each(function(item/*, index, length*/) {
                if (item instanceof CQ.form.MultiField.Item) {
                    item.field.enable();
                }
            }, this);
        });
    },
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
    },
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
CQ.Ext.reg("minmaxmultifield", CQ.form.MinMaxMultiField);

CQ.form.MinMaxMultiField.Item = CQ.Ext.extend(CQ.form.MultiField.Item, {


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
                            var msg = CQ.I18n.getMessage("The minimum number of items required is ") + minItems;           
                            CQ.Ext.Msg.show({
                                "title": CQ.I18n.getMessage("Remove Item"),
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

