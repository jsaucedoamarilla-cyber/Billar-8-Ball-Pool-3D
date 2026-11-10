# Panel de administración BILLAR 8 PRO

## Estado
Este panel es un borrador de desarrollo en una rama separada. No está publicado y no modifica el juego en producción.

## Qué prepara
- Acceso privado con Google.
- Resumen de jugadores con respaldo de nube, reportes, salas y bloqueos.
- Revisión manual de reportes.
- Escalera de moderación: aviso 1, aviso 2 y bloqueo al tercer acto sancionable.
- Registro de acciones y notificación dentro del juego.
- Posibilidad de levantar un bloqueo.

## Límites que hay que conocer
- Los jugadores invitados que nunca vincularon su cuenta guardan datos solo en su dispositivo; no aparecen como perfiles consultables en Firestore.
- El panel no detecta trampas automáticamente. Un reporte es una denuncia, no prueba. La sanción se aplica después de revisión humana.
- El bloqueo propuesto impide las operaciones online cubiertas por las reglas de Firestore. No elimina la sesión de Firebase Auth. Un invitado podría volver con otra UID; para reducirlo, debe vincular una cuenta permanente.
- La página espera un documento `admins/{UID}`. Ese documento debe crearlo el propietario desde Firebase Console; los usuarios no pueden autoasignarse el rol.
- `firestore-admin-addon.rules` es solo un complemento. No reemplazar ni publicar reglas todavía: al revisar Firebase Console se encontraron cambios sin publicar. Primero hay que revisar esos cambios y fusionar el complemento con cuidado.
- El juego actual tiene reportes y guardado cloud en una rama de desarrollo, pero el flujo de login/redirección en Android y la consulta de datos deben probarse en navegador y en dos dispositivos.

## Próximos pasos antes de usarlo
1. Revisar el diff de este PR.
2. Confirmar cuál UID Google será el administrador principal.
3. Revisar y fusionar las reglas con los cambios pendientes actuales de Firebase.
4. Crear el documento admins/{UID} desde Firebase Console.
5. Probar con una cuenta de jugador de prueba: primer aviso, segundo aviso, tercer bloqueo, notificación y levantamiento del bloqueo.
6. Probar que un jugador normal no pueda leer jugadores, reportes, sanciones ni cambiar sus propios permisos.
