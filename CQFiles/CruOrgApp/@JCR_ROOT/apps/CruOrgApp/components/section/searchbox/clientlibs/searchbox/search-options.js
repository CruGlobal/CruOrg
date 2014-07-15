function setPubWebSearch(engine)
{
	//If no option has been chosen, set the default search
	if(selection == undefined)
		selection = 'cru';
	
	if (selection=="give")
	{
		document.search.action = "https://give.cru.org/give/VirtualCommonLinks/process/search2";
		document.search.Query.disabled=false;
		document.search.Query.value = document.search.ssUserText.value;
	}
	else {
    	document.search.action = 'http://www.cru.org/search/index.htm?ssUserText=';
    	document.search.Query.disabled=true;                        
	} 
		
}


//Global variable for search options (i.e. current site, directory, or all sites)
var selection;


function chooseSearch(var1,var2,var3) {
	var currentSelection = document.getElementById(var1);
	var li2 = document.getElementById(var2);

	
	//Apply styling to the selected option and remove styling from other options
	currentSelection.className = "selected";
	li2.className = "";

	
	selection = currentSelection.id;
	
	return false;
}

function toggle_div(options,triangles) {
	var flip = document.getElementById(options);
	var shape = document.getElementById(triangles);
	if(flip.style.display == "block"){
		flip.style.display = "none";
		shape.style.borderTop = "5px solid #F9B625";
		shape.style.borderBottom = "none";
		shape.style.marginLeft = "3px";
	}
	else{
		flip.style.display = "block";
		shape.style.borderBottom = "5px solid #F9B625";
		shape.style.borderTop = "none";
		shape.style.marginLeft = "4px";
					
	}
	return false;
}

$(document).ready(function(){

    $('.js-search-menu').click(function(e) {
          $('body').toggleClass("menu-active"); //you can list several class names 
          $('.js-primary-search').toggleClass("active"); 
          e.preventDefault();
    });

});