package com.alqorut.mystory.widget

import android.content.Intent
import android.widget.RemoteViewsService

class MyStoryService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}