package com.example.natural.model;

public class UserResponse {
   private String realm;
   private String realmID;
   private String id;
   private String firstName;
   private String lastName;
   private String email;
   private boolean enabled;
   private long createdOn;
   private boolean serviceAccount;
   private String username;

   public String getRealm() {
      return realm;
   }

   public String getRealmID() {
      return realmID;
   }

   public String getId() {
      return id;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public String getEmail() {
      return email;
   }

   public boolean isEnabled() {
      return enabled;
   }

   public long getCreatedOn() {
      return createdOn;
   }

   public boolean isServiceAccount() {
      return serviceAccount;
   }

   public String getUsername() {
      return username;
   }
}
