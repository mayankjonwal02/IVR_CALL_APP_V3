package com.example.ivr_call_app_v3.android.FunctionalComponents





import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")


const val devicename = "BLEalarm"

data class msgupdate(var key: Int , var message: String)


class BluetoothViewModel() : ViewModel() {

    private lateinit var context: Context
    lateinit var shareit : sendreceive
    var bluetoothadapter = BluetoothAdapter.getDefaultAdapter()
    lateinit var socket : BluetoothSocket
    val _paireddevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    var paireddevices : StateFlow<List<BluetoothDevice>> = _paireddevices

    lateinit var device :BluetoothDevice


    val _status = MutableStateFlow<String>("Disconnected")
    val status : StateFlow<String> = _status

    val _blestatus = MutableStateFlow<String>("Disconnected")
    val blestatus : StateFlow<String> = _blestatus

    private var STATE_LISTENING = 1
    private var STATE_CONNECTING = 2
    private var STATE_CONNECTED = 3
    private var STATE_CONNECTION_FAILED = 4
    private var STATE_MESSAGE_RECEIVED = 5
    private var STATE_ALREADY_CONNECTED = 6

    fun initialiseBluetooth(context: Context) {
        this.context = context
    }

    fun enableBluetooth() {
        if (!bluetoothadapter.isEnabled) {
            var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "Permissions Not Granted", Toast.LENGTH_SHORT).show()
                return
            }
            context.startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchdevices()
    {
        if(bluetoothadapter != null)
        {

            _paireddevices.value = bluetoothadapter.bondedDevices.toList()
        }
    }

    @SuppressLint("MissingPermission")
    var handler = Handler{
            msg : Message ->
        when(msg.what)
        {
            STATE_CONNECTED ->
            {
                _blestatus.value = "Connected"
                _status.value= "Connected to ${device.name}"
                false
            }
            STATE_CONNECTING ->
            {
                _blestatus.value = "Connecting"
                _status.value= "Connecting"
                false
            }
            STATE_LISTENING ->
            {
                _blestatus.value = "Listening"
                _status.value= "Listening..."
                false
            }
            STATE_CONNECTION_FAILED ->
            {
                _blestatus.value = "Connection Error"
                _status.value= "Connection Error"
                false
            }
            STATE_MESSAGE_RECEIVED ->
            {
                val readbuffer = msg.obj as ByteArray
                var data = String(readbuffer , 0 ,msg.arg1 , Charsets.UTF_8)
//                var formatteddata = jsonstringtodataclass(data)
//                _mymessage.value = formatteddata
                false
            }


            else ->
            {
                _status.value = "error"
                false
            }

        }

    }




    inner class server() : Thread()
    {
        private lateinit var serverSocket: BluetoothServerSocket

        init {
            serverclass()
        }

        fun serverclass()
        {
            try
            {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(context,"Permission Not Granted",Toast.LENGTH_SHORT).show()
                    return
                }
                serverSocket = bluetoothadapter.listenUsingRfcommWithServiceRecord(devicename, uuid)
            }
            catch (e:IOException)
            {
                Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
            }

        }

        override fun run() {
            super.run()
            var socket : BluetoothSocket? = null


            while (socket == null) {
                try {
                    var handlermessage = Message.obtain()
                    handlermessage.what = STATE_CONNECTING
                    handler.sendMessage(handlermessage)

                    socket = serverSocket.accept()
                    device = serverSocket.accept().remoteDevice



                } catch (e: IOException) {
                    var handlermessage = Message.obtain()
                    handlermessage.what = STATE_CONNECTION_FAILED
                    handler.sendMessage(handlermessage)
                }

                if (socket != null) {
                    var handlermessage = Message.obtain()
                    handlermessage.what = STATE_CONNECTED
                    handler.sendMessage(handlermessage)

                    shareit = sendreceive(socket)
                    shareit.start()
                }
            }



        }
    }

    inner class client(var mydevice: BluetoothDevice) : Thread()
    {



        init {
            clientclass()
        }

        @SuppressLint("MissingPermission")
        fun clientclass()
        {
            device = mydevice

            try {

                socket = device.createRfcommSocketToServiceRecord(uuid)

                var handlermessage = Message.obtain()
                handlermessage.what = STATE_CONNECTING
                handler.sendMessage(handlermessage)
            }
            catch (e:IOException)
            {
                Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
                var handlermessage = Message.obtain()
                handlermessage.what = STATE_CONNECTION_FAILED
                handler.sendMessage(handlermessage)
//                CoroutineScope(Dispatchers.Main).launch{ mynavcontroller.navigate(screen.bleDevices.route) }
            }
        }

        @SuppressLint("MissingPermission", "CommitPrefEdits")
        override fun run() {
            super.run()

            try {

                socket.connect()
                var handlermessage = Message.obtain()
                handlermessage.what = STATE_CONNECTED
                handler.sendMessage(handlermessage)


//                sp?.edit()?.putString("bleaddr",device.address)?.apply()
//                shareit.write(1)
                shareit = sendreceive(socket)

//                CoroutineScope(Dispatchers.Main).launch {
//                    Toast.makeText(context , "shareit initialized" , Toast.LENGTH_SHORT).show()
//                    mynavcontroller.navigate(screen.lightbutton.route )
//                }

//                shareit.start()


            }
            catch (e:IOException)
            {
                var handlermessage = Message.obtain()
                handlermessage.what = STATE_CONNECTION_FAILED
                handler.sendMessage(handlermessage)
//                CoroutineScope(Dispatchers.Main).launch{ mynavcontroller.navigate(screen.bleDevices.route) }
            }
        }
    }

    inner class sendreceive(var socket: BluetoothSocket) : Thread()
    {
        private lateinit var inputstream : InputStream
        private lateinit var outputstream : OutputStream


        init {
            setupstream()
        }

        fun setupstream()
        {
            try{
                inputstream = socket.inputStream
                outputstream = socket.outputStream
            }
            catch (e:IOException)
            {
                e.printStackTrace()
            }

        }

        override fun run() {
            super.run()

            var buffer = ByteArray(1024)
            var bytes : Int

            while (true)
            {
                try {
                    bytes = inputstream.read(buffer)
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget()
                }
                catch (e:IOException){
                    e.printStackTrace()
                    handler.post {
                        Toast.makeText(context, "Error while receiving message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        fun write(data : Int)
        {

            var bytearray = data.toString().toByteArray()
            try {
                handler.postDelayed({
                    try {

                        outputstream.write(bytearray)
                    } catch (e: IOException) {
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }, 1000)
            }
            catch (e:IOException)
            {
                Log.e("TAG1", e.message.toString())
                handler.post {
                    // Update UI on the main thread
                    Toast.makeText(context, "Error while sending message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}


//fun getConnectedDeviceAddress(): String? {
//    return sp?.getString("bleaddr", null)
//}


//@SuppressLint("MissingPermission")
//fun autoConnectToBle(navHostController: NavHostController, context: Context)
//{
//    val savedaddr = getConnectedDeviceAddress()
//
//
//    if(!savedaddr.isNullOrBlank())
//    {
//        mybluetooth!!.fetchdevices()
//        val pairedDevices = BluetoothAdapter.getDefaultAdapter().bondedDevices
//        val savedDevice = pairedDevices.find { it.address == savedaddr }
//        CoroutineScope(Dispatchers.Main).launch {
//
//            Toast.makeText(context,savedaddr , Toast.LENGTH_SHORT).show()
//            if (savedDevice != null) {
//
//                try {
//                    mybluetooth?.client(savedDevice,navHostController)?.start()
//
//
////                    CoroutineScope(Dispatchers.Main).launch{
////                        mybluetooth?.shareit?.let {
////                            try {
////                                it.write(0)
////                            } catch (e: IOException) {
////                                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
////                            }
////                        } ?: run {
////                            // Handle the case when shareit is not initialized
////                            Toast.makeText(context, "shareit is not initialized", Toast.LENGTH_SHORT).show()
////                        }
////                    }
//                }
//                catch (e:IOException)
//                {
//                    Toast.makeText(context , e.message.toString() , Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//    else
//    {
//        Toast.makeText(context, "Device Null",Toast.LENGTH_SHORT).show()
//    }
////    if(!savedaddr.isNullOrBlank())
////    {
////        val pairedDevices = BluetoothAdapter.getDefaultAdapter().bondedDevices
////        val savedDevice = pairedDevices.find { it.address == savedaddr }
////
////        if(savedDevice != null)
////        {
////            mybluetooth?.client(savedDevice,navHostController)
////        }
////        else
////        {
////            mybluetooth?._blestatus?.value = "Disconnected"
////            navHostController.navigate(screen.bleDevices.route)
////        }
////
////    }
////    else
////    {
////        mybluetooth?._blestatus?.value = "Disconnected"
////        navHostController.navigate(screen.bleDevices.route)
////    }
//}









