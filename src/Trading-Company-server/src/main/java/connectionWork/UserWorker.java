package connectionWork;

import databaseWork.AnswerHelper;
import databaseWork.DBConst;
import databaseWork.DatabaseHandler;
import sample.ClientSocket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class UserWorker {

    public static String addUser(String str) throws SQLException {
        String array[] = str.trim().split(" ");

        if(checkAvailableUser(array[0]) == true) {

            String sqlSelect = "SELECT MAX(" + DBConst.USER_ID + ") FROM " + DBConst.USER_TABLE;

            ResultSet res = DatabaseHandler.getInstance().getResultSet(sqlSelect);
            int id = 0;
            if (res.next()) id = res.getInt(1);
            boolean access = false;
            if (array[7].equals("Пользователь") && array[7].equals("Поставщик")) access = true;

            String sqlInsert = "INSERT INTO " + DBConst.USER_TABLE + " (" + DBConst.USER_ID + ", " + DBConst.USER_LOGIN + ", "
                    + DBConst.USER_PASSWORD + ", " + DBConst.USER_EMAIL + ", " + DBConst.USER_PHONE + ", "
                    + DBConst.USER_FIRSTNAME + ", " + DBConst.USER_LASTNAME + ", " + DBConst.USER_PATRONYMIC + ", "
                    + DBConst.USER_ACCESS + ", " + DBConst.USER_CONFIRM_ACCESS + ") "
                    + "VALUES ('" + (id + 1) + "'" + ", '" + array[0] + "', '" + array[1] + "', '" + array[2]
                    + "', '" + array[3] + "', '" + array[4] + "', '" + array[5] + "', '" + array[6]
                    + "', '" + array[7] + "', '" + access + "')";

            DatabaseHandler.getInstance().execute(sqlInsert);

            String sql;

            if(array[7].equals("Поставщик")) {
                sql = "SELECT MAX(" + DBConst.SUPPLIER_ID_BASKET + ") FROM " + DBConst.SUPPLIERS_TABLE;
                res = DatabaseHandler.getInstance().getResultSet(sql);
                int idBasket = 0;
                if (res.next()) idBasket = res.getInt(1);
                sql = "INSERT INTO " + DBConst.BASKET_TABLE + " (" + DBConst.BASKET_ID + ", " + DBConst.BASKET_ID_USER
                        + ") VALUES ('" + (idBasket + 1) + "'" + ", '" + (id + 1) + "')";
                DatabaseHandler.getInstance().execute(sql);

                sql = "INSERT INTO " + DBConst.SUPPLIERS_TABLE + " (" + DBConst.SUPPLIER_ID + ", " + DBConst.SUPPLIER_ORG_NAME + ", "
                        + DBConst.SUPPLIER_CITY + ", " + DBConst.SUPPLIER_COUNTRY + ", " + DBConst.SUPPLIER_ADDRESS + ", "
                        + DBConst.SUPPLIER_ID_BASKET + ") "
                        + "VALUES ('" + (id + 1) + "'" + ", '" + array[8] + "', '" + array[9] + "', '" + array[10]
                        + "', '" + array[11] + "', '" + (idBasket+1)  + "')";
                DatabaseHandler.getInstance().execute(sql);
            }
            if(array[7].equals("Сотрудник")){
                sql = "SELECT " + DBConst.WAREHOUSE_GOODS_ID_GOOD +" FROM " + DBConst.WAREHOUSE_GOODS_TABLE;
                res = DatabaseHandler.getInstance().getResultSet(sql);
                int r = 0;
                while(res.next()) {
                    r++;
                }
                res = DatabaseHandler.getInstance().getResultSet(sql);

                HashSet<Integer> idGoodsTemp = new HashSet<>();

                for(int i = 0; i < r; i++){
                    res.next();
                    idGoodsTemp.add(res.getInt(1));
                }

                Integer[] idGoods = idGoodsTemp.toArray(new Integer[idGoodsTemp.size()]);

                for(int l = 0; l < idGoods.length; l++) {
                    sql = "INSERT INTO " + DBConst.METOD_TABLE
                            + "VALUES ('" + (id + 1) + "'" + ", '" +idGoods[l] + "', '" + 5 + "', '" + 5 + "')";
                    DatabaseHandler.getInstance().execute(sql);

                }


            }
            return AnswerHelper.answerSuccessfulAddUser("Пользователь успешно добавлен");
        }
        else{
            return AnswerHelper.answerErrorAddUser("Данный пользователь уже существует!");
        }

    }

    public static boolean checkAvailableUser(String login) throws SQLException {

        String sql = "SELECT COUNT(*) FROM " + DBConst.USER_TABLE + " WHERE " + DBConst.USER_LOGIN + " = '" + login + "'";

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int id = 0;
        if (res.next()) id = res.getInt(1);
        if(id == 0) return true;
        else return false;
    }

    public static String checkSignInUser(String str) throws SQLException{
        String array[] = str.trim().split(" ");
        String login = array[0], password = array[1], access = array[2];

        String sql = "SELECT COUNT(*) FROM " + DBConst.USER_TABLE + " WHERE " + DBConst.USER_LOGIN + " = '" + login
                + "' AND " + DBConst.USER_PASSWORD + " = '" + password + "' AND " + DBConst.USER_ACCESS + " = '" + access
                + "' AND " + DBConst.USER_CONFIRM_ACCESS + " = 'true'";
        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int number = 0;
        if (res.next()) number = res.getInt(1);
        if(number == 1) return AnswerHelper.answerSuccessfulSignInUser(login);
        else return AnswerHelper.answerErrorSignInUser("Ваши данные не довлены в систему!");
    }

    public static String getListUsers() throws SQLException{
        String sql = "SELECT * FROM " + DBConst.USER_TABLE;

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);

        String sentString = "";
        int columns = res.getMetaData().getColumnCount();
        while(res.next()){
            for(int i = 1; i <= columns; i++){
                if(i == columns){
                    sentString += res.getString(i);
                }
                sentString += res.getString(i) + " ";
            }
        }
        return AnswerHelper.answerListUsers(sentString);
    }

    public static String updateAccess(String login) throws SQLException {
        String array[] = login.trim().split(" ");
        String sql = "UPDATE " + DBConst.USER_TABLE + " SET " + DBConst.USER_CONFIRM_ACCESS + " = 'true' " +
                "WHERE " + DBConst.USER_LOGIN + " = '" + array[0] + "'";

        DatabaseHandler.getInstance().execute(sql);
        return "";
    }

    public static String deleteUser(String login) throws SQLException {
        String array[] = login.trim().split(" ");
        String sql = "DELETE FROM " + DBConst.USER_TABLE + " WHERE " + DBConst.USER_LOGIN + " = '" + array[0] + "'";

        DatabaseHandler.getInstance().execute(sql);
        return "";
    }

    public static String getNameSurname(String login) throws SQLException {
        String sql = "SELECT " + DBConst.USER_FIRSTNAME + ", " + DBConst.USER_LASTNAME
                + " FROM " + DBConst.USER_TABLE + " WHERE " + DBConst.USER_LOGIN + " = '" + login + "'";

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);

        String sentString = "";
        res.next();
        sentString = res.getString(1) + "%";
        sentString += res.getString(2);
        return AnswerHelper.answerNameSurname(sentString);
    }

    public static String getListSuppliers(String barcode) throws SQLException {
        String sql = "SELECT " + DBConst.BASKET_GOOD_TABLE_ID_BASKET + " FROM "
                + DBConst.BASKET_GOOD_TABLE + " WHERE " + DBConst.BASKET_GOOD_TABLE_ID_GOOD + " = " + barcode;

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int idBasket = 0;
        if (res.next()) idBasket = res.getInt(1);

        sql = "SELECT * FROM " + DBConst.SUPPLIERS_TABLE
                + " WHERE " + DBConst.SUPPLIER_ID_BASKET + " = " + idBasket;
        res = DatabaseHandler.getInstance().getResultSet(sql);
        String sentString = "";
        int columns = res.getMetaData().getColumnCount();
        while(res.next()){
            for(int i = 1; i <= columns; i++){
                if(i == columns){
                    sentString += res.getString(i);
                }
                else sentString += res.getString(i) + "%";
            }
        }
        return AnswerHelper.answerListSupplier(sentString);
    }

}
