package classes;

public class ResponseHandler {
    public static String response;
    public static String request;
    public static String massage;
    public static String name;
    public static String surname;
    public static String login;
    public static void handler(String string){
        String[] requestArray = string.split(" ");
        response = string;

        switch(requestArray[0]){
            case "AddUser":
                request = requestArray[1];
                if(request.equals("Error")) massage = string.replaceAll("AddUser Error", "").trim();
                else massage = string.replaceAll("AddUser Succes", "").trim();
                break;
            case "SignInUser":
                request = requestArray[1];
                if(request.equals("Error")) massage = string.replaceAll("SignInUser Error", "").trim();
                else massage = string.replaceAll("SignInUser Succes", "").trim();
                break;
            case "GetListUsers":
                request = requestArray[1];
                massage = string.replaceAll("GetListUsers", "").trim();
                break;
            case "UpdateAccess":
                massage = requestArray[1];
                break;
            case "GetListGoods":
                massage = string.replaceAll("GetListGoods", "").trim();
                break;
            case "GetNameSurname":
                massage = string.replaceAll("GetNameSurname", "").trim();
                String[] array = massage.split("%");
                name = array[0];
                surname = array[1];
                break;
            case "GetListWarehouse":
                massage = string.replaceAll("GetListWarehouse", "").trim();
                break;
            case "GetListSuppliers":
                massage = string.replaceAll("GetListSuppliers", "").trim();
                break;
            case "GetListGoodsAll":
                massage = string.replaceAll("GetListGoodsAll", "").trim();
                break;
            case "GetListGoodsWarehouse":
                massage = string.replaceAll("GetListGoodsWarehouse", "").trim();
                break;
            case "GetRating":
                massage = string.replaceAll("GetRating", "").trim();
                break;
            case "GetReportGood":
                massage = string.replaceAll("GetReportGood", "").trim();
                break;
            case "GetListGoodsRating":
                massage = string.replaceAll("GetListGoodsRating", "").trim();
                break;
            case "GetRatingMetod":
                massage = string.replaceAll("GetRatingMetod", "").trim();
                break;
            case "GetLineChartInfo":
                massage = string.replaceAll("GetLineChartInfo", "").trim();
                break;
            default: break;
        }
    }
}
