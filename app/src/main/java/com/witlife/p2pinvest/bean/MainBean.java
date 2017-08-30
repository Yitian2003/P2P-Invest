package com.witlife.p2pinvest.bean;

import java.util.List;

/**
 * Created by bruce on 23/08/2017.
 */

public class MainBean {

    /**
     * imageArr : [{"ID":"1","IMAPAURL":"http://gwop.xtrich.com/xtApp/lexianghuo1.html","IMAURL":"http://192.168.1.104:8080/P2PInvest/images/index01.png","DESC":"Share Your Experience"},{"ID":"2","IMAPAURL":"http://gwop.xtrich.com/xtApp/new-plan.html","IMAURL":"http://192.168.1.104:8080/P2PInvest/images/index02.png","DESC":"Expend Your Network"},{"ID":"3","IMAPAURL":"http://gwop.xtrich.com/xtApp/new-plan.html","IMAURL":"http://192.168.1.104:8080/P2PInvest/images/index03.png","DESC":"Unexpectable APP"},{"ID":"5","IMAPAURL":"http://gwop.xtrich.com/xtApp/twcx.html","IMAURL":"http://192.168.1.104:8080/P2PInvest/images/index04.png","DESC":"Crazy Shopping"}]
     * proInfo : {"id":"1","memberNum":"100","minTouMoney":"100","money":"10","name":"Rainbow Plan For NewBie","progress":"90","suodingDays":"30","yearRate":"8.00"}
     */

    private ProInfoBean proInfo;
    private List<ImageArrBean> imageArr;

    public ProInfoBean getProInfo() {
        return proInfo;
    }

    public void setProInfo(ProInfoBean proInfo) {
        this.proInfo = proInfo;
    }

    public List<ImageArrBean> getImageArr() {
        return imageArr;
    }

    public void setImageArr(List<ImageArrBean> imageArr) {
        this.imageArr = imageArr;
    }

    public static class ProInfoBean {
        /**
         * id : 1
         * memberNum : 100
         * minTouMoney : 100
         * money : 10
         * name : Rainbow Plan For NewBie
         * progress : 90
         * suodingDays : 30
         * yearRate : 8.00
         */

        private String id;
        private String memberNum;
        private String minTouMoney;
        private String money;
        private String name;
        private String progress;
        private String suodingDays;
        private String yearRate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMemberNum() {
            return memberNum;
        }

        public void setMemberNum(String memberNum) {
            this.memberNum = memberNum;
        }

        public String getMinTouMoney() {
            return minTouMoney;
        }

        public void setMinTouMoney(String minTouMoney) {
            this.minTouMoney = minTouMoney;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getSuodingDays() {
            return suodingDays;
        }

        public void setSuodingDays(String suodingDays) {
            this.suodingDays = suodingDays;
        }

        public String getYearRate() {
            return yearRate;
        }

        public void setYearRate(String yearRate) {
            this.yearRate = yearRate;
        }
    }

    public static class ImageArrBean {
        /**
         * ID : 1
         * IMAPAURL : http://gwop.xtrich.com/xtApp/lexianghuo1.html
         * IMAURL : http://192.168.1.104:8080/P2PInvest/images/index01.png
         * DESC : Share Your Experience
         */

        private String ID;
        private String IMAPAURL;
        private String IMAURL;
        private String DESC;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getIMAPAURL() {
            return IMAPAURL;
        }

        public void setIMAPAURL(String IMAPAURL) {
            this.IMAPAURL = IMAPAURL;
        }

        public String getIMAURL() {
            return IMAURL;
        }

        public void setIMAURL(String IMAURL) {
            this.IMAURL = IMAURL;
        }

        public String getDESC() {
            return DESC;
        }

        public void setDESC(String DESC) {
            this.DESC = DESC;
        }
    }
}
