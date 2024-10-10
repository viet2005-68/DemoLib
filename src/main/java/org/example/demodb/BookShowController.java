package org.example.demodb;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookShowController{
    private Connection connection;
    private Book selectedbook;
    DBConnection dbConnection = new DBConnection();
    @FXML
    private TableColumn<Book, String> author;

    @FXML
    private TableColumn<Book, String> image;

    @FXML
    private TableColumn<Book, String> isbn;

    @FXML
    private TableView<Book> tableBook;

    @FXML
    private TableColumn<Book, String> title;

    @FXML
    private TableColumn<Book, Integer> year;

    @FXML
    void EditOnAction(ActionEvent event) {

    }

    @FXML
    void backButtonOnAction(ActionEvent event) throws IOException {
        StageMachine.getInstance().changeScene("searchbook.fxml",event,600,275);
    }

    @FXML
    void deleteOnAction(ActionEvent event) {
        if(selectedbook == null) {
            Alert notion = new Alert(Alert.AlertType.WARNING);
            notion.setTitle("Warning");
            notion.setHeaderText(null);
            notion.setContentText("Please Select a Book which you wan to delete");
        } else {
            BookService.getInstance().deleteBook(selectedbook);
            showTable();
        }
    }


    public void showTable() {
        title.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        isbn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
        year.setCellValueFactory(new PropertyValueFactory<Book, Integer>("year"));
        connection = dbConnection.getConnection();
        String query = "select * from bookinfo";
        ObservableList<Book> booklist = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String author = resultSet.getString("Author");
                String isbn = resultSet.getString("ISBN");
                int year = resultSet.getInt("yearpublished");
                String imageUrl = resultSet.getString("ImageUrl");
                booklist.add(new Book(title, author, isbn, imageUrl, year));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        tableBook.setItems(booklist);
        image.setCellValueFactory(new PropertyValueFactory<>("imageLink"));
        image.setCellFactory(column -> new TableCell<Book, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);

                if(empty || imageUrl == null) {
                    setGraphic(null);
                } else {
                    Image image = new Image(imageUrl,50,50,true, true);
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        });
    }

    @FXML
    public void initialize() {
        showTable();
        tableBook.setOnMouseClicked(event -> {
            selectedbook = tableBook.getSelectionModel().getSelectedItem();
        });
    }

}
