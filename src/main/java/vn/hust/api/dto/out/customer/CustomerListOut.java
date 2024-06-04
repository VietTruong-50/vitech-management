package vn.hust.api.dto.out.customer;

import lombok.Data;
import vn.hust.api.repository.helper.Col;

@Data
public class CustomerListOut {
    @Col("id")
    private String id;
    @Col("username")
    private String username;
    @Col("first_name")
    private String firstName;
    @Col("email")
    private String email;
    @Col("last_name")
    private String lastName;
    @Col("created_date")
    private String createdDate;
    @Col("birth_day")
    private String birthDay;
}
