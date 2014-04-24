CQ.form.rte.plugins.QuotePlugin = new Class({

    toString: "QuotePlugin",

    extend: CUI.rte.plugins.SimpleFormatPlugin,

    _rtePluginType: "compat",

    constructor: function(/* varargs */) {
        if (typeof this.construct === 'function') {
            this.construct.apply(this, arguments);
        }
    },

    execute: function(id, value) {
        this.editorKernel.relayCmd(id, value);
    },

    initializeUI: function(tbGenerator) {
        this.commandsUI = [ ];
        var cmdCnt = this.commands.length;
        for (var cmdIndex = 0; cmdIndex < cmdCnt; cmdIndex++) {
            var command = this.commands[cmdIndex];
            var shortcut = null;
            var attributes = null;
            if (typeof(command) == "object") {
                shortcut = command.shortcut;
                if (command.className) {
                    attributes = {
                        cmd: command.command,
                        cmdValue: {
                            "class": command.className
                        }
                    };
                }
                command = command.command;
                this.commands[cmdIndex] = command;
            }
            if (this.isFeatureEnabled(command)) {
                var commandDef = tbGenerator.createElement(command, this, true, this.getTooltip(command), null, attributes);
                this.commandsUI.push(commandDef);
                tbGenerator.addElement(this.groupDef.id, this.groupDef.sort, commandDef,
                    (cmdIndex + 1) * 10);
                if (shortcut) {
                    this.editorKernel.registerKeyboardShortcut(shortcut, command);
                }
            }
        }
    },

    _init: function(editorKernel) {
        var plg = CQ.form.rte.plugins;
        plg.QuotePlugin.prototype.superClass._init.call(this, editorKernel, "quote",
            plg.Plugin.SORT_FORMAT, [ {
                "command": "blockquote"
            },{
                "command": "pullquote",
                "className": "pullquote"
            }]);
    },

    notifyPluginConfig: function(pluginConfig) {
        pluginConfig = pluginConfig || { };
        CUI.rte.Utils.applyDefaults(pluginConfig, {
            "tooltips": {
                "pullquote": {
                    "title": CUI.rte.Utils.i18n("Pullquote")
                },
                "blockquote": {
                    "title": CUI.rte.Utils.i18n("Blockquote")
                }
            }
        });
        this.config = pluginConfig;
    }
});

//register plugin
CQ.form.rte.plugins.PluginRegistry.register("quotePlugin", CQ.form.rte.plugins.QuotePlugin);