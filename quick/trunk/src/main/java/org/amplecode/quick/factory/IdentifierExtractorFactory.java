package org.amplecode.quick.factory;

/*
 * Copyright (c) 2008, the original author or authors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the AmpleCode project nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import org.amplecode.quick.StatementDialect;
import org.amplecode.quick.identifier.DerbyIdentifierExtractor;
import org.amplecode.quick.identifier.H2IdentifierExtractor;
import org.amplecode.quick.identifier.HsqlIdentifierExtractor;
import org.amplecode.quick.identifier.IdentifierExtractor;
import org.amplecode.quick.identifier.MySqlIdentifierExtractor;
import org.amplecode.quick.identifier.PostgreSqlIdentifierExtractor;

/**
 * @author Lars Helge Overland
 * @version $Id$
 */
public class IdentifierExtractorFactory
{
    /**
     * Creates an IdentifierExtractor instance based on the given dialect.
     * 
     * @param dialect the StatementDialect.
     * @return an IdentifierExtractor instance based on the given dialect.
     */
    public static IdentifierExtractor createIdentifierExtractor( StatementDialect dialect )
    {
        if ( dialect.equals( StatementDialect.MYSQL ) )
        {
            return new MySqlIdentifierExtractor();
        }
        else if ( dialect.equals( StatementDialect.POSTGRESQL ) )
        {
            return new PostgreSqlIdentifierExtractor();
        }
        else if ( dialect.equals( StatementDialect.H2 ) )
        {
            return new H2IdentifierExtractor();
        }
        else if ( dialect.equals( StatementDialect.HSQL ) )
        {
            return new HsqlIdentifierExtractor();
        }
        else if ( dialect.equals( StatementDialect.DERBY ) )
        {
            return new DerbyIdentifierExtractor();
        }
        else
        {
            throw new RuntimeException( "Unsupported dialect: " + dialect );
        }
    }
}
