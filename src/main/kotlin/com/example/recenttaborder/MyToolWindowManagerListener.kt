package com.example.recenttaborder

import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.ui.content.ContentManagerEvent
import com.intellij.ui.content.ContentManagerListener
import com.intellij.ui.content.impl.ContentManagerImpl
import com.intellij.ui.content.impl.TabbedContentImpl

class MyToolWindowManagerListener : ToolWindowManagerListener {
    init {
        println("Gogo")
    }


    override fun toolWindowsRegistered(ids: MutableList<String>, toolWindowManager: ToolWindowManager) {
        super.toolWindowsRegistered(ids, toolWindowManager)
        for(id in ids) {
            println("Registered tool window $id on $toolWindowManager")

            val toolWindow = toolWindowManager.getToolWindow(id)



            val tabbedContent = toolWindow?.contentManager?.selectedContent  as? TabbedContentImpl

            val cmi = toolWindow?.contentManager as? ContentManagerImpl

            cmi?.addContentManagerListener(object : ContentManagerListener {
                override fun selectionChanged(event: ContentManagerEvent) {
                    super.selectionChanged(event)

                    println("Selection changed $event for $id")
                }
            })

            // Get all the contents (tabs) in the tabbed content component
            val contents = tabbedContent?.tabs;

            println("hello")
        }
    }

    override fun toolWindowShown(toolWindow: ToolWindow) {
        super.toolWindowShown(toolWindow)

        println("Showing tool window " + toolWindow.id)
    }

    override fun stateChanged(toolWindowManager: ToolWindowManager) {
        super.stateChanged(toolWindowManager)
        println("MyToolWindowManagerListener - stateChanged")
    }
}