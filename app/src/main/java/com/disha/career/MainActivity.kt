package com.disha.career

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.disha.career.billing.BillingManager
import com.disha.career.ui.AppViewModel
import com.disha.career.ui.DishaApp
import com.disha.career.ui.theme.DishaTheme

class MainActivity : ComponentActivity() {
    private val vm: AppViewModel by viewModels()
    private lateinit var billing: BillingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        billing = BillingManager(applicationContext) { vm.markUnlocked() }
        billing.connect()

        setContent {
            DishaTheme {
                val price by billing.price.collectAsStateWithLifecycle()
                DishaApp(
                    vm = vm,
                    price = price,
                    onBuy = { billing.launchPurchase(this) },
                    // Lets you test the paid report before the Play product exists.
                    onDebugUnlock = if (BuildConfig.DEBUG) vm::markUnlocked else null,
                )
            }
        }
    }

    override fun onDestroy() {
        billing.end()
        super.onDestroy()
    }
}
