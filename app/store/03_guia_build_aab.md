# Guía: generar el .AAB firmado (Capacitor)

Play Store pide **AAB** (no APK) y, desde el 31/08/2026, apps nuevas y actualizaciones deben apuntar a **Android 16 (API 36)**.

## 1) Poner el juego actualizado
```
cp -r www/* <tu-proyecto>/www/
```
(no hace falta copiar a `android/app/src/main/assets/public`; `cap sync` lo hace).
Verificá que existan `www/manifest.webmanifest` y `www/icons/icon-192.png` (el index los referencia).

## 2) Capacitor actual
```
npm i @capacitor/core@latest @capacitor/cli@latest @capacitor/android@latest
npx cap sync android
```
Usá una versión de Capacitor que soporte API 36 (Capacitor 8 o superior).

## 3) Versión y SDK (`android/variables.gradle` y `android/app/build.gradle`)
- `compileSdkVersion = 36` y `targetSdkVersion = 36`
- `versionCode 1` y `versionName "1.0.0"` (cada subida nueva: versionCode +1)
- `applicationId` definitivo (ej. `com.tunombre.billar8`). **No se puede cambiar después de publicar.**

## 4) Manifest (`AndroidManifest.xml`)
- En `<activity>`: `android:screenOrientation="sensorLandscape"` (el juego es apaisado).
- `capacitor.config.json`: **sin** `server.url` (la app debe correr con el contenido local, no cargar un sitio web; si no, Play la rechaza por funcionalidad mínima).
- No agregues permisos que no uses.

## 5) Clave de firma (una sola vez; guardala en 2 lugares)
```
keytool -genkeypair -v -keystore billar8-upload.jks -alias billar8 -keyalg RSA -keysize 2048 -validity 10000
```
Creá `android/keystore.properties` (NO lo subas a git):
```
storeFile=../billar8-upload.jks
storePassword=****
keyAlias=billar8
keyPassword=****
```
En `android/app/build.gradle`, dentro de `android {}`:
```
def ks = new Properties(); def f = rootProject.file("keystore.properties")
if (f.exists()) ks.load(new FileInputStream(f))
signingConfigs { release { storeFile file(ks['storeFile']); storePassword ks['storePassword']; keyAlias ks['keyAlias']; keyPassword ks['keyPassword'] } }
buildTypes { release { signingConfig signingConfigs.release; minifyEnabled false } }
```
Activá **Play App Signing** al crear la app en Play Console (viene activado por defecto).

## 6) Compilar
```
cd android
./gradlew bundleRelease
```
Salida: `android/app/build/outputs/bundle/release/app-release.aab`

## 7) Probar antes de subir
- Instalar en un celular real (APK de debug o `bundletool`) y revisar: arranca sin pantalla en blanco, gira a apaisado, no se recorta con notch/barras (desde API 36 es edge-to-edge obligatorio), sonido, vibración, "Privacidad → Borrar datos".
- En Play Console, subir primero a **Prueba interna** y revisar el informe de pre-lanzamiento.

## 8) Subir
1. Play Console → Crear app (Juego, gratuita).
2. Completar **Contenido de la app** con `02_formularios_play_console.md`.
3. Ficha de tienda con `01_ficha_play_store.md` + `assets/`.
4. Prueba cerrada (≥12 testers, 14 días) si tu cuenta es personal nueva → pedir producción.
5. Lanzar a producción y esperar revisión (suele tardar de horas a unos días).

## Causas típicas de rechazo (y cómo se cubrieron)
| Motivo | Estado |
|---|---|
| Falta política de privacidad | Incluida (`privacy-policy.html` + botón dentro de la app) — falta que la publiques |
| Funciones rotas / "requiere conexión" sin servidor | Multijugador, clanes y rankings online **ocultos** en esta versión |
| Anuncio falso / contenido de relleno | Eliminado |
| Lenguaje de apuestas con "dinero" | Monedas virtuales aclaradas; "Apuesta" → "Entrada"; "Primer dólar" → "Primer diamante" |
| Data safety inconsistente | Respuestas listas: no se recopilan datos |
| Target API viejo | Ver paso 3 (API 36) |
| App que solo envuelve una web | App con contenido local, sin `server.url` |
