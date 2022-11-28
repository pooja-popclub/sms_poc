package com.avdt.readinboxsms

import android.util.Log
import com.avdt.readinboxsms.CancelableCallback
import retrofit2.Callback
import java.util.ArrayList

abstract class CancelableCallback<T> : Callback<T> {
    private var isCancelled = false
    private var mTag: Any? = null

    constructor() {
        mList.add(this)
    }

    constructor(tag: Any?) {
        mTag = tag
        mList.add(this)
    }

    fun cancel() {
        isCancelled = true
        mList.remove(this)
    }

    companion object {
        private val mList: MutableList<CancelableCallback<*>> = ArrayList()
        fun cancelAll() {
            val iterator = mList.iterator()
            while (iterator.hasNext()) {
                iterator.next().isCancelled = true
                iterator.remove()
            }
        }

        fun cancel(tag: Any?) {
            if (tag != null) {
                val iterator = mList.iterator()
                var item: CancelableCallback<*>
                while (iterator.hasNext()) {
                    item = iterator.next()
                    if (tag == item.mTag) {
                        item.isCancelled = true
                        iterator.remove()
                        Log.e("CallBack", " CallBack Cancelled $iterator")
                    }
                }
            }
        }
    }
}