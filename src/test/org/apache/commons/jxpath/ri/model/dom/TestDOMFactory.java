/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//jxpath/src/test/org/apache/commons/jxpath/ri/model/dom/TestDOMFactory.java,v 1.2 2003/01/20 00:00:29 dmitri Exp $
 * $Revision: 1.2 $
 * $Date: 2003/01/20 00:00:29 $
 *
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 2001, Plotnix, Inc,
 * <http://www.plotnix.com/>.
 * For more information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.w3c.dom.Node;

/**
 * Test AbstractFactory.
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 1.2 $ $Date: 2003/01/20 00:00:29 $
 */
public class TestDOMFactory extends AbstractFactory {

    /**
     * Return <b>false</b> if this factory cannot create the requested object.
     */
    public boolean createObject(
        JXPathContext context,
        Pointer pointer,
        Object parent,
        String name,
        int index) 
    {
        if (name.equals("location")
            || name.equals("address")
            || name.equals("street")) {
            addDOMElement((Node) parent, index, name);
            return true;
        }
        return false;
    }

    private void addDOMElement(Node parent, int index, String tag) {
        Node child = parent.getFirstChild();
        int count = 0;
        while (child != null) {
            if (child.getNodeName().equals(tag)) {
                count++;
            }
            child = child.getNextSibling();
        }

        // Keep inserting new elements until we have index + 1 of them
        while (count <= index) {
            Node newElement = parent.getOwnerDocument().createElement(tag);
            parent.appendChild(newElement);
            count++;
        }
    }

    public boolean declareVariable(JXPathContext context, String name) {
        return false;
    }
}
