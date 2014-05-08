
Cru.widgets.Util = {

    /**
     * Creates a new component under 'path' with the resourceType and nameHint specified
     **/ 
    addComponent : function(path, resourceType, nameHint){
        var data = {"sling:resourceType" : resourceType, ":nameHint" : nameHint};  //the parameters sent
        Xumak.Util.synchronousPost(path + "/", data);
    },

    /**
     * Deletes the node at 'path'
     **/
    deleteNode : function(path){
		var deleteNodePostData = {":operation" : "delete"};
		this.synchronousPost(path, deleteNodePostData);
    },

    /**
     * Saves a property to the CRX
     **/
    saveProperty : function(path, propertyName, propertyValue){
        var postData = {};
        postData[propertyName] = propertyValue;
		var response = this.synchronousPost(path, postData);
		return (response.headers.Status === "200") && (response.headers.Message === "OK");
	},
    /**
     * Makes an HTTP POST request synchrounously.
     *  @param path the path on the CRX
     *  @param postData a json Object containing the parameters that will be sent
	 *
     **/
    synchronousPost : function(path, postData){
		return CQ.shared.HTTP.post(path, null, postData, this, false, false);
	},

    /**
     * returns the name of the node given its path
     **/
    getNodeName : function(nodePath){
		var lastSlashIndex = nodePath.lastIndexOf("/");
        var nodeName = nodePath;
        if(lastSlashIndex != -1){
            if(this.endsWith(nodePath, "/")){
				nodePath = nodePath.substring(0, lastSlashIndex);
                lastSlashIndex = nodePath.lastIndexOf("/");
            }
			nodeName = nodePath.substr(lastSlashIndex + 1);
        }
        return nodeName;
    },

    /**
     * returns the parent path of the node which path is given
     **/
    getParentNode : function(nodePath){
		var lastSlashIndex = nodePath.lastIndexOf("/");
        if(lastSlashIndex != -1){
            if(this.endsWith(nodePath, "/")){
				nodePath = nodePath.substring(0, lastSlashIndex);
                lastSlashIndex = nodePath.lastIndexOf("/");
            }
			nodePath = nodePath.substr(0, lastSlashIndex);
        }
        return nodePath;
    },
	/**
     * Checks if str ends with suffix
     **/
    endsWith : function(str, suffix) {
    	return str.indexOf(suffix, str.length - suffix.length) !== -1;
	},

    /**
     * Checks if a DOM element exists
     **/
    elementExists : function(element){

        var elementIsNotNull = true;
        if(element == null){
			elementIsNotNull = false;
        }

        var elementHasChildren = true;
        if(element.length == 0){
			elementHasChildren = false;
        }

		return elementIsNotNull && elementHasChildren;
    },


    /*
    *  Get Global Property
    * */
    getGlobalProperty: function(property){
        var pagePath = CQ.WCM.getPagePath();
        var design = CQ.WCM.getDesign(pagePath);
        var GlobalPath = design.path + "/jcr:content/global.json"; //TODO refactor using xcqb approach
        var Global = CQ.HTTP.get(GlobalPath);
        var body = JSON.parse(Global.body);
        var value = body[property];
        return value;
    },

    /**
     * Get the total number of words in the text.
     * @param text with words to count.
     * @return {Number} total of words.
     */
    wordCount : function(text) {
        if (!text) return 0;

        text = text.replace(/(^\s*)|(\s*$)/gi,"");  //trim spaces
        text = text.replace(/[ ]{2,}/gi," ");       //only one space between words
        text = text.replace(/\n /,"\n");            //new lines aren't words.
        return text.split(' ').length;
    },

    /**
     * Get the total number of characters in the text.
     * @param text with chars to count.
     * @return {Number} total of chars.
     */
    characterCount : function(text) {
    	if (!text) return 0;

		return text.length;
	},

    /**
     * Checks if the link is external and validates
     * that contains the text 'http://' or 'https://' on it.
     * @param text to validate
     * @return validated text
     */
    externalLinkValidation : function(text) {
		if (!text) return true;

        var validArray = ["http://","https://"],
            length = validArray.length,
            valid = true;

        if (text.indexOf("/")!=0) {
			valid = false;
            while(length--) {
                if ((text.indexOf(validArray[length])!=-1)) {
                    valid = true;
                }
            }
            if (valid) {
                return valid;
            } else {
                return "Please add 'http://' or 'https://' at the beggining of the link.";	
            }
        } else {
            return valid;
        }
	}
};