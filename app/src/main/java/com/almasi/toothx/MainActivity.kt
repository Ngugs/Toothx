package com.almasi.toothx

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.almasi.toothx.ui.theme.ToothxTheme
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToothxTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun Greeting(name: String,
             modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            Log.d("dddd", BluetoothPrintersConnections.selectFirstPaired()?.isConnected.toString())
            print()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }

    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(12.dp)
    ) {
        Button(
            onClick = {
            if (permissions.all {
                    ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) == PackageManager.PERMISSION_GRANTED
                }){
                Log.d("fffff",BluetoothPrintersConnections.selectFirstPaired().toString())
                print()
            } else {
                // Request a permission
                permissionLauncher.launch(permissions)
            }

        }) {
            Text(text = "Pair Printer.")
        }


    }
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
fun print() {
    val printer = EscPosPrinter(
        BluetoothPrintersConnections.selectFirstPaired(),
        203,
        48f,
        32
    )
    val formattedText = createFormattedText(printer = printer)
    printer.printFormattedText(formattedText)
}

private fun createFormattedText(printer: EscPosPrinter): String {
    return "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
            "[L]\n" +
            "[C]================================\n" +
            "[L]\n" +
            "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
            "[L]  + Size : S\n" +
            "[L]\n" +
            "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
            "[L]  + Size : 57/58\n" +
            "[L]\n" +
            "[C]--------------------------------\n" +
            "[R]TOTAL PRICE :[R]34.98e\n" +
            "[R]TAX :[R]4.23e\n" +
            "[L]\n" +
            "[C]================================\n" +
            "[L]\n" +
            "[L]<font size='tall'>Customer :</font>\n" +
            "[L]Raymond DUPONT\n" +
            "[L]5 rue des girafes\n" +
            "[L]31547 PERPETES\n" +
            "[L]Tel : +33801201456\n" +
            "[L]\n" +
            "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
            "[C]<qrcode size='20'>https://dantsu.com/</qrcode>";
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToothxTheme {
        Greeting("Android")
    }
}