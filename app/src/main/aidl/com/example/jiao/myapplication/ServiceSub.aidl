// ServiceSub.aidl
package com.example.jiao.myapplication;

// Declare any non-default types here with import statements
import com.example.jiao.myapplication.LocalStub;
interface ServiceSub {
    void read(in int[] request,out int[] result);
    void register(in LocalStub localStub);
    void unReigister(in LocalStub localStub);
}
