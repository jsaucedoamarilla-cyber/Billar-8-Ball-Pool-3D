#!/usr/bin/env bash
# Copia el SDK de Firebase (compat) dentro de www/vendor para que la app NO cargue código desde internet.
# Uso (desde la raíz de tu proyecto Capacitor):  bash firebase/copiar-sdk.sh
set -e
npm i firebase
mkdir -p www/vendor
for f in app auth firestore; do
  src="node_modules/firebase/firebase-$f-compat.js"
  if [ ! -f "$src" ]; then echo "No encuentro $src (cambió el paquete). Revisá node_modules/firebase/ y copiá los *-compat.js"; exit 1; fi
  cp "$src" "www/vendor/firebase-$f-compat.js"
done
echo "SDK copiado a www/vendor/"
npx cap sync android
