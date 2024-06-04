package vn.hust.api.dto.out.customer;


import lombok.Data;
import vn.hust.api.repository.helper.Col;

import java.math.BigDecimal;

@Data
public class CustomerDetailOut {
    @Col("id")
    private String id;
    @Col("username")
    private String username;
    @Col("password")
    private String password;
    @Col("first_name")
    private String firstName;
    @Col("email")
    private String email;
    @Col("phone")
    private String phone;
    @Col("last_name")
    private String lastName;
    @Col("created_date")
    private String createdDate;
    @Col("birth_day")
    private String birthDay;
    @Col("avatar")
    private String avatar;
    @Col("gender")
    private String gender;
    @Col("city")
    private String city;
    @Col("specific_address")
    private String specificAddress;
    @Col("district")
    private String district;
    @Col("sub_district")
    private String subDistrict;

    private BigDecimal totalOrder;
    private BigDecimal totalSuccessOrder;
    private BigDecimal totalFailedOrder;
}
