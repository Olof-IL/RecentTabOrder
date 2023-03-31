package se.illusionlabs.recenttaborder

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.project.Project
import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.TabsListener


class MyProjectService(project: Project) {


    private var printDEBUG = true

    fun registerTabListener(project: Project) {
        val fex = FileEditorManagerEx.getInstance(project) as FileEditorManagerEx


        for (v in fex.windows) {
            v.tabbedPane.tabs.addListener(object : TabsListener {
                private var processing = false;
                override fun selectionChanged(oldSelection: TabInfo?, newSelection: TabInfo?) {
                    if(!processing) { // Handle unwanted recursion when removing and adding tabs.
                        processing = true
                        super.selectionChanged(oldSelection, newSelection)
                        if(printDEBUG) println("Tab selection changed! from $oldSelection to $newSelection")
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
        if(printDEBUG) println("MyProjectService Startup " + project.name)

        EditorFactory.getInstance().addEditorFactoryListener(object : EditorFactoryListener {
            private var didIt = false

            override fun editorCreated(event: EditorFactoryEvent) {
                // Get the tool window manager for the project
                // Called when a new editor window is created
                if(printDEBUG) println("New editor created ${event.editor}")

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
                if(printDEBUG) println("Editor released ${event.editor}");
            }
        }, project);
    }
}
