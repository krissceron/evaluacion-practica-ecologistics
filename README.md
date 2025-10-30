
#  EcoLogistics Integration

Proyecto de integración desarrollado con **Apache Camel** (Java + Maven) para la empresa ficticia **EcoLogistics**.  
El sistema implementa un flujo completo de integración empresarial que transforma información desde archivos CSV hacia un servicio API REST, exponiendo los datos en formato JSON.

---

##  Objetivo
Diseñar e implementar una **solución de integración funcional** que cumpla los siguientes puntos:

1. **Lectura de archivos CSV** con registros de envíos.  
2. **Transformación de datos** a formato JSON.  
3. **Exposición de servicios REST** (GET, POST) para consultar y registrar envíos.  
4. **Documentación automática de la API** mediante OpenAPI.

---

## ⚙️ Arquitectura del Sistema
[Archivo CSV] → [Apache Camel: File Component]
↓
[Transformación CSV → Objeto Java → JSON]
↓
[Memory Store (simula BD en memoria)]
↓
[Exposición vía REST (Netty HTTP + OpenAPI)]

Componentes principales:
- **Apache Camel** → Motor de integración.
- **Netty HTTP** → Servidor embebido para los endpoints REST.
- **Camel CSV + Jackson** → Conversión de formatos.
- **OpenAPI** → Documentación automática de la API.
- **SLF4J / Logback** → Logging estructurado.

---

##  Estructura del Proyecto
evaluacion-practica-ecologistics/
├── pom.xml
├── input/
│ └── envios.csv
├── output/
│ └── envios.json
└── src/
└── main/java/com/ecologistics/
├── AppMain.java
├── model/Envio.java
└── store/MemoryStore.java


---

##  Requerimientos

- **Java 17+**
- **Apache Maven 3.8+**
- **Postman o cURL** (para pruebas)
- **Puerto 8081 libre**

---

##  Instalación y Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/<tu-usuario>/ecologistics-integration.git
   cd ecologistics-integration

2. **Compilar el proyecto: **
   ```bash
	mvn -DskipTests clean package
3. **Ejecutar: **
   ```bash
	java -jar target/evaluacion-practica-ecologistics-1.0-SNAPSHOT.jar
