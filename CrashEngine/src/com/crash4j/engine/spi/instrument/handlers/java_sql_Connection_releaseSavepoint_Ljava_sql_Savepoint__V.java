/**
 * @copyright
 */

package com.crash4j.engine.spi.instrument.handlers;
import java.net.URISyntaxException;

import com.crash4j.engine.Action;
import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.JDBCResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.types.ActionClasses;

/**
 * Resource factory implementation for java.sql.Connection#releaseSavepoint(Ljava/sql/Savepoint;)V
 */
public class java_sql_Connection_releaseSavepoint_Ljava_sql_Savepoint__V extends java_sql_Base 
implements ResourceBuilder
{
}
