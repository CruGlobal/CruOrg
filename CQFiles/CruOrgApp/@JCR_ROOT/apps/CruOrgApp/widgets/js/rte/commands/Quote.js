CQ.form.rte.commands.Quotes = new Class({

    toString: "Quotes",

    extend: CQ.form.rte.commands.DefaultFormatting,

    execute: function(execDef) {
        var com = CUI.rte.Common;
        var nodeList = execDef.nodeList;
        var selection = execDef.selection;
        var context = execDef.editContext;
        if (!CUI.rte.Selection.isSelection(selection)) {
            // execDef.editContext.doc.execCommand(execDef.command, false, null);
            return this.setCaretTo(execDef);
        }
        var tagName = this.getTagNameForCommand(execDef.command);
        var attributes = execDef.value;
        // see queryState()
        var isActive = (com.getTagInPath(context, selection.startNode, tagName) != null);
        if (!isActive) {
            if (this.getClassNameForCommand(execDef.command)) {
                //if the command is a pullquote needs em tag.
                var dpr = CUI.rte.DomProcessor;
                var containerNode = dpr.restructureAsChild(context, nodeList.commonAncestor,
                    nodeList.createTopLevelDomNodes(), "em", null);
                var containerStrucNode = nodeList.createStructuralNode(containerNode, null);
                nodeList.nodes = [ containerStrucNode ];
                dpr.restructureAsChild(context, nodeList.commonAncestor, nodeList.createTopLevelDomNodes(),
                    "blockquote", attributes);
            } else {
                nodeList.surround(execDef.editContext, tagName, attributes);
            }
        } else {
            nodeList.removeNodesByTag(execDef.editContext, tagName, attributes, true);
            if (this.getClassNameForCommand(execDef.command)) {
                nodeList.removeNodesByTag(execDef.editContext, "em", null, true);
            }
        }
    },

    isCommand: function(cmdStr) {
        var cmdLC = cmdStr.toLowerCase();
        return (cmdLC == "blockquote") || (cmdLC == "pullquote");
    },

    getTagNameForCommand: function() {
        return "blockquote";
    },

    getClassNameForCommand: function(cmd) {
        var cmdLC = cmd.toLowerCase();
        var className = null;
        switch (cmdLC) {
            case "pullquote":
                className = "pullquote";
                break;
        }
        return className;
    },


    //determine if the button on the toolbox is pressed or non
    queryState: function(selectionDef, cmd) {
        var tagName = this.getTagNameForCommand(cmd);
        var context = selectionDef.editContext;
        var selection = selectionDef.selection;
        var attribs = { "class": "pullquote" };
        var classNameForCmd = this.getClassNameForCommand(cmd);
        var attributes = classNameForCmd ? attribs : null;
        var notAttributes = classNameForCmd ? null : attribs;

        var dpr = CUI.rte.DomProcessor;
        //is necessary upgrade the blockquote tag as container to ignore.
        dpr.TYPE_TABLE[tagName] = "ignore";
        return (this.getTagInPath(context, selection.startNode, tagName, attributes, notAttributes) != null);
    },

    getTagInPath: function(context, dom, tagName, attribs, notAttribs) {
        var com = CUI.rte.Common;
        while (dom) {
            if (dom.nodeType == 1) {
                if (dom == context.root) {
                    return null;
                }
                if (com.isTag(dom, tagName)) {
                    if ((!attribs || com.hasAttributes(dom, attribs)) &&
                        (!notAttribs || !com.hasAttributes(dom, notAttribs))) {
                        return dom;
                    }
                }
            }
            dom = com.getParentNode(context, dom);
        }
        return null;
    }
});

//register commands
CQ.form.rte.commands.CommandRegistry.register("blockquote", CQ.form.rte.commands.Quotes);
CQ.form.rte.commands.CommandRegistry.register("pullquote", CQ.form.rte.commands.Quotes);