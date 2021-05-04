package br.com.enterative.enterativeflutter

import android.content.ComponentName
import android.content.Intent
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    private val CHANNEL = "br.com.enterative/printer"

    @Suppress("UNCHECKED_CAST")
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->

            if (call.method == "print")
                this.print(call.arguments as Map<String, String>)

            result.success(null)
        }
    }

    private fun print(args: Map<String, String>) {
        val intent = Intent()
        intent.component = ComponentName("br.com.enterative.printer", "br.com.enterative.printer.Printer")
        intent.action = "ACTION_VIEW"
        for (entry in args.entries) {
            intent.putExtra(entry.key, entry.value)
        }
        startActivity(intent)
    }
}
