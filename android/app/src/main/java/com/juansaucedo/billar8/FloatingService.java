package com.juansaucedo.billar8;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PathEffect;
import android.graphics.DashPathEffect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatingService extends Service {
    private WindowManager windowManager;
    private GuideView guideView;
    private LinearLayout panel;
    private TextView bubble;
    private boolean lineVisible = true;
    private float angle = -90f;
    private float originX = .50f;
    private float originY = .70f;
    private float lineLength = .34f;
    private long expiresAt = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable expiryCheck = new Runnable() {
        @Override public void run() {
            if (expiresAt > 0 && System.currentTimeMillis() >= expiresAt) {
                stopSelf();
                return;
            }
            handler.postDelayed(this, 5000);
        }
    };

    @Override public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(new NotificationChannel(
                "b8_float_guide", "Guía flotante BILLAR 8 PRO", NotificationManager.IMPORTANCE_LOW));
        }
        Intent open = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 4813, open,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Notification.Builder nb = Build.VERSION.SDK_INT >= 26
            ? new Notification.Builder(this, "b8_float_guide")
            : new Notification.Builder(this);
        Notification notification = nb.setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentTitle("BILLAR 8 PRO")
            .setContentText("Guía flotante manual activa")
            .setContentIntent(pi).setOngoing(true).build();
        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(81, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        } else {
            startForeground(81, notification);
        }
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("expires_at")) {
            expiresAt = intent.getLongExtra("expires_at", 0);
            getSharedPreferences("aim_overlay", MODE_PRIVATE).edit().putLong("expires_at", expiresAt).apply();
        } else if (expiresAt == 0) {
            expiresAt = getSharedPreferences("aim_overlay", MODE_PRIVATE).getLong("expires_at", 0);
        }
        if (!Settings.canDrawOverlays(this) || expiresAt <= System.currentTimeMillis()) {
            stopSelf();
            return START_NOT_STICKY;
        }
        createOverlay();
        handler.removeCallbacks(expiryCheck);
        handler.post(expiryCheck);
        return START_STICKY;
    }

    private int dp(float value) {
        return (int)(value * getResources().getDisplayMetrics().density + .5f);
    }

    private int overlayType() {
        return Build.VERSION.SDK_INT >= 26
            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            : WindowManager.LayoutParams.TYPE_PHONE;
    }

    private WindowManager.LayoutParams params(int w, int h, int flags, int gravity) {
        return new WindowManager.LayoutParams(w, h, overlayType(), flags, PixelFormat.TRANSLUCENT);
    }

    private void createOverlay() {
        if (windowManager != null) { refresh(); return; }
        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);

        guideView = new GuideView();
        WindowManager.LayoutParams gp = params(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            Gravity.TOP | Gravity.START);
        try { windowManager.addView(guideView, gp); } catch (Exception e) { stopSelf(); return; }

        panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(8), dp(8), dp(8), dp(8));
        panel.setBackgroundColor(Color.argb(235, 12, 28, 43));
        TextView title = new TextView(this);
        title.setText("🎯 GUÍA FLOTANTE MANUAL");
        title.setTextColor(Color.WHITE); title.setTextSize(12); title.setGravity(Gravity.CENTER);
        panel.addView(title);
        panel.addView(row(button("↶  Girar", v -> { angle -= 3; refresh(); }),
            button("Girar  ↷", v -> { angle += 3; refresh(); })));
        panel.addView(row(button("←", v -> { originX = Math.max(.04f, originX - .015f); refresh(); }),
            button("↑", v -> { originY = Math.max(.08f, originY - .015f); refresh(); }),
            button("↓", v -> { originY = Math.min(.94f, originY + .015f); refresh(); }),
            button("→", v -> { originX = Math.min(.96f, originX + .015f); refresh(); })));
        panel.addView(row(button("Línea −", v -> { lineLength = Math.max(.08f, lineLength - .03f); refresh(); }),
            button("Línea +", v -> { lineLength = Math.min(.85f, lineLength + .03f); refresh(); })));
        panel.addView(row(button("Ocultar línea", v -> { lineVisible = !lineVisible; ((Button)v).setText(lineVisible ? "Ocultar línea" : "Mostrar línea"); refresh(); }),
            button("Cerrar panel", v -> { panel.setVisibility(View.GONE); })));
        TextView help = new TextView(this);
        help.setText("Ajustá manualmente el punto y la dirección sobre la mesa. No detecta bolas automáticamente.");
        help.setTextColor(Color.LTGRAY); help.setTextSize(10); help.setGravity(Gravity.CENTER);
        panel.addView(help);
        panel.setVisibility(View.GONE);

        bubble = new TextView(this);
        bubble.setText("🎯"); bubble.setTextSize(24); bubble.setGravity(Gravity.CENTER);
        bubble.setTextColor(Color.WHITE);
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL); bg.setColor(Color.argb(235, 25, 40, 72));
        bg.setStroke(dp(2), Color.rgb(255, 210, 63));
        bubble.setBackground(bg);
        bubble.setOnClickListener(v -> panel.setVisibility(panel.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));

        WindowManager.LayoutParams bp = params(dp(58), dp(58),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            Gravity.CENTER_VERTICAL | Gravity.START);
        bp.x = dp(8); bp.y = 0;
        WindowManager.LayoutParams pp = params(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            Gravity.TOP | Gravity.END);
        pp.x = dp(8); pp.y = dp(90);
        try {
            windowManager.addView(bubble, bp);
            windowManager.addView(panel, pp);
        } catch (Exception e) { stopSelf(); }
    }

    private LinearLayout row(View... views) {
        LinearLayout r = new LinearLayout(this); r.setOrientation(LinearLayout.HORIZONTAL);
        for (View v : views) r.addView(v, new LinearLayout.LayoutParams(0, dp(38), 1f));
        return r;
    }

    private Button button(String text, View.OnClickListener listener) {
        Button b = new Button(this); b.setText(text); b.setTextSize(10); b.setAllCaps(false);
        b.setPadding(dp(2), 0, dp(2), 0); b.setOnClickListener(listener); return b;
    }

    private void refresh() { if (guideView != null) guideView.invalidate(); }

    private class GuideView extends View {
        private final Paint line = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint halo = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint dot = new Paint(Paint.ANTI_ALIAS_FLAG);
        GuideView() {
            super(FloatingService.this);
            line.setColor(Color.rgb(255, 220, 65)); line.setStrokeWidth(dp(3));
            line.setStyle(Paint.Style.STROKE); line.setStrokeCap(Paint.Cap.ROUND);
            line.setPathEffect(new DashPathEffect(new float[]{dp(12), dp(8)}, 0));
            halo.setColor(Color.argb(80, 255, 220, 65)); halo.setStyle(Paint.Style.FILL);
            dot.setColor(Color.WHITE); dot.setStyle(Paint.Style.FILL);
        }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!lineVisible || expiresAt <= System.currentTimeMillis()) return;
            float sx = getWidth() * originX, sy = getHeight() * originY;
            double rad = Math.toRadians(angle);
            float ex = sx + (float)Math.cos(rad) * getWidth() * lineLength;
            float ey = sy + (float)Math.sin(rad) * getWidth() * lineLength;
            canvas.drawCircle(sx, sy, dp(12), halo);
            canvas.drawCircle(sx, sy, dp(5), dot);
            canvas.drawLine(sx, sy, ex, ey, line);
            canvas.drawCircle(ex, ey, dp(7), line);
        }
    }

    @Override public void onDestroy() {
        handler.removeCallbacks(expiryCheck);
        getSharedPreferences("aim_overlay", MODE_PRIVATE).edit().remove("expires_at").apply();
        try { if (guideView != null) windowManager.removeView(guideView); } catch (Exception ignored) {}
        try { if (panel != null) windowManager.removeView(panel); } catch (Exception ignored) {}
        try { if (bubble != null) windowManager.removeView(bubble); } catch (Exception ignored) {}
        guideView = null; panel = null; bubble = null; windowManager = null;
        if (Build.VERSION.SDK_INT >= 24) stopForeground(STOP_FOREGROUND_REMOVE); else stopForeground(true);
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}