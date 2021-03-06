/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.jellytools;

import org.netbeans.junit.NbTest;

/**
 * Test RuntimeTabOperator.
 *
 * @author Jiri Skrivanek
 */
public class RuntimeTabOperatorTest extends JellyTestCase {

    private static RuntimeTabOperator runtimeOper;
    public static String[] tests = {
        "testInvoke",
        "testTree",
        "testGetRootNode",
        "testVerify"
    };

    public RuntimeTabOperatorTest(java.lang.String testName) {
        super(testName);
    }

    public static NbTest suite() {
        return (NbTest) createModuleTest(RuntimeTabOperatorTest.class, tests);
    }

    /** Print out test name. */
    @Override
    public void setUp() {
        System.out.println("### " + getName() + " ###");
    }

    /**
     * Test of invoke method.
     */
    public void testInvoke() {
        RuntimeTabOperator.invoke().close();
        runtimeOper = RuntimeTabOperator.invoke();
    }

    /**
     * Test of tree method.
     */
    public void testTree() {
        ProjectsTabOperator.invoke();
        // has to make tab visible
        runtimeOper.tree();
    }

    /**
     * Test of getRootNode method.
     */
    public void testGetRootNode() {
        runtimeOper.getRootNode();
    }

    /**
     * Test of verify method.
     */
    public void testVerify() {
        runtimeOper.verify();
    }
}
