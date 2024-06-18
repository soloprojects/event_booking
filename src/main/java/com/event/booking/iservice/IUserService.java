package com.event.booking.iservice;


import com.event.booking.entity.User;

public interface IUserService {
     User findByEmail(String email);

}
