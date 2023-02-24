package com.example.recenttaborder

import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.TabsListener;

class MostRecentReorderingTabListener : TabsListener {
    init {
        println("MostRecentReorderingTabListener startup")
    }

    override fun selectionChanged(oldSelection: TabInfo?, newSelection: TabInfo?) {
        super.selectionChanged(oldSelection, newSelection)

        println("Selection changed")

    }
}