# 📚 Literalura - Catálogo de Libros

Literalura es una aplicación de consola desarrollada en **Java** utilizando **Spring Boot**. Permite buscar, registrar y analizar libros y autores a partir de la API pública de Gutendex, proporcionando estadísticas y gestión de datos en una base de datos PostgreSQL.

---

## 🚀 Características

1. **Buscar libros por título:** Consulta en la API de Gutendex y almacena resultados en la base de datos.
2. **Listar libros registrados:** Muestra los libros almacenados en el sistema.
3. **Listar autores registrados:** Obtén una lista de los autores guardados junto con sus libros.
4. **Buscar autores vivos por año:** Encuentra autores que estaban vivos en un año específico.
5. **Filtrar libros por idioma:** Muestra los libros disponibles en diferentes idiomas.
6. **Estadísticas de descargas:** Analiza la cantidad de descargas de los libros registrados.
7. **Top 10 libros más descargados:** Visualiza los libros más populares en base a las descargas.
8. **Buscar autor por nombre:** Encuentra información detallada de un autor registrado.
0. **Persistencia en base de datos:** Utiliza PostgreSQL para almacenar libros y autores.

---

## 🏗️ Estructura del Proyecto

El proyecto está organizado en las siguientes capas:

- **`controller/`**: Contiene la clase `Menu` para la interacción con el usuario.
- **`model/`**: Define las entidades `Autor`, `Libro` y `Idiomas`.
- **`repository/`**: Interfaces de acceso a la base de datos (JPA).
- **`service/`**: Lógica de negocio para libros y autores.
- **`resources/`**: Configuración de la aplicación en `application.properties`.

---

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.4.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Jackson (para manejo de JSON)**
- **Gutendex API (para obtener información de libros)**

---

## ⚙️ Instalación y Configuración

### 1. Clonar el repositorio

git clone https://github.com/TuUsuario/literalura.git
cd literalura

2. Configurar la base de datos
Modifica el archivo src/main/resources/application.properties con tus credenciales de PostgreSQL:

3. Construcción del proyecto
Ejecuta el siguiente comando para compilar:
mvn clean install

4. Ejecutar la aplicación
Inicia la aplicación con:
mvn spring-boot:run

📝 Uso de la Aplicación
Al ejecutar la aplicación, verás un menú con las siguientes opciones:

--------------
Bienvenido a Literalura
1.- Buscar libro por título
![image](https://github.com/user-attachments/assets/a793c1c0-8627-47fe-9dcc-a80ee08907ba)

2.- Listar libros registrados
![image](https://github.com/user-attachments/assets/de85e9cf-3cc2-44e7-838a-34b2ed44f810)

3.- Listar autores registrados
![image](https://github.com/user-attachments/assets/747bdef5-8f7c-44f9-a9a8-f080cb682890)

4.- Listar autores vivos en un determinado año
![image](https://github.com/user-attachments/assets/375746f9-65e1-4b3f-afda-e73c3dd3f6e9)

5.- Listar libros por idioma
![image](https://github.com/user-attachments/assets/37a124f0-b5a0-4baa-9e5c-b67f303edc7e)

6.- Estadísticas de libros por número de descargas
![image](https://github.com/user-attachments/assets/5bf10cbb-8435-40f7-96ed-1eb07f2acea8)

7.- Top 10 libros más descargados
![image](https://github.com/user-attachments/assets/11afc713-1f50-42c1-bb4a-4024e6bafa2a)

8.- Buscar autor por nombre
![image](https://github.com/user-attachments/assets/5ee10db2-60b4-4550-99e4-3a08d153e03f)

9.- Salir
