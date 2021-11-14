package com.reandroid.lib.arsc.container;

import com.reandroid.lib.arsc.base.Block;
import com.reandroid.lib.arsc.base.BlockContainer;
import com.reandroid.lib.arsc.base.BlockCounter;
import com.reandroid.lib.arsc.io.BlockReader;

import java.io.IOException;
import java.io.OutputStream;

public class SingleBlockContainer<T extends Block> extends BlockContainer<T> {
    private T mItem;
    public SingleBlockContainer(){
        super();
    }
    @Override
    protected void refreshChildes(){
        if(mItem!=null){
            if(mItem instanceof BlockContainer){
                ((BlockContainer)mItem).refresh();
            }
        }
    }
    @Override
    protected void onRefreshed() {

    }
    public T getItem() {
        return mItem;
    }
    public void setItem(T item) {
        if(item==null){
            if(mItem!=null){
                mItem.setIndex(-1);
                mItem.setParent(null);
            }
            mItem=null;
            return;
        }
        this.mItem = item;
        item.setIndex(getIndex());
        item.setParent(this);
    }
    public boolean hasItem(){
        return this.mItem!=null;
    }
    @Override
    public byte[] getBytes() {
        if(mItem!=null){
            return mItem.getBytes();
        }
        return null;
    }
    @Override
    public int countBytes() {
        if(mItem!=null){
            return mItem.countBytes();
        }
        return 0;
    }

    @Override
    public void onCountUpTo(BlockCounter counter) {
        if(counter.FOUND){
            return;
        }
        if(counter.END==this){
            counter.FOUND=true;
            return;
        }
        if(mItem!=null){
            mItem.onCountUpTo(counter);
        }
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException{
        if(mItem!=null){
            mItem.readBytes(reader);
        }
    }
    @Override
    public int onWriteBytes(OutputStream stream) throws IOException {
        if(mItem!=null){
            return mItem.writeBytes(stream);
        }
        return 0;
    }

    @Override
    public int childesCount() {
        return hasItem()?0:1;
    }

    @Override
    public T[] getChildes() {
        return null;
    }

    @Override
    public String toString(){
        if(mItem!=null){
            return mItem.toString();
        }
        return getClass().getSimpleName()+": EMPTY";
    }
}
