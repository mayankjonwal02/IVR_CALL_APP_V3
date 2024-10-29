import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.ivr_call_app_v3.android.FunctionalComponents.AlarmComponents.MyAlarmreceiver

// Create the BroadcastReceiver
class TestBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Log.d("TestBroadcastReceiver", "Received broadcast with action: $action")

        if (action == "com.example.ivr_call_app_v3.TEST_ACTION") {
            Toast.makeText(context, "Test Broadcast Received!", Toast.LENGTH_SHORT).show()
            Log.d("TestBroadcastReceiver", "Test Broadcast Received with action: $action")
        }
    }
}

@Composable
fun TestBroadcastUI() {
    val context = LocalContext.current

    Column {
        Button(onClick = {
            // Send a broadcast with the action declared in the manifest
            val intent = Intent(context,MyAlarmreceiver::class.java)
           intent.action = "com.example.ivr_call_app_v3.TEST_ACTION"

            context.sendBroadcast(intent) // This sends the broadcast to any receiver registered in the manifest
        }) {
            Text(text = "Send Test Broadcast")
        }

        Text(text = "Press the button to send a test broadcast", color = Color.Blue)
    }
}
