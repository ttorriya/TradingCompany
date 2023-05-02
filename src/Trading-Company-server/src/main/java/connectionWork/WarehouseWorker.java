package connectionWork;

import databaseWork.AnswerHelper;
import databaseWork.DBConst;
import databaseWork.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;

public class WarehouseWorker {

    public static String addWarehouse(String str) throws SQLException {
        String array[] = str.trim().split("%");
        String sql = "SELECT MAX(" + DBConst.WAREHOUSE_ID + ") FROM " + DBConst.WAREHOUSE_TABLE;

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int id = 0;
        if (res.next()) id = res.getInt(1);

        sql = "SELECT COUNT(*) FROM " + DBConst.WAREHOUSE_BASKET_TABlE;
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int count = 0;
        if (res.next()) count = res.getInt(1);

        int idBasket = 0;
        if (count != 0) {
            sql = "SELECT MAX(" + DBConst.WAREHOUSE_BASKET_ID + ") FROM " + DBConst.WAREHOUSE_BASKET_TABlE;
            res = DatabaseHandler.getInstance().getResultSet(sql);
            if (res.next()) idBasket = res.getInt(1);
        }

        sql = "INSERT INTO " + DBConst.WAREHOUSE_BASKET_TABlE + " VALUES (" + (idBasket + 1) + ", " + (id + 1) + ")";
        DatabaseHandler.getInstance().execute(sql);

        sql = "INSERT INTO " + DBConst.WAREHOUSE_TABLE
                + " VALUES ('" + (id + 1) + "'" + ", '" + array[0] + "', '" + array[1] + "', '" + (idBasket + 1)
                + "', '" + 0 + "')";
        DatabaseHandler.getInstance().execute(sql);
        return "";
    }

    public static String getListWarehouse() throws SQLException {
        String sql = "SELECT * FROM " + DBConst.WAREHOUSE_TABLE;

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int rows = 0;
        while (res.next()) {
            rows++;
        }
        res = DatabaseHandler.getInstance().getResultSet(sql);
        String sentString = "";
        int columns = res.getMetaData().getColumnCount();
        for (int j = 0; j < rows; j++) {
            res.next();
            for (int i = 1; i <= columns; i++) {
                if (j == (rows - 1) && i == columns) {
                    sentString += res.getString(i);
                } else sentString += res.getString(i) + "%";
            }
        }
        return AnswerHelper.answerListWarehouse(sentString);
    }

    public static String deleteWarehouse(String id) throws SQLException {
        String sql = "SELECT " + DBConst.WAREHOUSE_BASKET_ID + " FROM " + DBConst.WAREHOUSE_TABLE
                + " WHERE " + DBConst.WAREHOUSE_ID + " = '" + id + "'";
        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int idBasket = 0;
        if (res.next()) idBasket = res.getInt(1);
        sql = "DELETE FROM " + DBConst.WAREHOUSE_TABLE + " WHERE " + DBConst.WAREHOUSE_ID + " = '" + id + "'";
        DatabaseHandler.getInstance().execute(sql);

        sql = "DELETE FROM " + DBConst.WAREHOUSE_BASKET_TABlE + " WHERE " + DBConst.WAREHOUSE_BASKET_ID
                + " = " + idBasket;
        DatabaseHandler.getInstance().execute(sql);
        return "";
    }

    public static String editWarehouse(String str) throws SQLException {
        String array[] = str.trim().split("%");

        String sql = "UPDATE " + DBConst.WAREHOUSE_TABLE + " SET "
                + DBConst.WAREHOUSE_NAME + " = '" + array[1] + "', "
                + DBConst.WAREHOUSE_VOLUME + " = '" + array[2] + "' "
                + "WHERE " + DBConst.WAREHOUSE_ID + " = '" + array[0] + "'";

        DatabaseHandler.getInstance().execute(sql);
        return "";
    }

    public static String addDelivery(String str) throws SQLException {
        String array[] = str.trim().split("%");

        String sql = "SELECT " + DBConst.WAREHOUSE_ID_BASKET + " FROM " + DBConst.WAREHOUSE_TABLE
                + " WHERE " + DBConst.WAREHOUSE_ID + " = " + array[1];
        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int idBasket = 0;
        if (res.next()) idBasket = res.getInt(1);

        sql = "SELECT COUNT(*) FROM " + DBConst.WAREHOUSE_GOODS_TABLE + " WHERE "
                + DBConst.WAREHOUSE_GOODS_ID_BASKET + " = " + idBasket + " AND "
                + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + array[0] + "";
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int count = 0;
        if (res.next()) count = res.getInt(1);
        if (count == 0) {
            sql = "INSERT INTO " + DBConst.WAREHOUSE_GOODS_TABLE + " VALUES ("
                    + idBasket + ", " + array[0] + ", " + array[2] + ", " + array[3] + ")";
            DatabaseHandler.getInstance().execute(sql);
        } else {
            sql = "SELECT " + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE
                    + " WHERE "
                    + DBConst.WAREHOUSE_GOODS_ID_BASKET + " = " + idBasket + " AND "
                    + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + array[0];
            res = DatabaseHandler.getInstance().getResultSet(sql);
            int num = 0;
            if (res.next()) num = res.getInt(1);

            sql = "UPDATE " + DBConst.WAREHOUSE_GOODS_TABLE + " SET " + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER + " = "
                    + (num + Integer.valueOf(array[2]))
                    + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_BASKET + " = " + array[1].split(" ")[0] + " AND "
                    + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + array[0];
            DatabaseHandler.getInstance().execute(sql);
        }

        sql = "SELECT " + DBConst.WAREHOUSE_FULLNESS + " FROM " + DBConst.WAREHOUSE_TABLE + " WHERE "
                + DBConst.WAREHOUSE_ID + " = " + array[1];
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int space = 0;
        if (res.next()) space = res.getInt(1);

        sql = "SELECT " + DBConst.GOOD_VOLUME + " FROM " + DBConst.GOODS_TABLE
                + " WHERE " + DBConst.GOOD_BARCODE + " = " + array[0];
        res = DatabaseHandler.getInstance().getResultSet(sql);
        float volume = 0;
        if (res.next()) volume = res.getFloat(1);

        float number = Float.valueOf(array[2]) * volume + space;
        sql = "UPDATE " + DBConst.WAREHOUSE_TABLE + " SET " + DBConst.WAREHOUSE_FULLNESS + " = "
                + number
                + " WHERE " + DBConst.WAREHOUSE_ID + " = " + array[1];
        DatabaseHandler.getInstance().execute(sql);

        sql = "SELECT COUNT(*) FROM " + DBConst.DELIVERY_TABLE;
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int idDelivery = 0;
        if (res.next()) idDelivery = res.getInt(1);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        sql = "INSERT INTO " + DBConst.DELIVERY_TABLE + " VALUES(" + (idDelivery + 1) + ", '"
                + dateFormat.format(cal.getTime()) + "', " + idBasket + ")";
        DatabaseHandler.getInstance().execute(sql);

        sql = "SELECT " + DBConst.USER_ID + " FROM " + DBConst.USER_TABLE
                + " WHERE " + DBConst.USER_ACCESS + " = '" + "Сотрудник'";
        res = DatabaseHandler.getInstance().getResultSet(sql);

        int rows = 0;
        while (res.next()) {
            rows++;
        }
        int[] idUsers = new int[rows];
        res = DatabaseHandler.getInstance().getResultSet(sql);
        for (int i = 0; i < rows; i++) {
            res.next();
            idUsers[i] = res.getInt(1);
        }

        //array[0]

        for (int i = 0; i < idUsers.length; i++) {
            sql = "SELECT COUNT(*) FROM " + DBConst.METOD_TABLE + " WHERE "
                    + DBConst.METOD_ID_USER + " = " + idUsers[i] + " AND " + DBConst.METOD_ID_GOOD + " = " + array[0];
            res = DatabaseHandler.getInstance().getResultSet(sql);
            int countMetod = 0;
            if (res.next()) countMetod = res.getInt(1);

            if (countMetod == 0) {
                sql = "INSERT INTO " + DBConst.METOD_TABLE + " VALUES(" + idUsers[i] + ", "
                        + array[0] + ", " + 5 + ", " + 5 + ")";
                DatabaseHandler.getInstance().execute(sql);
            }
        }
        updateFullness();
        return "";
    }

    public static String getListGoodsWarehouse(String idBasket) throws SQLException {
        String sql = "SELECT " + DBConst.WAREHOUSE_GOODS_ID_GOOD + ", "
                + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE + " WHERE "
                + DBConst.WAREHOUSE_GOODS_ID_BASKET + " = " + idBasket;
        ResultSet res1 = DatabaseHandler.getInstance().getResultSet(sql);
        int rows = 0;
        while (res1.next()) {
            rows++;
        }
        String sendString = "";
        res1 = DatabaseHandler.getInstance().getResultSet(sql);
        for (int i = 0; i < rows; i++) {
            res1.next();

            sendString += res1.getInt(1) + "%";
            sendString += res1.getInt(2) + "%";
        }
        sendString = sendString.substring(0, sendString.length() - 1);
        return AnswerHelper.answerGetListGoodsWarehouse(sendString);
    }

    public static String transferGoods(String str) throws SQLException {
        String array[] = str.trim().split("%");

        String sql = "SELECT " + DBConst.WAREHOUSE_ID_BASKET + " FROM " + DBConst.WAREHOUSE_TABLE + " WHERE "
                + DBConst.WAREHOUSE_ID + " = " + array[1];
        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int idToBasket = 0;
        if (res.next()) idToBasket = res.getInt(1);

        sql = "SELECT " + DBConst.GOOD_VOLUME + " FROM " + DBConst.GOODS_TABLE + " WHERE "
                + DBConst.GOOD_BARCODE + " = " + array[2];
        res = DatabaseHandler.getInstance().getResultSet(sql);
        float space = 0;
        if (res.next()) space = res.getFloat(1);
        addDelivery(array[2] + "%" + array[1] + "%" + array[3] + "%" + space);

        sql = "SELECT " + DBConst.WAREHOUSE_ID_BASKET + " FROM " + DBConst.WAREHOUSE_TABLE + " WHERE "
                + DBConst.WAREHOUSE_ID + " = " + array[0];
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int idFromBasket = 0;
        if (res.next()) idFromBasket = res.getInt(1);

        sql = "SELECT " + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE
                + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_BASKET + " = " + idFromBasket + " AND "
                + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + array[2];
        res = DatabaseHandler.getInstance().getResultSet(sql);
        int number = 0;
        if (res.next()) number = res.getInt(1);


        if (number == Integer.valueOf(array[3])) {
            sql = "DELETE FROM " + DBConst.WAREHOUSE_GOODS_TABLE + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_BASKET
                    + " = " + idFromBasket + " AND " + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + array[2];
            DatabaseHandler.getInstance().execute(sql);

            sql = "UPDATE " + DBConst.WAREHOUSE_TABLE + " SET " + DBConst.WAREHOUSE_FULLNESS
                    + " = " + 0 + " WHERE " + DBConst.WAREHOUSE_ID
                    + " = " + idFromBasket;
            DatabaseHandler.getInstance().execute(sql);
            return "";
        } else {
            sql = "SELECT " + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE
                    + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_BASKET
                    + " = " + idFromBasket + " AND " + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + array[2];
            res = DatabaseHandler.getInstance().getResultSet(sql);
            int prevNumber = 0;
            if (res.next()) prevNumber = res.getInt(1);

            sql = "UPDATE " + DBConst.WAREHOUSE_GOODS_TABLE + " SET " + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER
                    + " = " + (prevNumber - Integer.valueOf(array[3])) + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_BASKET
                    + " = " + idFromBasket + " AND " + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + array[2];
            DatabaseHandler.getInstance().execute(sql);

            sql = "SELECT " + DBConst.WAREHOUSE_FULLNESS + " FROM " + DBConst.WAREHOUSE_TABLE + " WHERE "
                    + DBConst.WAREHOUSE_ID + " = " + array[0];
            res = DatabaseHandler.getInstance().getResultSet(sql);
            float spaceFrom = 0;
            if (res.next()) spaceFrom = res.getFloat(1);

            float number1 = spaceFrom - Float.valueOf(array[3]) * space;
            sql = "UPDATE " + DBConst.WAREHOUSE_TABLE + " SET " + DBConst.WAREHOUSE_FULLNESS + " = "
                    + number1
                    + " WHERE " + DBConst.WAREHOUSE_ID + " = " + array[0];
            DatabaseHandler.getInstance().execute(sql);
            return "";
        }
    }

    public static String getReportGood() throws SQLException {
        String sendString = "";
        String sql = "SELECT " + DBConst.WAREHOUSE_NAME + ", " + DBConst.WAREHOUSE_BASKET_ID
                + " FROM " + DBConst.WAREHOUSE_TABLE;

        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int rows = 0;
        while (res.next()) {
            rows++;
        }
        res = DatabaseHandler.getInstance().getResultSet(sql);
        for (int i = 0; i < rows; i++) {
            res.next();
            int idBasket = res.getInt(2);
            sendString += res.getString(1) + "%";
            sendString += getListGoodWarehouse(idBasket);
            if (i != (rows - 1)) sendString += "#";

        }
        return AnswerHelper.answerGetReportGood(sendString);
    }

    private static String getListGoodWarehouse(int idBasket) throws SQLException {
        String sql = "SELECT " + DBConst.WAREHOUSE_GOODS_ID_GOOD + ", " + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER
                + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE + " WHERE "
                + DBConst.WAREHOUSE_GOODS_ID_BASKET + " = " + idBasket;

        ResultSet res1 = DatabaseHandler.getInstance().getResultSet(sql);
        int rows = 0;
        while (res1.next()) {
            rows++;
        }
        String sendString = "";
        int idGood = 0;
        res1 = DatabaseHandler.getInstance().getResultSet(sql);
        for (int i = 0; i < rows; i++) {
            res1.next();
            if (i == (rows - 1)) {
                idGood = res1.getInt(1);
                sendString += res1.getInt(1) + "%";
                sendString += res1.getString(2) + "%";
                sql = "SELECT " + DBConst.GOOD_PRODUCT_NAME + ", " + DBConst.GOOD_PRICE + ", " + DBConst.GOOD_VOLUME
                        + " FROM " + DBConst.GOODS_TABLE + " WHERE " + DBConst.GOOD_BARCODE + " = " + idGood;
                ResultSet res2 = DatabaseHandler.getInstance().getResultSet(sql);
                int rows1 = 0;
                while (res2.next()) {
                    rows1++;
                }
                //sendString = "";
                res2 = DatabaseHandler.getInstance().getResultSet(sql);
                for (int q = 0; q < rows1; q++) {
                    res2.next();
                    if (q == (rows1 - 1)) {
                        sendString += res2.getString(1) + "%";
                        sendString += res2.getString(2) + "%";
                        sendString += res2.getFloat(3) + "%";
                    } else {
                        sendString += res2.getString(1) + "%";
                        sendString += res2.getString(2) + "%";
                        sendString += res2.getFloat(3) + "%";
                    }
                }
            } else {
                idGood = res1.getInt(1);
                sendString += res1.getInt(1) + "%";
                sendString += res1.getString(2) + "%";
                sql = "SELECT " + DBConst.GOOD_PRODUCT_NAME + ", " + DBConst.GOOD_PRICE + ", " + DBConst.GOOD_VOLUME
                        + " FROM " + DBConst.GOODS_TABLE + " WHERE " + DBConst.GOOD_BARCODE + " = " + idGood;
                ResultSet res2 = DatabaseHandler.getInstance().getResultSet(sql);
                int rows1 = 0;
                while (res2.next()) {
                    rows1++;
                }
                //sendString = "";
                res2 = DatabaseHandler.getInstance().getResultSet(sql);
                for (int q = 0; q < rows1; q++) {
                    res2.next();
                    if (q == (rows1 - 1)) {
                        sendString += res2.getString(1) + "%";
                        sendString += res2.getString(2) + "%";
                        sendString += res2.getFloat(3) + "%";
                    } else {
                        sendString += res2.getString(1) + "%";
                        sendString += res2.getString(2) + "%";
                        sendString += res2.getFloat(3) + "%";
                    }
                }
            }
        }
        return sendString;
    }

    private static String getListGoodW(int idBasket) throws SQLException {
        String sql = "SELECT " + DBConst.WAREHOUSE_GOODS_ID_GOOD + ", "
                + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE + " WHERE "
                + DBConst.WAREHOUSE_GOODS_ID_BASKET + " = " + idBasket;

        ResultSet res1 = DatabaseHandler.getInstance().getResultSet(sql);
        int rows = 0;
        while (res1.next()) {
            rows++;
        }
        String sendString = "";
        res1 = DatabaseHandler.getInstance().getResultSet(sql);
        for (int i = 0; i < rows; i++) {
            res1.next();
            if (i == (rows - 1)) {
                sendString += res1.getInt(1) + "%";
                sendString += res1.getString(2);
            } else {
                sendString += res1.getInt(1) + "%";
                sendString += res1.getString(2) + "%";
            }
        }
        return sendString;
    }

    public static String getLineChartInfo() throws SQLException {
        String sendString = "";
        Calendar cal = Calendar.getInstance();


        for (int k = -1; k < 5; k++) {
            int day = 0;
            String mounth = String.valueOf(cal.get(Calendar.MONTH) - k);
            int year = cal.get(Calendar.YEAR);
            if (Integer.valueOf(mounth) == 0) {
                year = 2020;
                mounth = "12";
            }
            if (cal.get(Calendar.MONTH) - k == 4) {
                day = 30;
            } else if (cal.get(Calendar.MONTH) - k == 5) day = 31;
            else if (cal.get(Calendar.MONTH) - k == 3) day = 31;
            else if (cal.get(Calendar.MONTH) - k == 2) day = 28;
            else if (cal.get(Calendar.MONTH) - k == 1) day = 31;
            else if (cal.get(Calendar.MONTH) - k == 0) day = 31;
            else day = 30;
            if (Integer.valueOf(mounth) < 10) mounth = "0" + mounth;
            String sql = "SELECT COUNT(*) " + " FROM " + DBConst.DELIVERY_TABLE
                    + " WHERE " + DBConst.DELIVERY_DATE + " BETWEEN '"
                    + year + "-" + mounth + "-" + 1 + "' AND '"
                    + year + "-" + mounth + "-" + day + "'";
            ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
            int count = 0;
            if (res.next()) count = res.getInt(1);

            if (count != 0) {

                sql = "SELECT " + DBConst.DELIVERY_ID_BASKET + " FROM " + DBConst.DELIVERY_TABLE
                        + " WHERE " + DBConst.DELIVERY_DATE + " BETWEEN '"
                        + year + "-" + mounth + "-" + 1 + "' AND '"
                        + year + "-" + mounth + "-" + day + "'";
                res = DatabaseHandler.getInstance().getResultSet(sql);

                int r = 0;
                while (res.next()) {
                    r++;
                }
                res = DatabaseHandler.getInstance().getResultSet(sql);

                HashSet<Integer> idGoodsTemp = new HashSet<>();


                for (int i = 0; i < r; i++) {
                    res.next();
                    idGoodsTemp.add(res.getInt(1));
                }

                Integer[] idBasket = idGoodsTemp.toArray(new Integer[idGoodsTemp.size()]);

                int tempSum = 0;
                for (int i = 0; i < idBasket.length; i++) {
                    sql = "SELECT " + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE
                            + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_BASKET + " = " + idBasket[i];
                    ResultSet res1 = DatabaseHandler.getInstance().getResultSet(sql);

                    int q = 0;
                    while (res1.next()) {
                        q++;
                    }
                    res1 = DatabaseHandler.getInstance().getResultSet(sql);

                    for (int j = 0; j < q; j++) {
                        res1.next();
                        tempSum += res1.getInt(1);
                    }
                }
                sendString += tempSum + "%";
            } else sendString += 0 + "%";
        }
        return AnswerHelper.answerGetLineChartInfo(sendString);
    }

    public static String deleteWarehouseGood(String idGood) throws SQLException {
        String sql = "DELETE FROM " + DBConst.WAREHOUSE_GOODS_TABLE + " WHERE "
                + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + idGood;
        DatabaseHandler.getInstance().execute(sql);

        sql = "DELETE FROM " + DBConst.METOD_TABLE + " WHERE "
                + DBConst.METOD_ID_GOOD + " = " + idGood;
        DatabaseHandler.getInstance().execute(sql);
        updateFullness();
        return "";
    }

    private static void updateFullness() throws SQLException {
        String sql = "SELECT " + DBConst.WAREHOUSE_GOODS_ID_GOOD + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE;
        ResultSet res = DatabaseHandler.getInstance().getResultSet(sql);
        int r = 0;
        while (res.next()) {
            r++;
        }
        res = DatabaseHandler.getInstance().getResultSet(sql);

        HashSet<Integer> idGoodsTemp = new HashSet<>();


        for (int i = 0; i < r; i++) {
            res.next();
            idGoodsTemp.add(res.getInt(1));
        }

        Integer[] idGoods = idGoodsTemp.toArray(new Integer[idGoodsTemp.size()]);

        sql = "UPDATE " + DBConst.WAREHOUSE_TABLE + " SET " + DBConst.WAREHOUSE_FULLNESS + " = 0";
        DatabaseHandler.getInstance().execute(sql);

        for (int i = 0; i < idGoods.length; i++) {
            sql = "SELECT " + DBConst.GOOD_VOLUME + " FROM " + DBConst.GOODS_TABLE
                    + " WHERE " + DBConst.GOOD_BARCODE + " = " + idGoods[i];
            res = DatabaseHandler.getInstance().getResultSet(sql);
            float volume = 0;
            if (res.next()) volume = res.getFloat(1);

            sql = "SELECT " + DBConst.WAREHOUSE_GOODS_ID_BASKET + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE
                    + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + idGoods[i];
            res = DatabaseHandler.getInstance().getResultSet(sql);
            r = 0;
            while (res.next()) {
                r++;
            }
            int[] idBaskets = new int[r];
            res = DatabaseHandler.getInstance().getResultSet(sql);
            for (int j = 0; j < r; j++) {
                res.next();
                idBaskets[j] = res.getInt(1);
            }


            for (int j = 0; j < idBaskets.length; j++) {
                sql = "SELECT " + DBConst.WAREHOUSE_GOOD_TABLE_NUMBER + " FROM " + DBConst.WAREHOUSE_GOODS_TABLE
                        + " WHERE " + DBConst.WAREHOUSE_GOODS_ID_GOOD + " = " + idGoods[i] + " AND "
                        + DBConst.WAREHOUSE_GOODS_ID_BASKET + " = " + idBaskets[j];
                res = DatabaseHandler.getInstance().getResultSet(sql);
                int numberGoods = 0;
                int qwe = 0;
                while (res.next()) {
                    qwe++;
                }
                if (qwe != 0) {
                    res = DatabaseHandler.getInstance().getResultSet(sql);
                    for (int k = 0; k < qwe; k++) {
                        res.next();
                        numberGoods += res.getInt(1);
                    }

                    sql = "SELECT " + DBConst.WAREHOUSE_FULLNESS + " FROM " + DBConst.WAREHOUSE_TABLE
                            + " WHERE " + DBConst.WAREHOUSE_ID_BASKET + " = " + idBaskets[j];
                    res = DatabaseHandler.getInstance().getResultSet(sql);
                    float fullness = 0;
                    if (res.next()) fullness = res.getFloat(1);


                    sql = "UPDATE " + DBConst.WAREHOUSE_TABLE + " SET " + DBConst.WAREHOUSE_FULLNESS + " = "
                            + (numberGoods * volume + fullness) + " WHERE "
                            + DBConst.WAREHOUSE_ID_BASKET + " = " + idBaskets[j];
                    DatabaseHandler.getInstance().execute(sql);
                }
            }
        }
    }
}
