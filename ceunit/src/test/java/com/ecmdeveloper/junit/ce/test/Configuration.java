package com.ecmdeveloper.junit.ce.test;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.StandardELContext;
import javax.el.ValueExpression;

public class Configuration {
	private static final String BUNDLE_NAME = "com.ecmdeveloper.junit.ce.test.configuration"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Configuration() {
	}

	private static Pattern expressionPattern = Pattern.compile("\\$\\{(.+?)\\}");
	
	public static String get(String key) {
	    return get(RESOURCE_BUNDLE, key);
	}
	
	public static String get(ResourceBundle bundle, String key) {
		try {
			String value = bundle.getString(key);
			if(value.indexOf("$") > -1){
	            ExpressionFactory factory = ExpressionFactory.newInstance();
	            ELContext context = new StandardELContext(factory);
	            Matcher m = expressionPattern.matcher(value);
	            while(m.find()){
	                String group = m.group(1);
	                if(group.startsWith("env_")){
	                    String varName = group.substring(4);
	                    ValueExpression vePojo = factory.createValueExpression(System.getenv(varName), String.class);
	                    context.getVariableMapper().setVariable(group, vePojo);
	                }else if(group.startsWith("jvm_")){
                        String varName = group.substring(4);
                        ValueExpression vePojo = factory.createValueExpression(System.getProperty(varName), String.class);
                        context.getVariableMapper().setVariable(group, vePojo);	                    
	                }else{
	                    // do nothing
	                }
	            }
	            ValueExpression ve = factory.createValueExpression(context, value, String.class);
	            // evaluated value of ${pojo.value}
	            value = (String)ve.getValue(context);			    
			}
			return value;
		} catch (MissingResourceException e) {
		    e.printStackTrace();
			return '!' + key + '!';
		}
	}
}
