package dat.exceptions;


public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    // Generalized so they can apply to multiple DAOs
    // Nested static classes, without access to one another
    public static class EntityNotFoundException extends DaoException {
        public EntityNotFoundException(Class<?> entityClass, Object id) {
            super(entityClass.getSimpleName() + " with ID " + id + " not found");
        }
    }

    public static class EntityDeleteException extends DaoException {
        public EntityDeleteException(Class<?> entityClass, Object id, Throwable cause) {
            super("Failed to delete " + entityClass.getSimpleName() + " with ID " + id, cause);
        }
    }

    public static class EntityUpdateException extends DaoException {
        public EntityUpdateException(Class<?> entityClass, Object id, Throwable cause) {
            super("Failed to update " + entityClass.getSimpleName() + " with ID " + id, cause);
        }
    }

    public static class EntityCreateException extends DaoException {
        public EntityCreateException(Class<?> entityClass, Throwable cause) {
            super("Failed to create " + entityClass.getSimpleName() + " entity", cause);
        }
    }

    public static class EntityFindAllException extends DaoException {
        public EntityFindAllException(Class<?> entityClass, Throwable cause) {
            super("Failed to find all " + entityClass.getSimpleName() + " entities", cause);
        }
    }

}
