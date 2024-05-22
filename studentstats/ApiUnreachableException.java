package studentstats;

/**
 * Exception indicating the API was unreachable (after the specified number of retries).
 *
 * <p>In a real application this would more typically be an Exception, not a RuntimeException, but
 * for our purposes we consider an unreachable API to be an irrecoverable error.
 */
public class ApiUnreachableException extends RuntimeException {}
