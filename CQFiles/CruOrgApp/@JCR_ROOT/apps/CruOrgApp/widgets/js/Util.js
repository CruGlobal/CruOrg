
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
    }

}