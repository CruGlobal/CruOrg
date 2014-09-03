//campus-locator.js
$(document).ready(function() {

    var cru_campus_finder_l10n = {"ministry_search":"http://ml.uscm.org/ministries.json?active=true","ministry_details":"http://ml.uscm.org/ministries/%d.json","ministries":{"CA":"Catalytic","SC":"Staffed Campus","IE":"Epic","ID":"Destino","II":"Impact","IN":"Nations","WI":"WSN ICS","WS":"WSN Stint","BR":"Bridges","MM":"Military Ministry","AA":"Athletes In Action","GR":"Grad Resource","CL":"Christian Leadership Ministries","KC":"Korean CCC","GK":"Greek","VL":"Valor","OT":"Other","FS":"Campus Field Ministry"}};
	
    $( function() {
        $( '.locator input[name="campus-name"]' ).autocomplete( {
            source: function( request, response ) {
                $.ajax( {
                    url: cru_campus_finder_l10n.ministry_search,
                    dataType: 'jsonp',
                    async: false,
                    data: {
                        'name': request.term,
                        'limit': 10
                    },
                    success: function( data ) {
						window.history.pushState('campusid', 'Title', '?');
                        response( $.map( data, function( item ) {
                            return {
                                label: item.name,
                                value: item.name,
                                id: item.id
                            };
                        } ) );
                    }
                } );
            },
            minLength: 2,
            autoFocus: true,
            select: function( event, ui ) {
                $( '.locator input[name="campus-id"]' ).val( ui.item.id );
                $( '.locator__state-name' ).text("");
                $( '.locator__state-results' ).text("");
                
                if( $( '.locator form' ).attr( 'action' ) === '' ) {
                    school_detail_results( ui.item.id );
                }
                
                else {
                    $( '.locator input[name="campus-name"]' ).val( ui.item.label );
                    $( '.locator form' ).get( 0 ).submit();
                }
            }
        } );
    
        if( $( '.locator input[name="campus-id"]' ).val() !== '' ) {
            school_detail_results( $( '.locator input[name="campus-id"]' ).val() );
        }
    } );

    function school_detail_results( campusid ) {
		var url = window.location.href;
		if (url.indexOf('campus-id=') != -1) {
			var campusid =  location.search.split('campus-id=')[1]
		}

        $.ajax( {
    	    url: cru_campus_finder_l10n.ministry_details.replace( '%d', campusid ),
    		dataType: 'jsonp',
            async: false,
    		data: {
    		    'active': 1
    		},
    		success: function( data ) {
                var results = $( '.locator__results' ).empty();
    			
    			if( data.strategies.length > 0 ) {
    			    var strategies = $.map( data.strategies, function( item ) {
    				
        				if( cru_campus_finder_l10n.ministries[ item.strategy ] ) {
        				    item.strategy = cru_campus_finder_l10n.ministries[ item.strategy ];
        					return item;
        				}
    				
    				return null;
        			} ).sort( function( a, b ) {
    				        return a.strategy.localeCompare( b.strategy );
    			    } );
    					
    
    				// School Name
    				$( '<h2 class="mb-"></h2>' )
    					.text( data.name )
    					.appendTo( results );
    					
                    var strategyBlock = $( '<ul class="block-list"></ul>' );
                        
    				$.each( strategies, function( index, item ) {    
        				//Only add strategy name if some form of contact info exists
        				if( item.contacts.length > 0 || item.url || item.facebook ) {
        					var strategyRow = $( '<li class="mb"></li>' ).appendTo( strategyBlock );
        					// Strategy Name
        					$( '<h3 class="h4  mb--  border-bottom-solid"></h3>' )
        						.text( item.strategy )
        						.appendTo( strategyRow );
        				}
    
    					// If There Is A Website Or Facebook URL Add That UL Block
    					if( item.url || item.facebook ) {
    						var socialBlock = $( '<ul class="block-list  mb--"></ul>' );
        					if( item.facebook ) {
        					    // Test to see if fully qualified url, if not add http://
        						if(item.facebook.substr(0,4) !== 'http'){
        							item.facebook = 'http://' + item.facebook;
    							}
        					    
        						$( '<li></li>' )
        						    .append( $( '<a></a>' ).attr( 'href', item.facebook ).text( item.facebook ))
            						.appendTo( socialBlock );
        					}
    					
        					if( item.url ) {
        						// Test for existance of url
        						if(item.url !== ''){
        							// Test to see if fully qualified url, if not add http://
        							if(item.url.substr(0,7) !== 'http://'){
        								item.url = 'http://' + item.url;
        							}
        						$( '<li></li>' )
        							.append( $( '<a></a>' ).attr( 'href', item.url ).text( item.url.substr(7)) )
        							.appendTo( socialBlock );
        						}
    					}
    					
    					$(socialBlock).appendTo( strategyRow );
    				}
    				
    				if( item.contacts.length > 0 ) {
    				    var contactList = $('<ul class="block-list  contacts"></ul>');
    				    
    				    $('<li><ul class="grid"><li class="contact__title-name  grid__item  desk--one-third">Name</li><li class="contact__title-name  grid__item  desk--one-third">Email</li><li class="contact__title-name  grid__item  desk--one-third">Phone</li></ul></li>')
    							.appendTo( contactList );
    							
    					$.each( item.contacts, function( index, contact ) {
    						var contactRow = $('<li class="contact__item"></li>');
    						var contactBlock = $( '<ul class="grid"></ul>' ),
    							name       = contact.preferred + ' ' + contact.last;
                            
                            contactBlock.append($('<li class="grid__item  desk--one-third">' + name + '</li>'));
                            
                            
    						if( contact.email ) {
    							contactBlock.append(
                                    $( '<li class="grid__item  desk--one-third"><a href="mailto:' + contact.email + '">' + contact.email + '<a></li>' )
                                ) ;
    						}
                            else {
    							contactBlock.append(
                                    $( '<li class="grid__item  desk--one-third"> </li>')
                                );
                                }
    						if( contact.phone ) {
    							contactBlock.append(
                                    $( '<li class="grid__item  desk--one-third">' + contact.phone + '</li>')
                                );
    						}
                            else {
    							contactBlock.append(
                                    $( '<li class="grid__item  desk--one-third"></li>')
                                );
                            }
    						contactRow.append( contactBlock );
    						contactList.append( contactRow );
    					} );
                                
                                strategyRow.append( contactList );
    							
    						}
                                        
                        } );
    					$(strategyBlock).appendTo( results );
    				}
    				// Remove the Campus name if no valid strategies were found
    				if ($(strategyBlock).children().length <= 0 ) {
    					results.empty();
    				}
					
					var str = data.name;
					var res = str.replace(/ /g, '+');
					window.history.pushState('campusid', 'Title', '?campus-name=' + res + '&campus-id=' + campusid);					
    
    		}
        } );		
    }

	// State Selection Box
	$('.locator__state-select').change(function() {
		// assign the value to a variable, so you can test to see if it is working
    state = $('.locator__state-select :selected').val();
	window.history.pushState('campusid', 'Title', '?');
    if (state !== ""){
		jsonLink = 'http://ml.uscm.org/ministries.json?state=' + state + '&active=true&callback=?';
		getStateResults();
		$(".locator__state-select").prop('selectedIndex',0);
		return false;
		} else {
			// User selected "Select A State" so clear fields
			$(".locator__state-results").text("");
			$(".locator__state-name").text("");
        }
    });
	


    // State Select Results
   function getStateResults() {
		$(".locator__results").text("");
		var stateSelectTitle ="";
		var states = {"AL": "Alabama", "AK": "Alaska", "AZ": "Arizona", "AR": "Arkansas", "CA": "California", "CO": "Colorado", "CT": "Connecticut",
		"DE": "Delaware", "FL": "Florida", "GA": "Georgia", "HI": "Hawaii", "ID": "Idaho", "IL": "Illinois", "IN": "Indiana",
		"IA": "Iowa", "KS": "Kansas", "KY": "Kentucky", "LA": "Louisiana", "ME": "Maine", "MD": "Maryland", "MA": "Massachusetts",
		"MI": "Michigan", "MN": "Minnesota", "MS": "Mississippi", "MO": "Missouri", "MT": "Montana", "NE": "Nebraska", "NV": "Nevada",
		"NH": "New Hampshire", "NJ": "New Jersey", "NM": "New Mexico", "NY": "New York", "NC": "North Carolina", "ND": "North Dakota",
		"OH": "Ohio", "OK": "Oklahoma", "OR": "Oregon", "PA": "Pennsylvania", "RI": "Rhode Island", "SC": "South Carolina",
		"SD": "South Dakota", "TN": "Tennessee", "TX": "Texas", "UT": "Utah", "VT": "Vermont", "VA": "Virginia", "WA": "Washington",
		"WV": "West Virginia", "WI": "Wisconsin", "WY": "Wyoming", "DC": "Washington DC"};

    stateSelectTitle = states[state];

    $(".locator__state-results").text("");
    $(".locator__state-name").text("Campuses in " + stateSelectTitle).removeClass("mb0");

        $.getJSON(jsonLink,

        function (data) {
            $.each(data, function (key, value) {
                $(".locator__state-results").append(
                    "<li><a class='ps--  block-link' href='" + value.id + "' name='select'>" + value.name +" <small>(" + value.city + ", " + value.state + ")</small></a></li>");
            });
        });
    }
    


	// State Select Detail Links
	$(document).on("click", "a[name=select]", function(e) {
		e.preventDefault();
		var campusid = this.getAttribute("href");
		$(".locator__state-results").text("");
		$(".locator__state-name").text("");
		school_detail_results( campusid );
	});
	
    // Remove jquery-ui helper span
    $(function() {
        $(".locator").find("span").remove();
        $(".locator").find("ul.ui-autocomplete-input").addClass("block-list");
    });

	
});







