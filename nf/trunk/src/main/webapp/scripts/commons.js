
// -----------------------------------------------------------------------------
// Global variables
// -----------------------------------------------------------------------------

var viewMode = 0;
var latestClipId = 0;
var currentlySelectedId = 0;

var COLOR_SELECTED = "#c8efac";
var COLOR_HOVER = "#a4e87a";
var COLOR_PASSIVE = "#6fd33d";

// -----------------------------------------------------------------------------
// Page init
// -----------------------------------------------------------------------------

function initClips()
{
  $( "li.clipItem, li.documentItem" ).css( "background-color", COLOR_PASSIVE );

  $( "li.clipItem, li.documentItem" ).mouseover( function() {
  	var id = $( this ).attr( "name" );
  	if ( currentlySelectedId != id ) { // Ignore if currently selected
    	$( this ).css( "background-color", COLOR_HOVER );
  	}
  });
  
  $( "li.clipItem, li.documentItem" ).mouseout( function() {
  	var id = $( this ).attr( "name" );
  	if ( currentlySelectedId != id ) { // Ignore if currently selected
	    $( this ).css( "background-color", COLOR_PASSIVE );
  	}
  });
  
  $( "li.clipItem" ).click( function() {
  	currentlySelectedId = $( this ).attr( "name" );
  	
  	$( "li.clipItem" ).css( "background-color", COLOR_PASSIVE ); // Set all to passive
  	
  	$( this ).css( "background-color", COLOR_SELECTED ); // Set this to active
  	
  	playVideo( currentlySelectedId );
  } );
    
  $( "li.documentItem" ).click( function() {
  	currentlySelectedId = $( this ).attr( "name" );
  	
  	$( "li.documentItem" ).css( "background-color", COLOR_PASSIVE ); // Set all to passive
  	
  	$( this ).css( "background-color", COLOR_SELECTED ); // Set this to active
  	
  	displayDocument( currentlySelectedId );
  } );
}

function initReferences()
{
	$( '#query' ).bind( 'keypress', function( event ) {
		if ( event && event.which == 13 ) {
			searchReferences(); } } );
	
	$( '#queryType' ).selectmenu();
	
	$( '#searchButton' ).button();
	$( '#categoryButton' ).button();
	
	$( '#query' ).autocomplete( { 
		minLength: 0,
		source:	function( request, response ) {
        	$.postJSON( 'tags', { term:extractLast( request.term ) }, function( data ) { response( data ); } );
		},
		focus: function() {				
			return false; // prevent value inserted on focus
		},
		select: function( event, ui ) {
			var terms = split( this.value ); // split input to array		
			terms.pop(); // remove the current input			
			terms.push( ui.item.value ); // add the selected item			
			terms.push( "" ); // add placeholder to get the comma-and-space at the end			
			this.value = terms.join( ", " ); // set new input
			
			return false;
		}
	} );
	
	$( '#search' ).load( 'latestClips' );
	
	$.getJSON( 'latestClipId', function( id ) { 
		latestClipId = id; 
	} );
}

function initFeedback()
{	
	$( '#eventteam' ).bind( 'keypress', function( event ) {
		if ( event && event.which == 13 ) {
			searchFeedback(); } } );
				
	$( '#person' ).bind( 'keypress', function( event ) {
		if ( event && event.which == 13 ) {
			searchFeedback(); } } );
			
	$( '#playlist' ).bind( 'keypress', function( event ) {
		if ( event && event.which == 13 ) {
			searchFeedback(); } } );
	
	$( '#searchButton' ).button();
	
    $( '#eventteam' ).autocomplete( { source:'eventsteams' } );
    $( '#person' ).autocomplete( { source:'persons' } );
    $( '#playlist' ).autocomplete( { source:'playlists' } );
    
	$( '#category' ).autocomplete( { 
		minLength: 0,
		source:	function( request, response ) {
        	$.postJSON( 'categories', { term:extractLast( request.term ) }, function( data ) { response( data ); } );
		},
		focus: function() {				
			return false; // prevent value inserted on focus
		},
		select: function( event, ui ) {
			var terms = split( this.value ); // split input to array		
			terms.pop(); // remove the current input			
			terms.push( ui.item.value ); // add the selected item			
			terms.push( "" ); // add placeholder to get the comma-and-space at the end			
			this.value = terms.join( ", " ); // set new input
			
			return false;
		}
	} );
	
	$( '#search' ).load( 'latestClips' );
	
	$.getJSON( 'latestClipId', function( id ) { 
		latestClipId = id; 
	} );
}

// -----------------------------------------------------------------------------
// CategoryTree
// -----------------------------------------------------------------------------

function displayCategoryOverview()
{
	if ( $f( 'player' ) )
    {
    	$f( 'player' ).stop();
    }
    
	$( '#intro' ).css( 'display', 'none' );
	$( '#tree' ).css( 'display', 'block' );
	$( '#video' ).css( 'display', 'none' );
	$( '#document' ).css( 'display', 'none' );
	
	$( '#tree' ).load( 'categoryOverview' );
}

/**
 * NOTE not in use at the moment
 */
function displayCategoryTree()
{
	if ( $f( 'player' ) )
    {
    	$f( 'player' ).stop();
    }
    
	$( '#intro' ).css( 'display', 'none' );
	$( '#tree' ).css( 'display', 'block' );
	$( '#video' ).css( 'display', 'none' );
	$( '#document' ).css( 'display', 'none' );
	
	$( '#tree' ).jstree({ 
		'html_data' : {
			'ajax' : {
				'url' : 'categoryTree'
			}
		},
		'plugins' : [ 'themes', 'html_data', 'cookies' ]
	});
}

// -----------------------------------------------------------------------------
// Search
// -----------------------------------------------------------------------------

function searchAssociations( code )
{
	if ( !isEmpty( code ) )
	{
		var url = 'asearch?code=' + code;
		$( '#search' ).load( url );
	}
}

function searchQuery( query )
{
	if ( !isEmpty( query ) )
	{
		$.postUTF8( 'rsearch', { "query":query }, function( data ) {
			$( '#search' ).html( data );
		} );
	}
}

function searchPagedQuery( query, queryType, page )
{
	if ( !isEmpty( query ) )
	{
		$.postUTF8( 'rsearch', { "query":query, "queryType":queryType, "page":page }, function( data ) {
			$( '#search' ).html( data );
		} );
	}
}

function searchHierarchy( id )
{
	var url = 'hsearch?id=' + id;
	
	$( '#search' ).load( url );
}

function searchReferences()
{
	var query = $( '#query' ).val();
	var queryType = $( '#queryType' ).val();
	
	if ( !isEmpty( query ) )
	{
		$.postUTF8( 'rsearch', { "query":query, "queryType":queryType }, function( data ) {
			$( '#search' ).html( data );
		} );
	}
}

function searchFeedback()
{
	var eventteam = $( '#eventteam' ).val();	
	var person = $( '#person' ).val();	
	var category = $( '#category' ).val();
	var playlist = $( '#playlist' ).val();
	
	if ( !isEmpty( eventteam ) || !isEmpty( person ) || !isEmpty( category ) || !isEmpty( playlist ) )
	{
		$.postUTF8( 'fsearch', { "eventteam":eventteam, "person":person, "category":category, "playlist":playlist }, function( data ) {
			 $( 'div#search' ).html( data );
		} );
	}
}

function searchStandard()
{
	$( '#category' ).autocomplete( "search", ":standard" );
}

function searchAll()
{
	$( '#category' ).autocomplete( "search", ":all" );
}

function searchMyOwn()
{
	$( '#playlist').autocomplete( "search", ":myown" );
}

function searchShared()
{
	$( '#playlist').autocomplete( "search", ":shared" );
}

function clearFeedbackFields()
{
	$( '#eventteam' ).val( '' );
	$( '#person' ).val( '' );
	$( '#category' ).val( '' );
	$( '#playlist' ).val( '' );
}

// -----------------------------------------------------------------------------
// Player options
// -----------------------------------------------------------------------------

function playLatestVideo()
{
	playVideo( latestClipId );
}

function playVideoByCode( code )
{
	$.getJSON( 'aclip?code=' + code, function( clip ) {
		playVideoInternal( clip );
	} );
}

function playVideo( id )
{
	$.getJSON( 'clip?id=' + id, function( clip ) {
		playVideoInternal( clip );
	} );
}

function playVideoInternal( clip )
{
	$( '#intro' ).css( 'display', 'none' );
	$( '#tree' ).css( 'display', 'none' );
	$( '#video' ).css( 'display', 'block' );
	$( '#document' ).css( 'display', 'none' );
	
	var url = staticBaseUrl + clip.filename;
	
    $f( 'player', { 
    	src: 'scripts/flowplayer.swf?c=' + (new Date()).getTime(), wmode: 'opaque' }, {
    	clip: {
    		url: url,
    		provider: 'lighttpd'
    	},
    	plugins: {
    		lighttpd: {
    			url: 'scripts/flowplayer.pseudostreaming.swf'
    		}
    	}    	
    } );
        
    $f( 'player' ).play();
    
    var playerEmbed = '<object id="flowplayer" width="600" height="337" data="http://releases.flowplayer.org/swf/flowplayer-3.2.5.swf" ' +
    		'type="application/x-shockwave-flash"><param name="movie" value="http://releases.flowplayer.org/swf/flowplayer-3.2.5.swf" />' +
    		'<param name="allowfullscreen" value="true" />' +
    		'<param name="flashvars" value=\'config={"clip":{"url":"' + url + '","autoPlay":false}}\' /></object>';
    
    $( '#playerEmbed' ).text( playerEmbed );
    
    var playerUrl = baseUrl + 'play?code=' + clip.code;
    
    $( '#playerUrl' ).text( playerUrl );
    
    $( '#playerTags' ).load( 'myTags?clipId=' + clip.id );
    
    $( '#playerComments' ).load( 'comments?clipId=' + clip.id );
    
    $( '#playerPlaylist' ).load( 'myPlaylists?clipId=' + clip.id );    
}

function togglePlayerEmbed()
{
	$( '#playerEmbed' ).slideToggle( 'fast' );
	$( '#playerUrl' ).hide();
	$( '#playerTags' ).hide();
	$( '#playerPlaylist' ).hide();
	$( '#playerComments' ).hide();
}

function togglePlayerUrl()
{
	$( '#playerEmbed' ).hide();
	$( '#playerUrl' ).slideToggle( 'fast' );
	$( '#playerTags' ).hide();
	$( '#playerPlaylist' ).hide();
	$( '#playerComments' ).hide();
}

function togglePlayerTags()
{
	$( '#playerEmbed' ).hide();
	$( '#playerUrl' ).hide();
	$( '#playerTags' ).slideToggle( 'fast' );
	$( '#playerPlaylist' ).hide();
	$( '#playerComments' ).hide();
}

function togglePlayerPlaylist()
{
	$( '#playerEmbed' ).hide();
	$( '#playerUrl' ).hide();
	$( '#playerTags' ).hide();
	$( '#playerPlaylist' ).slideToggle( 'fast' );
	$( '#playerComments' ).hide();
}

function togglePlayerComments()
{
	$( '#playerEmbed' ).hide();
	$( '#playerUrl' ).hide();
	$( '#playerTags' ).hide();
	$( '#playerPlaylist' ).hide();
	$( '#playerComments' ).slideToggle( 'fast' );
}

// -----------------------------------------------------------------------------
// Playlist
// -----------------------------------------------------------------------------

function addToPlaylist( playlistId, clipId )
{
	$.getJSON( 'addToPlaylist?playlistId=' + playlistId + '&clipId=' + clipId, function( result ) {
		if ( result ) {
			$( '#playlist' + playlistId ).css( 'background-color', '#b6dcb6' );
			document.getElementById( 'playlist' + playlistId ).onclick = function() { removeFromPlaylist( playlistId, clipId ); };
		} 
	} );
}

function removeFromPlaylist( playlistId, clipId )
{
	$.getJSON( 'removeFromPlaylist?playlistId=' + playlistId + '&clipId=' + clipId, function( result ) {
		if ( result ) {
			$( '#playlist' + playlistId ).css( 'background-color', '#f3f3f3' );
			document.getElementById( 'playlist' + playlistId ).onclick = function() { addToPlaylist( playlistId, clipId ); };
		} 
	} );
}

function searchMyPlaylists( clipId )
{
	var term = $( '#playlistTerm' ).val();
	
	if ( !isEmpty( term ) )
	{
		$.postJSON( 'searchMyPlaylists', { "term":term, "clipId":clipId }, function( data ) {
			$( '#playerPlaylist' ).html( data );
		} );
	}
}

function saveMyPlaylist( clipId )
{
	var name = $( '#playlistTerm' ).val();
	
	if ( !isEmpty( name ) )
	{
		$.postJSON( 'saveMyPlaylist', { "name":name }, function( result ) {
			if ( result ) {
				$( 'div#playerPlaylist' ).load( 'myPlaylists?clipId=' + clipId );
			}
		} );
	}
}

// -----------------------------------------------------------------------------
// Comment
// -----------------------------------------------------------------------------

function saveComment( clipId )
{
	var comment = $( '#commentField' ).val();
	
	if ( !isEmpty( comment ) )
	{
		$.postJSON( 'saveComment', { "clipId":clipId, "body":comment }, function( result ) {
			if ( result ) {
				$( '#playerComments' ).load( 'comments?clipId=' + clipId );
			}
		} );
	}
}

function deleteComment( clipId, commentId )
{
	$.getJSON( 'deleteComment', { clipId:clipId, commentId:commentId }, function( result ) {
		if ( result ) {
			$( '#playerComments' ).load( 'comments?clipId=' + clipId );
		}
	} );
}

// -----------------------------------------------------------------------------
// Document
// -----------------------------------------------------------------------------

function displayDocument( id )
{
	$.getJSON( 'doc?id=' + id, function( doc ) {
		displayDocumentInternal( doc );
	} );
}

function displayDocumentInternal( doc )
{
	$( '#intro' ).css( 'display', 'none' );
	$( '#tree' ).css( 'display', 'none' );
	$( '#video' ).css( 'display', 'none' );
	$( '#document' ).css( 'display', 'block' );
	
	var url = 'document?id=' + doc.id;
	
	$( '#viewer' ).load( url );
	
	var embed = '<a href="' + staticBaseUrl + doc.path + '">' + doc.title + '</a>';
	
	$( '#viewerEmbed' ).text( embed );
}

function toggleViewerEmbed()
{
	$( '#viewerEmbed' ).slideToggle( 'fast' );
}

// -----------------------------------------------------------------------------
// Playlist
// -----------------------------------------------------------------------------

function selectAllOptions( list )
{
	$( '#' + list ).each( function() {
    	$( '#' + list + ' option' ).attr( 'selected', 'selected' );
	} );
}

function submitPlaylistForm()
{
	selectAllOptions( 'selectedUsers' );
	
	$( '#playlistForm' ).submit();
}

// -----------------------------------------------------------------------------
// View mode
// -----------------------------------------------------------------------------

function changeViewMode()
{
	if ( viewMode == 0 ) // Large
	{
		$( '#container' ).css( 'width', '1340px' );
		$( '#container' ).css( 'margin-left', '-670px' );
		$( '#player' ).css( 'width', '1020px' );
		$( '#player' ).css( 'height', '573px' );
		$( '#player' ).css( 'background-image', 'url(images/introLarge.jpg)' );
		$( '#playerOptions' ).css( 'top', '689px' );
		$( '#playerEmbed' ).css( 'top', '727px' );
		$( '#playerUrl' ).css( 'top', '727px' );
		$( '#playerPlaylist' ).css( 'top', '727px' );
		$( '#playerComments' ).css( 'top', '727px' );
		$( '#playerTags' ).css( 'top', '727px' );
		
		viewMode = 1;
	}
	else // Small
	{
		$( '#container' ).css( 'width', '1000px' );
		$( '#container' ).css( 'margin-left', '-500px' );
		$( '#player' ).css( 'width', '700px' );
		$( '#player' ).css( 'height', '394px' );
		$( '#player' ).css( 'background-image', 'url(images/intro.jpg' );
		$( '#playerOptions' ).css( 'top', '510px' );
		$( '#playerEmbed' ).css( 'top', '548px' );
		$( '#playerUrl' ).css( 'top', '548px' );
		$( '#playerPlaylist' ).css( 'top', '548px' );
		$( '#playerComments' ).css( 'top', '548px' );
		$( '#playerTags' ).css( 'top', '548px' );
		
		viewMode = 0;
	}
}

// -----------------------------------------------------------------------------
// Clip operations
// -----------------------------------------------------------------------------

function updateClip( id )
{
	window.location.href = 'videoUpload?id=' + id;
}

function removeClip( id )
{
	$( '#confirmRemoveDiv' ).dialog( { title: 'Bekreft sletting', buttons: { 'OK': function() {
		window.location.href = 'deleteVideo?id=' + id;
	}, "Avbryt": function() {
		$( this ).dialog( 'close' );
	} }
	} );
}

function updateDocument( id )
{
	window.location.href = 'documentUpload?id=' + id;
}

function removeDocument( id )
{
	var ok = confirm( 'Vil du slette dette dokumentet?' );
	
	if ( ok )
	{
		window.location.href = 'deleteDocument?id=' + id;
	}
}

// -----------------------------------------------------------------------------
// Delete
// -----------------------------------------------------------------------------

function removeObject( url, name )
{
	$( '#confirmDeleteDiv' ).dialog( { title: name, buttons: { 'OK': function() {
		window.location.href = url; 
	}, "Avbryt": function() {
		$( this ).dialog( 'close' );
	} }
	} );
}

// -----------------------------------------------------------------------------
// Supportive methods
// -----------------------------------------------------------------------------

function isEmpty( val )
{
  return ( val == null || val.length == 0 );
}

function split( val ) 
{
	return val.split( /,\s*/ );
}

function extractLast( term )
{
	return split( term ).pop();
}

function emptyIfNull( val )
{
	return val == null ? '' : val;
}

function prependToList( listId, val, lab )
{
	$( '#' + listId ).prepend( '<option value="' + val + '">' + lab + '</option>' );
}

function appendToList( listId, val, lab )
{
	$( '#' + listId ).append( '<option value="' + val + '">' + lab + '</option>' );
}

function removeSelectedFromList( listId )
{
	$( '#' + listId + ' option:selected' ).remove();
}

function valueExists( listId, val )
{
	return $( '#' + listId + ' option[value="' + val + '"]' ).length > 0;
}

function moveOption( fromListId, toListId )
{
	var val = $( '#' + fromListId + ' option:selected' ).val();
	var lab = $( '#' + fromListId + ' option:selected' ).text();
	
	appendToList( toListId, val, lab );
	removeSelectedFromList( fromListId );
}
