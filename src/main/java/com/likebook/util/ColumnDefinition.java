package com.likebook.util;

public interface ColumnDefinition {

	 String CHAR = "Char";
	    String UNIQUEIDENTIFIER = "uniqueidentifier";
	    String BIGINT = "bigint";
	    String NVARCHAR8 = "nvarchar(8)";
	    String NVARCHAR50 = "nvarchar(50)";
	    String NVARCHAR500 = "nvarchar(500)";
	    String BIT = "bit not null default 0";
	    String BIT_TRUE = "bit not null default 1";
}
