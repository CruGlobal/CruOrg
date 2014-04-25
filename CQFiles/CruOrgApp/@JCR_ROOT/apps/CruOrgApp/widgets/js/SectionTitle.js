Cru.widgets.SectionTitle = {
	/**
	* Validate length for a specific 'value' string 
	* with 'limit' specify
	**/
	validateLength: function(value, limit) {
        if (Cru.widgets.Util.characterCount(value) > limit) {
            return "You have exceed the maximum character limit of " + limit;
        }
		return true;
	}
}
