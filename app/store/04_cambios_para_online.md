# Cambios en Play Console al activar el modo online (reemplaza lo de `02_formularios` en estos puntos)

## Política de privacidad
Usá la nueva `privacy-policy.html` (versión online). La anterior quedó como `privacy-policy-offline.html`.
Play también pide una **URL de eliminación de datos** si hay cuentas: usá la misma página (sección 6).

## Seguridad de los datos (Data safety) — versión online
Declará que **sí** se recopilan datos y revisá cada categoría contra el formulario vigente de Play:
| Categoría | Tipo | Finalidad | Compartido |
|---|---|---|---|
| Info personal | Nombre (apodo del jugador) | Funcionalidad de la app | No |
| Info personal | ID de usuario (UID anónimo de Firebase) | Funcionalidad, seguridad | No |
| Mensajes | Otros mensajes en la app (chat de partida) | Funcionalidad, seguridad/moderación | No |
| Actividad en la app | Interacciones en la app (resultados, ELO) | Funcionalidad | No |
| Ubicación | Aproximada, solo si el país lo detecta la app (si el jugador lo elige a mano, declarála igual por prudencia) | Funcionalidad | No |
- Cifrado en tránsito: **Sí** (HTTPS).
- Los usuarios pueden pedir que se borren sus datos: **Sí** (en la app + correo).
- Recopilación opcional: el online es opcional (se puede jugar sin conexión).
- "Compartido": Firebase actúa como proveedor de servicios, no cuenta como compartir según la definición de Play; confirmalo en el formulario.

## Clasificación de contenido (IARC)
Volvé a completar el cuestionario: **«Los usuarios pueden interactuar / intercambiar contenido» = Sí** (chat). Puede subir la edad recomendada.

## Público objetivo
13+ (no marcar menores de 13).

## Política de contenido generado por usuarios (UGC) — qué ya cubre la app
- ✅ Filtro automático de mensajes y nombres (incluye enlaces).
- ✅ Reglas de convivencia que se aceptan antes de jugar online.
- ✅ Reportar y bloquear al rival desde el chat de la partida.
- ✅ Lista de baneados (`banned/{uid}`) que corta el acceso a toda la base.
- ⚠️ **Vos tenés que revisar la colección `reports`** en Firebase Console y actuar (Google evalúa que haya moderación real).
- ⚠️ En esta primera versión online están **ocultos** espectadores, clanes, torneos online y rankings de regalos (`B8_ONLY_MATCHES = true`), porque su chat/nombres aún no tienen botón de reportar.
