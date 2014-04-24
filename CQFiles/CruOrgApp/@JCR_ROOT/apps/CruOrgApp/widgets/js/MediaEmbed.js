Cru.widgets.MediaEmbed = {

	/**
	* Validate the source into embed code.
	**/
	validate: function(value) {
        var beginMessage =  "The media Embed ";
        var endMessage = " not is valid.";
        var isValid = beginMessage + endMessage;
        var scripts = value.match(/((href|src)="([^\'\"]+))/g);
        if(scripts && scripts.length > 0){
            for (var i=0; i< scripts.length; i++) {
                var src =scripts[i].replace(/(href|src)=\"/,"");
                if(src !== undefined){
                    if (this.isValidUrl(src)) {
                        isValid = this.checkDomains(src);
                        if(isValid !== true){
                            return isValid;
                        }
                    }else{
                        return beginMessage + src + endMessage;
                    }
                }
            }
        }
		return isValid;
	},

    /**
     * Validate if the Url contains http or https and valid characters.
     **/
    isValidUrl: function(url){
        var isValid = false;
        var RegExp = /^(http:|https:)?\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
        if (RegExp.test(url)) {
            isValid = true;
        }
        return isValid;
    },

    /**
     * Validate if the domains is contains into global valid domains.
     **/
    checkDomains: function(src){
        var beginMessage =  "The media Embed ";
        var endMessage = " do not match with valid domains.";
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
        return  beginMessage + src + endMessage;
    },

    /**
     * Compare source with global valid domains.
     **/
    compareDomain: function(domain , src){
        var containsDomain = false;
        if (src.indexOf(domain) > 0) {
            containsDomain = true;
        }
        return containsDomain;
    }


}
