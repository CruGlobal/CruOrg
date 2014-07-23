
CQ.Ext.override(CUI.rte.plugins.LinkPlugin, {

    /**
     * Creates a link using the internal link dialog.
     * @private
     */
    modifyLink: function(context) {
        var com = CUI.rte.Common;
        var dm = this.editorKernel.getDialogManager();
        var dh = CUI.rte.ui.DialogHelper;
        if (!this.linkDialog || dm.mustRecreate(this.linkDialog)) {
            var dialogHelper = dm.createDialogHelper();
            var linkRules = this.editorKernel.htmlRules.links;
            var dialogConfig = {
                "configVersion": 1,
                "defaultDialog": {
                    "dialogClass": {
                        "type": dh.TYPE_DIALOG
                    }
                },
                "parameters": {
                    "linkRules": linkRules,
                    "editorKernel": this.editorKernel,
                    "command": this.pluginId + "#modifylink"
                }
            };
            if (this.config.linkDialogConfig) {
                var addDialogConfig = this.config.linkDialogConfig;
                if (addDialogConfig.linkAttributes) {
                    com.removeJcrData(addDialogConfig.linkAttributes);
                    var linkAttribs = com.toArray(addDialogConfig.linkAttributes);
                    dialogConfig.additionalFields = [ ];

                    var attribCnt = linkAttribs.length;
                    for (var a = 0; a < attribCnt; a++) {
                        var attrib = linkAttribs[a];
                        var type = attrib.type || attrib.xtype;
                        var attribName = attrib.attribute;
                        var attribLabel = attrib.label || attrib.fieldLabel;
                        var itemData = {
                            "item": dialogHelper.createItem(type, attribName, attribLabel),
                            "fromModel": function(obj, field) {
                                if (dialogHelper.getItemType(field) == dh.TYPE_HIDDEN) {
                                    return;
                                }
                                var attribName = dialogHelper.getItemName(field);
                                var attribValue = com.getAttribute(obj.dom, attribName);
                                if (attribValue) {
                                    dialogHelper.setItemValue(field, attribValue);
                                } else {
                                    dialogHelper.setItemValue(field, "");
                                }
                            },
                            "toModel": function(obj, field) {
                                var attribName = dialogHelper.getItemName(field);
                                if (!obj.attributes) {
                                    obj.attributes = { };
                                }
                                var value = dialogHelper.getItemValue(field);
                                if (value && (value.length > 0)) {
                                    obj.attributes[attribName] = value;
                                } else {
                                    obj.attributes[attribName] =
                                        CUI.rte.commands.Link.REMOVE_ATTRIBUTE;
                                }
                            }
                        };
                        delete attrib.attribute;
                        delete attrib.type;
                        delete attrib.xtype;
                        delete attrib.label;
                        delete attrib.fieldLabel;
                        CUI.rte.Utils.applyDefaults(itemData.item, attrib);
                        dialogConfig.additionalFields.push(itemData);
                    }
                    delete addDialogConfig.linkAttributes;
                }
                dialogConfig.dialogProperties = addDialogConfig;
            }
            if (linkRules.targetConfig) {
                if (linkRules.targetConfig.mode != "blank") {
                    dialogConfig.disabledDefaultFields = [ "targetBlank" ];
                }
                if (linkRules.targetConfig.mode == "manual") {
                    if (!dialogConfig.additionalFields) {
                        dialogConfig.additionalFields = { };
                    }
                    var targetItem = dialogHelper.createItem(dh.TYPE_TEXTFIELD, "target",
                            CUI.rte.Utils.i18n("Anchor"));
                    dialogConfig.additionalFields.push({
                        "item": targetItem,
                        "fromModel": function(obj, field) {
                            if (obj.dom && obj.dom["target"]) {
                                dialogHelper.setItemValue(field, obj.dom["target"]);
                            }
                        },
                        "toModel": function(obj, field) {
                            if (!obj.attributes) {
                                obj.attributes = { };
                            }
                            var value = dialogHelper.getItemValue(field);
                            if (value && (value.length > 0)) {
                                obj.attributes["target"] = value;
                            } else {
                                obj.attributes["target"] = null;
                            }
                        }
                    });
                }
            }

            CUI.rte.Utils.applyDefaults(dialogConfig, this.config.linkDialogConfig || { });

            dialogHelper.configure(dialogConfig);

            this.linkDialog = dialogHelper.create();
            //added validation for the link path field
             this.linkDialog.dialogItems.items[0].validator = function(){
                return Cru.widgets.Util.rteExternalLinkValidation(this.getValue());
             };
            dialogHelper.calculateInitialPosition();
        }
        var linkToEdit = null;
        var selectionDef = this.editorKernel.analyzeSelection();
        if (selectionDef.anchorCount == 1) {
            linkToEdit = selectionDef.anchors[0];
        }
        linkToEdit = linkToEdit || { };
        if (typeof linkToEdit.attributes === 'undefined')
            linkToEdit.attributes = { };
        this.linkDialog.initializeEdit(this.editorKernel, linkToEdit,
                CUI.rte.Utils.scope(this.applyLink, this));
        if (com.ua.isIE) {
            this.savedRange = context.doc.selection.createRange();
        }
        dm.show(this.linkDialog);
    }


});
