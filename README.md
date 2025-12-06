# App 📱

[![Android CI](https://img.shields.io/badge/Android-Stable-green?logo=android)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue?logo=kotlin)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 1.- Descripción

**EvaluableTema01** es una aplicación Android educativa desarrollada para estudiantes de 2DAM.  
Permite realizar **llamadas rápidas**, configurar un número predeterminado, abrir URLs, reproducir sorpresas aleatorias y crear alarmas automáticamente.  

Combina **interfaz sencilla**, **gestión de permisos en tiempo de ejecución** y **uso de SharedPreferences** para almacenar datos del usuario.

---

## 2.- Funcionalidades Principales

1. **Llamada rápida (SOS)**  
   - Llama al número configurado desde la pantalla principal.
   - Verifica permisos `CALL_PHONE` en tiempo de ejecución.

2. **Acceso a Funciones Secundarias (desde `ConfActivity`)**
   - Formulario de Configuración de Número: Permite al usuario guardar el número de teléfono favorito.
   - Validación de número utilizando Google libphonenumber.
   - Minijuego "Adivina el Número": Un juego donde el usuario debe adivinar el número que sale al tirar unos dados.

3. **Abrir URL aleatoria**  
   - Botón que abre la página [The Useless Web](https://theuselessweb.com/).

4. **Sorpresa Aleatoria**  
   - Reproduce un sonido o abre un video de YouTube al azar.

5. **Alarma automática**  
   - Crea una alarma 2 minutos después de pulsar el botón.
   - Configuración automática usando `AlarmClock.ACTION_SET_ALARM`.

6. **Gestión de permisos**  
   - Solicita permisos `CALL_PHONE` y `SET_ALARM` en tiempo de ejecución.
   - Redirige a la configuración de la app si el permiso es denegado.

---

## 3.- Requisitos del Sistema

- Android Studio 2022 o superior.  
- Dispositivo con Android 6.0 (API 23) o superior.  
- Permisos necesarios:
  - `CALL_PHONE` → Para realizar llamadas desde la app.  
  - `SET_ALARM` → Para crear alarmas automáticamente.

---

## 4.- Instalación y Build

1. Clona el repositorio y ejecutalo:

```bash
git clone https://github.com/tu_usuario/SOSPhone.git

```

## 5.- Instalación y Build

1. Abre el proyecto en **Android Studio**.  

2. Sincroniza el proyecto con Gradle para descargar las dependencias necesarias.  

3. Conecta un dispositivo Android o inicia un **emulador**.  

4. Ejecuta la aplicación desde **Android Studio** usando **Run > App**.

```bash
./gradlew assembleDebug
```
## 6.- Estructura del proyecto (desactualizado)
```bash
APP/
├─ app/
│  ├─ src/main/
│  │  ├─ java/com/example/sosphone/
│  │  │  ├─ MainActivity.kt        # Lógica principal y botones de la pantalla principal
│  │  │  ├─ ConfActivity.kt        # Pantalla con ImageViews para elegir el Formulario o Minijuego
│  │  │  ├─ FormActivity.kt        # Lógica del formulario de configuración de número
│  │  │  └─ DiceGameActivity.kt    # Lógica del minijuego de adivinar el número
│  │  ├─ res/
│  │  │  ├─ layout/
│  │  │  │  ├─ activity_main.xml
│  │  │  │  ├─ activity_conf.xml
│  │  │  │  ├─ activity_form.xml
│  │  │  │  └─ activity_dice_game.xml
│  │  │  ├─ drawable/
│  │  │  ├─ values/
│  │  │  │  └─ strings.xml
│  │  │  └─ mipmap/
│  │  └─ AndroidManifest.xml
├─ build.gradle
└─ README.md
```

## 7.- Notas Adicionales

1. Diseñada con fines educativos y de práctica en Android Studio y Kotlin.
2. Algunas funciones (alarmas, llamadas) pueden no funcionar en emuladores sin soporte de teléfono o alarmas.

