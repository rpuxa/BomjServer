package ru.rpuxa.bomjserver.server

import java.net.InetAddress
import java.net.ServerSocket

private const val ADDRESS = "89.223.31.120"
private const val PORT = 7158

class SocketServer : Server {

    private var serverSocket: ServerSocket? = null
    private var stop = false

    override fun start(address: String) {
        var adr = address
        if (adr.isBlank())
            adr = ADDRESS
        try {
            stop = false
            serverSocket = ServerSocket(PORT, 0, InetAddress.getByName(adr))
            while (!stop)
                CommandExecutor.connect(serverSocket!!.accept())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun stop() {
        try {
            stop = true
            if (serverSocket != null)
                serverSocket!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
