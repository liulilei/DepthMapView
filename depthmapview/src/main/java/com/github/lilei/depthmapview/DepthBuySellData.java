package com.github.lilei.depthmapview;

/**
 * @Description:
 * @author: lll
 * @date: 2019-07-19
 */
public class DepthBuySellData {

    public DepthBuySellData(String price, String amount) {
        this.price = price;
        this.amount = amount;
    }

    public String price;
    public String amount;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
