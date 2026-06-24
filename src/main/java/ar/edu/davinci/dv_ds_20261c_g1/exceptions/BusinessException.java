package ar.edu.davinci.dv_ds_20261c_g1.exceptions;

/**
 * Excepcion de negocio para errores controlados de la aplicacion.
 */
public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
