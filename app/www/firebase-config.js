/* Configuración de Firebase para Billar 8.
   Mientras apiKey esté vacío, el juego funciona 100% sin conexión y las funciones online quedan ocultas.
   Pegá acá los valores de: Firebase Console → Configuración del proyecto → Tus apps → Web → firebaseConfig.
   (La apiKey web NO es un secreto: la seguridad la dan las reglas de Firestore.) */
window.B8_FIREBASE_CONFIG = {
  apiKey: "",
  authDomain: "",
  projectId: "",
  storageBucket: "",
  messagingSenderId: "",
  appId: ""
};
/* Enlace de tu app en Play Store (para invitar amigos), ej.: https://play.google.com/store/apps/details?id=com.tunombre.billar8 */
window.B8_INVITE_URL = "";
/* true = primera versión online: solo partidas 1 vs 1 con chat (con reportar/bloquear) y ranking ELO.
   Espectadores, clanes, torneos online y rankings de regalos quedan ocultos hasta que sumes moderación para ellos. */
window.B8_ONLY_MATCHES = true;
