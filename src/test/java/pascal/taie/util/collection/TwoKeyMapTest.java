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

package pascal.taie.util.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class TwoKeyMapTest {

    @Test
    public void testPut() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        m.put(1, 1, 1);
        m.put(1, 2, 2);
        m.put(3, 4, 12);
        Assert.assertNotNull(m.put(1, 1, 1));
        Assert.assertNull(m.put(3, 6, 18));
        Assert.assertNotNull(m.put(3, 6, 18));
        Assert.assertEquals(4, m.size());
    }

    @Test
    public void testPutAll1() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        m.putAll(1, Map.of(1, 1, 2, 2, 3, 3, 4, 4));
        m.putAll(2, Map.of(1, 2, 2, 4, 3, 6, 4, 8));
        Assert.assertEquals(8, m.size());
    }

    @Test
    public void testPutAll2() {
        TwoKeyMap<Integer, Integer, Integer> m1 = Maps.newTwoKeyMap();
        m1.putAll(1, Map.of(1, 1, 2, 2, 3, 3, 4, 4));
        m1.putAll(2, Map.of(1, 2, 2, 4, 3, 6, 4, 8));

        TwoKeyMap<Integer, Integer, Integer> m2 = Maps.newTwoKeyMap();
        m2.putAll(3, Map.of(1, 3, 2, 6, 3, 9, 4, 12, 5, 15));
        m1.putAll(m2);
        Assert.assertEquals(13, m1.size());
    }

    @Test
    public void testRemove() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        m.put(1, 1, 1);
        m.put(1, 2, 2);
        m.put(3, 4, 12);
        Assert.assertEquals(3, m.size());
        Assert.assertEquals(1, (int) m.remove(1, 1));
        Assert.assertEquals(2, m.size());
        Assert.assertNull(m.remove(1, 1));
        Assert.assertEquals(2, m.size());
        Assert.assertEquals(2, (int) m.remove(1, 2));
        Assert.assertEquals(1, m.size());
        Assert.assertEquals(12, (int) m.remove(3, 4));
        Assert.assertEquals(0, m.size());
        Assert.assertTrue(m.isEmpty());
    }

    @Test
    public void testRemoveAll() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        m.putAll(1, Map.of(1, 1, 2, 2, 3, 3, 4, 4));
        m.putAll(2, Map.of(1, 2, 2, 4, 3, 6, 4, 8));
        m.putAll(3, Map.of(1, 3, 2, 6, 3, 9, 4, 12, 5, 15));

        Assert.assertFalse(m.removeAll(4));
        Assert.assertTrue(m.removeAll(1));
        Assert.assertEquals(9, m.size());
        Assert.assertTrue(m.removeAll(2));
        Assert.assertEquals(5, m.size());
        Assert.assertTrue(m.removeAll(3));
        Assert.assertEquals(0, m.size());
        Assert.assertFalse(m.removeAll(1));
    }

    @Test
    public void testGet() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        m.putAll(1, Map.of(1, 1, 2, 2, 3, 3, 4, 4));
        m.putAll(2, Map.of(1, 2, 2, 4, 3, 6, 4, 8));
        m.putAll(3, Map.of(1, 3, 2, 6, 3, 9, 4, 12, 5, 15));

        Assert.assertEquals(1, (int) m.get(1, 1));
        Assert.assertEquals(8, (int) m.get(2, 4));
        Assert.assertEquals(12, (int) m.get(3, 4));
        Assert.assertNull(m.get(10, 10));
    }

    @Test
    public void testContainsKey() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        m.putAll(1, Map.of(1, 1, 2, 2, 3, 3, 4, 4));
        m.putAll(2, Map.of(1, 2, 2, 4, 3, 6, 4, 8));

        Assert.assertTrue(m.containsKey(1, 1));
        Assert.assertTrue(m.containsKey(2));
        Assert.assertTrue(m.containsValue(8));

        Assert.assertFalse(m.containsKey(1, 2333));
        Assert.assertFalse(m.containsKey(2333));
        Assert.assertFalse(m.containsValue(2333));

        m.remove(1, 1);
        Assert.assertFalse(m.containsKey(1, 1));
    }

    @Test
    public void testEquals() {
        TwoKeyMap<Integer, Integer, Integer> m1 = Maps.newTwoKeyMap();
        m1.putAll(10, Map.of(1, 10, 2, 20, 3, 30));
        m1.putAll(11, Map.of(4, 44, 5, 55, 6, 66));

        TwoKeyMap<Integer, Integer, Integer> m2 = Maps.newTwoKeyMap();
        m2.putAll(m1);
        Assert.assertEquals(m1, m2);
        m2.remove(11, 4);
        Assert.assertNotEquals(m1, m2);
        m2.put(11, 4, 44);
        Assert.assertEquals(m1, m2);
        m2.put(12, 5, 60);
        Assert.assertNotEquals(m1, m2);

        m1.clear();
        Assert.assertNotEquals(m1, m2);
        m2.clear();
        Assert.assertEquals(m1, m2);
    }

    @Test
    public void testEntrySet() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        var entrySet = m.entrySet();
        m.put(1, 1, 1);
        m.putAll(10, Map.of(1, 10, 2, 20, 3, 30));
        m.putAll(11, Map.of(4, 44, 5, 55, 6, 66));
        Assert.assertEquals(7, entrySet.size());
        m.removeAll(10);
        Assert.assertEquals(4, entrySet.size());
        m.clear();
        Assert.assertTrue(entrySet.isEmpty());
    }

    @Test
    public void testTwoKeySet() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        var twoKeySet = m.twoKeySet();
        m.put(1, 1, 1);
        m.putAll(10, Map.of(1, 10, 2, 20, 3, 30));
        m.putAll(11, Map.of(4, 44, 5, 55, 6, 66));
        Assert.assertEquals(7, twoKeySet.size());
        m.removeAll(10);
        Assert.assertEquals(4, twoKeySet.size());
        m.clear();
        Assert.assertTrue(twoKeySet.isEmpty());
    }

    @Test
    public void testValues() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        var values = m.values();
        m.put(1, 1, 1);
        m.putAll(10, Map.of(1, 10, 2, 20, 3, 30));
        m.putAll(11, Map.of(4, 44, 5, 55, 6, 66));
        Assert.assertEquals(7, values.size());
        m.removeAll(10);
        Assert.assertEquals(4, values.size());
        m.clear();
        Assert.assertTrue(values.isEmpty());
    }

    @Test
    public void testGetOrDefault() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        Assert.assertEquals(777, (int) m.getOrDefault(1, 1, 777));
        m.put(1, 1, 1);
        Assert.assertEquals(1, (int) m.getOrDefault(1, 1, 777));
    }

    @Test
    public void testComputeIfAbsent() {
        TwoKeyMap<Integer, Integer, Integer> m = Maps.newTwoKeyMap();
        Assert.assertEquals(777,
                (int) m.computeIfAbsent(1, 1, (k1, k2) -> 777));
        Assert.assertEquals(777, (int) m.get(1, 1));
    }
}
