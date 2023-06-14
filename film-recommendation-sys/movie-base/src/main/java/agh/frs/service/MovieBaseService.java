package agh.frs.service;

import agh.frs.model.Result;
import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;

public class MovieBaseService {
    public static RestTemplate restTemplate = new RestTemplate();
    public static final String MOVIES_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "c7a878cc32dd231fed3c6987e722bcaf";


    public static Result getMovies(){
        return new Gson().fromJson(getAllMovies(), Result.class);
    }

    private static String getAllMovies(){
        return restTemplate.getForObject(MOVIES_URL + "popular?api_key=" + API_KEY + "&language=pl-PL&page=1",
                String.class);
    }
}
