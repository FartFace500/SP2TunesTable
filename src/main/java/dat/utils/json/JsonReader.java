package dat.utils.json;

import dat.dtos.AlbumDTO;
import dat.dtos.SongDTO;
import dat.dtos.TracksDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {

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
            album.setTracks(new TracksDTO()); // Initialize TracksDTO
            album.getTracks().setSongs(songs); // Set the items in TracksDTO

            albumResult = album;

            // Print the item details
            System.out.println("name: " + album.getName());
            System.out.println("type: " + album.getType());
            System.out.println("total songs: " + album.getTotalSongs());
            System.out.println("total songs: " + album.getTotalSongs());
            System.out.println("release date: " + album.getReleaseDate());
            System.out.println("artists: " + album.getArtists().toString());
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
                for (JsonNode albumNode : albumsNode) {
                    AlbumDTO album = objectMapper.treeToValue(albumNode, AlbumDTO.class);

                    List<SongDTO> songs = objectMapper.treeToValue(albumNode.path("tracks").path("items"),
                            objectMapper.getTypeFactory().constructCollectionType(List.class, SongDTO.class));

                    album.setTracks(new TracksDTO());
                    album.getTracks().setSongs(songs);

                    albumList.add(album);
                }
            }

            // Print album details
            albumList.forEach(album -> {
                System.out.println("name: " + album.getName());
                System.out.println("type: " + album.getType());
                System.out.println("total songs: " + album.getTotalSongs());
                System.out.println("release date: " + album.getReleaseDate());
                System.out.println("artists: " + album.getArtists().toString());
                album.getTracks().getSongs().forEach(System.out::println);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return albumList;
    }

}
