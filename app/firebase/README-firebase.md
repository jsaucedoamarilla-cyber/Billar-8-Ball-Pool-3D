# Poner Billar 8 online con Firebase

La app ya está conectada: si `www/firebase-config.js` tiene datos, usa Firebase; si está vacío, funciona 100 % offline y oculta lo online.

## 1) Crear el proyecto (10 min)
1. https://console.firebase.google.com → **Agregar proyecto** (desactivá Google Analytics: menos datos que declarar).
2. **Authentication → Método de acceso → Anónimo → Habilitar.**
3. **Firestore Database → Crear base de datos** → modo **producción** → región cercana (ej. `southamerica-east1`, São Paulo).
4. **Configuración del proyecto → Tus apps → </> Web** → copiá `firebaseConfig` y pegalo en `www/firebase-config.js`.
5. En `firebase-config.js` poné `B8_INVITE_URL` con el enlace de tu app en Play Store (cuando lo tengas).

## 2) Publicar reglas e índices
```
npm i -g firebase-tools
firebase login
firebase use --add          # elegí tu proyecto
cd firebase && firebase deploy --only firestore
```
(`firestore.rules`, `firestore.indexes.json` y `firebase.json` están en esta carpeta.)

## 3) SDK dentro de la app (sin cargar código de internet)
Desde la raíz de tu proyecto Capacitor: `bash firebase/copiar-sdk.sh` → copia los `*-compat.js` a `www/vendor/` y hace `cap sync`.

## 4) Probar
Dos celulares (o un celular + emulador) → **Multijugador → Crear sala** en uno y **Unirme** con el código en el otro. Probá chat, **Reportar/Bloquear** y **Ajustes → Privacidad → Borrar todos mis datos**.
Los reportes aparecen en Firestore → colección `reports`.

## 5) Moderación
- Revisá `reports` con frecuencia.
- Para banear: Firestore → colección `banned` → crear documento con ID = UID del jugador (campo cualquiera). Queda sin acceso a toda la base.
- Un baneado anónimo puede volver reinstalando; es una limitación del acceso anónimo.

## Límites honestos (leelos)
- **Prueba hecha:** verifiqué el cableado con un Firebase *simulado* (sala creada con el UID correcto, aviso de reglas, filtro). **No** probé contra Firebase real ni con el emulador de reglas: probalo antes de publicar.
- El juego es **cliente-autoritativo**: cada celular decide el resultado. Un jugador técnico puede hacer trampa o inflar su ELO/regalos; las reglas solo evitan que escriba en datos ajenos. Para rankings serios hace falta lógica en servidor (Cloud Functions, plan Blaze).
- Reglas de `clanes` y torneos son amplias (cualquier jugador autenticado puede editarlos).
- Los chats y salas **no se borran solos** (al terminar solo se borra el documento de la sala). Hacé limpieza periódica o agregá una Cloud Function.
- **App Check** (recomendado contra bots) con el SDK web dentro de un WebView requiere reCAPTCHA; evaluá activarlo en modo "monitor" primero.
- **Cuotas/costos:** cada tiro online escribe en Firestore. Mirá las cuotas vigentes del plan gratuito y poné alerta de presupuesto.
- Cuenta anónima: si el jugador borra los datos de la app o reinstala, pierde su perfil online (es el costo de no pedir correo).
