package com.avdt.retorfit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

   public class Platforms {
      @SerializedName("grocery")
      ArrayList<String> grocery;
      @SerializedName("utility payment")
      ArrayList<String> utilityPayment;
      @SerializedName("entertainment")
      ArrayList<String> entertainment;
      @SerializedName("food ordering")
      ArrayList<String> foodOrdering;
      @SerializedName("dining")
      ArrayList<String> dining;
      @SerializedName("travel")
      ArrayList<String> travel;
      @SerializedName("ecom")
      ArrayList<String> ecom;
      @SerializedName("pharma")
      ArrayList<String> pharma;

      public ArrayList<String> getGrocery() {
         return grocery;
      }

      public void setGrocery(ArrayList<String> grocery) {
         this.grocery = grocery;
      }

      public ArrayList<String> getUtilityPayment() {
         return utilityPayment;
      }

      public void setUtilityPayment(ArrayList<String> utilityPayment) {
         this.utilityPayment = utilityPayment;
      }

      public ArrayList<String> getEntertainment() {
         return entertainment;
      }

      public void setEntertainment(ArrayList<String> entertainment) {
         this.entertainment = entertainment;
      }

      public ArrayList<String> getFoodOrdering() {
         return foodOrdering;
      }

      public void setFoodOrdering(ArrayList<String> foodOrdering) {
         this.foodOrdering = foodOrdering;
      }

      public ArrayList<String> getDining() {
         return dining;
      }

      public void setDining(ArrayList<String> dining) {
         this.dining = dining;
      }

      public ArrayList<String> getTravel() {
         return travel;
      }

      public void setTravel(ArrayList<String> travel) {
         this.travel = travel;
      }

      public ArrayList<String> getEcom() {
         return ecom;
      }

      public void setEcom(ArrayList<String> ecom) {
         this.ecom = ecom;
      }

      public ArrayList<String> getPharma() {
         return pharma;
      }

      public void setPharma(ArrayList<String> pharma) {
         this.pharma = pharma;
      }
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
      @SerializedName("message")
      String message;

      public String getMessage() {
         return message;
      }

      public void setMessage(String message) {
         this.message = message;
      }

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
