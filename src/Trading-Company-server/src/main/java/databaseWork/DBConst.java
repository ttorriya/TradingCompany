package databaseWork;

public class DBConst {
    public static final String USER_TABLE = "public.\"Users\""; //таблица пользователей
    public static final String GOODS_TABLE = "public.\"Goods\""; //таблица товаров
    public static final String BASKET_TABLE = "public.\"Basket\""; //таблица корзины поставки
    public static final String BASKET_GOOD_TABLE = "public.\"basket_goods\""; //промежуточна таблица товаров в корзине
    //public static final String BUYER_TABLE = "public.\"Buyer\""; //таблица покупателей
    public static final String METOD_TABLE = "public.\"metod\""; //таблица покупателей
    public static final String WAREHOUSE_BASKET_TABlE = "public.\"Warehouse_basket\""; //таблица корзин товаров складs
    public static final String WAREHOUSE_GOODS_TABLE = "public.\"Warehouse_goods\""; //таблица товаров корзины
    public static final String SUPPLIERS_TABLE = "public.\"Suppliers\""; //таблица поставщиков
    public static final String WAREHOUSE_TABLE = "public.\"Warehouse\""; //таблица складов
    public static final String DELIVERY_TABLE = "public.\"Delivery\""; //таблица поставок

    public static final String USER_ID = "id"; //id пользователя
    public static final String USER_LOGIN = "login"; //логин
    public static final String USER_PASSWORD = "password"; //пароль
    public static final String USER_EMAIL = "email"; //почта
    public static final String USER_PHONE = "phone_number"; //номер телефона
    public static final String USER_FIRSTNAME = "firstname"; //имя
    public static final String USER_LASTNAME = "lastname"; //фамилия
    public static final String USER_PATRONYMIC = "patronymic"; //отчество
    public static final String USER_ACCESS = "access_rights"; //права доступа
    public static final String USER_CONFIRM_ACCESS = "confirm_access"; //разрешение в доступе пользоватлея к системе

    public static final String GOOD_BARCODE = "barcode"; //штрих код
    public static final String GOOD_PRODUCT_NAME = "product_name"; //название товара
    public static final String GOOD_UNIT = "unit_measurement"; //ед. измерения
    public static final String GOOD_QUANTITY = "quantity"; //количетсво
    public static final String GOOD_PRICE = "price"; //цена
    public static final String GOOD_VOLUME = "occupied_space"; //занимаемое место

    public static final String BASKET_ID = "id"; //id корзины
    public static final String BASKET_ID_USER = "id_user"; //id пользователя

    public static final String BASKET_GOOD_TABLE_ID_BASKET = "id_basket"; //id корзины
    public static final String BASKET_GOOD_TABLE_ID_GOOD = "id_good"; //id товара


    public static final String METOD_ID_USER = "iduser"; //id пользователя
    public static final String METOD_ID_GOOD = "idgood"; //id товара
    public static final String METOD_RATING_NUM = "rating_num"; //оценка
    public static final String METOD_RATING_PRICE = "rating_price"; //оценка



    /*public static final String BUYER_ID = "id"; //id пользователя
    public static final String BUYER_ID_BASKET = "id_basket"; //id корзины покупки*/

    public static final String DELIVERY_ID = "id";
    public static final String DELIVERY_DATE = "date";
    public static final String DELIVERY_ID_BASKET = "id_warehouse_basket";



    public static final String SUPPLIER_ID = "id"; //id поставщика
    public static final String SUPPLIER_ORG_NAME = "org_name"; //имя организации
    public static final String SUPPLIER_CITY = "city"; //страна
    public static final String SUPPLIER_COUNTRY = "country"; //город
    public static final String SUPPLIER_ADDRESS= "address"; //адрес
    public static final String SUPPLIER_ID_BASKET= "id_basket"; //id корзины

    public static final String WAREHOUSE_ID = "id"; //id склада
    public static final String WAREHOUSE_NAME = "name"; //наименование склада
    public static final String WAREHOUSE_VOLUME = "volume"; //объём склада
    public static final String WAREHOUSE_ID_BASKET = "idbasket"; //id корзины
    public static final String WAREHOUSE_FULLNESS = "fullness"; //заполненность

    public static final String WAREHOUSE_BASKET_ID = "id"; //id корзины
    public static final String WAREHOUSE_BASKET_ID_WAREHOUSE = "idwarehouse"; //id склада

    public static final String WAREHOUSE_GOODS_ID_BASKET = "idbasket"; // id корзины
    public static final String WAREHOUSE_GOODS_ID_GOOD = "idgood"; // id товара
    public static final String WAREHOUSE_GOOD_TABLE_NUMBER = "number"; //количество
    public static final String WAREHOUSE_GOOD_TABLE_PRICE = "price"; //количество
}
