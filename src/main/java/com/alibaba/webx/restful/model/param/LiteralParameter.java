package com.alibaba.webx.restful.model.param;

import com.alibaba.webx.restful.model.Parameter;
import com.alibaba.webx.restful.model.converter.TypeConvertException;
import com.alibaba.webx.restful.model.converter.TypeConverter;
import com.alibaba.webx.restful.process.RestfulRequestContext;

public abstract class LiteralParameter implements Parameter {

    private final String        name;
    private final TypeConverter typeConverter;
    private final Object        defaultValue;

    public LiteralParameter(String name, TypeConverter typeConverter, Object defaultValue){
        super();
        this.name = name;
        this.typeConverter = typeConverter;
        this.defaultValue = defaultValue;
    }

    @Override
    public Object getParameterValue(RestfulRequestContext requestContext) throws TypeConvertException {
        String literalValue = getLiteralValue(requestContext);

        if (literalValue == null || literalValue.length() == 0) {
            return defaultValue;
        }

        return typeConverter.convert(literalValue);
    }

    public abstract String getLiteralValue(RestfulRequestContext requestContext);

    public String getName() {
        return name;
    }

    public TypeConverter getTypeConverter() {
        return typeConverter;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

}
