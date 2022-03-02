package cn.cry.springbootredis.bo;


import org.springframework.stereotype.Component;

/**
 * 库存实体类
 */
@Component
public class Inventory {
    private Long commodityId;           //商品ID
    private String commodityName;       //商品名称
    private Long surplus;               //剩余量
    private Long lucyNumber;            //幸运用户数

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public Long getSurplus() {
        return surplus;
    }

    public void setSurplus(Long surplus) {
        this.surplus = surplus;
    }

    public Long getLucyNumber() {
        return lucyNumber;
    }

    public void setLucyNumber(Long lucyNumber) {
        this.lucyNumber = lucyNumber;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "commodityId=" + commodityId +
                ", commodityName='" + commodityName + '\'' +
                ", surplus=" + surplus +
                ", lucyNumber=" + lucyNumber +
                '}';
    }
}