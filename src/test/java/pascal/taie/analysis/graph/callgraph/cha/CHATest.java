/*
 * Tai-e: A Static Analysis Framework for Java
 *
 * Copyright (C) 2020-- Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020-- Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * Tai-e is only for educational and academic purposes,
 * and any form of commercial use is disallowed.
 * Distribution of Tai-e is disallowed without the approval.
 */

package pascal.taie.analysis.graph.callgraph.cha;

import org.junit.Test;
import pascal.taie.TestUtils;

public class CHATest {

    @Test
    public void testStaticCall() {
        TestUtils.testCHA("StaticCall");
    }

    @Test
    public void testSpecialCall() {
        TestUtils.testCHA("SpecialCall");
    }

    @Test
    public void testVirtualCall() {
        TestUtils.testCHA("VirtualCall");
    }

    @Test
    public void testInterface() {
        TestUtils.testCHA("Interface");
    }
}