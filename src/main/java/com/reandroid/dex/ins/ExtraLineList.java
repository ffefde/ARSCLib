package com.reandroid.dex.ins;

import com.reandroid.common.ArraySupplier;
import com.reandroid.utils.collection.ArraySupplierIterator;
import com.reandroid.utils.collection.EmptyIterator;

import java.util.Arrays;
import java.util.Iterator;

public class ExtraLineList implements ArraySupplier<ExtraLine>, Iterable<ExtraLine> {
    private ExtraLine[] elements;
    private int size;
    private boolean sorted;

    public ExtraLineList(){
    }

    public boolean isEmpty(){
        return getCount() != 0;
    }
    @Override
    public Iterator<ExtraLine> iterator() {
        sort();
        return ArraySupplierIterator.of(this);
    }
    @Override
    public ExtraLine get(int i){
        if(elements != null){
            return elements[i];
        }
        return null;
    }
    @Override
    public int getCount(){
        return size;
    }
    private void sort(){
        if(this.sorted){
            return;
        }
        ExtraLine[] elements = this.elements;
        if(elements == null || this.size < 2){
            this.sorted = true;
            return;
        }
        Arrays.sort(elements, 0, this.size, ExtraLine.COMPARATOR);
        this.sorted = true;
    }
    public void add(Iterator<ExtraLine> iterator){
        while (iterator.hasNext()){
            add(iterator.next());
        }
    }
    public void add(ExtraLine extraLine){
        if (extraLine == null){
            return;
        }
        ensureCapacity();
        this.elements[size] = extraLine;
        this.size ++;
        this.sorted = false;
    }
    public void trimToSize(){
        if(availableCapacity() == 0){
            return;
        }
        int size = this.size;
        if(size == 0){
            this.elements = null;
            return;
        }
        boolean sorted = this.sorted;
        this.sorted = true;
        ExtraLine[] update = new ExtraLine[size];
        System.arraycopy(this.elements, 0, update, 0, size);
        this.elements = update;
        this.sorted = sorted;
    }
    private void ensureCapacity(){
        int capacity;
        if(this.size == 0){
            capacity = 1;
        }else {
            capacity = DEFAULT_CAPACITY;
        }
        ensureCapacity(capacity);
    }
    private void ensureCapacity(int capacity) {
        if(availableCapacity() >= capacity){
            return;
        }
        int size = this.size;
        int length = size + capacity;
        ExtraLine[] update = new ExtraLine[length];
        ExtraLine[] elements = this.elements;
        if(elements == null || size == 0){
            this.elements = update;
            return;
        }
        boolean sorted = this.sorted;
        this.sorted = true;
        System.arraycopy(elements, 0, update, 0, size);
        this.elements = update;
        this.sorted = sorted;
    }
    private int availableCapacity(){
        ExtraLine[] elements = this.elements;
        if(elements != null){
            return elements.length - size;
        }
        return 0;
    }

    public static ExtraLineList add(ExtraLineList list, Iterator<ExtraLine> iterator){
        if(iterator == null || !iterator.hasNext()){
            if(list == null || list.isEmpty()){
                return EMPTY;
            }
            return list;
        }
        if(list == null || list == EMPTY){
            list = new ExtraLineList();
        }
        list.add(iterator);
        return list;
    }

    public static ExtraLineList add(ExtraLineList list, ExtraLine extraLine){
        if(extraLine == null){
            if(list.isEmpty()){
                return EMPTY;
            }
            return list;
        }
        if(list == EMPTY){
            list = new ExtraLineList();
        }
        list.add(extraLine);
        return list;
    }

    public static final ExtraLineList EMPTY = new ExtraLineList(){

        @Override
        public void add(Iterator<ExtraLine> iterator) {
            if(iterator == null || !iterator.hasNext()){
                return;
            }
            throw new IllegalArgumentException("Empty ExtraLineList");
        }
        @Override
        public void add(ExtraLine extraLine) {
            if(extraLine != null){
                throw new IllegalArgumentException("Empty ExtraLineList");
            }
        }
        @Override
        public Iterator<ExtraLine> iterator() {
            return EmptyIterator.of();
        }
        @Override
        public boolean isEmpty() {
            return true;
        }
        @Override
        public ExtraLine get(int i) {
            throw new IllegalArgumentException("Empty ExtraLineList");
        }
        @Override
        public int getCount() {
            return 0;
        }
        @Override
        public void trimToSize() {
        }
    };

    private static final int DEFAULT_CAPACITY = 3;
}
