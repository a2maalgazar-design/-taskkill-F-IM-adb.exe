package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.net.URLEncoder

object WhatsAppUtil {

    data class DispatchedOrderItem(
        val name: String,
        val qty: Double,
        val unit: String
    )

    fun sanitizeEgyptianPhone(phone: String): String {
        val digits = phone.filter { it.isDigit() }
        return when {
            digits.startsWith("01") && digits.length == 11 -> "20" + digits.substring(1)
            digits.startsWith("201") && digits.length == 12 -> digits
            digits.length == 10 && digits.startsWith("1") -> "20$digits"
            digits.isNotEmpty() -> digits
            else -> "201000000000"
        }
    }

    fun buildKitchenOrderMessage(
        orderCode: String,
        categoryName: String,
        requesterName: String,
        items: List<DispatchedOrderItem>,
        notes: String = ""
    ): String {
        val validItems = items.filter { it.qty > 0 }
        val itemsList = validItems.mapIndexed { idx, it ->
            val formattedQty = if (it.qty % 1.0 == 0.0) it.qty.toLong().toString() else it.qty.toString()
            "${idx + 1}. ${it.name}: $formattedQty ${it.unit}"
        }.joinToString("\n")

        val count = validItems.size

        return """
📋 *طلب توريد مطبخ رسمي - KOMS*
━━━━━━━━━━━━━━━━━━
🔹 *كود الطلبية:* $orderCode
🔹 *القسم / التصنيف:* $categoryName
🔹 *المسؤول بالوردية:* $requesterName
🔹 *الموعد الإلزامي للوصول:* 01:00 ظهراً
━━━━━━━━━━━━━━━━━━
📦 *قائمة الأصناف المطلوبة ($count صنف):*
$itemsList
━━━━━━━━━━━━━━━━━━
${if (notes.isNotBlank()) "📌 *ملاحظات الشيف:* $notes\n━━━━━━━━━━━━━━━━━━\n" else ""}✅ *نظام KOMS الذكي للمطابخ*
        """.trimIndent()
    }

    fun openWhatsApp(context: Context, rawPhone: String, messageText: String): Boolean {
        return try {
            val sanitized = sanitizeEgyptianPhone(rawPhone)
            val encodedMessage = URLEncoder.encode(messageText, "UTF-8")
            val uri = Uri.parse("https://wa.me/$sanitized?text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            Toast.makeText(context, "تعذر فتح تطبيق واتساب. تم نسخ نص الطلب.", Toast.LENGTH_LONG).show()
            false
        }
    }
}
