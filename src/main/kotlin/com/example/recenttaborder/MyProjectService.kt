package com.example.recenttaborder

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.openapi.wm.impl.ToolWindowManagerImpl
import com.intellij.serviceContainer.ComponentManagerImpl
import com.intellij.ui.content.ContentManager
import com.intellij.ui.content.impl.TabbedContentImpl
import com.intellij.ui.tabs.JBTabs.SelectionChangeHandler
import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.TabsListener
import com.intellij.util.messages.MessageBusConnection


class MyProjectService(project: Project) {

    private var messageBusConnection: MessageBusConnection? = null

    private var didIt = false

    init {
        println("MyProjectService Startup " + project.name)

        messageBusConnection = project.messageBus.connect()


/*
        messageBusConnection?.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
            object : FileEditorManagerListener {

                override fun selectionChanged(event: FileEditorManagerEvent) {



                    super.selectionChanged(event)
                    val openFiles = event.manager.openFiles
                    if(openFiles.size >= 2) {
                        // Reorder the open files
                        val lastFile = openFiles.last()
                        event.manager.openFile(openFiles.first(), true)
                        event.manager.closeFile(lastFile)


                    }
                }
            })
*/
/*        val fem = FileEditorManager.getInstance(project)
        fem.addFileEditorManagerListener(object : FileEditorManagerListener {
            override fun selectionChanged(event: FileEditorManagerEvent) {
                super.selectionChanged(event)

                println("Callback: Selection changed in fileEditorManager! from ${event.oldEditor?.file}(${event.oldEditor?.name}) to ${event.newEditor?.file}(${event.newEditor?.name})")


            }
        })
*/



//        project.
        EditorFactory.getInstance().addEditorFactoryListener(object : EditorFactoryListener {

            override fun editorCreated(event: EditorFactoryEvent) {
                // Get the tool window manager for the project
                val toolWindowManager = ToolWindowManager.getInstance(project)
                // Called when a new editor window is created
                println("New editor window created with toolwindowmanager $toolWindowManager")

                if(!didIt) {

                    val fex = FileEditorManagerEx.getInstance(project) as FileEditorManagerEx

                    for (v in fex.windows) {
                        v.tabbedPane.tabs.addListener(object : TabsListener {
                            override fun selectionChanged(oldSelection: TabInfo?, newSelection: TabInfo?) {
                                super.selectionChanged(oldSelection, newSelection)

                                println("Tab selection changed! from $oldSelection to $newSelection")

                                if(oldSelection != null) {
//                                    v.tabbedPane.tabs.addTab(newSelection, 0);
//                                    v.tabbedPane.tabs.removeTab(newSelection);
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
                    didIt = true
                }

//                event.editor


                val ids = toolWindowManager.toolWindowIds;
                for (id in ids) {
                    println(id)
                }

                // Get the tool window containing the tabbed content component
                val toolWindow = toolWindowManager.getToolWindow("EditorTabs")

                val tabbedContent = toolWindow?.contentManager?.selectedContent // as TabbedContentImpl


                // Get all the contents (tabs) in the tabbed content component
//                val contents = tabbedContent?.tabs;

                println("jeee");
            }

            override fun editorReleased(event: EditorFactoryEvent) {
                // Called when an editor window is closed
            }
        }, project);


//        ToolWindowManagerListener mm

        // Get the tool window manager for the project
        val toolWindowManager = ToolWindowManager.getInstance(project) as ToolWindowManagerImpl

//        toolWindowManager.toolWindowsPane
//
//        toolWindowManager.addToolWindowManagerListener(ob)

        // Get the tool window containing the tabbed content component
        val toolWindow = toolWindowManager.getToolWindow("EditorTabs")


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
