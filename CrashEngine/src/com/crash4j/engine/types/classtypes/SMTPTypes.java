/**
 * @copyright
 */
package com.crash4j.engine.types.classtypes;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Stack;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.actions.ActionImpl;
import com.crash4j.engine.spi.instrument.handlers.RecognizedProtocolHandler;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.HttpResourceSpiImpl;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.resources.impl.SMTPResourceSpiImpl;
import com.crash4j.engine.spi.traits.LifecycleHandler;
import com.crash4j.engine.spi.traits.ProtocolBasedResourceFacotry;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.ResourceTypes;

/**
 * SMTP protocol types
 * @author <crash4j team>
 * 
 */
public enum SMTPTypes  implements ProtocolBasedResourceFacotry
{
	// Sent by a client to identify itself, usually with a domain name.
	HELO,
	// Enables the server to identify its support for Extended Simple Mail
	// Transfer Protocol (ESMTP) commands.
	EHLO,
	// Identifies the sender of the message; used in the form MAIL FROM:.
	MAILFROM,
	// Identifies the message recipients; used in the form RCPT TO:.
	RCPTTO,
	// Allows the client and server to switch roles and send mail in the reverse
	// direction without having to establish a new connection.
	TURN,
	// The ATRN (Authenticated TURN) command optionally takes one or more
	// domains as a parameter. The ATRN command must be rejected if the session
	// has not been authenticated.
	ATRN,
	// Provides a mechanism by which the SMTP server can indicate the maximum
	// size message supported. Compliant servers must provide size extensions to
	// indicate the maximum size message that can be accepted. Clients should
	// not send messages that are larger than the size indicated by the server.
	SIZE,
	// An extension of SMTP. ETRN is sent by an SMTP server to request that
	// another server send any e-mail messages that it has.
	ETRN,
	// Provides the ability to send a stream of commands without waiting for a
	// response after each command.
	PIPELINING,
	// An ESMTP command that replaces the DATA command. So that the SMTP host
	// does not have to continuously scan for the end of the data, this command
	// sends a BDAT command with an argument that contains the total number of
	// bytes in a message. The receiving server counts the bytes in the message
	// and, when the message size equals the value sent by the BDAT command, the
	// server assumes it has received all of the message data.
	CHUNKING,
	// Sent by a client to initiate the transfer of message content.
	DATA,
	// An ESMTP command that enables delivery status notifications.
	DSN,
	// Nullifies the entire message transaction and resets the buffer.
	RSET,
	// Verifies that a mailbox is available for message delivery; for example,
	// vrfy ted verifies that a mailbox for Ted resides on the local server.
	// This command is off by default in Exchange implementations.
	VRFY,
	// Returns a list of commands that are supported by the SMTP service.
	HELP,
	// Terminates the session.
	QUIT,
	// A method that is used by Microsoft Exchange Server 2003 and Exchange 2000
	// Server servers to authenticate.
	XEXPSGSSAPI,
	// A method that is used by Exchange 2000 and Exchange 2003 servers to
	// authenticate.
	XEXPS,
	// Provides the ability to propagate message properties during
	// server-to-server communication.
	XEXCH50,
	// Adds support for link state routing in Exchange.
	XLINK2STATE;

	/** mul^2 mask supports combinations */
	private int mask = 0;
	private ResourceSpec spec = null;

	/**
	 * @param action
	 */
	private SMTPTypes() 
	{
		this.mask = (int) Math.pow(2, (this.ordinal() + 1));
        this.spec = ResourceManagerSpi.createSpec(ActionClasses.SMTP.name(), name(), "", "", false, (LifecycleHandler)new RecognizedProtocolHandler(), 
				new ActionImpl(name(), ActionClasses.SMTP, this), ResourceTypes.SERVICE, null, new HashSet<Class<?>>(), false, 0xFF);;
	}

	public static SMTPTypes fromString(String s) 
	{
		return SMTPTypes.valueOf(s.toUpperCase());
	}

	public int toInt() {
		return this.ordinal();
	}

	public int toMask() {
		return mask;
	}

	@Override
	public ResourceSpi createResource(ResourceSpi owner, Stack<String> params)
			throws IOException 
	{
		if (!(owner instanceof NetworkResourceSpiImpl))
		{
			return null;
		}
		
		// All of this should be hidden below specific filter.
		NetworkResourceSpiImpl netr = (NetworkResourceSpiImpl)owner;
		try
		{
			return new SMTPResourceSpiImpl(this.spec, netr.getHost(), netr.getPort());
		}
		catch (Exception e)
		{
			throw new IOException(e);
		}
	}

	@Override
	public ResourceSpec getSpec() 
	{
		return this.spec;
	}

}
