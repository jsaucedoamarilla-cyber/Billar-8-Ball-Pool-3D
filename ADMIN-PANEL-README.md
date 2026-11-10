# Panel de administración BILLAR 8 PRO

## Estado
Panel privado preparado en una rama de desarrollo. No está publicado en el juego ni modifica la rama principal.

## Incluye
- Acceso del administrador mediante Google.
- Verificación del permiso con el documento `admins/{UID}`.
- Listado de jugadores con respaldo cloud en `data/users/{UID}/save`.
- Consulta de reportes, salas y cuentas bloqueadas.
- Moderación manual escalonada: primer aviso, segundo aviso y bloqueo al tercer aviso.
- Registro de cada medida en `moderation/{UID}/events`.
- Notificación dentro del juego y posibilidad de levantar un bloqueo.
- Cálculo transaccional del siguiente nivel de sanción para evitar incrementos simultáneos perdidos.

## Reglas candidatas
El archivo `firestore-admin-addon.rules` contiene ahora un conjunto completo basado en las últimas reglas publicadas que se inspeccionaron, más los permisos del panel. Actualiza las rutas existentes en lugar de agregar bloques duplicados.

**Importante:** Firebase Console mostraba cambios sin publicar. Todavía no conocemos el contenido de esos cambios pendientes, por lo que esta versión candidata NO debe publicarse ni reemplazar el editor hasta compararla y conservar cualquier trabajo pendiente. No se ha desplegado.

## Límites
- Solo aparecen jugadores que hayan vinculado una cuenta y guardado progreso en la nube. Los invitados que nunca vincularon cuenta conservan su progreso local y no pueden enumerarse desde Firestore.
- La lista consulta hasta 300 respaldos por carga; si el juego supera ese volumen habrá que agregar paginación.
- Un reporte es una denuncia, no una prueba. El administrador revisa antes de sancionar.
- El bloqueo impide operaciones online cubiertas por las reglas; no elimina la cuenta Firebase Authentication. Un invitado podría volver con otra UID.
- Google OAuth y Firestore aún requieren pruebas reales en Chrome y con dos cuentas/dispositivos.
- La página está pensada para abrirse desde HTTPS en Chrome; no se garantiza el inicio Google dentro de un WebView local APK.

## Configuración del único administrador
1. Comparar y validar las reglas candidatas antes de publicarlas.
2. Abrir el panel desde el sitio HTTPS e iniciar sesión con la cuenta Google elegida.
3. Si aún no está autorizada, copiar el UID que muestra la pantalla.
4. Desde Firebase Console, crear manualmente el documento `admins/{UID}`. No permitir que jugadores se autoasignen ese rol.
5. Verificar que otra cuenta Google no autorizada vea solo la pantalla de acceso denegado.

No compartir contraseñas ni claves privadas. El UID no es una contraseña.

## Pruebas pendientes antes de producción
- Validar reglas en Firebase Emulator Suite y comparar los cambios pendientes de Firebase Console.
- Confirmar que el admin puede leer jugadores, reportes, salas y bloqueos.
- Confirmar que un jugador común no puede leer datos ajenos ni consultar reportes.
- Probar aviso 1, aviso 2, bloqueo al tercer acto, entrega del aviso final y desbloqueo.
- Probar vinculación Google, persistencia y recuperación del progreso entre dos teléfonos.
