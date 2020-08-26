package edu.uci.ics.lillih1.service.movies.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.lillih1.service.movies.MovieService;
import edu.uci.ics.lillih1.service.movies.logger.ServiceLogger;
import edu.uci.ics.lillih1.service.movies.models.*;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TmdbService
{
    private final static String MOVIE_ID_FILE = "movie_id_file.txt";
    private final static String MOVIE_DETAIL_FILE = "movie_detail_file";
    private final static String MOVIE_RESULT_FILE = "movie_result_file.txt";
    private final static String MOVIE_COUNT_FILE = "movie_count_file";

    private final static int MAX_COUNT = 35;

    private final static String endpoint = "https://api.themoviedb.org/3/movie/";
    private final static String KEY = "fec8c7e4e42682945da3e3d5ba20b518";

    public static TmdbReadIdListResponseModel readIdLidt(String text) throws Exception
    {
        String MOVIE_LIST_JSON_FILE = "tmdb_movie_id_list.json";


        FileWriter writer = new FileWriter(MOVIE_LIST_JSON_FILE);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        bufferedWriter.write(text);
        bufferedWriter.close();


        File in = new File(MOVIE_LIST_JSON_FILE);
        BufferedReader input = new BufferedReader(new FileReader(in));

        File outfile = new File(MOVIE_ID_FILE);

        BufferedWriter output = new BufferedWriter(new FileWriter(outfile, true));

        ObjectMapper mapper = new ObjectMapper();
        String line = null;

        int numMovies = 0;
        while ((line = input.readLine()) != null && line.length() != 0) {
            TmdbIdModel tmidmodel = mapper.readValue(line, TmdbIdModel.class);
            output.write(tmidmodel.getId() + "");
            output.newLine();
            numMovies++;
        }

        output.close();
        input.close();

        TmdbReadIdListResponseModel response = new TmdbReadIdListResponseModel();
        response.setNummovies(numMovies);

        return response;
    }


    public static TmdbGetAllResponseModel getMovies(TmdbGetAllRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Get Movie Service...");

        Map<Integer, Integer> map = new HashMap<>();

        int start = requestModel.getStart();
        int limit = requestModel.getLimit();

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        File outfile = new File(MOVIE_DETAIL_FILE);
        BufferedWriter output = new BufferedWriter(new FileWriter(outfile, true));

        File outfile1 = new File(MOVIE_RESULT_FILE);
        BufferedWriter output1 = new BufferedWriter(new FileWriter(outfile1, true));

        File in = new File(MOVIE_ID_FILE);
        BufferedReader input = new BufferedReader(new FileReader(in));
        int i=0;
        while (i<start)
        {
            input.readLine();
            i++;
        }

        int end = start+limit;

        int downloadedCount = 0;
        int skippedCount = 0;

        int count = 0;
        long t1 = (new Date()).getTime();

        boolean readNext = true;
        String line = null;
        while (i< end)
        {
            if (readNext) {
                line = input.readLine();
                if (line == null || line.trim().length() == 0) {
                    break;
                }
                line = line.trim();
            }

            String url = endpoint + line;

            WebTarget webTarget = client.target(url);
            Map<String, String> params = new HashMap<String, String>();
            params.put("api_key", KEY);
            params.put("append_to_response", "credits");

            for (String key : params.keySet()) {
                webTarget = webTarget.queryParam(key, params.get(key));
            }

            ServiceLogger.LOGGER.info("URI: " + webTarget.getUri());

            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

            long before = (new Date()).getTime();
            Response response = invocationBuilder.get();
            long after = (new Date()).getTime();

            ServiceLogger.LOGGER.info("status: " + response.getStatus() + ", time: " + (after-before));

            if (response.getStatus() == 200) {
                count++;

                String jsonText = response.readEntity(String.class);
                ServiceLogger.LOGGER.info("i: " + i + ", jsonText: " + jsonText);

                output.write(jsonText);
                output.newLine();

                output1.write("i: " + i + ", id: " + line + ", INSERTED");
                output1.newLine();

                int curr = map.getOrDefault(response.getStatus(), 0);
                map.put(response.getStatus(), curr+1);

                readNext = true;
                i++;
            }
            else if (response.getStatus() == 429) {
                ServiceLogger.LOGGER.info("i: " + i);
                readNext = false;
            }
            else
            {
                ServiceLogger.LOGGER.info("http status: " + response.getStatus() + "i: " + i + ", id: " + line);
                output1.write("i: " + i + ", id: " + line + ", http status: " +  response.getStatus());
                output1.newLine();

                int curr = map.getOrDefault(response.getStatus(), 0);
                map.put(response.getStatus(), curr+1);

                readNext = true;
                i++;
            }

            Thread.sleep(100);

            if (count >= MAX_COUNT)
            {
                long t2 = (new Date()).getTime();
                long elapse = t2 - t1;
                ServiceLogger.LOGGER.info("Last " + MAX_COUNT + " pulls in " + elapse + " ms.");

                if (elapse <= 10000)
                {
                    long sleep = 10000 - elapse;
                    ServiceLogger.LOGGER.info("Sleep for " + sleep + " ms.");

                    Thread.sleep(sleep);
                }
                count = 0;
                t1 = (new Date()).getTime();
            }
        }

        output.close();
        input.close();

        return new TmdbGetAllResponseModel(limit, map);
    }

    public static void pullMovieDetails(int start, int limit, int index) throws Exception
    {
        ServiceLogger.LOGGER.info("Get Movie Details Async...");

        Map<Integer, Integer> map = new HashMap<>();

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        File outfile = new File(MOVIE_DETAIL_FILE + index + ".json");
        BufferedWriter output = new BufferedWriter(new FileWriter(outfile, true));

        File outfile1 = new File(MOVIE_RESULT_FILE);
        BufferedWriter output1 = new BufferedWriter(new FileWriter(outfile1, true));

        File outfile2 = new File(MOVIE_COUNT_FILE);
        BufferedWriter output2 = new BufferedWriter(new FileWriter(outfile2, true));


        File in = new File(MOVIE_ID_FILE);
        BufferedReader input = new BufferedReader(new FileReader(in));
        int i=0;
        while (i<start)
        {
            input.readLine();
            i++;
        }

        int end = start+limit;

        int downloadedCount = 0;
        int skippedCount = 0;

        int count = 0;
        long t1 = (new Date()).getTime();

        boolean readNext = true;
        String line = null;
        while (i< end)
        {
            if (readNext) {
                line = input.readLine();
                if (line == null || line.trim().length() == 0) {
                    break;
                }
                line = line.trim();
            }

            String url = endpoint + line;

            WebTarget webTarget = client.target(url);
            Map<String, String> params = new HashMap<String, String>();
            params.put("api_key", KEY);
            params.put("append_to_response", "credits");

            for (String key : params.keySet()) {
                webTarget = webTarget.queryParam(key, params.get(key));
            }

            ServiceLogger.LOGGER.info("URI: " + webTarget.getUri());

            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

            long before = (new Date()).getTime();
            Response response = invocationBuilder.get();
            long after = (new Date()).getTime();

            ServiceLogger.LOGGER.info("status: " + response.getStatus() + ", time: " + (after-before));

            if (response.getStatus() == 200) {
                count++;

                String jsonText = response.readEntity(String.class);
                ServiceLogger.LOGGER.info("i: " + i + ", jsonText: " + jsonText);

                output.write(jsonText);
                output.newLine();

                output1.write("i: " + i + ", id: " + line + ", INSERTED");
                output1.newLine();

                int curr = map.getOrDefault(response.getStatus(), 0);
                map.put(response.getStatus(), curr+1);

                readNext = true;
                i++;
            }
            else if (response.getStatus() == 429) {
                ServiceLogger.LOGGER.info("i: " + i);
                readNext = false;
            }
            else
            {
                ServiceLogger.LOGGER.info("http status: " + response.getStatus() + "i: " + i + ", id: " + line);
                output1.write("i: " + i + ", id: " + line + ", http status: " +  response.getStatus());
                output1.newLine();

                int curr = map.getOrDefault(response.getStatus(), 0);
                map.put(response.getStatus(), curr+1);

                readNext = true;
                i++;
            }

            Thread.sleep(100);

            if (count >= MAX_COUNT)
            {
                long t2 = (new Date()).getTime();
                long elapse = t2 - t1;
                ServiceLogger.LOGGER.info("Last " + MAX_COUNT + " pulls in " + elapse + " ms.");

                if (elapse <= 10000)
                {
                    long sleep = 10000 - elapse;
                    ServiceLogger.LOGGER.info("Sleep for " + sleep + " ms.");

                    Thread.sleep(sleep);
                }
                count = 0;
                t1 = (new Date()).getTime();
            }
        }


        for (Integer key : map.keySet())
        {
            line = key + " " + map.get(key);
            output2.write(line);
            output2.newLine();
        }

        output.close();
        output1.close();
        output2.close();
        input.close();
    }


    public static TmdbGetAllResponseModel pullResult() throws Exception {
        ServiceLogger.LOGGER.info("Pull result...");

        Map<Integer, Integer> map = new HashMap<>();

        File in = new File(MOVIE_COUNT_FILE + ".txt");


        BufferedReader input = new BufferedReader(new FileReader(in));

        String line = null;
        while ((line = input.readLine()) != null) {
            String[] strs = line.split(" ");

            int n1 = Integer.parseInt(strs[0]);
            int n2 = Integer.parseInt(strs[1]);

            int curr = map.getOrDefault(n1, 0);
            map.put(n1, curr + n2);
        }

        input.close();
        return new TmdbGetAllResponseModel(map.get(200), map);
    }


    public static TmdbInsertResponseModel insert(TmdbInsertRequestModel requestModel) throws Exception
    {
        ServiceLogger.LOGGER.info("Insert Movie Service...");

        int start = requestModel.getStart();
        int limit = requestModel.getLimit();
        int index = requestModel.getIndex();

        File in = new File(MOVIE_DETAIL_FILE + index + ".json");

        BufferedReader input = new BufferedReader(new FileReader(in));
        int i=0;
        while (i<start)
        {
            input.readLine();
            i++;
        }

        int end = start+limit;

        ObjectMapper mapper = new ObjectMapper();

        int updateCount = 0;
        int insertCount = 0;
        int skippedCount = 0;

        String line = null;
        while (i< end && (line = input.readLine()) != null)
        {
            line = line.trim();

            TMMovieModel tmMoviemodel=null;
            try {
                tmMoviemodel = mapper.readValue(line, TMMovieModel.class);
//                String text = mapper.writeValueAsString(tmMoviemodel);
//                ServiceLogger.LOGGER.info("Converted: " + text);


            } catch (Exception e)
            {
                e.printStackTrace();
                ServiceLogger.LOGGER.info("Line: " + line);

                i++;
                skippedCount++;
                continue;
            }

            if (tmMoviemodel.getImdb_id() == null || tmMoviemodel.getImdb_id().trim().length() == 0)
            {
                i++;
                skippedCount++;
                continue;
            }

            MovieModel movie = null;
            try {
                movie = mapToMovieModel(tmMoviemodel);
            } catch (Exception e)
            {
                e.printStackTrace();
                String text = mapper.writeValueAsString(tmMoviemodel);
                ServiceLogger.LOGGER.info("Converted: " + text);
                i++;
                skippedCount++;
                continue;
            }


//            String text = mapper.writeValueAsString(movie);
//            ServiceLogger.LOGGER.info("Converted: " + text);


            Boolean existed = loadMovieFromDB(movie);

            if (existed)
            {
                try {
                    boolean updated = updateMovie(movie);

                    List<TmdbCastModel> casts = tmMoviemodel.getCredits().getCast();

                    updateStars(casts, movie.getMovieId());

                    if (updated) updateCount++;

                    ServiceLogger.LOGGER.info("Updated: " + movie.getMovieId());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    insertMovie(movie);
                    for (TmdbCastModel cast : tmMoviemodel.getCredits().getCast()) {
                        addStar(cast.getName(), movie.getMovieId());
                    }
                    insertCount++;

                    ServiceLogger.LOGGER.info("Inserted: " + movie.getMovieId());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

//            String text = mapper.writeValueAsString(movie);
//            ServiceLogger.LOGGER.info("Converted: " + text);
            i++;
        }

        input.close();

        ServiceLogger.LOGGER.info("Update count: " + updateCount);
        ServiceLogger.LOGGER.info("Insert count: " + insertCount);
        ServiceLogger.LOGGER.info("Skipped count: " + skippedCount);

        ServiceLogger.LOGGER.info("No change count: " + (i - start - updateCount - insertCount));

        return new TmdbInsertResponseModel(requestModel.getLimit(), insertCount, updateCount);
    }


    private static MovieModel mapToMovieModel(TMMovieModel tmMoviemodel)
    {
        MovieModel result = new MovieModel();

        result.setMovieId(tmMoviemodel.getImdb_id());
        result.setTitle(tmMoviemodel.getTitle());

        boolean flag = false;
        for (TmdbCrewModel crew : tmMoviemodel.getCredits().getCrew())
        {
            if ("Director".equals(crew.getJob())) {
                result.setDirector(crew.getName());
                flag = true;
                break;
            }
        }

        if (!flag) result.setDirector("");

        if (tmMoviemodel.getRelease_date() != null && tmMoviemodel.getRelease_date().length() > 4) {
            result.setYear(Integer.parseInt(tmMoviemodel.getRelease_date().substring(0, 4)));
        }
        else
        {
            result.setYear(0);
        }
        result.setBackdrop_path(tmMoviemodel.getBackdrop_path());
        result.setBudget(tmMoviemodel.getBudget());
        result.setOverview(tmMoviemodel.getOverview());
        result.setPoster_path(tmMoviemodel.getPoster_path());
        result.setRevenue(tmMoviemodel.getRevenue());
        result.setRating(tmMoviemodel.getVote_average());
        result.setNumVotes(tmMoviemodel.getVote_count());
        result.setGenres(tmMoviemodel.getGenres());

        return result;
    }


    private static boolean loadMovieFromDB(MovieModel model) throws Exception
    {
        String query = "SELECT id FROM movies where id=?";

        PreparedStatement ps = MovieService.getCon().prepareStatement(query);

        ps.setString(1, model.getMovieId());

//        ServiceLogger.LOGGER.info("Query: " + ps.toString());

        ResultSet rs = ps.executeQuery();
        return rs.next();
    }


    private static boolean updateMovie(MovieModel model) throws Exception
    {
        boolean movieUpdated = false;

        String query = "UPDATE movies ";

        boolean flag = false;

        if (model.getBackdrop_path() != null) {
            query += flag ? "," : "set ";
            flag = true;
            query += "backdrop_path=?";
            movieUpdated = true;
        }

        if (model.getBudget() != null)
        {
            query += flag ? "," : "set ";
            flag = true;
            query += "budget=?";
            movieUpdated = true;
        }

        if (model.getYear() != null)
        {
            query += flag ? "," : "set ";
            flag = true;
            query += "year=?";
            movieUpdated = true;
        }

        if (model.getOverview() != null)
        {
            query += flag ? "," : "set ";
            flag = true;
            query += "overview=?";
            movieUpdated = true;
        }
        if (model.getPoster_path() != null)
        {
            query += flag ? "," : "set ";
            flag = true;
            query += "poster_path=?";
            movieUpdated = true;
        }
        if (model.getRevenue() != null)
        {
            query += flag ? "," : "set ";
            flag = true;
            query += "revenue=?";
            movieUpdated = true;
        }

        if (movieUpdated) {
            query += " where id=? and title=? and year=?";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);

            int col = 1;

            if (model.getBackdrop_path() != null) {
                ps.setString(col++, model.getBackdrop_path());
            }
            if (model.getBudget() != null)
            {
                ps.setInt(col++, model.getBudget());
            }
            if (model.getYear() != null)
            {
                ps.setInt(col++, model.getYear());
            }
            if (model.getOverview() != null)
            {
                ps.setString(col++, model.getOverview());
            }
            if (model.getPoster_path() != null)
            {
                ps.setString(col++, model.getPoster_path());
            }
            if (model.getRevenue() != null)
            {
                ps.setInt(col++, model.getRevenue());
            }

            ps.setString(col++, model.getMovieId());
            ps.setString(col++, model.getTitle());
            ps.setInt(col++, model.getYear());

//            ServiceLogger.LOGGER.info("Query: " + ps.toString());

            ps.execute();
        }

        boolean ratingUpdated = false;
        if (model.getRating() != null || model.getNumVotes() != null)
        {
            String query1 = "select * from ratings where movieId = ?";
            PreparedStatement ps = MovieService.getCon().prepareStatement(query1);
            ps.setString(1, model.getMovieId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String query2 = "UPDATE ratings set ";
                if (model.getRating() != null) {
                    query2 += "rating=" + model.getRating();
                }
                if (model.getNumVotes() != null) {
                    query2 += ",numVotes=" + model.getNumVotes();
                }

                query2 += " where movieId=?";

                ps = MovieService.getCon().prepareStatement(query2);

                ps.setString(1, model.getMovieId());

//                ServiceLogger.LOGGER.info("Query: " + ps.toString());
                ps.execute();

            } else {
                insertRating(model);
            }

            ratingUpdated = true;
        }

        boolean genreUpdated = false;
        List<GenreModel> genres = GenreService.fetchGenreIdRecords(model.getMovieId());
        Set<String> set = new HashSet<String>();

        for (GenreModel g : genres)
        {
            set.add(g.getName());
        }

        List<GenreModel> list = new ArrayList<GenreModel>();
        for (GenreModel g : model.getGenres())
        {
            if (!set.contains(g.getName()))
            {
                list.add(g);
            }
        }

        if (list.size() > 0)
        {
//            ServiceLogger.LOGGER.info("Inserting genres: ");
            List<Integer> genresIds = MovieAddService.insertGenres(list);

//            ServiceLogger.LOGGER.info("Inserting genres_in_movies: ");
            MovieAddService.insertGenreInMovie(model.getMovieId(), genresIds);

            genreUpdated = true;
        }

        return (movieUpdated || ratingUpdated || genreUpdated);
    }



    private static void updateStars(List<TmdbCastModel> casts, String movieId) throws Exception
    {
        for (TmdbCastModel cast : casts)
        {
            String query = "select id from stars where name = ?;";

            PreparedStatement ps = MovieService.getCon().prepareStatement(query);
            ps.setString(1, cast.getName());
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                String id = rs.getString("id");

                String query1 = "select * from stars_in_movies where starId = ? and movieId = ?;";
                ps = MovieService.getCon().prepareStatement(query1);
                ps.setString(1,id);
                ps.setString(2,movieId);
                rs = ps.executeQuery();

                if (!rs.next()) {
                    String query2 = "INSERT INTO stars_in_movies (starId, movieId) values(?,?)";
                    ps = MovieService.getCon().prepareStatement(query2);
                    ps.setString(1, id);
                    ps.setString(2, movieId);
                    ps.execute();
                }
            }
            else
            {
                addStar(cast.getName(), movieId);
            }
        }
    }

    private static void insertMovie(MovieModel movie) throws Exception
    {
        List<Integer> genres = MovieAddService.insertGenres(movie.getGenres());

        insertMovieRecord(movie);

//        ServiceLogger.LOGGER.info("Inserting genres_in_movies: ");
        MovieAddService.insertGenreInMovie(movie.getMovieId(), genres);

//        ServiceLogger.LOGGER.info("Inserting rating: ");

        insertRating(movie);
    }

    private static void insertMovieRecord(MovieModel movie) throws Exception
    {
//        ServiceLogger.LOGGER.info("Insert a movie: " + movie.getMovieId());

        String nameStr = "INSERT INTO movies (id";
        String valStr = ") VALUES ('" + movie.getMovieId() + "'";

        if (movie.getTitle() != null) {
            nameStr += ",title";
            valStr += ",?";
        }

        if (movie.getYear() != null)
        {
            nameStr += ",year";
            valStr += ",?";
        }

        if (movie.getDirector() != null)
        {
            nameStr += ",director";
            valStr += ",?";
        }

        if (movie.getBackdrop_path() != null )
        {
            nameStr += ",backdrop_path";
            valStr += ",?";
        }

        if (movie.getBudget() != null)
        {
            nameStr += ",budget";
            valStr += ",?";
        }

        if (movie.getOverview() != null)
        {
            nameStr += ",overview";
            valStr += ",?";
        }

        if (movie.getPoster_path() != null)
        {
            nameStr += ",poster_path";
            valStr += ",?";
        }

        if (movie.getRevenue() != null)
        {
            nameStr += ",revenue";
            valStr += ",?";
        }

        valStr += ");";

        String query = nameStr + valStr;

//        ServiceLogger.LOGGER.info("Query: " + query);

        PreparedStatement ps = MovieService.getCon().prepareStatement(query);

        int col = 1;

        if (movie.getTitle() != null) {
            ps.setString(col++, movie.getTitle());
        }

        if (movie.getYear() != null)
        {
            ps.setInt(col++, movie.getYear());
        }

        if (movie.getDirector() != null)
        {
            ps.setString(col++, movie.getDirector());
        }

        if (movie.getBackdrop_path() != null )
        {
            ps.setString(col++, movie.getBackdrop_path());
        }

        if (movie.getBudget() != null)
        {
            ps.setInt(col++, movie.getBudget());
        }

        if (movie.getOverview() != null)
        {
            ps.setString(col++, movie.getOverview());
        }

        if (movie.getPoster_path() != null)
        {
            ps.setString(col++, movie.getPoster_path());
        }

        if (movie.getRevenue() != null)
        {
            ps.setInt(col++, movie.getRevenue());
        }

//        ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
        ps.execute();

//        ServiceLogger.LOGGER.info("Insert executed: ");
    }

    private static void insertRating(MovieModel movie) throws Exception
    {
        String query = "INSERT INTO ratings(movieId,rating,numVotes) VALUES (?,?,?); ";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, movie.getMovieId());
        ps.setFloat(2, movie.getRating());
        ps.setInt(3,movie.getNumVotes());
        ps.execute();
    }

    public static void addStar(String name, String movieId) throws Exception
    {
        String query = "select name from stars where name = ?;";
        PreparedStatement ps = MovieService.getCon().prepareStatement(query);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return;
        }

        String starId = StarService.generateNewStarID();
        String query1 = "INSERT INTO stars (id, name) VALUES (?, ?); ";

        ps = MovieService.getCon().prepareStatement(query1);
        ps.setString(1, starId);
        ps.setString(2, name);
        ps.execute();

        String query2 = "INSERT INTO stars_in_movies (starId, movieId) values(?,?)";
        ps = MovieService.getCon().prepareStatement(query2);
        ps.setString(1, starId);
        ps.setString(2, movieId);

//        ServiceLogger.LOGGER.info("Query: " + ps.toString());

        ps.execute();
    }

}
