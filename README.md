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

## 2.- Capturas de Pantalla

### Pantalla Principal
![Pantalla Principal](docs/screenshots/main_activity.png)

### Configuración de Teléfono
![Configuración](docs/screenshots/conf_activity.png)

---

## 3.- Funcionalidades Principales

1. **Llamada rápida (SOS)**  
   - Llama al número configurado desde la pantalla principal.
   - Verifica permisos `CALL_PHONE` en tiempo de ejecución.

2. **Configuración de número de teléfono**  
   - Pantalla de configuración (`ConfActivity`) para guardar el número favorito.
   - Validación de número utilizando **Google libphonenumber**.

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

## 4.- Requisitos del Sistema

- Android Studio 2022 o superior.  
- Dispositivo con Android 6.0 (API 23) o superior.  
- Permisos necesarios:
  - `CALL_PHONE` → Para realizar llamadas desde la app.  
  - `SET_ALARM` → Para crear alarmas automáticamente.

---

## 5.- Instalación y Build

1. Clona el repositorio:

```bash
git clone https://github.com/tu_usuario/SOSPhone.git
