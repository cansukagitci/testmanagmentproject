package com.example.testmanagment.model;

import java.util.List;

public class UserResponse {
    private List<UserDetail> result;

    public UserResponse(List<UserDetail> result) {
        this.result = result;
    }

    public List<UserDetail> getResult() {
        return result;
    }

    public void setResult(List<UserDetail> result) {
        this.result = result;
    }

    public static class UserDetail {
        private int value;
        private boolean status;
        private String message;

        public UserDetail(int value, boolean status, String message) {
            this.value = value;
            this.status = status;
            this.message = message;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
