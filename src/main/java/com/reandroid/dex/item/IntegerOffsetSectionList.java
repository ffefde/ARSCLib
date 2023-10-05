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
package com.reandroid.dex.item;

import com.reandroid.dex.sections.SectionType;
import com.reandroid.utils.collection.ArrayIterator;

import java.util.Iterator;
import java.util.function.Predicate;

public class IntegerOffsetSectionList<T extends DataSectionEntry> extends IntegerList implements Iterable<T>{
    private final SectionType<T> sectionType;
    private T[] items;

    public IntegerOffsetSectionList(SectionType<T> sectionType) {
        super();
        this.sectionType = sectionType;
    }
    public T addNew(){
        T item = getSection(sectionType).createItem();
        add(item.getOffset());
        return item;
    }

    @Override
    public void removeSelf() {
        setItems(null);
        super.removeSelf();
    }

    public void remove(T item) {
        remove(t -> t == item);
    }
    public void remove(Predicate<? super T> filter) {
        T[] items = this.items;
        if(items == null){
            return;
        }
        int length = items.length;
        boolean found = false;
        for(int i = 0; i < length; i++){
            T item = items[i];
            if(filter.test(item)){
                items[i] = null;
                found = true;
            }
        }
        if(found){
            removeNulls();
        }
    }
    void removeNulls() {
        T[] items = this.items;
        if(items == null || items.length == 0){
            setItems(null);
            return;
        }
        int length = items.length;
        int count = 0;
        for(int i = 0; i < length; i++){
            if(items[i] == null){
                count ++;
            }
        }
        if(count == 0){
            return;
        }
        T[] update = sectionType.getCreator()
                .newInstance(length - count);
        int index = 0;
        for(int i  = 0; i < length; i++){
            T element = items[i];
            if(element != null){
                update[index] = element;
                index++;
            }
        }
        setItems(update);
    }
    @Override
    public Iterator<T> iterator() {
        return ArrayIterator.of(items);
    }
    public T getItem(int i){
        if(i < 0){
            return null;
        }
        T[] items = this.items;
        if(items == null || i >= items.length){
            return null;
        }
        return items[i];
    }
    public T[] getItems() {
        return items;
    }
    public void setItems(T[] items){
        if(items == this.items){
            return;
        }
        if(isEmpty(items)){
            this.items = null;
            setSize(0);
            return;
        }
        int length = items.length;
        setSize(length, false);
        for(int i = 0; i < length; i++){
            T item = items[i];
            put(i, getData(item));
        }
        this.items = items;
    }

    @Override
    void onChanged() {
        super.onChanged();
        updateItems();
    }

    @Override
    protected void onPreRefresh() {
        super.onPreRefresh();
        refreshItems();
    }

    private void refreshItems(){
        T[] items = this.items;
        if(isEmpty(items)){
            this.items = null;
            setSize(0);
            return;
        }
        int length = items.length;
        setSize(length, false);
        boolean found = false;
        for(int i = 0; i < length; i++){
            T item = items[i];
            int data = getData(item);
            put(i, getData(item));
            if(data == 0) {
                items[i] = null;
                found = true;
            }
        }
        if(found){
            removeNulls();
        }
    }
    private int getData(T item){
        if(item == null){
            return 0;
        }
        return item.getOffset();
    }
    private void updateItems(){
        items = get(sectionType, toArray());
    }
    private boolean isEmpty(T[] items){
        if(items == null || items.length == 0){
            return true;
        }
        for(int i = 0; i < items.length; i++){
            if(items[i] != null){
                return false;
            }
        }
        return true;
    }
}
