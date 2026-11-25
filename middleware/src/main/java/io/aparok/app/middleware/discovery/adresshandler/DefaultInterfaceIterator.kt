package io.aparok.app.middleware.discovery.adresshandler

import java.net.NetworkInterface
import io.aparok.mw.service.InterfaceIterator

class DefaultInterfaceIterator(
    private val iter: Iterator<NetworkInterface>
) : InterfaceIterator {
    override fun next(): LocalInterface? {
        if (iter.hasNext()) {
            return LocalInterface(iter.next())
        }
        return null
    }
}