package com.anthem.oss.nimbus.test.sample.um.model.view;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by AF13233 on 8/25/16.
 */
@Getter
@Setter
@ToString
public class Address {

    //@Indexed(unique = true)
    @Id
    private Integer addressId;

    private String street;

    private String zipCode;
    
}
