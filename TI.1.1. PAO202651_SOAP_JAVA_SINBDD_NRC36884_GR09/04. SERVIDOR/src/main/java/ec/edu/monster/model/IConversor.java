package ec.edu.monster.model;

/**
 * Interfaz genérica para el patrón Estrategia (Strategy Pattern)
 * que define el contrato algorítmico de transformación entre unidades
 * pertenecientes a una misma magnitud física.
 * 
 * Proyecto: TI.1.1 SOAP Java SINBDD
 * Asignatura: Arquitectura de Software | NRC: 36884 | Grupo: GR09
 * Autor: Julio Enrique Viche Castillo
 * 
 * @param <U> Tipo de enumeración que representa el catálogo estricto de unidades.
 */
public interface IConversor<U> {

    /**
     * Convierte un valor numérico desde una unidad de origen hacia una unidad de destino.
     * 
     * @param valor Valor numérico a convertir.
     * @param origen Unidad de origen tipada.
     * @param destino Unidad de destino tipada.
     * @return Valor numérico resultante de la conversión.
     */
    double convertir(double valor, U origen, U destino);

    /**
     * Resuelve y valida una cadena de texto para determinar la unidad correspondiente.
     * 
     * @param identificador Nombre o símbolo de la unidad ingresada.
     * @return La unidad tipada correspondiente.
     * @throws IllegalArgumentException si la unidad no pertenece al catálogo oficial de 5 unidades.
     */
    U resolverUnidad(String identificador);
}
