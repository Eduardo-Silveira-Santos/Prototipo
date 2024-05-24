package com.eduardosantos.prototipo;

import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private String userName;
    private String userEmail;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
