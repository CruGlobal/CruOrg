CQ.form.rte.commands.Quotes = new Class({

    toString: "Quotes",

    extend: CQ.form.rte.commands.DefaultFormatting,

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