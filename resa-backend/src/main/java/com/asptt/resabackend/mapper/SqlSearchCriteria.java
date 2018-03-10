package com.asptt.resabackend.mapper;

import java.util.Arrays;

public class SqlSearchCriteria {
	
	private int nbParam;
	private StringBuffer sql;
	private Object[] args;
	
	public SqlSearchCriteria(int nbParam, StringBuffer sql, Object[] args) {
		super();
		this.nbParam = nbParam;
		this.sql = sql;
		this.args = args;
	}

	public int getNbParam() {
		return nbParam;
	}

	public StringBuffer getSql() {
		return sql;
	}

	public Object[] getArgs() {
		return args;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(args);
		result = prime * result + nbParam;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SqlSearchCriteria other = (SqlSearchCriteria) obj;
		if (!Arrays.equals(args, other.args))
			return false;
		if (nbParam != other.nbParam)
			return false;
		return true;
	}
	

}
