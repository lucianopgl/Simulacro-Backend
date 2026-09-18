# Parcial Práctico: Sistema Evaluador de Riesgo Crediticio (Fintech)

**Tiempo orientativo: 45 minutos.** Partir exactamente del proyecto preparcial. Se entregan este documento, `datos-parcial.csv` y la carpeta `tests-nuevos`. Mantener los tests existentes.

El CSV del parcial contiene 140 filas de datos más encabezado (141 líneas); es un conjunto distinto del preparcial de 60 filas. El volumen no agrega reglas ni tareas de programación. 

La *fintech* incorpora si el cliente que realiza la solicitud de credito es `EN_MORA` o `NORMAL`. Se necesita procesar las solicitudes considerando el nivel de morosidad del cliente y obtener información de analisis financiero para los mismos.

## Reglas Nuevas

El nuevo encabezado del archivo es `idCliente,tipoCliente,ingresosMensuales,totalDeudasActuales,estadoSolicitud,nivelMorosidad`. Se conserva el formato original de las cinco columnas. Cada fila debe tener el ancho indicado en su encabezado, no aceptar filas de cinco campos bajo un encabezado de seis, ni a la inversa.

Para las filas con los estados *APROBADO, PRE_APROBADO*, el sexto campo puede admitir `EN_MORA` o `NORMAL`, las solicitudes *RECHAZADO* solo adminte `EN_MORA`. Despues de quitar espacios externos, si una solicitud `RECHAZADO` tiene un valor **distinto** a `EN_MORA`, esa fila se considera invalida, para las solicitudes con los estados *APROBADO, PRE_APROBADO* se considera invalida luego de quitar los espacios externos se encuentra vacio, o bien si el estado es un valor desconocido.

- NORMAL conserva todas las reglas anteriores y sus calculos tal cual estan definidos.
- EN_MORA se consideran como solicitudes validas si sus ingresos superan los 25000 y sus deudas no pueden superar el 12% de sus ingresos
- Conservar la precedencia del preparcial: verificar ancho y después descartar BLACK sin validar pesos ni modalidad. Las filas inválidas se registran y la carga continúa.

Ejemplos:

- Cliente PREMIUM con ingresos declarados de 15000 y deudas de 3000 y con nivel de morosidad NORMAL su score financiero es de 80.
- Cliente PREMIUM  con ingresos declarados de 15000 y su nivel de morosidad EN_MORA invalido y debe lanzar IllegarArgumentException
- Cliente PREMIUM  con ingresos declarados de 15000 y deudas de 3000,  su nivel de morosidad EN_MORA invalido y debe lanzar IllegarArgumentException.

## Resultado Requerido

1. Integrar la lectura de ambas versiones del CSV sin duplicar el proceso de carga.
2. Reflejar las nuevas reglas en el proceso de importación de Solicitudes de Créditos.
3. Incorporar en `CarteraCreditos` un conteo por Nivel de Morosidad. Puede elegir el nombre y representación de esa operación; para una colección vacía debe dar un resultado vacío o conteos cero.
4. Hacer que el main use por defecto el archivo del parcial y muestre resumen, cantidad de objetos, cantidades NORMAL/EN_MORA, total y totales por zona. Seguir delegando cálculos.
5. Conservar el comportamiento anterior y pasar los tests existentes y los nuevos. Puede agregar tests propios.

Se evalúa comportamiento observable, encapsulamiento y distribución de responsabilidades. Puede usar herencia, composición u otra solución razonable. Puede usar Streams o ciclos; no se exige una jerarquía ni una técnica determinada. No se evalúa el nombre concreto de una subclase.

## Preparacion y Entrega

Desde la raíz de su copia de `fintech`, copiar `tests-nuevos/ar` a `src/test/java/ar/edu/backend` fusionando directorios. Copiar `datos-parcial.csv` a `datos/datos-parcial.csv` y ajustar la ruta predeterminada del main.

```sh
mvn test
java -cp target/classes ar.edu.backend.fintech.App datos/datos-parcial.csv
```

Los nuevos tests usan archivos temporales; compilan sobre el proyecto inicial y algunos deben fallar antes de resolver. El conteo por modalidad y el main se revisan además mediante ejecución, sin exigir un método nuevo con firma fija.

Entregar un único ZIP del proyecto con `pom.xml`, `src/` y `datos/`; excluir `target/`, `.git/` y archivos del IDE. Subirlo en el ítem 10 del cuestionario. Incluir un texto breve `DECISIONES.md` (3–5 líneas) que indique la estrategia elegida y si quedó algo pendiente.

Distribución sugerida: 5 minutos de lectura, 25 de cambios, 10 de pruebas y 5 para revisar y empaquetar.