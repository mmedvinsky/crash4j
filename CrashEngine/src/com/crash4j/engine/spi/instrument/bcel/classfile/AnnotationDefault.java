/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.crash4j.engine.spi.instrument.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.crash4j.engine.spi.instrument.bcel.Constants;

/**
 * represents the default value of a annotation for a method info
 * 
 * @version $Id: AnnotationDefault 1 2005-02-13 03:15:08Z dbrosius $
 * @author <A HREF="mailto:dbrosius@qis.net">D. Brosius</A>
 * @since 5.3
 */
public class AnnotationDefault extends Attribute
{
        private static final long serialVersionUID = -4017327188724019487L;
    ElementValue default_value;

        /**
         * @param name_index
         *            Index pointing to the name <em>Code</em>
         * @param length
         *            Content length in bytes
         * @param file
         *            Input stream
         * @param constant_pool
         *            Array of constants
         */
        public AnnotationDefault(int name_index, int length,
                        DataInputStream file, ConstantPool constant_pool)
                        throws IOException
        {
                this(name_index, length, (ElementValue) null,
                                constant_pool);
                default_value = ElementValue.readElementValue(file, constant_pool);
        }

        /**
         * @param name_index
         *            Index pointing to the name <em>Code</em>
         * @param length
         *            Content length in bytes
         * @param defaultValue
         *            the annotation's default value
         * @param constant_pool
         *            Array of constants
         */
        public AnnotationDefault(int name_index, int length,
                        ElementValue defaultValue, ConstantPool constant_pool)
        {
                super(Constants.ATTR_ANNOTATION_DEFAULT, name_index, length, constant_pool);
                setDefaultValue(defaultValue);
        }

        /**
         * Called by objects that are traversing the nodes of the tree implicitely
         * defined by the contents of a Java class. I.e., the hierarchy of methods,
         * fields, attributes, etc. spawns a tree of objects.
         * 
         * @param v
         *            Visitor object
         */
        @Override
        public void accept(Visitor v)
        {
                // v.visitAnnotationDefault(this);
        }

        /**
         * @param defaultValue
         *            the default value of this methodinfo's annotation
         */
        public final void setDefaultValue(ElementValue defaultValue)
        {
                default_value = defaultValue;
        }

        /**
         * @return the default value
         */
        public final ElementValue getDefaultValue()
        {
                return default_value;
        }

        @Override
        public Attribute copy(ConstantPool _constant_pool)
        {
                throw new RuntimeException("Not implemented yet!");
        }

    @Override
    public final void dump(DataOutputStream dos) throws IOException
    {
      super.dump(dos);
      default_value.dump(dos);
    }
}
