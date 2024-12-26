package com.example.testmanagment.dto;

public class ProjectDto {
                    // Proje ID'si (opsiyonel: zaten veritabanında var ise)
    private String name;              // Projenin adı
    private String description;       // Projenin açıklaması
    private Long userId;              // Projeyi oluşturan kullanıcının ID'si
    private String label;

    // Default constructor
    public ProjectDto() {}

    public ProjectDto(String name, String description, Long userId, String label) {

        this.name = name;
        this.description = description;
        this.userId = userId;
        this.label = label;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
