package de.hhn.aib.labsw.blackmirror.controller.gesture


import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import de.hhn.aib.labsw.blackmirror.controller.PageController
import de.hhn.aib.labsw.blackmirror.model.Gesture

/**
 * controller handling Gestures
 */
class SerialGestureController(
    private val port: SerialPort,
    var pageController: PageController
) {
    init {
        port.baudRate = 9600
        port.openPort()
        port.addDataListener(SerialHandler())
    }

    inner class SerialHandler() : SerialPortDataListener {
        /**
         * listen to new data received events
         */
        override fun getListeningEvents(): Int {
            return SerialPort.LISTENING_EVENT_DATA_RECEIVED
        }

        /**
         * handle the data
         */
        override fun serialEvent(event: SerialPortEvent?) {
            if (event != null) {
                val data = event.receivedData
                if (data.size == 1) {
                    //only get the first byte, everything else is irrelevant
                    val num = data[0].toInt()
                    try {
                        //try to convert the int to the gesture
                        val gesture = Gesture.values()[num]
                        //handle the gesture and call the necessary methods in page controller
                        when (gesture) {
                            Gesture.LEFT -> pageController.goToPreviousPage()
                            Gesture.RIGHT -> pageController.goToNextPage()
                            else -> {}
                        }
                    } catch (e: Exception) {
                        /* no-op */
                    }
                }
            }
        }

    }
}