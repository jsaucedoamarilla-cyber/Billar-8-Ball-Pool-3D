# BILLAR 8 PRO — APK Android

Este proyecto compila una aplicación Android WebView que incluye el juego ubicado en `app/www`.
El código web se conserva en esa carpeta y se empaqueta como assets de Android.

## Compilación automática
Cada cambio en `android/**` o `app/www/**` ejecuta el flujo **Build BILLAR 8 PRO APK**.
También se puede ejecutar manualmente desde GitHub → Actions → Build BILLAR 8 PRO APK → Run workflow.

Cuando termina correctamente, descargar el artefacto **BILLAR-8-PRO-APK**. Dentro está `app-debug.apk`.

## Estado de pruebas
El flujo está preparado, pero el APK aún debe compilarse y probarse en un teléfono Android real.
La conexión Firebase requiere internet para las funciones online. El modo local del juego está dentro del APK.
