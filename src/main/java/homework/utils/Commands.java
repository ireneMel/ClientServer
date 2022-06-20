package homework.utils;

/*
Взнати кількість товару на складі
Списати певну кількість товару
Зарахувати певну кількість товару
Встановити ціну на конкретний товар

Додати групу товарів
Додати назву товару до групи
 */

public class Commands {
    public static final int
            COMMAND_SIZE = 6;
    public static final int
            PRODUCT_GET = 0,
            PRODUCT_INCREASE = 1,
            PRODUCT_DECREASE = 2,
            PRODUCT_ADD_NAME = 3,
            PRODUCT_SET_PRICE = 4;

    public static final int
            GROUP_ADD = 5,
            GROUP_ADD_PRODUCT_NAME = 6;
}
