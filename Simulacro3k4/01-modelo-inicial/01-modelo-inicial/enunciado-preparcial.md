# Preparcial: Gestión de Catálogo de Juegos de Mesa

## Contexto
La tienda "BGG Clone" necesita un sistema para procesar su catálogo de juegos de mesa almacenado en archivos CSV. El objetivo es leer los datos, validar su integridad, y generar estadísticas básicas.

## Estructura del Proyecto
El proyecto Maven incluye las siguientes clases en el paquete `ar.edu.backend.juegos`:
- `JuegoMesa`: Representa un juego con sus atributos y reglas de negocio.
- `ParserJuegos`: Encargado de la lectura del archivo CSV.
- `ResultadoParseo`: Resume el resultado del procesamiento.
- `ServicioJuegos`: Provee operaciones de consulta sobre la colección de juegos.

## Reglas de Negocio Iniciales
1. **Validación de Datos**: Un juego es válido si:
   - Su ID no es nulo ni vacío.
   - Su nombre no es vacío.
   - Su año de publicación está entre 1900 y 2026.
   - Su rating está entre 0.0 y 10.0.
   - Su peso (complejidad) está entre 1.0 y 5.0.
   - El máximo de jugadores es mayor o igual al mínimo.
2. **Procesamiento CSV**:
   - Si una fila tiene formato incorrecto o viola invariantes de construcción, se considera **inválida**.
   - Si el juego es construible pero su **rating es menor a 1.0**, se considera **descartado** (no se incluye en la colección final).
   - El resto de los casos se consideran **procesados**.
3. **Cálculo Derivado**: La `puntuacionAjustada` se calcula como `rating * peso`.

## Tareas Recomendadas
- Explore el código proporcionado.
- Ejecute los tests unitarios con `mvn test`.
- Ejecute la clase `Main` y observe la salida por consola.
- Asegúrese de comprender cómo se distribuyen las responsabilidades entre el parser, el modelo y el servicio.

---
**Nota**: Este material es preparatorio para el parcial. Durante el examen se solicitará una evolución del modelo.

