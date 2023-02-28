package com.example.recenttaborder

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.fileEditor.impl.EditorWindowHolder
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.WindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.openapi.wm.impl.ToolWindowManagerImpl
import com.intellij.psi.impl.source.tree.injected.EditorWindowTracker
import com.intellij.psi.impl.source.tree.injected.EditorWindowTrackerImpl
import com.intellij.serviceContainer.ComponentManagerImpl
import com.intellij.ui.content.ContentManager
import com.intellij.ui.content.impl.TabbedContentImpl
import com.intellij.ui.tabs.JBTabs.SelectionChangeHandler
import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.TabsListener
import com.intellij.util.messages.MessageBusConnection


class MyProjectService(project: Project) {

    private var didIt = false

    private var registeredWindows = setOf<EditorWindow>()

    fun registerTabListener(project: Project) {
        val fex = FileEditorManagerEx.getInstance(project) as FileEditorManagerEx


        for (v in fex.windows) {
            v.tabbedPane.tabs.addListener(object : TabsListener {
                override fun selectionChanged(oldSelection: TabInfo?, newSelection: TabInfo?) {
                    super.selectionChanged(oldSelection, newSelection)
//                    println("Tab selection changed! from $oldSelection to $newSelection")
                    if(oldSelection != null) {
                        val newTabs = mutableListOf<TabInfo?>();
                        newTabs.add(newSelection);
                        for(tab in v.tabbedPane.tabs.tabs) {
                            if(tab != newSelection) {
                                newTabs.add(tab);
                            }
                        }
                        v.tabbedPane.tabs.removeAllTabs()
                        for(tab in newTabs) {
                            v.tabbedPane.tabs.addTab(tab);
                        }
                    }
                }
            })
        }
    }

    init {
        println("MyProjectService Startup " + project.name)

        EditorFactory.getInstance().addEditorFactoryListener(object : EditorFactoryListener {

            override fun editorCreated(event: EditorFactoryEvent) {
                // Get the tool window manager for the project
                // Called when a new editor window is created
                println("New editor created ${event.editor}")

                // Haven't found a way to listen for when the EditorWindow is created, so instead I listen for when FileEditor is created.
                // When the first FileEditor is created I can call FileEditorManagerEx.getInstance(project) and get a list of windows,
                // I do this only once assuming the list of windows does not change.
                if(!didIt) {

                    registerTabListener(project)

                    didIt = true
                }
            }

            override fun editorReleased(event: EditorFactoryEvent) {
                // Called when an editor window is closed
                println("Editor released ${event.editor}");
            }
        }, project);


        // Below is a lot of approaches tried that seems they don't work...
        // Either straight up does not work, or is using deprecated calls.

//        ToolWindowManagerListener mm

        // Get the tool window manager for the project
//        val toolWindowManager = ToolWindowManager.getInstance(project) as ToolWindowManagerImpl

//        toolWindowManager.toolWindowsPane
//
//        toolWindowManager.addToolWindowManagerListener()

        // Get the tool window containing the tabbed content component
//        val toolWindow = toolWindowManager.getToolWindow("EditorTabs")


        // Get the tabbed content component from the tool window
//        val tabbedContentUI = toolWindow?.contentManager?.

        // Listen for tab change events
//        tabbedContentUI.addListener(object : TabbedContentUIListener {
//            override fun selectionChanged(tab: com.intellij.openapi.wm.ToolWindowTab) {
//                println("Active tab: ${tab.text}")
//            }
//        })
        // Get the tabbed content component from the tool window
//        val tabbedContent = toolWindow?.contentManager?.selectedContent as TabbedContentImpl


        // Get all the contents (tabs) in the tabbed content component
  //      val contents = tabbedContent?.tabs;

        /*

        // Get the component manager for the project
        val componentManager = project.getComponent(ComponentManagerImpl::class.java)

        // Get the tabbed content component for the project
        val tabbedContent = componentManager.ideFrame.contentManager.selectedContent as TabbedContentImpl
*/
        /*

//        System.getenv("CI")
            ?: TODO("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
        val componentManager = project.getComponent(ComponentManagerImpl::class.java) as ComponentManagerImpl

        // Get the tabbed content component for the project

        // Get the tabbed content component for the project
        val tabbedContent = componentManager.ideFrame.contentManagher.sel
        val contentManager = componentManager.getService<ContentManager>()
*/

    }

    /**
     * Chosen by fair dice roll, guaranteed to be random.
     */
    fun getRandomNumber() = 4
}
