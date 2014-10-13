/**
 * Copyright WalletKey Inc. 
 *
 */

/**
 * get available sessions
 */
function refreshSessions(apik, func)
{
	$.ajax({
		  type: "GET",
		  url: "/crash4j/core/services/sessions/"+apik
		}).done(function( msg ) 
		{
		   func(msg);
		});	
}

/**
 * get available resources
 */
function getResources(sessionId, func)
{
	$.ajax({
		  type: "GET",
		  url: "/crash4j/core/services/resources/"+sessionId
		}).done(function( msg ) 
		{
		   func(msg);
		});	
}