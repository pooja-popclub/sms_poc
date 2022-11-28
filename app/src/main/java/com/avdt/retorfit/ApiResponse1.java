package com.avdt.retorfit;

import com.google.gson.annotations.SerializedName;

public class ApiResponse1 {

   @SerializedName("is_success")
   boolean isSuccess;
   @SerializedName("message")
   boolean message;
   @SerializedName("data")
   DTO data;

   public boolean isSuccess() {
      return isSuccess;
   }

   public void setSuccess(boolean success) {
      isSuccess = success;
   }

   public boolean isMessage() {
      return message;
   }

   public void setMessage(boolean message) {
      this.message = message;
   }

   public DTO getData() {
      return data;
   }

   public void setData(DTO data) {
      this.data = data;
   }

   public class DTO {
      @SerializedName("Food Ordering")
      String foodOrdering;
      @SerializedName("Grocery")
      String grocery;
      @SerializedName("Utility Payment")
      String utilityPayment;
      @SerializedName("Entertainment")
      String entertainment;
      @SerializedName("Dining")
      String dinning;
      @SerializedName("Travel")
      String travel;
      @SerializedName("Ecom")
      String ecom;
      @SerializedName("Pharma")
      String pharma;
      @SerializedName("coins")
      String coins;

      public String getFoodOrdering() {
         return foodOrdering;
      }

      public void setFoodOrdering(String foodOrdering) {
         this.foodOrdering = foodOrdering;
      }

      public String getGrocery() {
         return grocery;
      }

      public void setGrocery(String grocery) {
         this.grocery = grocery;
      }

      public String getUtilityPayment() {
         return utilityPayment;
      }

      public void setUtilityPayment(String utilityPayment) {
         this.utilityPayment = utilityPayment;
      }

      public String getEntertainment() {
         return entertainment;
      }

      public void setEntertainment(String entertainment) {
         this.entertainment = entertainment;
      }

      public String getDinning() {
         return dinning;
      }

      public void setDinning(String dinning) {
         this.dinning = dinning;
      }

      public String getTravel() {
         return travel;
      }

      public void setTravel(String travel) {
         this.travel = travel;
      }

      public String getEcom() {
         return ecom;
      }

      public void setEcom(String ecom) {
         this.ecom = ecom;
      }

      public String getPharma() {
         return pharma;
      }

      public void setPharma(String pharma) {
         this.pharma = pharma;
      }

      public String getCoins() {
         return coins;
      }

      public void setCoins(String coins) {
         this.coins = coins;
      }
   }

}
