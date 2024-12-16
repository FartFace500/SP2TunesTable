package app.utils.json;

import app.dtos.AlbumDTO;
import app.dtos.ArtistDTO;
import app.dtos.SongDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonReader {
    public static void main(String[] args) {
        readAlbums("");
    }

    // Use in populate run method for one album
    public static AlbumDTO readAlbum(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        AlbumDTO albumResult = null;
        try {
            // Read JSON file and convert to JsonNode
            JsonNode rootNode = null;
            if (filePath.equals("")){
            rootNode = objectMapper.readTree(new File("src/result.json"));
            } else {
            rootNode = objectMapper.readTree(new File(filePath));
            }

            // Deserialize general information into AlbumDTO
            AlbumDTO album = objectMapper.treeToValue(rootNode, AlbumDTO.class);

            // Access the "items" array directly from tracks
            List<SongDTO> songs = objectMapper.treeToValue(rootNode.path("tracks").path("items"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, SongDTO.class));
            album.setSongs(songs);

            albumResult = album;

            // Print the item details
            System.out.println("name: " + album.getName());
            System.out.println("type: " + album.getType());
            System.out.println("release date: " + album.getReleaseDate());
            System.out.println("total songs: " + album.getTotalSongs());
            System.out.println("total songs: " + album.getTotalSongs());
            System.out.println("artist: " + album.getArtist());
            songs.forEach(System.out::println);
//            System.out.println("songs: " + album.getTracks().getSongs().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return albumResult;
    }

    // Use in populate run method for multiple albums
    public static List<AlbumDTO> readAlbums(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<AlbumDTO> albumList = new ArrayList<>();

        try {
            JsonNode rootNode = null;
            if (filePath.equals("")){
                rootNode = objectMapper.readTree(new File("src/result.json"));
            } else {
                rootNode = objectMapper.readTree(new File(filePath));
            }

            JsonNode albumsNode = rootNode.path("albums");

            if (albumsNode.isArray()) {
                for (JsonNode node : albumsNode) {
                    AlbumDTO album = objectMapper.treeToValue(node, AlbumDTO.class);

                    List<SongDTO> songs = objectMapper.treeToValue(node.path("tracks").path("items"),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, SongDTO.class));
                    String imageUrl = objectMapper.treeToValue(node.path("images").get(0).path("url"),
                            objectMapper.getTypeFactory().constructType(String.class));
                    ArtistDTO artist = objectMapper.treeToValue(node.path("artists").get(0),
                            objectMapper.getTypeFactory().constructType(ArtistDTO.class));

                    album.setImageUrl(imageUrl);
                    album.setSongs(songs);
                    album.setArtist(artist);

                    albumList.add(album);
                }
            }

            albumList = addInfo(albumList);

            List<String> songIdList = new ArrayList<>();
            List<String> artistIdList = new ArrayList<>();
            // Print album details
            albumList.forEach(album -> {
                System.out.println("name: " + album.getName());
                System.out.println("spotify id: " + album.getSpotifyId());
                System.out.println("image url: " + album.getImageUrl());
                System.out.println("release date: " + album.getReleaseDate());
                System.out.println("type: " + album.getType());
                System.out.println("total songs: " + album.getTotalSongs());
                System.out.println("artist: " + album.getArtist());
                System.out.println("artist-album: " + album.getArtist().getAlbums().toString());
                System.out.println("artist-song: " + album.getArtist().getSongs().toString());
                album.getSongs().forEach(System.out::println);
                System.out.println("");
//                album.getTracks().getSongs().forEach(song -> songIdList.add(song.getSpotifyId()));
//                artistIdList.add(album.getArtists().get(0).getSpotifyId());
            });
//            System.out.println(artistIdList.size()+"/50 artist ids");
//            System.out.println(String.join("%2C", artistIdList));
//
//            List<String> tempList = new ArrayList<>();
//            for (String s : songIdList) {
//                tempList.add(s);
//                if (tempList.size() == 50) {
//                    System.out.println();
//                    System.out.println(tempList.size()+"/50 song ids");
//                    System.out.println(String.join("%2C", tempList));
//                    System.out.println();
//                    tempList.clear();
//                }
//            }
//            System.out.println();
//            System.out.println(tempList.size()+"/50 song ids");
//            System.out.println(String.join("%2C", tempList));
//            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return albumList;
    }

    private static List<AlbumDTO> addInfo(List<AlbumDTO> albums) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, SongDTO> songs = new HashMap<>();
        Map<String, ArtistDTO> artists = new HashMap<>();

        String filePathA = "src/artists.json";
        String filePathT1 = "src/tracks1.json";
        String filePathT2 = "src/tracks2.json";
        String filePathT3 = "src/tracks3.json";

        List<String> filePathsTracks = List.of(filePathT1, filePathT2, filePathT3);
        List<String> filePathsArtists = List.of(filePathA);

        for (String filePath : filePathsTracks) {
            try {
                JsonNode rootNode = objectMapper.readTree(new File(filePath));

                JsonNode tracksNode = rootNode.path("tracks");

                if (tracksNode.isArray()) {
                    for (JsonNode node : tracksNode) {
                        SongDTO song = objectMapper.treeToValue(node, SongDTO.class);
                        String imageUrl = objectMapper.treeToValue(node.path("album").path("images").get(0).path("url"),
                                objectMapper.getTypeFactory().constructType(String.class));
                        String releaseDate = objectMapper.treeToValue(node.path("album").path("release_date"),
                                objectMapper.getTypeFactory().constructType(String.class));
                        song.setImageUrl(imageUrl);
                        song.setReleaseDate(releaseDate);
//                        System.out.println();
//                        System.out.println(song);
                        songs.put(song.getSpotifyId(), song);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String filePath : filePathsArtists) {
            try {
                JsonNode rootNode = objectMapper.readTree(new File(filePath));

                JsonNode artistsNode = rootNode.path("artists");

                if (artistsNode.isArray()) {
                    for (JsonNode node : artistsNode) {
                        ArtistDTO artist = objectMapper.treeToValue(node, ArtistDTO.class);
                        String imageUrl = objectMapper.treeToValue(node.path("images").get(0).path("url"),
                                objectMapper.getTypeFactory().constructType(String.class));
                        artist.setImageUrl(imageUrl);
//                        System.out.println();
//                        System.out.println(artist);
                        artists.put(artist.getSpotifyId(), artist);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (AlbumDTO album : albums) {
            ArtistDTO artist = album.getArtist();
            artist.setImageUrl(artists.get(artist.getSpotifyId()).getImageUrl());
            artist.setGenres(artists.get(artist.getSpotifyId()).getGenres());
            artist.setPopularity(artists.get(artist.getSpotifyId()).getPopularity());
            artist.getAlbums().add(album);
            album.getSongs().forEach(song -> artist.getSongs().add(song));
            album.setArtist(artist);
            for (SongDTO song : album.getSongs()) {
                song.setImageUrl(songs.get(song.getSpotifyId()).getImageUrl());
                song.setReleaseDate(songs.get(song.getSpotifyId()).getReleaseDate());
                song.setAlbum(album);
            }
        }
            return albums;
    }

}
