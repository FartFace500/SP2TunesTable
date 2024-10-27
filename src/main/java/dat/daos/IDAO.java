package dat.daos;

import java.util.List;

public interface IDAO<T, I> {

    T read(I id);

    List<T> readAll();

    T create(T entity);

    T update(I id, T entity);

    void delete(I id);

    boolean validatePrimaryKey(I id);
}