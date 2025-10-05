package com.wecp.progressive.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
   private String token;
   private String roles;
   private Integer userId;
   
   public LoginResponse(@JsonProperty("token") String token, String roles, Integer userId) {
      this.token = token;
      this.roles = roles;
      this.userId = userId;
   }

   public LoginResponse(String token) {
      this.token = token;
   }

   public String getToken() {
      return token;
   }

   public String getRoles() {
      return roles;
   }

   public Integer getUserId() {
      return userId;
   }
}
