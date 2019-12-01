package com.ecmdeveloper.junit.ce.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class ConfigurationTest
{
    @Test
    public void testConfiguration() throws IOException{  
        String  magic = "Magic";
        System.setProperty("username", magic);
        String jvmReadedValue = Configuration.get("TestConfiguration.username");
        Assert.assertEquals(magic, jvmReadedValue);
    }
}
