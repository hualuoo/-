import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Controller_RepairMain {
    public Label LoginUser_Label;

    /*维修信息    RNo编号    RSubDate提交日期    RTitle标题    RText正文    RState状态    RReply回复    RSolveDate解决日期    ONo业主编号*/
    public TableView<Data_RepairTable> Repair_TableView;
    public TableColumn<Data_RepairTable ,String> RNo_TableColumn;
    public TableColumn<Data_RepairTable ,String> RSubDate_TableColumn;
    public TableColumn<Data_RepairTable ,String> RTitle_TableColumn;
    public TableColumn<Data_RepairTable ,String> RState_TableColumn;
    public TableColumn<Data_RepairTable ,String> ONo_TableColumn;
    public TableColumn<Data_RepairTable ,String> OName_TableColumn;
    ObservableList<Data_RepairTable> RepairTableView_List = FXCollections.observableArrayList();

    public TextField Search_RNo_TextField,Search_OName_TextField;
    public DatePicker Search_RSubDate_DatePicker;
    public CheckBox Search_NoRepair_CheckBox,Search_YesRepair_CheckBox;
    public Button Search_Clean_Button;

    String query;
    ResultSet result;

    public void initialize() {
        //初始化
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);
        //自定义日期选择器DatePicker
        setDataStyle(Search_RSubDate_DatePicker);

        Repair_TableView.setItems(RepairTableView_List);
        RNo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRNo());
        RSubDate_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRSubDate());
        RTitle_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRTitle());
        RState_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRState());
        ONo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getONo());
        OName_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getOName());

        showRepairTableView();

        //搜索文本栏变动监听
        Search_RNo_TextField.textProperty()
                .addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_Repair();
            }
        });
        //时间选择器变动监听
        Search_RSubDate_DatePicker.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_Repair();
            }
        });
        //搜索文本栏变动监听
        Search_OName_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_Repair();
            }
        });
        //搜索复选框变动监听
        Search_NoRepair_CheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                search_Repair();
            }
        });
        Search_YesRepair_CheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                search_Repair();
            }
        });
    }
    public void setDataStyle(DatePicker datepicker){
        //自定义日期选择器
        String pattern = "yyyy-MM-dd";
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        datepicker.setConverter(converter);
    }
    public void showRepairTableView(){
        query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo";
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            while (result.next()){
                RepairTableView_List.add(new Data_RepairTable(result.getString("RNo"),
                        result.getString("RSubDate"),
                        result.getString("RTitle"),
                        result.getString("RText"),
                        result.getString("RState"),
                        result.getString("RReply"),
                        result.getString("RSolveDate"),
                        result.getString("ONo"),
                        result.getString("OName"),
                        result.getString("OSex"),
                        result.getString("OTel"),
                        result.getString("OID"),
                        result.getString("ONote")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void search_Repair(){
        RepairTableView_List.clear();
        if(Search_RSubDate_DatePicker.getValue()==null && Search_NoRepair_CheckBox.isSelected() && Search_YesRepair_CheckBox.isSelected()){
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_RSubDate_DatePicker.getValue()!=null && Search_NoRepair_CheckBox.isSelected() && Search_YesRepair_CheckBox.isSelected()){
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "RSubDate=\'" + Search_RSubDate_DatePicker.getValue() + "\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_RSubDate_DatePicker.getValue()==null && Search_NoRepair_CheckBox.isSelected()==false && Search_YesRepair_CheckBox.isSelected()){
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "RState =\'已维修\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_RSubDate_DatePicker.getValue()!=null && Search_NoRepair_CheckBox.isSelected()==false && Search_YesRepair_CheckBox.isSelected()){
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "RSubDate=\'" + Search_RSubDate_DatePicker.getValue() + "\' AND " +
                    "RState =\'已维修\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_RSubDate_DatePicker.getValue()==null && Search_NoRepair_CheckBox.isSelected() && Search_YesRepair_CheckBox.isSelected()==false){
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "RState =\'未维修\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_RSubDate_DatePicker.getValue()!=null && Search_NoRepair_CheckBox.isSelected() && Search_YesRepair_CheckBox.isSelected()==false){
            query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo WHERE " +
                    "RNo LIKE \'%" + Search_RNo_TextField.getText() + "%\' AND " +
                    "RSubDate=\'" + Search_RSubDate_DatePicker.getValue() + "\' AND " +
                    "RState =\'未维修\' AND " +
                    "OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        }
        if(Search_NoRepair_CheckBox.isSelected()==false && Search_YesRepair_CheckBox.isSelected()==false){
            return;
        }
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            while (result.next()){
                RepairTableView_List.add(new Data_RepairTable(result.getString("RNo"),
                        result.getString("RSubDate"),
                        result.getString("RTitle"),
                        result.getString("RText"),
                        result.getString("RState"),
                        result.getString("RReply"),
                        result.getString("RSolveDate"),
                        result.getString("ONo"),
                        result.getString("OName"),
                        result.getString("OSex"),
                        result.getString("OTel"),
                        result.getString("OID"),
                        result.getString("ONote")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_SearchCleanButton(){
        Search_RNo_TextField.setText("");
        Search_RSubDate_DatePicker.setValue(null);
        Search_NoRepair_CheckBox.setSelected(true);
        Search_YesRepair_CheckBox.setSelected(true);
        Search_OName_TextField.setText("");
    }
    public void click_IndexToggleButton(){
        //主界面-房屋管理界面切换
        try {
            Parent Index_Root = FXMLLoader.load(getClass().getResource("GUI_IndexMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-主界面");
            Main.Login_Stage.setScene(new Scene(Index_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_CarToggleButton(){
        //车辆管理界面切换
        try {
            Parent Car_Root = FXMLLoader.load(getClass().getResource("GUI_CarMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-车辆管理界面");
            Main.Login_Stage.setScene(new Scene(Car_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}