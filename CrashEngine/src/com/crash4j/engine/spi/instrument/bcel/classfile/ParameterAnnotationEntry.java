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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.crash4j.engine.spi.instrument.bcel.Constants;

/**
 * represents one parameter annotation in the parameter annotation table
 *
 * @version $Id: ParameterAnnotationEntry
 * @author  <A HREF="mailto:dbrosius@qis.net">D. Brosius</A>
 * @since 5.3
 */
public class ParameterAnnotationEntry implements Node, Constants {

    private int annotation_table_length;
    private AnnotationEntry[] annotation_table;


    /**
     * Construct object from file stream.
     * @param file Input stream
     * @throws IOException
     */
    ParameterAnnotationEntry(DataInputStream file, ConstantPool constant_pool) throws IOException {
        annotation_table_length = (file.readUnsignedShort());
        annotation_table = new AnnotationEntry[annotation_table_length];
        for (int i = 0; i < annotation_table_length; i++) {
            // TODO isRuntimeVisible
            annotation_table[i] = AnnotationEntry.read(file, constant_pool, false);
        }
    }


    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    public void accept( Visitor v ) {
        // v.visitParameterAnnotationEntry(this);
    }


    /**
     * @return the number of annotation entries in this parameter annotation
     */
    public final int getNumAnnotations() {
        return annotation_table_length;
    }


    /**
     * returns the array of annotation entries in this annotation
     */
    public AnnotationEntry[] getAnnotationEntries() {
        return annotation_table;
    }

    public void dump(DataOutputStream dos) throws IOException {
        dos.writeShort(annotation_table_length);
        for(int i = 0; i < annotation_table_length; i++) {
            annotation_table[i].dump(dos);
        }
    }

  public static ParameterAnnotationEntry[] createParameterAnnotationEntries(Attribute[] attrs) {
      // Find attributes that contain parameter annotation data
      List<ParameterAnnotationEntry> accumulatedAnnotations = new ArrayList<ParameterAnnotationEntry>(attrs.length);
      for (Attribute attribute : attrs) {
          if (attribute instanceof ParameterAnnotations) {
              ParameterAnnotations runtimeAnnotations = (ParameterAnnotations)attribute;
              Collections.addAll(accumulatedAnnotations, runtimeAnnotations.getParameterAnnotationEntries());
          }
      }
      return accumulatedAnnotations.toArray(new ParameterAnnotationEntry[accumulatedAnnotations.size()]);
  }
}

