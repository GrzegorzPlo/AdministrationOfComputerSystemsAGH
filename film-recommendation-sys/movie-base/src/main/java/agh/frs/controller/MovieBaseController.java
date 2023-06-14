package agh.frs.controller;

import agh.frs.model.Result;

import agh.frs.service.MovieBaseService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieBaseController {
    @CrossOrigin
    @GetMapping("/api/movie/getMovies")
    public Result getRecommendation(){
            return MovieBaseService.getMovies();
    }
}