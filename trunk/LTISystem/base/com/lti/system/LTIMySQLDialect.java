package com.lti.system;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.VarArgsSQLFunction;


public class LTIMySQLDialect extends MySQLDialect {

	public LTIMySQLDialect() {
		super();
	    registerHibernateType(Types.DECIMAL, Hibernate.BIG_DECIMAL.getName());
	    registerHibernateType(Types.LONGVARCHAR, 65535, "text");
	    //registerFunction("bitwise_and", new BitWiseFunction("bitwise_and", Hibernate.LONG));
	    registerFunction("bit_not", new SQLFunctionTemplate(Hibernate.INTEGER, "~?1"));
	    registerFunction("bit_and", new VarArgsSQLFunction(Hibernate.INTEGER, "(", "&", ")"));
	    registerFunction("bit_or", new VarArgsSQLFunction(Hibernate.INTEGER, "(", "|", ")"));
	}

}
