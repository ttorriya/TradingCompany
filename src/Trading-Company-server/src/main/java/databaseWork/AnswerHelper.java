package databaseWork;

public class AnswerHelper {

    public static String answerErrorAddUser(String error){
        return "AddUser Error " + error;
    }

    public static String answerSuccessfulAddUser(String str){
        return "AddUser Succes " + str;
    }

    public static String answerErrorSignInUser(String str) {return "SignInUser Error " + str;}
    public static String answerSuccessfulSignInUser(String str) {return "SignInUser Succes " + str;}

    public static String answerListUsers(String str) {return "GetListUsers " + str;}

    public static String answerListGoods(String str) {return "GetListGoods " + str;}

    public static String answerNameSurname(String str) {return "GetNameSurname " + str;}

    public static String answerListWarehouse(String str) {return "GetListWarehouse " + str;}

    public static String answerListSupplier(String str) {return "GetListSuppliers " + str;}

    public static String answerListGoodsAll(String str){return "GetListGoodsAll " + str;}

    public static String answerGetListGoodsRating(String str){return "GetListGoodsRating " + str;}

    public static String answerGetListGoodsWarehouse(String str){return "GetListGoodsWarehouse " + str;}

    public static String answerGetRating(String str){return "GetRating " + str;}

    public static String answerGetReportGood(String str){ return "GetReportGood " + str;}

    public static String answerGetRatingMetod(String str){return "GetRatingMetod " + str;}

    public static String answerGetLineChartInfo(String str){return "GetLineChartInfo " + str;}
}
