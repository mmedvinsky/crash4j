/**
 * This is a test http parser
 */
package com.crash4j.engine.spi.protocol.impl;


import java.io.Reader;
import java.util.Stack;

import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.classtypes.HTTPTypes;
import com.crash4j.engine.types.classtypes.SMTPTypes;
import com.crash4j.engine.types.classtypes.FTPTypes;

%%

%class GenericProtocolLexer
%public

%{
	/**
	 * Assigns action class. 
	 */
	private ActionClasses actionClass = null;
	/**
	 * ClassType assigned.
	 */
	private Enum<?> classType = null;
	
	private Stack<String> extractions = new Stack<String>();

	/**
	 * Return extracted data as related to ActionClasses and ActionClassTypes.
	 */
	public Stack<String> getExtractions()
	{
		return extractions;
	}
	
	/**
	 * Detected protocol Event
	 */
	public ActionClasses getActionClass()
	{
		return actionClass;
	}	

	/**
	 * Detected protocol Event
	 */
	public Enum<?> getActionClassType()
	{
		return classType;
	}	
		
	public boolean hasProtocol()
	{
		return (this.classType != null && this.actionClass != null);
	}
	
	/**
	 * Reset and point to a new stream to be consumed.
	 */
	public void resetLexer(Reader r)
	{
		actionClass = null;
		classType = null;
		extractions.clear();
		yyreset(r);
	}
%} 

%function scan
%full
%ignorecase


/* ----------------------------------------------------------------------------
 * General definitions. 
 * These apply to a numnber of network RFC's including 2396 (URI), 
 * 2109 (cookie), and 2616 (HTTP1.1).
 *
 * tspecials mapping.
 *
 * asc  dec oct    asc  dec oct    asc  dec oct    asc  dec oct   
 * ------------    ------------    ------------    ------------ 
 * <\t> 009 011    <,>  044 054    <=>  061 075    <\>  092 134
 * < >  032 040    </>  047 057    <>>  062 076    <]>  093 135
 * <">  034 042    <:>  058 072    <?>  063 077    <{>  123 173
 * <(>  040 050    <;>  059 073    <@>  064 100    <}>  125 175
 * <)>  041 051    <<>  060 074    <[>  091 133
 *
 * The TOKEN token, is a strict RFC compliant name/value pattern. 
 */

ANY=.
OCTET=[\000-\177]
CHAR=[\000-\176]
UPALPHA=[A-Z]
LOALPHA=[a-z]
ALPHA=[A-Za-z]
DIGIT=[0-9]
HEX=[A-Fa-f0-9]
ALPHANUM=({ALPHA}|{DIGIT})
CTL=[\000-\037\177]
CR=[\015]
LF=[\012]
SP=[\040]
HT=[\011]
DQ=[\042] 
CRLF={CR}{LF}|{LF}
LWS=({CRLF})({SP}|{HT})*
WS=({SP}|{HT})
SPACES=({SP}|{HT})*
EQUAL={SPACES}"="{SPACES}
TEXT=[\011\040-\176]
QDTEXT=[\011\040\041\043-\176]
QUOTED_STRING={DQ}{QDTEXT}*{DQ}
TSPECIALS=[\t \"\(\),/:;<=>?@\[\\\]\{\}]
TOKEN=[\041\043-\047\052-\053\055\056\060-\071\101-\132\136-\172\174\176]+
DIGITS={DIGIT}+

%x HTTP_CLIENT_METHOD, HTTP_CLIENT_URI, HTTP_CLIENT_VERSION


/** HTTP/1.x protocol recognition */
HTTP_GET="GET"  
HTTP_POST="POST" 
HTTP_PUT="PUT"
HTTP_HEAD="HEAD"
HTTP_OPTIONS="OPTIONS"
HTTP_DELETE="DELETE"
HTTP_TRACE="TRACE"
HTTP_CONNECT="CONNECT"
/**
 * SMPT COMMANDS
 */
//Sent by a client to identify itself, usually with a domain name.
MAIL_HELO="HELO"
//Enables the server to identify its support for Extended Simple Mail Transfer Protocol (ESMTP) commands.
MAIL_EHLO="EHLO"
//Identifies the sender of the message; used in the form MAIL FROM:.
MAIL_MAILFROM="MAIL FROM"
//Identifies the message recipients; used in the form RCPT TO:.
MAIL_RCPTTO="RCPT TO"
//Allows the client and server to switch roles and send mail in the reverse direction without having to establish a new connection.
MAIL_TURN="TURN"
//The ATRN (Authenticated TURN) command optionally takes one or more domains as a parameter. The ATRN command must be rejected if the session has not been authenticated.
MAIL_ATRN="ATRN"
//An extension of SMTP. ETRN is sent by an SMTP server to request that another server send any e-mail messages that it has.
MAIL_ETRN="ETRN"
//Provides the ability to send a stream of commands without waiting for a response after each command.
MAIL_PIPELINING="PIPELINING"
//An ESMTP command that replaces the DATA command. So that the SMTP host does not have to continuously scan for the end of the data, this command sends a BDAT command with an argument that contains the total number of bytes in a message. The receiving server counts the bytes in the message and, when the message size equals the value sent by the BDAT command, the server assumes it has received all of the message data.
MAIL_CHUNKING="CHUNKING"
//Sent by a client to initiate the transfer of message content.
MAIL_DATA="DATA"
//An ESMTP command that enables delivery status notifications.
MAIL_DSN="DSN"
//Nullifies the entire message transaction and resets the buffer.
MAIL_RSET="RSET"
//Verifies that a mailbox is available for message delivery; for example, vrfy ted verifies that a mailbox for Ted resides on the local server. This command is off by default in Exchange implementations.
MAIL_VRFY="VRFY"
//Returns a list of commands that are supported by the SMTP service.
MAIL_HELP="HELP"
//Terminates the session.
MAIL_QUIT="QUIT"
//A method that is used by Microsoft Exchange Server 2003 and Exchange 2000 Server servers to authenticate.
MAIL_XEXPSGSSAPI="X-EXPS GSSAPI"
//A method that is used by Exchange 2000 and Exchange 2003 servers to authenticate.
MAIL_XEXPS=LOGIN="X-EXPS=LOGIN"
//Provides the ability to propagate message properties during server-to-server communication.
MAIL_XEXCH50="X-EXCH50"
//Adds support for link state routing in Exchange.
MAIL_XLINK2STATE="X-LINK2STATE"


/* FTP Commands */

FTP_ABOR="ABOR"
FTP_ACCT="ACCT"
FTP_ADAT="ADAT"
FTP_ALLO="ALLO"
FTP_APPE="APPE"
FTP_AUTH="AUTH"
FTP_CCC="CCC"
FTP_CDUP="CDUP"
FTP_CONF="CONF"
FTP_CWD="CWD"
FTP_DELE="DELE"
FTP_ENC="ENC"
FTP_EPRT="EPRT"
FTP_EPSV="EPSV"
FTP_FEAT="FEAT"
FTP_HELP="HELP"
FTP_LANG="LANG"
FTP_LIST="LIST"
FTP_LPRT="LPRT"
FTP_LPSV="LPSV"
FTP_MDTM="MDTM"
FTP_MIC="MIC"
FTP_MKD="MKD"
FTP_MLSD="MLSD"
FTP_MLST="MLST"
FTP_MODE="MODE"
FTP_NLST="NLST"
FTP_NOOP="NOOP"
FTP_OPTS="OPTS"
FTP_PASS="PASS"
FTP_PASV="PASV"
FTP_PBSZ="PBSZ"
FTP_PORT="PORT"
FTP_PROT="PROT"
FTP_PWD="PWD"
FTP_QUIT="QUIT"
FTP_REIN="REIN"
FTP_REST="REST"
FTP_RETR="RETR"
FTP_RMD="RMD"
FTP_RNFR="RNFR"
FTP_RNTO="RNTO"
FTP_SITE="SITE"
FTP_SMNT="SMNT"
FTP_STAT="STAT"
FTP_STOR="STOR"
FTP_STOU="STOU"
FTP_STRU="STRU"
FTP_SYST="SYST"
FTP_TYPE="TYPE"
FTP_USER="USER"
FTP_XCUP="XCUP"
FTP_XMKD="XMKD"
FTP_XPWD="XPWD"
FTP_XRCP="XRCP"
FTP_XRMD="XRMD"
FTP_XRSQ="XRSQ"
FTP_XSEM="XSEM"
FTP_XSEN="XSEN"/*              */


URI_RESERVED    =     [;/?:@&=+$,]
URI_MARK        =     [-_.!~*'()]
URI_UNRESERVED  =    ({ALPHANUM}|{URI_MARK})
URI_ESCAPED     =    "%"{HEX}{HEX}
URI_URIC        =        ({URI_RESERVED}|{URI_UNRESERVED}|{URI_ESCAPED})

URIC_NO_SLASH   =  ({URI_UNRESERVED}|{URI_ESCAPED}|[;?:@&=+$,])
URI_OPAQUE_PART =  {URIC_NO_SLASH}{URI_URIC}*

URI_IPV4_ADDR   =    {DIGIT}{1,3}"."{DIGIT}{1,3}"."{DIGIT}{1,3}"."{DIGIT}{1,3}
URI_PORT        =     {DIGIT}*
URI_DOMAINLABEL =    {ALPHANUM}|{ALPHANUM}({ALPHANUM}|"-"|"_")*{ALPHANUM}
URI_TOPLABEL    =    {ALPHA}|{ALPHA}({ALPHANUM}|"-"|"_")*{ALPHANUM}
URI_HOSTNAME    =    ({URI_DOMAINLABEL}".")*{URI_TOPLABEL}"."{0,1}
URI_HOST        =    {URI_HOSTNAME}|{URI_IPV4_ADDR}
URI_HOSTPORT    =    {URI_HOST}(":"{URI_PORT}){0,1}

URI_USERINFO    =   ({URI_UNRESERVED}|{URI_ESCAPED}|[;:&=+$,])*
URI_SERVER      =   (({URI_USERINFO}"@"){0,1}{URI_HOSTPORT}){0,1}
URI_REG_NAME    =   ({URI_UNRESERVED}|{URI_ESCAPED}|[$,;:@&=+])+
URI_AUTHORITY   =   ({URI_SERVER}|{URI_REG_NAME})

URI_PCHAR       =  ({URI_UNRESERVED}|{URI_ESCAPED}|[:@&=+$,])
URI_PARAM       =    {URI_PCHAR}*
URI_SEGMENT     =     {URI_PCHAR}*(";"{URI_PARAM})*
URI_PATH_SEGMENTS =  {URI_SEGMENT}("/"{URI_SEGMENT})*
URI_PATH          =      ({URI_ABS_PATH}|{URI_OPAQUE_PART})
URI_REL_SEGMENT   =   ({URI_UNRESERVED}|{URI_ESCAPED}|[;@&=+$,])+

URI_QUERY         =  {URI_URIC}*
URI_FRAGMENT   =     {URI_URIC}*

URI_NET_PATH   =     "//"{URI_AUTHORITY}({URI_ABS_PATH}){0,1}
URI_ABS_PATH   =     "/"{URI_PATH_SEGMENTS}
URI_REL_PATH   =     {URI_REL_SEGMENT}({URI_ABS_PATH}){0,1}
URI_HIER_PART  =     ({URI_NET_PATH}|{URI_ABS_PATH})("?"{URI_QUERY}){0,1}

URI_SCHEME      =    {ALPHA}({ALPHA}|{DIGIT}|[+-.])*


HTTPV           =    "HTTP/"{DIGIT}[.]{DIGIT}
/*
 * The following are not used by the LEX, but show the full composition of 
 * any URI, based on the above tokens.
 */

URI_ABSOLUTE  = ({URI_SCHEME}":"({URI_HIER_PART}|{URI_OPAQUE_PART}))
URI_RELATIVE  = ({URI_NET_PATH}|{URI_ABS_PATH}|{URI_REL_PATH})("?"{URI_QUERY}){0,1}
URI_REFERENCE = ({URI_ABSOLUTE}|{URI_RELATIVE}){0,1}("#"{URI_FRAGMENT}){0,1}


%% 

<HTTP_CLIENT_URI> {
	{URI_REFERENCE}  {
		extractions.push(yytext());
	}
	{SPACES} {
		yybegin(HTTP_CLIENT_VERSION);
	}
}

<HTTP_CLIENT_VERSION> {
	{HTTPV}  {
		actionClass = ActionClasses.HTTP;	
		extractions.push(yytext());
		yybegin(YYINITIAL);
		return null;
	}
}

<HTTP_CLIENT_METHOD> {
	{SPACES} {
		yybegin(HTTP_CLIENT_URI);
	}
}

/**
 * Initial state detection
 */ 
<YYINITIAL> {

	{HTTP_GET}  {
		classType = HTTPTypes.GET;
		yybegin(HTTP_CLIENT_METHOD);		
	}
	{HTTP_POST}  {
		classType = HTTPTypes.POST;	
		yybegin(HTTP_CLIENT_METHOD);		
	}	
	{HTTP_PUT}  {
		classType = HTTPTypes.PUT;		
		yybegin(HTTP_CLIENT_METHOD);		
	}	
	{HTTP_HEAD}  {
		classType = HTTPTypes.HEAD;		
		yybegin(HTTP_CLIENT_METHOD);		
	}	
	{HTTP_OPTIONS}  {
		classType = HTTPTypes.OPTIONS;		
		yybegin(HTTP_CLIENT_METHOD);		
	}	
	{HTTP_DELETE}  {
		classType = HTTPTypes.DELETE;		
		yybegin(HTTP_CLIENT_METHOD);		
	}	
	{HTTP_TRACE}  {
		classType = HTTPTypes.TRACE;		
		yybegin(HTTP_CLIENT_METHOD);		
	}	
	{HTTP_CONNECT}  {
		classType = HTTPTypes.CONNECT;		
		yybegin(HTTP_CLIENT_METHOD);		
	}	
	
	{MAIL_HELO} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.HELO;
		return null;		
	}
	{MAIL_EHLO} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.EHLO;
		return null;		
	}
	{MAIL_MAILFROM} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.MAILFROM;
		return null;		
	}
	{MAIL_RCPTTO} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.RCPTTO;
		return null;		
	}
	{MAIL_TURN} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.TURN;
		return null;		
	}
	{MAIL_ATRN} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.ATRN;
		return null;		
	}
	{MAIL_ETRN} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.ETRN;
		return null;		
	}
	{MAIL_PIPELINING} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.PIPELINING;
		return null;		
	}
	{MAIL_CHUNKING} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.CHUNKING;
		return null;		
	}
	{MAIL_DATA} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.DATA;
		return null;		
	}
	{MAIL_DSN} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.DSN;
		return null;		
	}
	{MAIL_RSET} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.RSET;
		return null;		
	}
	{MAIL_VRFY} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.VRFY;
		return null;		
	}
	{MAIL_HELP} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.HELP;
		return null;		
	}
	{MAIL_QUIT} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.QUIT;
		return null;		
	}
	{MAIL_XEXPSGSSAPI} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.XEXPSGSSAPI;
		return null;		
	}
	{MAIL_XEXPS} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.XEXPS;
		return null;		
	}
	{MAIL_XEXCH50} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.XEXCH50;
		return null;		
	}
	{MAIL_XLINK2STATE} {
		actionClass = ActionClasses.SMTP;	
		classType = SMTPTypes.XLINK2STATE;
		return null;		
	}	



	{FTP_ABOR} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.ABOR;
	      return null;
	}
	{FTP_ACCT} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_ADAT} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_ALLO} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_APPE} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_AUTH} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_CCC} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_CDUP} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.CDUP;
	      return null;
	}
	{FTP_CONF} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_CWD} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.CWD;
	      return null;
	}
	{FTP_DELE} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.DELE;
	      return null;
	}
	{FTP_ENC} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_EPRT} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_EPSV} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_FEAT} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_HELP} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_LANG} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_LIST} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.LIST;
	      return null;
	}
	{FTP_LPRT} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_LPSV} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_MDTM} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_MIC} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_MKD} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.MKD;
	      return null;
	}
	{FTP_MLSD} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.MLSD;
	      return null;
	}
	{FTP_MLST} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.MLST;
	      return null;
	}
	{FTP_MODE} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.MODE;
	      return null;
	}
	{FTP_NLST} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.NLST;
	      return null;
	}
	{FTP_NOOP} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_OPTS} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.OPTS;
	      return null;
	}
	{FTP_PASS} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_PASV} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_PBSZ} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_PORT} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_PROT} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_PWD} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.PWD;
	      return null;
	}
	
	{FTP_QUIT} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_REIN} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.REIN;
	      return null;
	}
	{FTP_REST} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.REST;
	      return null;
	}
	{FTP_RETR} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.RETR;
	      return null;
	}
	{FTP_RMD} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.RMD;
	      return null;
	}
	{FTP_RNFR} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.RNFR;
	      return null;
	}
	{FTP_RNTO} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.RNTO;
	      return null;
	}
	{FTP_SITE} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.SITE;
	      return null;
	}
	{FTP_SMNT} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.SMNT;
	      return null;
	}
	{FTP_STAT} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.STAT;
	      return null;
	}
	{FTP_STOR} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.STOR;
	      return null;
	}
	{FTP_STOU} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.STOU;
	      return null;
	}
	
	{FTP_STRU} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.STRU;
	      return null;
	}
	{FTP_SYST} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.SYST;
	      return null;
	}
	{FTP_TYPE} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.TYPE;
	      return null;
	}
	{FTP_USER} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_XCUP} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.CDUP;
	      return null;
	}
	{FTP_XMKD} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.MKD;
	      return null;
	}
	{FTP_XPWD} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.PWD;
	      return null;
	}
	{FTP_XRCP} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_XRMD} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.RMD;
	      return null;
	}
	{FTP_XRSQ} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_XSEM} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}
	{FTP_XSEN} {
	       actionClass = ActionClasses.FTP;
	      classType = FTPTypes.FTPCMD;
	      return null;
	}

}

