//summer-missions.js
$(document).ready(function() {
	var url = window.location.href;
	
	//Limits the JSON call to pages that contain a div with the class 'summer-missions' which is contained in the component.
	if ( $( '.js-summer-missions' ).length ) {
		
	    var cru_summer_projects_json = {"project_search":"http://sp.campuscrusadeforchrist.com/projects.json","project_details":"http://sp.campuscrusadeforchrist.com/projects/%d.json"};
		var tripID = urlParam( 'tripid' )
		
		//Is this a Trip or a Search
		if (tripID) {
            $( '.js-summer-missions > .grid__item' ).addClass( 'display--none' );
            $( '.js-summer-missions' ).removeClass( 'grid' )
			
		    function project_detail_results( projectid ) {
				var jsonLink = cru_summer_projects_json.project_details.replace( '%d', tripID );

		        $.ajax( {
		    	    url: jsonLink,
		    		dataType: 'jsonp',
		            async: false,
		    		data: {
		    		    'active': 1
				},
		
				success: function( data ) {
		    		// Project Name
					$( '.post__headline' ).empty();
				
		    		$( '<h1 class="post-title  pt_x"></h1>' )
						.text( data.project.name )
						.appendTo( '.post__headline' );
				
					// Inset Sidebar
					$( '<nav class="post-nav"><ul class="bare-list  small-text"></ul></nav>')
						.appendTo( '.js-summer-missions' );				
				
					if (data.project.display_location) {	
						$( '<li></li>')
							.html( '<strong>Location:</strong> ' +  data.project.display_location )
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data.project.start_date ) {
						var startDate = $.datepicker.formatDate('M d, yy', new Date( data.project.start_date ));
						var endDate = $.datepicker.formatDate('M d, yy', new Date( data.project.end_date ));
						$( '<li></li>' )
							.html( '<strong>Term:</strong> ' +  startDate + ' – ' + endDate )
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data.project.url ) {
                        if ( data.project.url_title ) {
                            var urlTitle = data.project.url_title;
                        }
                        else {
                            var urlTitle = 'Trip Website';
                        }
                            
						$( '<li></li>' )
							.html( '<strong>Website:</strong> <a href="' + data.project.url + '"  target="_blank">' + urlTitle + '</a>')
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data.project.ministry_focuses ) {
						$( '<li></li>' )
							.html( '<strong>Ministry Focus:</strong> ' + data.project.primary_focus_name )
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data.project.student_cost ) {
						$( '<li></li>' )
							.html( '<strong>Cost:</strong> $' + data.project.student_cost )
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data.project.weeks ) {
						$( '<li></li>' )
							.html( '<strong>Length:</strong> ' + data.project.weeks + ' weeks')
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data.project.job ) {
						$( '<li></li>' )
							.html( '<strong>Can a student get a job?</strong> Yes')
							.appendTo( '.post-nav > ul' );
					}
					else {
						$( '<li></li>' )
							.html( '<strong>Can a student get a job?</strong> No')
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data.project.pd_name ) {
						$( '<li></li>' )
							.html( '<hr class="me" /><strong>Trip Director:</strong> <a href="mailto:' + data.project.pd_email + '">' + data.project.pd_name + '</a>')
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data.project.apd_name ) {
						$( '<li></li>' )
							.html( '<strong>Trip Director:</strong> <a href="mailto:' + data.project.apd_email + '">' + data.project.apd_name + '</a>')
							.appendTo( '.post-nav > ul' );
					}
				
					//Description				
					$( '<div class="js-summer-missions__description mb"></div>' )
						.html( data.project.description )
						.appendTo( '.js-summer-missions' );
				
					//Apply Button
					if ( data.project.use_provided_application ) {
						$( '<div></div>' )
							.html( '<a href="https://sp.campuscrusadeforchrist.com/apply?p=' + data.project.id + '" class="button  button--primary">Apply For This Project</a>')
							.appendTo( '.js-summer-missions' );
					}
				
					//Remove Bad Styles From Database
					$( '.js-summer-missions *' ).removeAttr( 'style' ).removeClass( 'MsoNormal' );

				},
				error: function( data ) {
					$( '<p><strong>Error:</strong> No trip found.<br />Go Back?<br />Search box?<br />Go to list of all projects? </p>' )
						.appendTo( '.summer-project' );
				}
		        } );		
		    }
			project_detail_results();
		}
	
		else {
		    function project_search() {
				
				var jsonLink = cru_summer_projects_json.project_search + '?' + urlParams;

		        $.ajax( {
	 	    	    url: jsonLink,
	 	    		dataType: 'json',
	 	            async: false,
	 	    		data: {
	 	    		    'active': 1
	 			},
			
	 			success: function( data ){ 
			        $.getJSON(jsonLink,

			        function (data) {
			            $.each(data, function (key, value) {
			                $( '.js-summer-missions__search' ).append('<p><a href="?tripid=' + value.project.id + '">' + value.project.name + '</a></p>');
			            });
			        });
	 			}
				});
			}
			project_search();
		}
	}
});