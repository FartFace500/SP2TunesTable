package app.daos.impl;

import app.daos.IDAO;
import app.dtos.AlbumDTO;
import app.entities.Album;
import app.exceptions.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AlbumDAO implements IDAO<AlbumDTO, Integer> {

    private static AlbumDAO instance;
    private static EntityManagerFactory emf;

    public static AlbumDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AlbumDAO();
        }
        return instance;
    }

    @Override
    public AlbumDTO read(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Album album = em.find(Album.class, integer);
            if (album == null) {
                throw new DaoException.EntityNotFoundException(Album.class, integer);
            }
            return new AlbumDTO(album);
        } catch (Exception e) {
            throw new DaoException.EntityNotFoundException(Album.class, integer);
        }
    }

    @Override
    public List<AlbumDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<AlbumDTO> query = em.createQuery("SELECT new app.dtos.AlbumDTO(a) FROM Album a", AlbumDTO.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException.EntityFindAllException(Album.class, e);
        }
    }

    @Override
    public AlbumDTO create(AlbumDTO albumDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Album album = new Album(albumDTO);
            int existingAlbums = em.createQuery("SELECT COUNT(a) FROM Album a", Long.class).getSingleResult().intValue();
            album.giveId(existingAlbums);
            em.persist(album);
            em.getTransaction().commit();
            return new AlbumDTO(album);
        } catch (Exception e) {
            throw new DaoException.EntityCreateException(Album.class, e);
        }
    }

    @Override
    public AlbumDTO update(Integer integer, AlbumDTO albumDTO) {        // TODO correct the setting of fields
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Album a = em.find(Album.class, integer);
            if (a == null) {
                throw new DaoException.EntityNotFoundException(Album.class, integer);
            }
            a.setName(albumDTO.getName());
//            a.setPopularity(albumDTO.getPopularity());
            Album mergedAlbum = em.merge(a);
            em.getTransaction().commit();
            return mergedAlbum != null ? new AlbumDTO(mergedAlbum) : null;
        } catch (Exception e) {
            throw new DaoException.EntityUpdateException(Album.class, integer, e);
        }
    }

    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Album album = em.find(Album.class, integer);
            if (album == null) {
                throw new DaoException.EntityNotFoundException(Album.class, integer);
            }
            em.remove(album);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new DaoException.EntityDeleteException(Album.class, integer, e);
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Album album = em.find(Album.class, integer);
            return album != null;
        }
    }
}
