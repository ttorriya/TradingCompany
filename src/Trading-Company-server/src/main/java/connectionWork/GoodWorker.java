package connectionWork;

import databaseWork.AnswerHelper;
import databaseWork.DBConst;
import databaseWork.DatabaseHandler;
import sample.ClientSocket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class GoodWorker {

    public static String getListGoods(String login) throws  SQLException{
        String sql = "SELECT " + DBConst.USER_ID + " FROM " + DBConst.USER_TABLE + " WHERE "
                + DBConst.USER_LOGIN + " = '" + login + "'";
        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int idUser = 0;
        if (res.next()) idUser = res.getInt(1);

        sql = "SELECT " + DBConst.SUPPLIER_ID_BASKET + " FROM " + DBConst.SUPPLIERS_TABLE
                + " WHERE " + DBConst.SUPPLIER_ID + " = '" + idUser + "'";
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int idBasket = 0;
        if (res.next()) idBasket = res.getInt(1);

        sql = "SELECT * FROM " + DBConst.BASKET_GOOD_TABLE +
                " WHERE " + DBConst.BASKET_GOOD_TABLE_ID_BASKET + " = '" + idBasket + "'";
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int columns = 0;
        while(res.next()) {
            columns++;
        }
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int[] idGoods = new int[columns];
        for(int i = 0; i < columns; i++){
            if (res.next()) {
                res.getInt(1);
                idGoods[i] = res.getInt(2);
            }
        }
        columns = res.getMetaData().getColumnCount();

        String sentString = "";

        for(int j = 0; j < idGoods.length; j++){
            sql = "SELECT * FROM " + DBConst.GOODS_TABLE +
                    " WHERE " + DBConst.GOOD_BARCODE + " = '" + idGoods[j] + "'";
            res = DatabaseHandler.getInstance().getResultSet(sql);

            columns = res.getMetaData().getColumnCount();
            while(res.next()){
                for(int i = 1; i <= columns; i++){
                    if(j == (idGoods.length - 1) && i == columns) sentString += res.getString(i);
                    else sentString += res.getString(i) + "%";
                }
            }
        }

        return AnswerHelper.answerListGoods(sentString);
    }

    public static String getListGoodsRating(String login) throws SQLException{
        String sentString = "";

        String sql = "SELECT " + DBConst.WAREHOUSE_GOODS_ID_GOOD +" FROM " + DBConst.WAREHOUSE_GOODS_TABLE;
        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int r = 0;
        while(res.next()) {
            r++;
        }
        res = DatabaseHandler.getInstance().getResultSet(sql);

        HashSet<Integer> idGoodsTemp = new HashSet<>();

        //int[] idGoods = new int[r];

        for(int i = 0; i < r; i++){
            res.next();
            idGoodsTemp.add(res.getInt(1));
        }

        Integer[] idGoods = idGoodsTemp.toArray(new Integer[idGoodsTemp.size()]);

        for(int l = 0; l < idGoods.length; l++) {
            sql = "SELECT * FROM " + DBConst.GOODS_TABLE + " WHERE " + DBConst.GOOD_BARCODE + " = " + idGoods[l];
            res = DatabaseHandler.getInstance().getResultSet(sql);

            int rows = 0;
            while (res.next()) {
                rows++;
            }
            res = DatabaseHandler.getInstance().getResultSet(sql);

            int columns = res.getMetaData().getColumnCount();
            for (int j = 0; j < rows; j++) {
                res.next();
                for (int i = 1; i <= columns; i++) {
                    if (j == (rows - 1) && i == columns) sentString += res.getString(i) + "%";
                    else sentString += res.getString(i) + "%";
                }
                sql = "SELECT " + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER
                        + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE
                        + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + idGoods[l];
                ResultSet res1 = DatabaseHandler.getInstance().getResultSet(sql);
                int temp = 0;
                while (res1.next()) {
                    temp += res1.getInt(1);
                }
                sentString += temp + "%";
                sql = "SELECT " + DBConst.WAREHOUSE_GOOD_TABLE_PRICE + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE
                        + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + idGoods[l];
                res1 = DatabaseHandler.getInstance().getResultSet(sql);
                res1.next();
                sentString += res1.getString(1) + "%";
            }


        }
        return AnswerHelper.answerGetListGoodsRating(sentString);
    }

    public static String deleteSupplierGood(String str) throws SQLException {
        String array[] = str.trim().split(" ");

        String sql = "SELECT " + DBConst.USER_ID + " FROM " + DBConst.USER_TABLE + " WHERE "
                + DBConst.USER_LOGIN + " = '" + array[1] + "'";

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int idUser = 0;
        if (res.next()) idUser = res.getInt(1);

        sql = "SELECT " + DBConst.SUPPLIER_ID_BASKET + " FROM " + DBConst.SUPPLIERS_TABLE
                + " WHERE " + DBConst.SUPPLIER_ID + " = '" + idUser + "'";
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int idBasket = 0;
        if (res.next()) idBasket = res.getInt(1);

        sql = "DELETE FROM " + DBConst.BASKET_GOOD_TABLE + " WHERE "
                + DBConst.BASKET_GOOD_TABLE_ID_BASKET + " = '" + idBasket + "' AND "
                + DBConst.BASKET_GOOD_TABLE_ID_GOOD + " = '" + array[0] + "'";

        DatabaseHandler.getInstance().execute(sql);

        sql = "DELETE FROM " + DBConst.GOODS_TABLE + " WHERE "
                + DBConst.GOOD_BARCODE + " = '" + array[0] + "'";

        DatabaseHandler.getInstance().execute(sql);
        return "";
    }

    public static String addSupplierGood(String str) throws SQLException{
        String array[] = str.trim().split("%");

        String sql = "SELECT MAX(" + DBConst.GOOD_BARCODE + ") FROM " + DBConst.GOODS_TABLE;

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int id = 0;
        if (res.next()) id = res.getInt(1);

        sql = "INSERT INTO " + DBConst.GOODS_TABLE + " (" + DBConst.GOOD_BARCODE + ", "
                + DBConst.GOOD_PRODUCT_NAME + ", " + DBConst.GOOD_UNIT + ", " + DBConst.GOOD_QUANTITY
                + ", " + DBConst.GOOD_PRICE + ", " + DBConst.GOOD_VOLUME + ") "
                + "VALUES ('" + (id + 1) + "'" + ", '" + array[0] + "', '" + array[1] + "', '" + array[2]
                + "', '" + array[3] + "', '" + array[4] + "')";

        DatabaseHandler.getInstance().execute(sql);

        sql = "SELECT " + DBConst.USER_ID + " FROM " + DBConst.USER_TABLE + " WHERE "
                + DBConst.USER_LOGIN + " = '" + array[5] + "'";
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int idSupplier = 0;
        if (res.next()) idSupplier = res.getInt(1);

        sql = "SELECT " + DBConst.SUPPLIER_ID_BASKET + " FROM " + DBConst.SUPPLIERS_TABLE
                + " WHERE " + DBConst.SUPPLIER_ID + " = '" + idSupplier + "'";
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int idBasket = 0;
        if (res.next()) idBasket = res.getInt(1);

        sql = "INSERT INTO " + DBConst.BASKET_GOOD_TABLE + " (" + DBConst.BASKET_GOOD_TABLE_ID_BASKET + ", "
                + DBConst.BASKET_GOOD_TABLE_ID_GOOD + ") "
                + "VALUES ('" + idBasket + "'" + ", '" + (id + 1) + "')";
        DatabaseHandler.getInstance().execute(sql);
        return "";
    }

    public static String editSupplierGood(String str) throws SQLException{
        String array[] = str.trim().split("%");

        String sql = "UPDATE " + DBConst.GOODS_TABLE + " SET " + DBConst.GOOD_PRODUCT_NAME + " = '" + array[0] + "', "
                + DBConst.GOOD_UNIT + " = '" + array[1] + "', " + DBConst.GOOD_QUANTITY + " = '" + array[2] + "', "
                + DBConst.GOOD_PRICE + " = '" + array[3] + "', " + DBConst.GOOD_VOLUME + " = '" + array[4] + "' "
                + "WHERE " + DBConst.GOOD_BARCODE + " = '" + array[5] + "'";

        DatabaseHandler.getInstance().execute(sql);
        DatabaseHandler.getInstance().closeConnection();
        return "";
    }

    public static String getListGoodsAll() throws SQLException {
        String sentString = "";

        String sql = "SELECT * FROM " + DBConst.GOODS_TABLE;

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int rows = 0;
        while(res.next()) {
            rows++;
        }
        res = DatabaseHandler.getInstance().getResultSet(sql);

        int columns = res.getMetaData().getColumnCount();
        for(int j = 0; j < rows; j++){
            res.next();
            for (int i = 1; i <= columns; i++) {
                if (j == (rows - 1) && i == columns) sentString += res.getString(i);
                else sentString += res.getString(i) + "%";
            }
        }
        return AnswerHelper.answerListGoodsAll(sentString);
    }

    public static String addRating(String str) throws SQLException{
        String array[] = str.trim().split("%");

        String sql = "SELECT " + DBConst.USER_ID + " FROM " + DBConst.USER_TABLE + " WHERE "
                + DBConst.USER_LOGIN + " = '" + array[0] + "'";

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int idUser = 0;
        if (res.next()) idUser = res.getInt(1);

        sql = "SELECT COUNT(*) FROM " + DBConst.METOD_TABLE + " WHERE " + DBConst.METOD_ID_USER
                + " = " + idUser + " AND " + DBConst.METOD_ID_GOOD + " = " + array[1];
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int count = 0;
        if (res.next()) count = res.getInt(1);

        if(count == 0){
            sql = "INSERT INTO " + DBConst.METOD_TABLE + " VALUES (" + idUser + ", " + array[1] + ", " + array[2] + ")";
            DatabaseHandler.getInstance().execute(sql);
        }
        else {
            sql = "UPDATE " + DBConst.METOD_TABLE + " SET " + DBConst.METOD_RATING_NUM + " = " + array[2]
                    + ", " + DBConst.METOD_RATING_PRICE + " = " + array[3]
                    + " WHERE " + DBConst.METOD_ID_USER + " = " + idUser
                    + " AND " + DBConst.METOD_ID_GOOD + " = " + array[1];
            DatabaseHandler.getInstance().execute(sql);
        }
        return "";
    }

    public static String getRating(String login) throws SQLException {
        String sql = "SELECT " + DBConst.USER_ID + " FROM " + DBConst.USER_TABLE
                + " WHERE " + DBConst.USER_LOGIN + " = '" + login + "'";

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int idUser = 0;
        if (res.next()) idUser = res.getInt(1);

        sql = "SELECT " + DBConst.METOD_ID_GOOD + ", " + DBConst.METOD_RATING_NUM + ", " + DBConst.METOD_RATING_PRICE
                + " FROM " + DBConst.METOD_TABLE + " WHERE " + DBConst.METOD_ID_USER + " = " + idUser;
                res = DatabaseHandler.getInstance().getResultSet(sql);
        int rows = 0;
        while(res.next()) {
            rows++;
        }
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int columns = res.getMetaData().getColumnCount();
        String sendString ="";

        for(int j = 0; j < rows; j++){
            res.next();
            for (int i = 1; i <= columns; i++) {
                if (j == (rows - 1) && i == columns) sendString += res.getInt(i);
                else {
                    sendString += res.getInt(i) + "%";
                }
            }
        }
        return AnswerHelper.answerGetRating(sendString);
    }

    public static String getRatingMetod(String barcode) throws SQLException {
        String sql = "SELECT " + DBConst.METOD_ID_USER + " , " + DBConst.METOD_RATING_NUM + ", "
                + DBConst.METOD_RATING_PRICE
                + " FROM " + DBConst.METOD_TABLE + " WHERE " + DBConst.METOD_ID_GOOD + " = " + barcode;

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        String sendString = "";
        while(res.next()){
            sendString += res.getInt(1) + "%";
            sendString += res.getFloat(2) + "%";
            sendString += res.getFloat(3) + "%";
        }
        return AnswerHelper.answerGetRatingMetod(sendString);
    }

    public static String updatePriceRealise(String str) throws SQLException{
        String[] array = str.split("%");

        String sql = "UPDATE " + DBConst.WAREHOUSE_GOODS_TABLE + " SET "
                + DBConst.WAREHOUSE_GOOD_TABLE_PRICE + " = " + array[1]
                + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + array[0];
        DatabaseHandler.getInstance().execute(sql);
        return "";
    }

}
