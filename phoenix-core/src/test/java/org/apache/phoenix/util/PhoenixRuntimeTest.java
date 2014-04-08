/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.phoenix.util;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.apache.phoenix.query.BaseConnectionlessQueryTest;
import org.apache.phoenix.query.QueryConstants;
import org.apache.phoenix.schema.PColumn;
import org.apache.phoenix.schema.PTable;
import org.junit.Test;

public class PhoenixRuntimeTest extends BaseConnectionlessQueryTest {
    @Test
    public void testEncodeDecode() throws Exception {
        Connection conn = DriverManager.getConnection(getUrl());
        conn.createStatement().execute(
                "CREATE TABLE t(org_id CHAR(3) not null, p_id CHAR(3) not null, date DATE not null, e_id CHAR(3) not null, old_value VARCHAR, new_value VARCHAR " +
                "CONSTRAINT pk PRIMARY KEY (org_id, p_id, date, e_id))");
        Date date = new Date(System.currentTimeMillis());
        Object[] expectedValues = new Object[] {"abc", "def", date, "123"};
        byte[] value = PhoenixRuntime.encodePK(conn, "T", expectedValues);
        Object[] actualValues = PhoenixRuntime.decodePK(conn, "T", value);
        assertEquals(Arrays.asList(expectedValues), Arrays.asList(actualValues));
    }
    
    @Test
    public void testColumnFamilyParsing() {
    	String columnName = "F.C";
    	assertTrue(columnName.contains(QueryConstants.NAME_SEPARATOR));
		String[] tokens = columnName.split(QueryConstants.NAME_SEPARATOR_REGEX);
    	assertTrue(tokens.length==2);
    	
		String familyName = tokens[0];
		String familyColumn = tokens[1];
		assertEquals("F",familyName);
		assertEquals("C",familyColumn);
		
    }
    
    
    //TODO add tests for the following:
    /*
     * 
       public static List<ColumnInfo> generateColumnInfo(Connection conn,
            String tableName, List<String> columns)
            throws SQLException {...}
            
        public static ColumnInfo getColumnInfo(PTable table, String columnName) throws SQLException {...}
        
        public static ColumnInfo getColumnInfo(PColumn pColumn) throws SQLException {...}
     */
}
