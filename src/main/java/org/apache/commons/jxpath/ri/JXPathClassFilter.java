/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jxpath.ri;

/**
 * Class filter (optional) to be used by JXPath.
 *
 * System property "jxpath.class.deny" can be set to specify the list of restricted classnames.
 * This property takes a list of java classnames (use comma as separator to specify more than one class).
 * If this property is not set, it exposes any java class to javascript
 * Ex: jxpath.class.deny=java.lang.Runtime will deny exposing java.lang.Runtime class via xpath, while all other classes will be exposed.
 *
 * @author bhmohanr-techie
 * @version $Revision$ $Date$
 */
public interface JXPathClassFilter {

    /**
     * Should the Java class of the specified name be exposed via xpath?
     * @param className is the fully qualified name of the java class being
     * checked. This will not be null. Only non-array class names will be
     * passed.
     * @return true if the java class can be exposed via xpath, false otherwise
     */
    public boolean exposeToXPath(String className);
}
