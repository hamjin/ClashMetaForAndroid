package com.github.kr328.clash.service.clash.module

import android.app.Service
import android.os.Build
import com.github.kr328.clash.common.constants.Intents
import com.github.kr328.clash.common.log.Log
import com.github.kr328.clash.service.TunService

class CloseModule(service: Service) : Module<CloseModule.RequestClose>(service) {
    object RequestClose

    fun isAlwaysOn(service: Service): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && service is TunService && service.isAlwaysOn
    }

    override suspend fun run() {
        val broadcasts = receiveBroadcast {
            addAction(Intents.ACTION_CLASH_REQUEST_STOP)
        }

        broadcasts.receive()

        if (isAlwaysOn(service))
            return

        Log.d("User request close")

        return enqueueEvent(RequestClose)
    }
}