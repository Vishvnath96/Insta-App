package com.example.vps.ui.util.common;

public enum Category {
   home("Home"),
   world("world"),
   business("business"),
   entertainment("entertainment"),
   environment("environment"),
   general("general"),
   health("health"),
   science("science"),
   sports("sport"),
   fashion("fashion"),
   society("society"),
   culture("culture"),
   technology("technology");

   public final String title;

   Category(String title) {
       this.title = title;
   }
}
