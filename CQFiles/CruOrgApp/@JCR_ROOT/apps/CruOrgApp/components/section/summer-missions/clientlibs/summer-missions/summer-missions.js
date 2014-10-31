//summer-missions.js
$(document).ready(function() {
	var url = window.location.href;
	
	//Limits the JSON call to pages that contain a div with the class 'summer-missions' which is contained in the component.
	if ( $( '.js-summer-missions' ).length ) {
		
	    var cru_summer_projects_json = {"project_search":"http://sp.campuscrusadeforchrist.com/projects.json","project_details":"http://sp.campuscrusadeforchrist.com/projects.json?id=%d"};
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
		
				success: function( data ) {
		    		// Project Name
					$( '.post__headline' ).empty();
				
		    		$( '<h1 class="post-title  pt_x"></h1>' )
						.text( data[0].project.name )
						.appendTo( '.post__headline' );
				
					// Inset Sidebar
					$( '<nav class="post-nav"><ul class="bare-list  small-text"></ul></nav>')
						.appendTo( '.js-summer-missions' );				
				
					if (data[0].project.display_location) {	
						$( '<li></li>')
							.html( '<strong>Location:</strong> ' +  data[0].project.display_location )
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data[0].project.start_date ) {
						var startDate = $.datepicker.formatDate('M d, yy', new Date( data[0].project.start_date ));
						var endDate = $.datepicker.formatDate('M d, yy', new Date( data[0].project.end_date ));
						$( '<li></li>' )
							.html( '<strong>Term:</strong> ' +  startDate + ' – ' + endDate )
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data[0].project.url ) {
                        if ( data[0].project.url_title ) {
                            var urlTitle = data[0].project.url_title;
                        }
                        else {
                            var urlTitle = 'Trip Website';
                        }
                            
						$( '<li></li>' )
							.html( '<strong>Website:</strong> <a href="' + data[0].project.url + '"  target="_blank">' + urlTitle + '</a>')
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data[0].project.ministry_focuses ) {
						$( '<li></li>' )
							.html( '<strong>Ministry Focus:</strong> ' + data[0].project.primary_focus_name )
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data[0].project.student_cost ) {
						$( '<li></li>' )
							.html( '<strong>Cost:</strong> $' + data[0].project.student_cost )
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data[0].project.weeks ) {
						$( '<li></li>' )
							.html( '<strong>Length:</strong> ' + data[0].project.weeks + ' weeks')
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data[0].project.job ) {
						$( '<li></li>' )
							.html( '<strong>Can a student get a job?</strong> Yes')
							.appendTo( '.post-nav > ul' );
					}
					else {
						$( '<li></li>' )
							.html( '<strong>Can a student get a job?</strong> No')
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data[0].project.pd_name ) {
						$( '<li></li>' )
							.html( '<hr class="me" /><strong>Trip Director:</strong> <a href="mailto:' + data[0].project.pd_email + '">' + data[0].project.pd_name + '</a>')
							.appendTo( '.post-nav > ul' );
					}
				
					if ( data[0].project.apd_name ) {
						$( '<li></li>' )
							.html( '<strong>Trip Director:</strong> <a href="mailto:' + data[0].project.apd_email + '">' + data[0].project.apd_name + '</a>')
							.appendTo( '.post-nav > ul' );
					}
				
					//Description				
					$( '<div class="js-summer-missions__description mb"></div>' )
						.html( data[0].project.description )
						.appendTo( '.js-summer-missions' );
				
					//Apply Button
					if ( data[0].project.use_provided_application ) {
						$( '<div class="mb_x"></div>' )
							.html( '<a href="https://sp.cru.org/apply?p=' + data[0].project.id + '" class="button  button--primary" onclick="event.preventDefault(); summermissions_apply(this.href);">Apply For This Project</a>')
							.appendTo( '.js-summer-missions' );
					}
				
					//Remove Bad Styles From Database
					$( '.js-summer-missions *' ).removeAttr( 'style' ).removeClass( 'MsoNormal' );

				},
				error: function( data ) {
					$( '<p><strong>Error:</strong> No trip found.<br /><br /><a href="?">Go to list of all projects</a></p>' )
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
	 	    		dataType: 'jsonp',
	 	            async: false,
			
	 			success: function( data ){ 
		            $.each(data, function (key, value) {
		                $( '.js-summer-missions__search' ).append('<p><a href="?tripid=' + value.project.id + '">' + value.project.name + '</a></p>');
		            });
	 			}
				});
			}
			project_search();
		}
	}
});

function summermissions_apply(link){
    window.open(link, "_blank", "toolbar=no, location=no, status=no, titlebar=no");
}