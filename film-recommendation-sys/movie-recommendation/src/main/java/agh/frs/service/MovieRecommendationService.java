package agh.frs.service;


import agh.frs.model.Result;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieRecommendationService {
    public static RestTemplate restTemplate = new RestTemplate();
    public static final String MOVIES_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "c7a878cc32dd231fed3c6987e722bcaf";


    public static Result getRecommendation(int movieId){
        return new Gson().fromJson(getRecommendationForMovie(movieId), Result.class);
    }

    private static String getRecommendationForMovie(int movieId){
        return restTemplate.getForObject(MOVIES_URL + movieId + "/recommendations?api_key=" + API_KEY + "&language=pl-PL&page=1",
                String.class);
    }
}
