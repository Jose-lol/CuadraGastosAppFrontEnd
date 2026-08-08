# CuadraGastos - Android

Aplicación Android para gestionar y saldar gastos entre amigos, compañeros de piso, grupos de viaje y otros grupos.

La aplicación permite organizar los gastos realizados dentro de un grupo, consultar los saldos y facilitar la liquidación de las deudas entre sus participantes.

> 🚧 **Proyecto personal en desarrollo.** La aplicación continúa evolucionando con nuevas funcionalidades y mejoras.

---

## 📱 Descripción

CuadraGastos nace como una aplicación para simplificar la gestión de gastos compartidos.

Está pensada para situaciones como:

- Viajes entre amigos.
- Gastos compartidos entre compañeros de piso.
- Comidas o actividades en grupo.
- Gastos comunes entre familiares o amigos.

El objetivo es que los usuarios puedan registrar los gastos realizados y conocer fácilmente cuánto debe cada persona y a quién debe pagar.

---

## 🚀 Tecnologías

- **Android Studio**
- **Android**
- **Java / Kotlin** 
- **REST API**
- **JWT**
- **Access Tokens**
- **Refresh Tokens**
- **Google Authentication**

> Si tu aplicación está desarrollada únicamente en Kotlin o únicamente en Java, elimina la que no corresponda.

---

## 🔐 Autenticación

La aplicación utiliza un sistema de autenticación basado en **Access Tokens y Refresh Tokens**.

### Inicio de sesión

El usuario puede iniciar sesión mediante:

- Usuario y contraseña.
- Cuenta de Google.

La autenticación se realiza contra el backend desarrollado con **Spring Boot y Spring Security**.

---

## 🔄 Renovación automática de sesión

Uno de los objetivos principales de la aplicación es evitar que el usuario tenga que volver a iniciar sesión cuando su Access Token caduca.

El funcionamiento es:

```text
Usuario inicia sesión
        │
        ▼
   Access Token
        │
        ▼
   Uso de la aplicación
        │
        ▼
Access Token caduca
        │
        ▼
 Android utiliza Refresh Token
        │
        ▼
   Solicita nuevo Access Token
        │
        ▼
Continúa la sesión
```

De esta forma, la renovación de la sesión se realiza de forma transparente para el usuario.

---

## 👥 Gestión de grupos

La aplicación está diseñada para trabajar con diferentes tipos de grupos.

Por ejemplo:

- Grupos de amigos.
- Compañeros de piso.
- Viajes.
- Familia.
- Cualquier grupo que comparta gastos.

Los usuarios pueden participar en grupos y registrar los gastos realizados dentro de ellos.

---

## 💰 Gestión de gastos

La aplicación permite registrar gastos realizados dentro de un grupo y utilizar esta información para calcular los saldos entre los participantes.

El objetivo es facilitar la respuesta a preguntas como:

> ¿Quién ha pagado?

> ¿Cuánto corresponde a cada persona?

> ¿Quién debe dinero?

> ¿A quién hay que pagar?

---

## 🔗 Comunicación con el backend

La aplicación Android se comunica con el backend mediante una **API REST**.

```text
┌─────────────────────┐
│    Android App      │
│      Frontend       │
└──────────┬──────────┘
           │
        REST API
           │
           ▼
┌─────────────────────┐
│     Spring Boot     │
│       Backend       │
└─────────────────────┘
```

El backend se encarga de la autenticación, autorización, gestión de usuarios, grupos, gastos y demás lógica de negocio.

**Repositorio del backend:**

https://github.com/Jose-lol/CuadraGastosAppBackEnd

---

## 🏗️ Arquitectura

El proyecto está organizado siguiendo una separación entre la aplicación Android y el backend.

```text
                  ┌──────────────────────┐
                  │      Android         │
                  │       App            │
                  └──────────┬───────────┘
                             │
                             │ REST API
                             │
                             ▼
                  ┌──────────────────────┐
                  │      Spring Boot     │
                  │       Backend        │
                  └──────────────────────┘
```

La aplicación Android se encarga de la interfaz y de la interacción con el usuario, mientras que el backend gestiona la lógica de negocio y los datos.

## ⚙️ Configuración

Para ejecutar la aplicación es necesario disponer del proyecto backend funcionando y correctamente configurado.

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU-USUARIO/TU-REPOSITORIO.git
```

### 2. Abrir el proyecto

Abrir el proyecto utilizando **Android Studio**.

### 3. Configurar la URL del backend

Configurar la dirección de la API REST utilizada por la aplicación.

Por ejemplo:

```text
http://10.0.2.2:8080
```

> La dirección dependerá de la configuración utilizada y de si la aplicación se ejecuta en un emulador o en un dispositivo físico.

### 4. Ejecutar la aplicación

Sincronizar el proyecto con Gradle y ejecutar la aplicación utilizando un emulador Android o un dispositivo físico.

---

## 🔑 Configuración de Google

La aplicación permite iniciar sesión mediante una cuenta de Google.

Para utilizar esta funcionalidad es necesario configurar correctamente las credenciales correspondientes tanto en el proyecto Android como en el backend.

Las credenciales y claves privadas no deben incluirse en el repositorio.

---

## 📌 Estado del proyecto

**En desarrollo 🚧**

Actualmente se continúa trabajando en:

- Nuevas funcionalidades.
- Gestión de grupos.
- Gestión de gastos.
- Cálculo de saldos.
- Mejoras en la autenticación.
- Mejoras en la experiencia de usuario.
- Integración y mejoras de comunicación con el backend.

---

## 🔗 Proyecto completo

Este repositorio forma parte de un proyecto compuesto por:

### Backend

Java + Spring Boot + Spring Security + Redis + MySQL

**Repositorio:**  
https://github.com/Jose-lol/CuadraGastosAppBackEnd

### Frontend

Aplicación Android desarrollada con Android Studio.

**Repositorio:**  
https://github.com/Jose-lol/CuadraGastosAppFrontEnd

---

## 👨‍💻 Autor

**Jose Antonio Norte Lopez**

Proyecto personal desarrollado como parte de mi aprendizaje y evolución como desarrollador de software.
