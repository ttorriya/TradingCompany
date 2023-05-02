package classes;

public class RequestHandler {

    public static String requestAddUser(String login, String password, String email,
                                        String phoneNumber, String firstname, String lastname,
                                        String patronymic, String accessRights){
        return "AddUser" + " " + login + " " + password + " " + email + " " + phoneNumber
                + " " + firstname + " " + lastname + " " + patronymic + " " + accessRights;
    }

    public static String requestAddUser(String login, String password, String email,
                                        String phoneNumber, String firstname, String lastname,
                                        String patronymic, String accessRights, String org_name,
                                        String city, String country, String address){
        return "AddUser" + " " + login + " " + password + " " + email + " " + phoneNumber
                + " " + firstname + " " + lastname + " " + patronymic + " " + accessRights
                + " " + org_name + " " + city + " " + country + " " + address;
    }

    public static String requestCheckSignInUser(String login, String password, String access){
        return "SignInUser" + " " + login + " " + password + " " + access;
    }

    public static String requestGetListUsers(){
        return "GetListUsers";
    }

    public static String requestModifyAccess(String login){
        return "UpdateAccess " + login;
    }

    public static String requestDeleteUser(String login){return "DeleteUser " + login;}

    public static String requestGetListGoods(String login){return "GetListGoods " + login;}

    public static String requestDeleteSupplierGood(int idGood, String loginSupplier){return "DeleteSupplierGood "
            + idGood + " " + loginSupplier;}

    public static String requestAddSupplierGood(String name, String unit, String number, String price, String space, String loginUser){
        return "AddSupplierGood " + name + "%" + unit + "%" + number + "%" + price + "%" + space + "%" + loginUser;
    }

    public static String requestEditSupplierGood(int id, String name, String unit, String number, String price, String space){
        return "EditSupplierGood " + name + "%" + unit + "%" + number + "%" + price + "%" + space + "%" + id;
    }

    public static String requestGetNameSurname(String login){return "GetNameSurname " + login;}

    public static String requestGetListWarehouses(){return "GetListWarehouse";}

    public static String requestAddWarehouse(String name, String volume){return "AddWarehouse " + name + "%" + volume;}

    public static String requestDeleteWarehouse(int id){return "DeleteWarehouse " + id;}

    public static String requestEditWarehouse(String id, String name, String volume){return "EditWarehouse "
            + id + "%" + name + "%" + volume;}

    public static String requestGetListSuppliers(int barcode){return "GetListSuppliers " + barcode;}

    public static String requestAddDelivery(String str){ return "AddDelivery " + str;}

    public static String requestGetListGoodsAll(){return "GetListGoodsAll";}

    public static String requestGetListGoodsRating(String login){return "GetListGoodsRating " + login;}

    public static String requestGetListGoodsWarehouse(int id){return "GetListGoodsWarehouse " + id;}

    public static String requestTransferGoods(String idFromWarehouse, String idToWarehouse, int idGood, String numberGoods){
        return "TransferGoods " + idFromWarehouse + "%" + idToWarehouse + "%" + idGood + "%" + numberGoods;
    }

    public static String requestAddRating(String login, int idGood, String ratingNum, String ratingPrice){
        return "AddRating " + login + "%" + idGood + "%" + ratingNum + "%" + ratingPrice;
    }

    public static String requestGetRating(String loginUser){return "GetRating " + loginUser;}

    public static String requestGetReportGood(){return "GetReportGood";}

    public static String requestGetRatingMetod(String barcode){return "GetRatingMetod " + barcode;}

    public static String requestUpdatePriceRealise(int barcode, String price){return "UpdatePriceRealise "
            + barcode + "%" + price;}

    public static String requestGetLineChartInfo(){return "GetLineChartInfo";}

    public static String requestDeleteWarehouseGood(int id){return "DeleteWarehouseGood " + id;}

}
