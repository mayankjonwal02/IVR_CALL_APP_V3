import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class BluetoothViewModel(private val context: Context) : ViewModel() {

    private val deviceUUID = UUID.fromString("0c145d00-e465-4617-89fa-d82c678f7972")
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null

    private val gson = Gson()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val pairedDevices: StateFlow<List<BluetoothDevice>> get() = _pairedDevices

    private val _connectionStatus = MutableStateFlow("Disconnected")
    val connectionStatus: StateFlow<String> get() = _connectionStatus

    private val _receivedData = MutableStateFlow<Map<String, Any>?>(null)
    val receivedData: StateFlow<Map<String, Any>?> get() = _receivedData

    private var connectedDevice: BluetoothDevice? = null

    private var listenJob: Job? = null
    private var connectJob: Job? = null
    private var serverSocket: BluetoothServerSocket? = null

    @SuppressLint("MissingPermission")
    fun fetchPairedDevices() {
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(context, "Enable Bluetooth to proceed", Toast.LENGTH_SHORT).show()
            return
        }

        val devices = bluetoothAdapter.bondedDevices.toList()
        _pairedDevices.value = devices
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        if (connectJob != null) {
            connectJob?.cancel()
            connectJob = null
        }

        connectJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                // Wait until disconnect completes before connecting to a new device
                if (disconnect().await()) {
                    _connectionStatus.value = "Connecting..."
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(deviceUUID)

                    bluetoothSocket?.connect()
                    bluetoothSocket?.let {
                        connectedDevice = device
                        outputStream = it.outputStream
                        inputStream = it.inputStream
                        _connectionStatus.value = "Connected"
                        listenForData() // Automatically start listening after connecting
                    }
                } else {
                    _connectionStatus.value = "Disconnect Failed"
                }

            } catch (e: Exception) {
                _connectionStatus.value = "Connection Failed"
                Log.e("Bluetooth", "Error during connection: ${e.message}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun waitForConnection() {
        if (connectJob != null) {
            connectJob?.cancel()
            connectJob = null
        }

        connectJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                // Wait until disconnect completes before continuing
                if (disconnect().await()) {
                    _connectionStatus.value = "Waiting for Connection..."

                    // Open server socket to listen for incoming connections
                    serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord("MyBluetoothApp", deviceUUID)
                    bluetoothSocket = serverSocket?.accept()  // Wait for an incoming connection
                    serverSocket?.close()  // Close server socket after accepting one connection

                    if (bluetoothSocket != null) {
                        connectedDevice = bluetoothSocket?.remoteDevice
                        outputStream = bluetoothSocket?.outputStream
                        inputStream = bluetoothSocket?.inputStream
                        _connectionStatus.value = "Connected"
                        listenForData()  // Start listening for incoming data
                    } else {
                        _connectionStatus.value = "Connection Failed"
                    }
                }

            } catch (e: IOException) {
                _connectionStatus.value = "Connection Failed"
                Log.e("Bluetooth", "Error during connection: ${e.message}")
            }
        }
    }

    fun sendData(data: Map<String, Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (outputStream != null) {
                try {
                    val jsonData = gson.toJson(data)
                    outputStream?.write(jsonData.toByteArray())
                } catch (e: Exception) {
                    Log.e("Bluetooth", "Failed to send data: ${e.message}")
                }
            } else {
                Log.e("Bluetooth", "No device connected")
            }
        }
    }

    private fun listenForData() {
        listenJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                inputStream?.let {
                    val buffer = ByteArray(1024)
                    while (true) {
                        val bytesRead = it.read(buffer)
                        if (bytesRead == -1) break
                        val receivedMessage = String(buffer, 0, bytesRead)
                        val data = gson.fromJson<Map<String, Any>>(receivedMessage, Map::class.java)
                        _receivedData.value = data // Update received data
                    }
                }
            } catch (e: IOException) {
                Log.e("Bluetooth", "Error in listenForData: ${e.message}")
                _connectionStatus.value = "Disconnected"
            }
        }
    }

    private suspend fun disconnect(): Deferred<Boolean> = coroutineScope {
        async(Dispatchers.IO) {
            try {
                listenJob?.cancel()
                listenJob = null
                bluetoothSocket?.closeSafely()
                bluetoothSocket = null
                outputStream = null
                inputStream = null
                connectedDevice = null
                _connectionStatus.value = "Disconnected"
                true
            } catch (e: Exception) {
                Log.e("Bluetooth", "Error during disconnect: ${e.message}")
                _connectionStatus.value = "Error"
                false
            }
        }
    }

    private fun BluetoothSocket?.closeSafely() {
        try {
            this?.close()
        } catch (e: IOException) {
            Log.e("Bluetooth", "Error closing socket: ${e.message}")
        }
    }
}
