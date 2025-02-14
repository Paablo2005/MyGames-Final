package utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import models.Game;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidades para interactuar con la API de RAWG.
 * <p>
 * Esta clase proporciona métodos para realizar solicitudes HTTP a la API RAWG,
 * parsear las respuestas JSON y convertirlas en objetos {@link models.Game}. Además,
 * incluye métodos para filtrar y buscar juegos según distintos parámetros.
 * </p>
 */
public class ApiUtils {
    /**
     * Clave de API utilizada para autenticar las solicitudes a la API RAWG.
     */
    private static final String API_KEY = "d1893f9cf1ed4022900123eaa2bc63cf";
    
    /**
     * Obtiene una lista de juegos a partir de la API RAWG.
     *
     * @param page     el número de página a solicitar.
     * @param pageSize el número de juegos por página.
     * @return una lista de objetos {@link models.Game} obtenidos de la API.
     */
    public static List<Game> fetchGames(int page, int pageSize) {
        String url = "https://api.rawg.io/api/games?key=" + API_KEY 
                   + "&page=" + page 
                   + "&page_size=" + pageSize;
        return getGamesFromUrl(url);
    }

    /**
     * Realiza una solicitud a la API y parsea la respuesta JSON para obtener una lista de juegos.
     *
     * @param url la URL completa de la solicitud a la API.
     * @return una lista de objetos {@link models.Game} extraídos de la respuesta JSON.
     */
    private static List<Game> getGamesFromUrl(String url) {
        String response = makeRequest(url);
        List<Game> games = new ArrayList<>();
        if (response != null) {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray results = jsonResponse.getJSONArray("results");
            
            for (int i = 0; i < results.length(); i++) {
                JSONObject gameObj = results.getJSONObject(i);
                String name = gameObj.getString("name");
                String imageUrl = gameObj.optString("background_image", "");
                
                System.out.println("Game " + (i + 1) + " - Name: " + name + ", Image URL: " + imageUrl);
                
                Game game = new Game();
                game.setName(name);
                game.setPrincipalImg(imageUrl);
                games.add(game);
            }
        }
        return games;
    }
    
    /**
     * Realiza una solicitud HTTP GET a la URL especificada.
     *
     * @param url la URL a la que se realizará la solicitud.
     * @return el cuerpo de la respuesta como cadena de texto, o {@code null} en caso de error.
     */
    private static String makeRequest(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            
            Response response = client.newCall(request).execute();
            return response.body().string();
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Convierte una cadena de calificación ESBR a su valor enumerado correspondiente.
     * <p>
     * Si la cadena no coincide exactamente con el nombre del valor del enumerado,
     * se realizan algunas conversiones específicas.
     * </p>
     *
     * @param rating la cadena de calificación obtenida de la API.
     * @return el valor {@link Rating} correspondiente a la cadena proporcionada.
     */
    public static Rating getEsbrRating(String rating) {
        if (rating.equals("Everyone 10+")) {
            return Enum.valueOf(Rating.class, "Everyone10Plus");
        } else if (rating.equals("Adults Only")) {
            return Enum.valueOf(Rating.class, "AdultsOnly");
        } else if (rating.equals("Rating Pending")) {
            return Enum.valueOf(Rating.class, "RatingPending");
        } else {
            return Enum.valueOf(Rating.class, rating);            
        }
    }
    
    /**
     * Enumeración de calificaciones ESBR.
     */
    public enum Rating {
        Everyone,
        Everyone10Plus,
        Teen,
        Mature,
        AdultsOnly,
        RatingPending
    }
    
    /**
     * Retorna la clave de API utilizada en las solicitudes.
     *
     * @return la cadena de la clave de API.
     */
    public static String getKey() {
        return API_KEY;
    }
    
    /**
     * Obtiene una lista de juegos filtrados por nombre.
     *
     * @param name     el nombre (o parte del nombre) del juego a buscar.
     * @param page     el número de página a solicitar.
     * @param pageSize el número de juegos por página.
     * @return una lista de objetos {@link models.Game} que coinciden con el nombre proporcionado.
     */
    public static List<Game> fetchGamesByName(String name, int page, int pageSize) {
        try {
            String encodedName = URLEncoder.encode(name, "UTF-8");
            String url = "https://api.rawg.io/api/games?key=" + API_KEY 
                       + "&search=" + encodedName 
                       + "&page=" + page 
                       + "&page_size=" + pageSize;
            return getGamesFromUrl(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una lista de juegos aplicando filtros de géneros y plataformas.
     *
     * @param genres   una cadena que contiene los géneros a filtrar, separados por comas.
     * @param platforms una cadena que contiene las plataformas a filtrar, separados por comas.
     * @param page     el número de página a solicitar.
     * @param pageSize el número de juegos por página.
     * @return una lista de objetos {@link models.Game} que cumplen con los filtros especificados.
     */
    public static List<Game> fetchGamesByFilters(String genres, String platforms, int page, int pageSize) {
        try {
            String url = "https://api.rawg.io/api/games?key=" + API_KEY;
            if (genres != null && !genres.isEmpty()) {
                url += "&genres=" + URLEncoder.encode(genres, "UTF-8");
            }
            if (platforms != null && !platforms.isEmpty()) {
                url += "&platforms=" + URLEncoder.encode(platforms, "UTF-8");
            }
            url += "&page=" + page + "&page_size=" + pageSize;
            System.out.println("URL de filtrado: " + url);
            return getGamesFromUrl(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene una lista de juegos filtrados por nombre, géneros y plataformas.
     *
     * @param name      el nombre (o parte del nombre) del juego a buscar.
     * @param genres    una cadena que contiene los géneros a filtrar, separados por comas.
     * @param platforms una cadena que contiene las plataformas a filtrar, separados por comas.
     * @param page      el número de página a solicitar.
     * @param pageSize  el número de juegos por página.
     * @return una lista de objetos {@link models.Game} que cumplen con la búsqueda y los filtros especificados.
     */
    public static List<Game> fetchGamesByNameAndFilters(String name, String genres, String platforms, int page, int pageSize) {
        try {
            String encodedName = URLEncoder.encode(name, "UTF-8");
            String url = "https://api.rawg.io/api/games?key=" + API_KEY + "&search=" + encodedName;
            
            if (genres != null && !genres.isEmpty()) {
                url += "&genres=" + URLEncoder.encode(genres, "UTF-8");
            }
            
            if (platforms != null && !platforms.isEmpty()) {
                url += "&platforms=" + URLEncoder.encode(platforms, "UTF-8");
            }
            
            url += "&page=" + page + "&page_size=" + pageSize;
            
            System.out.println("URL combinada: " + url);
            return getGamesFromUrl(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
