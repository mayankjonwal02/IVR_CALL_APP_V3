//import android.Manifest
//import android.os.Build
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.*
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.Modifier
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.background
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import com.example.ivr_call_app_v3.android.DataClasses.User
//import com.example.ivr_call_app_v3.android.FunctionalComponents.Storage.DatabaseHelper
//import com.example.ivr_call_app_v3.android.FunctionalComponents.AlarmComponents.scheduleservice
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun Users() {
//    val context = LocalContext.current
//
//    var getpermissions = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
//            permissions ->
//            if( permissions.values.all { it })
//            {
//                CoroutineScope(Dispatchers.Main).launch{
//                    Toast.makeText(context,"Permissions Granted",Toast.LENGTH_LONG).show()
//                }
//            }
//        else
//            {
//                CoroutineScope(Dispatchers.Main).launch{
//                    Toast.makeText(context,"Permissions Not Granted",Toast.LENGTH_LONG).show()
//                }
//            }
//    }
//
//    LaunchedEffect(Unit) {
//        getpermissions.launch(arrayOf(Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.BLUETOOTH_ADVERTISE,Manifest.permission.BLUETOOTH_PRIVILEGED,Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN,Manifest.permission.CALL_PHONE,Manifest.permission.READ_CONTACTS,Manifest.permission.SEND_SMS))
//    }
//    // Initialize the DatabaseHelper
//    val dbHelper = remember { DatabaseHelper(context) }
//
//    // State to hold the list of users
//    var users by remember { mutableStateOf(mutableListOf<User>()) }
//
//    // States for user input
//    var name by remember { mutableStateOf(TextFieldValue("")) }
//    var age by remember { mutableStateOf(TextFieldValue("")) }
//    var id by remember { mutableStateOf(0) }
//
//    // Load users when the composable is first composed
//    LaunchedEffect(Unit) {
//        val retrievedUsers = dbHelper.getAllUsers().toMutableList()
//        users = retrievedUsers
//        id = retrievedUsers.size + 1
//    }
//
//    // Function to add a new user
//    fun addUser() {
//        val userName = name.text
//        val userAge = age.text.toIntOrNull()
//
//        if (userName.isNotEmpty() && userAge != null) {
//            val newUser = User(name = userName, age = userAge, id = id)
//            dbHelper.insertUser(userName, userAge)
//            users = users.toMutableList().apply { add(newUser) }
//            name = TextFieldValue("") // Clear the input fields
//            age = TextFieldValue("")
//            id += 1
//        }
//    }
//
//    // UI layout
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(16.dp)
//    ) {
//        // Input fields for name and age
//        TextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Name") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = age,
//            onValueChange = { age = it },
//            label = { Text("Age") },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Number
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Button to add user
//        Button(
//            onClick = { addUser() },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text("Add User")
//        }
//
//
//        Button(
//            onClick = { scheduleservice(context = context) },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text("Schedule Alarm")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display the list of users
//        LazyColumn {
//            items(users) { user ->
//                Text(text = "Name: ${user.name}, Age: ${user.age}")
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//    }
//}
//
//
