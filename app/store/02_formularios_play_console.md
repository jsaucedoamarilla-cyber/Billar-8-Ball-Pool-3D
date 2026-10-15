# Respuestas sugeridas para Play Console (versión actual de la app)

Respondé siempre con lo que la app **realmente** hace. Si agregás funciones después (anuncios, compras, multijugador, cuentas), hay que actualizar estos formularios.

## 1. Contenido de la app → Política de privacidad
URL pública de `privacy-policy.html` (GitHub Pages, Google Sites o tu web). Debe abrir sin login.

## 2. Anuncios
**¿Tu app contiene anuncios?** → **No**.

## 3. Acceso a la app
**Todas las funciones están disponibles sin credenciales** (se juega como invitado, sin login).

## 4. Público objetivo y contenido
- Grupos de edad: **13–15, 16–17 y 18+** (no marcar menores de 13 → evita los requisitos de "Diseñada para familias").
- ¿Atrae involuntariamente a niños? Es un juego de billar; si Play lo pregunta, respondé con honestidad según tu criterio.

## 5. Seguridad de los datos (Data safety)
La versión incluida guarda todo **solo en el dispositivo** (localStorage), sin enviar nada a servidores propios ni de terceros, sin SDK de analítica, anuncios ni crash reporting.
- ¿Recopila o comparte datos de usuario requeridos? → **No**.
- Declaración: "No se recopilan datos" / "No se comparten datos".
- Cifrado en tránsito / eliminación de datos: no aplican (no hay datos recopilados). Igual la app incluye el botón **Ajustes → Privacidad → Borrar todos mis datos**.

## 6. Clasificación de contenido (cuestionario IARC)
Categoría: **Juego**. Respuestas:
- Violencia, sangre, contenido sexual, lenguaje soez, drogas/alcohol/tabaco: **No**.
- Interacción entre usuarios / chat / compartir ubicación: **No** (el multijugador y el chat están desactivados en esta versión).
- Compras digitales: **No**.
- Juegos de azar simulados (casino, póker, tragamonedas): **No**. (Hay una ruleta gratuita de recompensas y monedas virtuales sin valor real; si el cuestionario pregunta por recompensas aleatorias, respondé con honestidad.)
- Resultado esperado: apto para todo público (PEGI 3 / ESRB Everyone aprox.). El resultado final lo calcula el cuestionario, no esta guía.

## 7. Otras declaraciones
- App de noticias: No · Gobierno: No · Salud/medicina: No · Finanzas: No · COVID-19: No · Criptomonedas: No.
- ID de publicidad (Advertising ID): **No** lo usa. (Capacitor no lo incluye por defecto; verificá que ninguna librería lo agregue.)
- Permisos: solo los que declara el manifiesto por defecto de Capacitor (INTERNET). No pidas cámara, micrófono, ubicación ni almacenamiento.

## 8. Cuenta personal nueva (creada después del 13/11/2023)
Antes de producción: **prueba cerrada con ≥12 testers durante 14 días seguidos**, luego solicitar acceso a producción. Las cuentas de organización están exentas. Las pruebas internas **no** cuentan.
