Cru.widgets.MediaEmbed = {
	/**
	* Validate length for a specific 'value' string 
	* with 'limit' specify
	**/
	validate: function(value) {
        var isValid =  "The media Embed not is valid."
        var src = this.getSource(value);
        if (this.isValidUrl(src)) {
           isValid = this.checkDomains(src);
        }
		return isValid;
	},


    getSource: function(value){
        var src = $(value).attr("src");
        if (src === undefined) {
            src = $(value).attr("href");
        }
        return src;
    },


    isValidUrl: function(url){
        var isValid = false;
        var RegExp = /^(http:|https:)?\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
        if (RegExp.test(url)) {
            isValid = true;
        }
        return isValid;
    },

    checkDomains: function(src){
        var domains = Cru.widgets.Util.getGlobalProperty("domains");
        if (domains) {
            if (Array.isArray(domains)) {
                for (var i=0; i< domains.length; i++) {
                    if (this.compareDomain(domains[i] , src)) {
                        return true;
                    }
                }
            } else if (this.compareDomain(domains , src)) {
                return true;

            }
        }
        return  "The Media Embed source do not match with valid domains.";
    },

    compareDomain: function(domain , src){
        var containsDomain = false;
        if (src.indexOf(domain) > 0) {
            containsDomain = true;
        }
        return containsDomain;
    }


}
