# Portafolio Web - Isaac Guzman

![Preview del Portafolio](./public/img/proyectos/portafolio.webp)

Portafolio web personal enfocado en mostrar proyectos, habilidades y servicios de desarrollo web con un diseño moderno y orientado a resultados.

---

## 🚀 Sobre el proyecto

Este portafolio fue desarrollado con el objetivo de:

- Mostrar mis proyectos de forma clara y atractiva  
- Presentar mis servicios como desarrollador web  
- Generar oportunidades laborales y clientes  

---
## 🛠 Tech Stack & Tools
<p align="left">
  <a href="https://skillicons.dev" target="_blank">
    <img src="https://skillicons.dev/icons?i=html,css,js,figma&perline=4" alt="Core Technologies"/>
  </a>
</p>

---

## 📁 Estructura del proyecto
```
portfolio-web/
├── index.html
├── public/
│   ├── icons/
│   └── images/
├── src/
│   ├── js/
│   │   └── script.js
│   ├── css/
│   │   ├── button.css
│   │   ├── footer.css
│   │   ├── form.css
│   │   ├── header.css
│   │   ├── index.css
│   │   ├── index.min.css
│   │   ├── mascotas.css
│   │   ├── mascotas.min.css
│   │   ├── proyectos.css
│   │   ├── proyectos.min.css
│   │   ├── responsive.css
│   │   └── responsive.min.css
│   └── views/
│       ├── mascotas.html
│       └── proyectos.html
└── README.md
```
---

## 🧪 Nueva plataforma: ejecución local

La nueva versión (Spring Boot + Angular) se desarrolla en este mismo repositorio:

```
backend/              API con Spring Boot 4 y Java 21
frontend/             sitio con Angular 22 y SSR
docs/                 documentación del proyecto
docker-compose.yml    PostgreSQL para desarrollo local
```

### Requisitos

- [JDK 21](https://adoptium.net/)
- [Node.js](https://nodejs.org/) 22.22.3, 24.15.0 o 26 en adelante (lo que pide Angular CLI 22)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### 1. Levantar la base de datos

```bash
docker compose up -d
```

Crea un contenedor `portfolio-postgres` con PostgreSQL 18 en el puerto `5432`. Los datos se guardan en el volumen `postgres-data`, así que no se pierden al apagarlo. Para detenerlo: `docker compose down`.

### 2. Arrancar el backend

```bash
cd backend
./mvnw spring-boot:run
```

En Windows (CMD o PowerShell) usa `mvnw.cmd spring-boot:run`. La API queda en `http://localhost:8080`.

La conexión usa estas variables de entorno; si no existen, toma los valores de `docker-compose.yml`:

| Variable | Valor local por defecto |
|---|---|
| `DB_HOST` | `localhost` |
| `DB_PORT` | `5432` |
| `DB_NAME` | `portfolio` |
| `DB_USER` | `portfolio` |
| `DB_PASSWORD` | `portfolio` |

### 3. Arrancar el frontend

```bash
cd frontend
npm install
npm start
```

El sitio queda en `http://localhost:4200` y se recarga solo al guardar cambios.

Para probar el servidor con SSR, como en producción:

```bash
cd frontend
npm run build
NG_ALLOWED_HOSTS=localhost npm run serve:ssr:portfolio
```

En PowerShell, define la variable antes: `$env:NG_ALLOWED_HOSTS="localhost"; npm run serve:ssr:portfolio`. El sitio queda en `http://localhost:4000`.

`NG_ALLOWED_HOSTS` es la lista de dominios desde los que el servidor acepta peticiones, separados por comas. Sin ella responde `400 Bad Request`. Es una protección de Angular contra ataques SSRF.

### 4. Correr los tests

```bash
cd backend
./mvnw test
```

Necesitan la base de datos del paso 1 encendida.

```bash
cd frontend
npm test -- --watch=false
```

Sin `--watch=false` los tests se quedan escuchando cambios y se repiten al guardar.

---

## 🌐 Demo

**[Ver portafolio](https://isaac-g17.github.io)**.

---

## 👨‍💻 Autor

Este proyecto fue creado por **[Isaac Guzmán Mora](https://github.com/Isaac-G17)**.
