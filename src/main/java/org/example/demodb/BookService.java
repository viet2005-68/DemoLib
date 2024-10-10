package org.example.demodb;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookService {

    private static BookService instance;

    private final OkHttpClient client = new OkHttpClient();

    private BookService() {}

    public static BookService getInstance() {
        if (instance == null) {
            instance = new BookService();
        }
        return instance;
    }

    public JSONArray searchBooks(String query) {
        String keyAPI = "AIzaSyC8Wq4sinCA-uJQp-QP4hrLB06K--OwYP0";
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=" + keyAPI;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try(Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);
            System.out.println("Success");
            return jsonObject.getJSONArray("items");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addBook(Book book) {
        DBConnection connection = new DBConnection();
        Connection conDB = connection.getConnection();

        String queryInsert = "INSERT INTO bookInfo(ISBN, Title, Author, yearpublished, imageUrl) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = conDB.prepareStatement(queryInsert);
            preparedStatement.setString(1,book.getIsbn());
            preparedStatement.setString(2,book.getTitle());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.setInt(4, book.getYear());
            preparedStatement.setString(5, book.getImageLink());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(Book book) {
        DBConnection connection = new DBConnection();
        Connection conDB = connection.getConnection();

        String queryDelete = "DELETE FROM bookInfo WHERE ISBN= ?";

        try {
            PreparedStatement preparedStatement = conDB.prepareStatement(queryDelete);
            preparedStatement.setString(1,book.getIsbn());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
