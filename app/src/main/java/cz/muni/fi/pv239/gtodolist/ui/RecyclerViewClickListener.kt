package cz.muni.fi.pv239.gtodolist.ui

import android.view.View

interface RecyclerViewClickListener {
    fun recyclerViewListClicked(v: View?, position: Int)
}