
function initVideo()
{
	$( '#availableCategories' ).autocomplete( { 
		source:'clipcategories',
		select: function( event, ui ) 
		{
			if( !valueExists( 'selectedCategories', ui.item.id ) )
			{			
				prependToList( 'selectedCategories', ui.item.id, ui.item.value );
			}
						
			$( '#availableCategories' ).val( '' );
			return false;
		}
	} );
	
	$( '#availablePersons' ).autocomplete( { 
		source:'persons',
		select: function( event, ui ) 
		{
			if( !valueExists( 'selectedPersons', ui.item.id ) )
			{			
				prependToList( 'selectedPersons', ui.item.id, ui.item.value );
			}
						
			$( '#availablePersons' ).val( '' );
			return false;
		}
	} );
}

function initDocument()
{
	$( '#availableCategories' ).autocomplete( { 
		source:'categories',
		select: function( event, ui ) 
		{
			if( !valueExists( 'selectedCategories', ui.item.id ) )
			{			
				prependToList( 'selectedCategories', ui.item.id, ui.item.value );
			}
						
			$( '#availableCategories' ).val( '' );
			return false;
		}
	} );
}

// -----------------------------------------------------------------------------
// Video
// -----------------------------------------------------------------------------

function submitVideo()
{
	selectAllOptions( 'selectedCategories' );
	selectAllOptions( 'selectedPersons' );
	$( '#videoForm' ).submit();
}

// -----------------------------------------------------------------------------
// Document
// -----------------------------------------------------------------------------

function submitDocument()
{
	selectAllOptions( 'selectedCategories' );
	$( '#documentForm' ).submit();
}