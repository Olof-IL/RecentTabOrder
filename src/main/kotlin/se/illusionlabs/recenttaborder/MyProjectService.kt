package se.illusionlabs.recenttaborder

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

    private var processing = false;

    fun registerTabListener(project: Project) {
        val fex = FileEditorManagerEx.getInstance(project) as FileEditorManagerEx


        for (v in fex.windows) {
            v.tabbedPane.tabs.addListener(object : TabsListener {
                override fun selectionChanged(oldSelection: TabInfo?, newSelection: TabInfo?) {
                    if(!processing) { // Handle unwanted recursion when removing and adding tabs.
                        processing = true
                        super.selectionChanged(oldSelection, newSelection)
                        println("Tab selection changed! from $oldSelection to $newSelection")
                        if (oldSelection != null) {
                            val newTabs = mutableListOf<TabInfo?>();
                            newTabs.add(newSelection);
                            for (tab in v.tabbedPane.tabs.tabs) {
                                if (tab != newSelection) {
                                    newTabs.add(tab);
                                }
                            }
                            v.tabbedPane.tabs.removeAllTabs()
                            for (tab in newTabs) {
                                v.tabbedPane.tabs.addTab(tab);
                            }
                        }
                        processing = false
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
    }
}
