package com.imran.amqp;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Syed Jaleel
 */
@Component
public class Receiver {

    public String  receiveMessage(String apiGatewayTask) {
        return "Hello world";
    }

}
