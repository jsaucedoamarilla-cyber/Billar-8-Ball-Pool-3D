package com.juansaucedo.billar8;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int REQUEST_OVERLAY = 4812;
    private static final int REQUEST_IMAGE_PICKER = 4813;
    private ValueCallback<Uri[]> pendingFileCallback;
    private WebView web;
    private long pendingOverlayExpiry = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        web = new WebView(this);
        web.setBackgroundColor(0xFF0C1F1F);
        setContentView(web);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        web.addJavascriptInterface(new AimBridge(), "AndroidAim");
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {
                if (pendingFileCallback != null) {
                    pendingFileCallback.onReceiveValue(null);
                    pendingFileCallback = null;
                }
                pendingFileCallback = filePathCallback;
                Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
                pick.addCategory(Intent.CATEGORY_OPENABLE);
                pick.setType("image/*");
                try {
                    startActivityForResult(Intent.createChooser(pick, "Elegir foto de perfil"), REQUEST_IMAGE_PICKER);
                } catch (Exception e) {
                    pendingFileCallback.onReceiveValue(null);
                    pendingFileCallback = null;
                    Toast.makeText(MainActivity.this, "No se pudo abrir la galería.", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
        web.loadUrl("file:///android_asset/index.html");
        hideSystemBars();
    }

    private class AimBridge {
        @JavascriptInterface
        public void start(long expiresAt) {
            runOnUiThread(() -> startAimOverlay(expiresAt));
        }

        @JavascriptInterface
        public void stop() {
            runOnUiThread(() -> stopAimOverlay());
        }

        @JavascriptInterface
        public void copyText(String value) {
            runOnUiThread(() -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(ClipData.newPlainText("ID del jugador", value == null ? "" : value));
                    Toast.makeText(MainActivity.this, "ID del jugador copiado.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @JavascriptInterface
        public void requestAccess(String message) {
            runOnUiThread(() -> {
                String safeMessage = message == null ? "" : message;
                Uri uri = Uri.parse("https://wa.me/595984363598?text=" + Uri.encode(safeMessage));
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "No se pudo abrir WhatsApp. Revisá que esté instalado.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void startAimOverlay(long expiresAt) {
        if (expiresAt <= System.currentTimeMillis()) {
            Toast.makeText(this, "El permiso de líneas ya venció.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            pendingOverlayExpiry = expiresAt;
            Toast.makeText(this, "Autorizá BILLAR 8 PRO para mostrar el botón flotante.", Toast.LENGTH_LONG).show();
            Intent permission = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
            startActivityForResult(permission, REQUEST_OVERLAY);
            return;
        }
        Intent service = new Intent(this, FloatingService.class);
        service.putExtra("expires_at", expiresAt);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(service);
        else startService(service);
        Toast.makeText(this, "Botón flotante activado.", Toast.LENGTH_SHORT).show();
    }

    private void stopAimOverlay() {
        pendingOverlayExpiry = 0;
        stopService(new Intent(this, FloatingService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER) {
            if (pendingFileCallback != null) {
                Uri[] results = null;
                if (resultCode == RESULT_OK && data != null) {
                    Uri selected = data.getData();
                    if (selected != null) results = new Uri[]{selected};
                }
                pendingFileCallback.onReceiveValue(results);
                pendingFileCallback = null;
            }
            return;
        }
        if (requestCode == REQUEST_OVERLAY) {
            if (Settings.canDrawOverlays(this) && pendingOverlayExpiry > System.currentTimeMillis()) {
                long expiry = pendingOverlayExpiry;
                pendingOverlayExpiry = 0;
                startAimOverlay(expiry);
            } else {
                pendingOverlayExpiry = 0;
                Toast.makeText(this, "Sin permiso de superposición no puede aparecer el botón flotante.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void hideSystemBars() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemBars();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (web != null) web.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (web != null) web.onResume();
        hideSystemBars();
    }

    @Override
    protected void onDestroy() {
        if (web != null) {
            web.destroy();
            web = null;
        }
        super.onDestroy();
    }
}