package edu.miu.cs.cs544.enums;

public enum Constants {
    MAKE_RESERVATION_FAIL("You are not able to make reservation, Please try again later!"),
    UPDATE_RESERVATION_FAIL("You are not able to update reservation, Please try again later!"),
    CANCEl_RESERVATION_FAIL("You are not able to cancel reservation, Please try again later!"),
    REMOVE_RESERVATION_FAIL("You are not able to remove reservation, Please try again later"),
    GET_RESERVATION_FAIL("You are not able to get reservation, Please try again later"),

    RESERVATION_CHECKINGFAIL("You are not able to check in reservation, Please try again later"),
    //Product
    GET_PRODUCT_FAIL("You are not able to get product(s) currently, Please try again later"),
    ADD_PRODUCT_FAIL("You are not able to add product currently, Please try again later"),
    UPDATE_PRODUCT_FAIL("You are not able to update this product currently, Please try again later"),
    DELETE_PRODUCT_FAIL("You are not able to delete this product currently, Please try again later"),

    //Customer
    GET_CUSTOMER_FAIL("You are not able to get customer(s) currently, Please try again later"),
    ADD_CUSTOMER_FAIL("You are not able to add customer currently, Please try again later"),
    UPDATE_CUSTOMER_FAIL("You are not able to update this customer currently, Please try again later"),
    DELETE_CUSTOMER_FAIL("You are not able to delete this customer currently, Please try again later"),

    MAIL_SEND_FAIL("You are not able to send mail, Please try again later");
    private final String message;
    Constants(String message) {
        this.message = message;
    }

    public String getmessage() {
        return message;
    }
}
