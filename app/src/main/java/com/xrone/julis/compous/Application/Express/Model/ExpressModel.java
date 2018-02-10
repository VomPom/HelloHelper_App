package com.xrone.julis.compous.Application.Express.Model;

/**
 * Created by Julis on 2017/12/23.
 */

public class ExpressModel {

        /**
         * message : 【菜鸟驿站】您的12月9日百世快递包裹等您很久了，请凭（3-5-6075）尽快到商学院大门对面优纳学驾中心菜鸟驿站领取，谢谢理解
         * altitude : 27.917014
         * longitude : 120.691276
         * place : 商学院大门对面
         * keyword : 优纳学驾中心
         */

        private String message;
        private String altitude;
        private String longitude;
        private String place;
        private String keyword;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getAltitude() {
            return altitude;
        }

        public void setAltitude(String altitude) {
            this.altitude = altitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }
}
