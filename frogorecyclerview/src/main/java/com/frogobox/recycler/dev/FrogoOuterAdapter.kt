package com.frogobox.recycler.dev

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frogobox.recycler.R
import com.frogobox.recycler.core.FrogoRecyclerViewListener
import com.frogobox.recycler.core.IFrogoViewHolder

/*
 * Created by Amir on 03/03/2021
 * FrogoRecyclerView Source Code
 * -----------------------------------------
 * Name     : Muhammad Faisal Amir
 * E-mail   : faisalamircs@gmail.com
 * Github   : github.com/amirisback
 * -----------------------------------------
 * Copyright (C) 2021 FrogoBox Inc.      
 * All rights reserved
 *
 */
class FrogoOuterAdapter(private val mItemClickListener: FrogoRecyclerViewListener<Int>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mList = mutableListOf<MutableList<Int>>()
    private val listPosition = HashMap<Int, Int>()
    private var sharedPool = RecyclerView.RecycledViewPool()

    fun setupData(list: MutableList<MutableList<Int>>) {
        mList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val innerRv = RecyclerView(parent.context)

        // inflate inner item, find innerRecyclerView by ID
        val innerLLM = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)
        innerLLM.initialPrefetchItemCount = 7 // depends on screen size
        innerRv.apply {
            setHasFixedSize(true)
            layoutManager = innerLLM
            setRecycledViewPool(sharedPool)
        }
        return FrogoOuterHolder(innerRv)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.itemViewType) {
            else -> {
                val holder = viewHolder as FrogoOuterHolder

                // attibute inner element nested recyclerview
                holder.bindView(R.layout.cell_nested_list, mList[position], mItemClickListener,
                    object : IFrogoViewHolder<Int> {
                        override fun setupInitComponent(view: View, data: Int) {
                            val tv = view.findViewById<TextView>(R.id.text)
                            tv.text = data.toString()
                        }
                    })

                val p = if (listPosition.containsKey(position) && listPosition[position]!! >= 0) {
                    listPosition[position]!!
                } else {
                    0
                }
                holder.getLinearLayoutManager().scrollToPositionWithOffset(p, 0)
            }
        }
    }

    override fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.adapterPosition
        val cellViewHolder = viewHolder as FrogoOuterHolder
        val firstVisiblePosition =
            cellViewHolder.getLinearLayoutManager().findFirstVisibleItemPosition()
        listPosition[position] = firstVisiblePosition
        super.onViewRecycled(viewHolder)
    }

    override fun getItemCount(): Int {
        if (mList.isNullOrEmpty()) {
            return 0
        }
        return mList.size
    }

}