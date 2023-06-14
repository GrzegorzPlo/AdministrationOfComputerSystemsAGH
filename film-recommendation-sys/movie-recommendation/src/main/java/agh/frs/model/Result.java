package agh.frs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class Result{
    private int page;
    private ArrayList<Movie> results;
    private int total_pages;
    private int total_results;
}
