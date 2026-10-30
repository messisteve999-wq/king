package com.king.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat

/**
 * Bulle flottante de King : un corbeau noir.
 * Un clic ouvre ou ferme la fenêtre flottante de messages (plus petite, police réduite).
 */
class FloatingBubbleService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var bubble: View
    private var chatWindow: View? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        startAsForeground()
        addBubble()
    }

    private fun startAsForeground() {
        val channelId = "king_bubble"
        val manager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(channelId, "King", NotificationManager.IMPORTANCE_LOW)
            )
        }
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("King")
            .setContentText("Bulle flottante active")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
    }

    private fun overlayType(): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE

    private fun addBubble() {
        val size = (56 * resources.displayMetrics.density).toInt()
        bubble = TextView(this).apply {
            text = "🐦‍⬛"
            textSize = 24f
            gravity = Gravity.CENTER
            setBackgroundColor(Color.BLACK)
            setTextColor(Color.WHITE)
        }
        val params = WindowManager.LayoutParams(
            size, size,
            overlayType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 300
        }

        bubble.setOnClickListener { toggleChat() }
        windowManager.addView(bubble, params)
    }

    private fun toggleChat() {
        val existing = chatWindow
        if (existing != null) {
            windowManager.removeView(existing)
            chatWindow = null
            return
        }
        val width = (260 * resources.displayMetrics.density).toInt()
        val height = (320 * resources.displayMetrics.density).toInt()
        val view = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#1A1A1A"))
            addView(TextView(this@FloatingBubbleService).apply {
                text = "King"
                textSize = 12f
                setTextColor(Color.parseColor("#E53935"))
                setPadding(16, 12, 16, 12)
            })
            addView(TextView(this@FloatingBubbleService).apply {
                text = "Aucune conversation pour l'instant."
                textSize = 11f
                setTextColor(Color.WHITE)
                setPadding(16, 8, 16, 8)
            })
        }
        val params = WindowManager.LayoutParams(
            width, height,
            overlayType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 380
        }
        windowManager.addView(view, params)
        chatWindow = view
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        chatWindow?.let { windowManager.removeView(it) }
        windowManager.removeView(bubble)
        super.onDestroy()
    }
}
