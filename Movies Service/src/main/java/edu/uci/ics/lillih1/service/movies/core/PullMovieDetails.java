package edu.uci.ics.lillih1.service.movies.core;


public class PullMovieDetails extends Thread {
    private int start;
    private int limit;
    private int index;

    public PullMovieDetails() {
    }

    public PullMovieDetails(int start, int limit, int index) {
        this.start = start;
        this.limit = limit;
        this.index = index;
    }


    @Override
    public void run() {
        try {
            TmdbService.pullMovieDetails(start, limit, index);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
