/*
 * Copyright (c) 2017 xiaoniu, Inc. All rights reserved.
 *
 * @author chunlin.li
 *
 */
package com.fast.rpc.sample.server.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述: XXXX
 * <p/>
 * 创建人: chunlin.li
 * <p/>
 * 创建时间: 2018/10/29.
 * <p/>
 * Copyright (c) 深圳市牛鼎丰科技有限公司-版权所有
 */
public class Test {

    public static void main(String[] args) {
        Collections.synchronizedMap(new HashMap<String, String>());
    }


    public static <K,V> Map<K,V> synchronizedMap(Map<K,V> m) {
        return null;
    }
}
