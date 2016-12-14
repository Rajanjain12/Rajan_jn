package com.rsnt.web.util;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class CustomDialect extends MySQLDialect {


public CustomDialect(){
    registerFunction("group_concat", new StandardSQLFunction("group_concat",Hibernate.STRING));
    registerFunction("coalesce", new StandardSQLFunction("coalesce", Hibernate.STRING));
    registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());

	}
}