# Parcial práctico: juegos cooperativos

**Tiempo orientativo: 45 minutos.** Partir del proyecto preparcial. Se entregan este documento, `datos-parcial.csv` y la carpeta `tests-nuevos`. Mantener los tests existentes.

El CSV del parcial contiene 140 filas de datos más encabezado (141 líneas).

## Reglas nuevas
El nuevo encabezado es `id;nombre;anio;min_jugadores;max_jugadores;duracion;rating;peso;categoria;dificultad_cooperativa`. Conservar también el formato original de nueve columnas: todos sus juegos son normales. Cada fila debe tener el ancho indicado por su encabezado.

Para juegos cooperativos (indicado por la presencia de un valor en la 10ma columna):
- La dificultad cooperativa debe ser un entero entre 1 y 10. De lo contrario, la fila es inválida.
- La puntuación ajustada se calcula como: `(rating * peso) + (dificultad_cooperativa / 2.0)`.

## Resultado requerido
1. Integrar la lectura de ambas versiones del CSV sin duplicar el proceso de carga.
2. Hacer que `getPuntuacionAjustada()` refleje la nueva regla mediante polimorfismo.
3. **Servicio**: Incorpore un método que calcule la suma total de la `puntuacionAjustada` y otro que cuente juegos por tipo (Normal/Cooperativo).
4. **Main**: Actualice el `Main` para que use por defecto `datos/datos-parcial.csv` y muestre un resumen completo (panorama) que incluya el total de puntos y los conteos por tipo.
5. Conservar el comportamiento anterior y pasar los tests existentes y los nuevos.

---
**Éxito en el examen.**
