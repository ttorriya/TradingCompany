package connectionWork;

import java.io.IOException;
import java.sql.SQLException;

public class ActionMenu {

    public String dataProcessing(String string) throws SQLException, IOException, ClassNotFoundException {
        String[] requestArray;
        requestArray = string.split(" ");
        switch(requestArray[0]){
            case "AddUser":
                return UserWorker.addUser(string.replaceAll(requestArray[0], ""));
            case "SignInUser":
                return UserWorker.checkSignInUser(string.replaceAll(requestArray[0], ""));
            case "GetListUsers":
                return UserWorker.getListUsers();
            case "UpdateAccess":
                return UserWorker.updateAccess(string.replaceAll(requestArray[0], ""));
            case "DeleteUser":
                return UserWorker.deleteUser(string.replaceAll(requestArray[0], ""));
            case "GetListGoods":
                return GoodWorker.getListGoods(string.replaceAll(requestArray[0], "").trim());
            case "DeleteSupplierGood":
                return GoodWorker.deleteSupplierGood(string.replaceAll(requestArray[0], "").trim());
            case "AddSupplierGood":
                return GoodWorker.addSupplierGood(string.replaceAll(requestArray[0], "").trim());
            case "EditSupplierGood":
                return GoodWorker.editSupplierGood(string.replaceAll(requestArray[0], "").trim());
            case "GetNameSurname":
                return UserWorker.getNameSurname(string.replaceAll(requestArray[0], "").trim());
            case "AddWarehouse":
                return WarehouseWorker.addWarehouse(string.replaceAll(requestArray[0], "").trim());
            case "GetListWarehouse":
                return WarehouseWorker.getListWarehouse();
            case "DeleteWarehouse":
                return WarehouseWorker.deleteWarehouse(string.replaceAll(requestArray[0], "").trim());
            case "EditWarehouse":
                return WarehouseWorker.editWarehouse(string.replaceAll(requestArray[0], "").trim());
            case "GetListSuppliers":
                return UserWorker.getListSuppliers(string.replaceAll(requestArray[0], "").trim());
            case "GetListGoodsAll":
                return GoodWorker.getListGoodsAll();
            case "AddDelivery":
                return WarehouseWorker.addDelivery(string.replaceAll(requestArray[0], "").trim());
            case "GetListGoodsWarehouse":
                return WarehouseWorker.getListGoodsWarehouse(string.replaceAll(requestArray[0], "").trim());
            case "TransferGoods":
                return WarehouseWorker.transferGoods(string.replaceAll(requestArray[0], "").trim());
            case "AddRating":
                return GoodWorker.addRating(string.replaceAll(requestArray[0], "").trim());
            case "GetRating":
                return GoodWorker.getRating(string.replaceAll(requestArray[0], "").trim());
            case "GetReportGood":
                return WarehouseWorker.getReportGood();
            case "GetListGoodsRating":
                return GoodWorker.getListGoodsRating(string.replaceAll(requestArray[0], "").trim());
            case "GetRatingMetod":
                return GoodWorker.getRatingMetod(string.replaceAll(requestArray[0], "").trim());
            case "UpdatePriceRealise":
                return GoodWorker.updatePriceRealise(string.replaceAll(requestArray[0], "").trim());
            case "GetLineChartInfo":
                return WarehouseWorker.getLineChartInfo();
            case "DeleteWarehouseGood":
                return WarehouseWorker.deleteWarehouseGood(string.replaceAll(requestArray[0], "").trim());
            default: return "";
        }
    }
}
