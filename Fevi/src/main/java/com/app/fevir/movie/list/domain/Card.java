package com.app.fevir.movie.list.domain;

/**
 * Created by 1000742 on 15. 1. 5..
 */
public class Card {
    private String id;
    private String source;
    private String picture;
    private String description;
    /*
        Page 관련 도메인
    */
    private String name;
    private String category;
    private String profileImage;
    private String updatedTime;
    private String createdTime;

    public Card() { }

    public Card(String id, String source, String picture, String description, String name, String category, String profileImage, String updatedTime, String createdTime) {
        this.id = id;
        this.source = source;
        this.picture = picture;
        this.description = description;
        this.name = name;
        this.category = category;
        this.profileImage = profileImage;
        this.updatedTime = updatedTime;
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", picture='" + picture + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                ", createdTime='" + createdTime + '\'' +
                '}';
    }

    public static class Builder {
        private String id;
        private String source;
        private String picture;
        private String description;
        private String name;
        private String category;
        private String profileImage;
        private String updatedTime;
        private String createdTime;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder picture(String picture) {
            this.picture = picture;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder profileImage(String profile_image) {
            this.profileImage = profile_image;
            return this;
        }

        public Builder updatedTime(String updated_time) {
            this.updatedTime = updated_time;
            return this;
        }

        public Builder createdTime(String created_time) {
            this.createdTime = created_time;
            return this;
        }

        public Card createCard() {
            return new Card(id, source, picture, description, name, category, profileImage, updatedTime, createdTime);
        }
    }
}
