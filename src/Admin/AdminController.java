package Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import dbUtil.dbConnection;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private TextField id;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private DatePicker dob;
    @FXML
    private TextField deleteId; //for deletion
    @FXML
    private TextField updateId; // for updation
    @FXML
    private TextField colNo;  // for updation
    @FXML
    private TextField newValue;  // for updation
    @FXML
    private TableView<StudentData> studenttable;
    @FXML
    private TableColumn<StudentData,String> idcolumn;
    @FXML
    private TableColumn<StudentData,String> firstnamecolumn;
    @FXML
    private TableColumn<StudentData,String> lastnamecolumn;
    @FXML
    private TableColumn<StudentData,String> emailcolumn;
    @FXML
    private TableColumn<StudentData,String> dobcolumn;

    private dbConnection dc;
    private ObservableList<StudentData> data;

    private String sql = "SELECT * FROM Students";

    public void initialize(URL url, ResourceBundle rb){
        this.dc= new dbConnection();
    }

    @FXML
    public void loadStudentData(javafx.event.ActionEvent event) throws SQLException{
        try{
            Connection conn = dbConnection.getConnection();
            this.data= FXCollections.observableArrayList();

            ResultSet rs= conn.createStatement().executeQuery(sql);
            while(rs.next()){
                this.data.add(new StudentData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));
              }
        }catch(SQLException e){
           System.err.println("Error"+e);
        }
        this.idcolumn.setCellValueFactory(new PropertyValueFactory<StudentData,String>("ID"));
        this.firstnamecolumn.setCellValueFactory(new PropertyValueFactory<StudentData,String>("firstName"));
        this.lastnamecolumn.setCellValueFactory(new PropertyValueFactory<StudentData,String>("lastName"));
        this.emailcolumn.setCellValueFactory(new PropertyValueFactory<StudentData,String>("email"));
        this.dobcolumn.setCellValueFactory(new PropertyValueFactory<StudentData,String>("DOB"));

        this.studenttable.setItems(null);
        this.studenttable.setItems(this.data);
    }
    @FXML
    public void addStudent(javafx.event.ActionEvent event){
        String sqlInsert="INSERT INTO Students(ID,Fname,Lname,Email,DOB) VALUES(?,?,?,?,?)";

        try{
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt= conn.prepareStatement(sqlInsert);

            stmt.setString(1,this.id.getText());
            stmt.setString(2,this.firstname.getText());
            stmt.setString(3,this.lastname.getText());
            stmt.setString(4,this.email.getText());
            stmt.setString(5,this.dob.getEditor().getText());

            stmt.execute();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void clearFields(javafx.event.ActionEvent event){
        this.id.setText("");
        this.firstname.setText("");
        this.lastname.setText("");
        this.email.setText("");
        this.dob.setValue(null);
    }

    @FXML
    public void deleteRecord(){
        try{
            Connection conn = dbConnection.getConnection();

            Integer a = Integer.parseInt(this.deleteId.getText());

            String sql = "delete from Students where ID = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1,a);

            Integer k = stmt.executeUpdate();
            if(k>0){

                System.out.println("Delete successful");
            }else{
                System.out.println("Delete failed");
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateRecord(){

        try{
            Connection conn = dbConnection.getConnection();


            Integer i = Integer.parseInt(this.updateId.getText());
            Integer j = Integer.parseInt(this.colNo.getText());

            if(j==2){

             String sql = "update Students set Fname= ? where ID= ?";
             PreparedStatement stmt = conn.prepareStatement(sql);
             stmt.setString(1,this.newValue.getText());
             stmt.setString(2,String.valueOf(i));
             stmt.executeUpdate();

            }else if(j==3){

                String sql = "update Students set Lname= ? where ID= ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1,this.newValue.getText());
                stmt.setString(2,String.valueOf(i));
                stmt.executeUpdate();

            }else if(j==4){

                String sql = "update Students set Email= ? where ID= ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1,this.newValue.getText());
                stmt.setString(2,String.valueOf(i));
                stmt.executeUpdate();

            }else{

                String sql = "update Students set DOB= ? where ID= ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1,this.newValue.getText());
                stmt.setString(2,String.valueOf(i));
                stmt.executeUpdate();

            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
