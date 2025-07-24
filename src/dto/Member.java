// dto/Member.java
package dto;

import java.time.LocalDateTime;

public class Member {
    private String memberId;
    private String password;
    private String name;
    private String phone;
    private String creditNumber;
    private LocalDateTime createdAt;

    public Member() {
    }

    public Member(String memberId, String password, String name, String phone, String creditNumber) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.creditNumber = creditNumber;
        this.createdAt = LocalDateTime.now();

    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
    }
}