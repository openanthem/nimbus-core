package com.antheminc.oss.nimbus.test.sample.um.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.antheminc.oss.nimbus.test.sample.um.model.view.Address;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by AF13233 on 8/25/16.
 */
@Getter
@Setter
@ToString
public class PatientTest {

    @Id
    //@Indexed(unique = true)
    private Integer patientId;

    private String firstName;

    private String lastName;

    private List<Address> addresses;

}
