package com.abel.aisoftwareengineer.tools;

public interface Tool {
    String name();
    String description();
    String execute(String inputJson);
}
