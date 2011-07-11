jQuery.extend( {
	
	postJSON: function( url, data, success ) {
		$.ajax( { url:url, data:data, success:success, type:'post', dataType:'json', contentType:'application/x-www-form-urlencoded;charset=utf-8' } );
	},

	postUTF8: function( url, data, success ) {
		$.ajax( { url:url, data:data, success:success, type:'post', contentType:'application/x-www-form-urlencoded;charset=utf-8' } );
	}
} );