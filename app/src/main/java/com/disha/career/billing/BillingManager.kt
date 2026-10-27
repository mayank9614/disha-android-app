package com.disha.career.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * One-time Google Play purchase that unlocks the full report forever.
 *
 * There is no backend, so purchases are trusted on the device. That is easier to
 * crack than server verification, but acceptable for a ₹9 unlock.
 */
class BillingManager(
    context: Context,
    private val onEntitled: () -> Unit,
) : PurchasesUpdatedListener {

    private val client = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    private var productDetails: ProductDetails? = null

    private val _price = MutableStateFlow<String?>(null)
    /** Localised price from Play, e.g. "₹9.00"; null until Play answers. */
    val price: StateFlow<String?> = _price.asStateFlow()

    fun connect() {
        if (client.isReady) return
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingResponseCode.OK) {
                    queryProduct()
                    restorePurchases()
                }
            }

            override fun onBillingServiceDisconnected() = Unit
        })
    }

    /** Returns false when Play isn't ready, so the UI can tell the student. */
    fun launchPurchase(activity: Activity): Boolean {
        val details = productDetails ?: run { connect(); return false }
        val params = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(details).build())
            )
            .build()
        return client.launchBillingFlow(activity, params).responseCode == BillingResponseCode.OK
    }

    fun end() = client.endConnection()

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingResponseCode.OK) purchases?.forEach(::handlePurchase)
    }

    private fun queryProduct() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRODUCT_FULL_REPORT)
                        .setProductType(ProductType.INAPP)
                        .build()
                )
            )
            .build()
        client.queryProductDetailsAsync(params) { result, queryResult ->
            if (result.responseCode != BillingResponseCode.OK) return@queryProductDetailsAsync
            productDetails = queryResult.productDetailsList.firstOrNull()
            _price.value = productDetails?.oneTimePurchaseOfferDetails?.formattedPrice
        }
    }

    private fun restorePurchases() {
        val params = QueryPurchasesParams.newBuilder().setProductType(ProductType.INAPP).build()
        client.queryPurchasesAsync(params) { result, purchases ->
            if (result.responseCode == BillingResponseCode.OK) purchases.forEach(::handlePurchase)
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (PRODUCT_FULL_REPORT !in purchase.products) return
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return
        if (!purchase.isAcknowledged) {
            // Play refunds purchases that aren't acknowledged within 3 days.
            val params = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
            client.acknowledgePurchase(params) { }
        }
        onEntitled()
    }

    companion object {
        /** Must match the in-app product ID created in Play Console. */
        const val PRODUCT_FULL_REPORT = "full_report"
    }
}
