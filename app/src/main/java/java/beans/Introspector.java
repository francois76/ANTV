/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package java.beans;

/**
 * The <code>Introspector</code> is a utility for developers to figure out
 * which properties, events, and methods a JavaBean supports.
 * <p>
 * The <code>Introspector</code> class walks over the class/superclass chain
 * of the target bean class. At each level it checks if there is a matching
 * <code>BeanInfo</code> class which provides explicit information about the
 * bean, and if so uses that explicit information. Otherwise it uses the low
 * level reflection APIs to study the target class and uses design patterns to
 * analyze its behaviour and then proceeds to continue the introspection with
 * its baseclass.
 * </p>
 * <p>
 * To look for the explicit information of a bean:
 * </p>
 * <ol>
 * <li>The <code>Introspector</code> appends "BeanInfo" to the qualified name
 * of the bean class, try to use the new class as the "BeanInfo" class. If the
 * "BeanInfo" class exsits and returns non-null value when queried for explicit
 * information, use the explicit information</li>
 * <li>If the first step fails, the <code>Introspector</code> will extract a
 * simple class name of the bean class by removing the package name from the
 * qualified name of the bean class, append "BeanInfo" to it. And look for the
 * simple class name in the packages defined in the "BeanInfo" search path (The
 * default "BeanInfo" search path is <code>sun.beans.infos</code>). If it
 * finds a "BeanInfo" class and the "BeanInfo" class returns non-null value when
 * queried for explicit information, use the explicit information</li>
 * </ol>
 */

public class Introspector extends java.lang.Object {

    private Introspector() {
        super();

    }


    public static String decapitalize(String name) {
        return com.googlecode.openbeans.Introspector.decapitalize(name);
    }

}


