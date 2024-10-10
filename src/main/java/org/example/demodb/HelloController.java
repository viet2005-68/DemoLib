package org.example.demodb;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressIndicator;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class HelloController {
    @FXML
    private ProgressIndicator progress;

    @FXML
    private TableColumn<Book, String> authorBook;

    @FXML
    private TableView<Book> bookView;

    @FXML
    private TableColumn<Book, String> imageBook;

    @FXML
    private TableColumn<Book, String> isbnBook;

    @FXML
    private TextField querySeach;

    @FXML
    private TableColumn<Book, String> tittleBook;

    @FXML
    private TableColumn<Book, Integer> yearBook;

    @FXML
    void ShowOnAction(ActionEvent event) throws IOException {
        StageMachine.getInstance().changeScene("bookShow.fxml",event,700,600);
    }

    @FXML
    void addOnAction(ActionEvent event) {
        BookService bookService = BookService.getInstance();
        if(selectedBook != null ){
            bookService.addBook(selectedBook);
            selectedBook = null;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No book selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a book");

            alert.showAndWait();
        }
    }

    private Book selectedBook;
    public HelloController() {
        progress = new ProgressIndicator();
        progress.setVisible(false);
    }
    @FXML
    void searchBookOnAction(ActionEvent event) {
        String query = querySeach.getText();
        if (query!=null && !query.isEmpty()) {
           search(query);
        }
    }

    private void search(String query) {
        BookService bookService = BookService.getInstance();
        JSONArray bookArrays = bookService.searchBooks(query);

        ObservableList<Book> bookList = FXCollections.observableArrayList();

        for (int i = 0; i < bookArrays.length(); i++) {
            JSONObject bookJson = bookArrays.getJSONObject(i).getJSONObject("volumeInfo");
            if(bookJson != null) {
                String title = bookJson.optString("title", "No title");
                //String author = bookJson.optString("author", "No author");
                JSONArray authorsArray = bookJson.optJSONArray("authors");
                String author = (authorsArray != null && authorsArray.length() > 0) ? authorsArray.getString(0) : "No author";
                String isbn = null;
                if(bookJson.optJSONArray("industryIdentifiers")!=null) {
                    isbn = bookJson.optJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier");
                }
                JSONObject imageLinks = bookJson.optJSONObject("imageLinks");
                String imageUrl = (imageLinks != null) ? imageLinks.optString("thumbnail","") : "";

                int year = 0;
                if (bookJson.has("publishedDate")) {
                    String pushlishedDate = bookJson.optString("publishedDate");
                    if (pushlishedDate.length() >= 4) {
                        year = Integer.parseInt(pushlishedDate.substring(0, 4));
                    }
                }

                bookList.add(new Book(title, author, isbn, imageUrl,year));
            }
        }
        bookView.setItems(bookList);

    }

    @FXML
    public void initialize() {
        tittleBook.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorBook.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnBook.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        yearBook.setCellValueFactory(new PropertyValueFactory<>("year"));

        imageBook.setCellValueFactory(new PropertyValueFactory<>("imageLink"));
        imageBook.setCellFactory(column -> new TableCell<Book, String>(){
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String imageLink, boolean empty) {
                super.updateItem(imageLink, empty);
                if (empty || imageLink == null || imageLink.isEmpty())
                {
                    setGraphic(null);
                } else {
                    Image image = new Image(imageLink,50,50,true,true);
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        });

        bookView.setOnMouseClicked(event -> {
            selectedBook = bookView.getSelectionModel().getSelectedItem();
            //System.out.println(selectedBook.toString());
        });
    }

}
