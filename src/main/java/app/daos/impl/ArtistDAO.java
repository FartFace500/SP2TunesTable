package app.daos.impl;

import app.daos.IDAO;
import app.dtos.ArtistDTO;
import app.entities.Artist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ArtistDAO implements IDAO<ArtistDTO, Integer> {

    private static ArtistDAO instance;
    private static EntityManagerFactory emf;

    public static ArtistDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ArtistDAO();
        }
        return instance;
    }

    @Override
    public ArtistDTO read(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Artist artist = em.find(Artist.class, integer);
            return new ArtistDTO(artist);
        }
    }

    @Override
    public List<ArtistDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<ArtistDTO> query = em.createQuery("SELECT new app.dtos.ArtistDTO(a) FROM Artist a", ArtistDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public ArtistDTO create(ArtistDTO artistDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Artist artist = new Artist(artistDTO);
            em.persist(artist);
            em.getTransaction().commit();
            return new ArtistDTO(artist);
        }
    }

    @Override
    public ArtistDTO update(Integer integer, ArtistDTO artistDTO) {     // TODO correct the setting of fields
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Artist a = em.find(Artist.class, integer);
            a.setName(artistDTO.getName());
            Artist mergedArtist = em.merge(a);
            em.getTransaction().commit();
            return mergedArtist != null ? new ArtistDTO(mergedArtist) : null;
        }
    }

    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Artist artist = em.find(Artist.class, integer);
            if (artist != null) {
                em.remove(artist);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Artist artist = em.find(Artist.class, integer);
            return artist != null;
        }
    }
}
