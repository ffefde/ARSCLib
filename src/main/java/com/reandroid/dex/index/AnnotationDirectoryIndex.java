/*
 *  Copyright (C) 2022 github.com/REAndroid
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reandroid.dex.index;


import com.reandroid.arsc.io.BlockReader;
import com.reandroid.dex.base.DexItem;
import com.reandroid.dex.base.IndirectInteger;

import java.io.IOException;

public class AnnotationDirectoryIndex extends DexItem {

    private final IndirectInteger classOffset;
    private final IndirectInteger fieldCount;
    private final IndirectInteger methodCount;
    private final IndirectInteger parameterCount;
    private final IndirectInteger annotationStart;

    public AnnotationDirectoryIndex() {
        super(SIZE);
        this.classOffset = new IndirectInteger(this, OFFSET_CLASS_OFFSET);
        this.fieldCount = new IndirectInteger(this, OFFSET_FIELD_COUNT);
        this.methodCount = new IndirectInteger(this, OFFSET_METHOD_COUNT);
        this.parameterCount = new IndirectInteger(this, OFFSET_PARAMETER_COUNT);
        this.annotationStart = new IndirectInteger(this, OFFSET_ANNOTATIONS_START);
    }
    public IndirectInteger getClassOffset() {
        return classOffset;
    }
    public IndirectInteger getFieldCount() {
        return fieldCount;
    }
    public IndirectInteger getMethodCount() {
        return methodCount;
    }
    public IndirectInteger getParameterCount() {
        return parameterCount;
    }
    public IndirectInteger getAnnotationsStart() {
        return annotationStart;
    }
    public int getFieldOffset(int index){
        return getInteger(getBytesInternal(), SIZE + index * 8);
    }
    public int getMethodOffset(int index){
        int offset = SIZE + getFieldCount().get() * 8;
        return getInteger(getBytesInternal(), offset + index * 8);
    }
    public int getParameterOffset(int index){
        int offset = SIZE + getFieldCount().get() * 8 + getMethodCount().get() * 8;
        return getInteger(getBytesInternal(), offset + index * 8);
    }
    @Override
    public void onReadBytes(BlockReader reader) throws IOException{
        setBytesLength(SIZE, false);
        byte[] bytes = getBytesInternal();
        reader.readFully(bytes);

        int size = SIZE + getFieldCount().get() * 8
                + getMethodCount().get() * 8
                + getParameterCount().get() * 8;

        if(size == SIZE){
            return;
        }
        setBytesLength(size, false);
        bytes = getBytesInternal();
        reader.read(bytes, SIZE, size - SIZE);

    }

    @Override
    public String toString() {
        return  "classOffset=" + classOffset +
                ", fieldCount=" + fieldCount +
                ", methodCount=" + methodCount +
                ", parameterCount=" + parameterCount +
                ", annotationStart=" + annotationStart;
    }

    private static final int OFFSET_CLASS_OFFSET = 0;
    private static final int OFFSET_FIELD_COUNT = 4;
    private static final int OFFSET_METHOD_COUNT = 8;
    private static final int OFFSET_PARAMETER_COUNT = 12;
    private static final int OFFSET_ANNOTATIONS_START = 16;
    private static final int SIZE = 20;

}
