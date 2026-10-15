# Billar 8 — Paquete Play Store + Firebase

## Contenido
- `www/` → `index.html`, `firebase-config.js` (vacío), se suma `vendor/` con `firebase/copiar-sdk.sh`.
- `firebase/` → reglas, índices, `README-firebase.md` (pasos), script del SDK.
- `privacy-policy.html` (versión **online**) y `privacy-policy-offline.html`.
- `store/01…03` (ficha, formularios base, build AAB) y `store/04_cambios_para_online.md`.
- `assets/` → ícono, gráfico de funciones, 6 capturas.

## Dos formas de publicar
- **A) Primero offline:** dejá `firebase-config.js` vacío y usá `privacy-policy-offline.html` + formularios de `02`.
- **B) Ya con online:** completá Firebase (README) y usá `privacy-policy.html` + `04_cambios_para_online.md`.
  Podés empezar en A y pasar a B con una actualización.

## Lo nuevo en el juego (online real)
- Login anónimo de Firebase y base Firestore (el juego ya hablaba "Firestore": solo cambié el origen de la base).
- UID de Firebase = ID del jugador (las reglas lo exigen).
- Reglas de convivencia, filtro de mensajes/nombres/enlaces, reportar y bloquear, lista de baneados.
- "Borrar todos mis datos" también borra los datos online.
- Foto de perfil nunca se sube a la nube.
- Primera versión online = solo partidas 1 vs 1 + ranking ELO (`B8_ONLY_MATCHES`).
- Pantalla de espera de sala: botones **Compartir por WhatsApp** y **Copiar** con el código de sala.
- Enlace de invitación: ya no usa `localhost`; usa `B8_INVITE_URL` o muestra el código de sala.

## Pendiente tuyo
- [ ] Crear proyecto Firebase y pegar la config.
- [ ] Desplegar reglas e índices; copiar el SDK.
- [ ] Probar con 2 celulares y probar las reglas.
- [ ] Reemplazar correo/nombre en la política y publicarla.
- [ ] Actualizar Data safety e IARC (`04_…`).
- [ ] Compilar AAB (API 36) — ver `store/03_guia_build_aab.md`.
