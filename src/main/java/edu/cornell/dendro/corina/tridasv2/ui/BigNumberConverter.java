/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.tridasv2.ui;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

import com.l2fprod.common.util.converter.ConverterRegistry;
import com.l2fprod.common.util.converter.NumberConverters;

public class BigNumberConverter extends NumberConverters {

	public void register(ConverterRegistry registry) {
		// big integer
		registry.addConverter(BigInteger.class, BigDecimal.class, this);
		registry.addConverter(BigInteger.class, Double.class, this);
		registry.addConverter(BigInteger.class, Float.class, this);
		registry.addConverter(BigInteger.class, BigInteger.class, this);
		registry.addConverter(BigInteger.class, Integer.class, this);
		registry.addConverter(BigInteger.class, Long.class, this);
		registry.addConverter(BigInteger.class, Short.class, this);
		registry.addConverter(BigInteger.class, String.class, this);

		registry.addConverter(BigDecimal.class, BigInteger.class, this);
		registry.addConverter(Double.class, BigInteger.class, this);
		registry.addConverter(Float.class, BigInteger.class, this);
		registry.addConverter(Integer.class, BigInteger.class, this);
		registry.addConverter(Long.class, BigInteger.class, this);
		registry.addConverter(Short.class, BigInteger.class, this);
		registry.addConverter(String.class, BigInteger.class, this);
		
		// big decimal
		registry.addConverter(BigDecimal.class, BigDecimal.class, this);
		registry.addConverter(BigDecimal.class, Double.class, this);
		registry.addConverter(BigDecimal.class, Float.class, this);
		registry.addConverter(BigDecimal.class, BigInteger.class, this);
		registry.addConverter(BigDecimal.class, Integer.class, this);
		registry.addConverter(BigDecimal.class, Long.class, this);
		registry.addConverter(BigDecimal.class, Short.class, this);
		registry.addConverter(BigDecimal.class, String.class, this);

		registry.addConverter(Double.class, BigDecimal.class, this);
		registry.addConverter(Float.class, BigDecimal.class, this);
		registry.addConverter(Integer.class, BigDecimal.class, this);
		registry.addConverter(Long.class, BigDecimal.class, this);
		registry.addConverter(Short.class, BigDecimal.class, this);
		registry.addConverter(String.class, BigDecimal.class, this);

	}

	/**
	 * Almost a direct rip from NumberConverters.java in l2fprod jar
	 */
	@SuppressWarnings("unchecked")
	public Object convert(Class targetType, Object value) {
		// are we dealing with a number to number conversion?
		if ((value instanceof Number) && Number.class.isAssignableFrom(targetType)) {
			if (BigDecimal.class.equals(targetType)) {
				return new BigDecimal(value.toString());
			} else if (Double.class.equals(targetType)) {
				return new Double(((Number) value).doubleValue());
			} else if (Float.class.equals(targetType)) {
				return new Float(((Number) value).floatValue());
			} else if (BigInteger.class.equals(targetType)) {
				return new BigInteger(((Number) value).toString());
			} else if (Integer.class.equals(targetType)) {
				return new Integer(((Number) value).intValue());
			} else if (Long.class.equals(targetType)) {
				return new Long(((Number) value).longValue());
			} else if (Short.class.equals(targetType)) {
				return new Short(((Number) value).shortValue());
			} else {
				throw new IllegalArgumentException(
						"this code must not be reached");
			}
		} else if ((value instanceof Number) && String.class.equals(targetType)) {
			NumberFormat format = NumberFormat.getNumberInstance();
			
			format.setMinimumIntegerDigits(1);
			format.setMaximumIntegerDigits(100);
			format.setMinimumFractionDigits(0);
			format.setMaximumFractionDigits(100);

			if(value instanceof BigInteger || value instanceof BigDecimal)
				return format.format(value);
			else 
				throw new IllegalArgumentException(
						"this code must not be reached");
		} else if ((value instanceof String) && Number.class.isAssignableFrom(targetType)) {
			if (BigDecimal.class.equals(targetType)) {
				return new BigDecimal((String) value);
			} else if (BigInteger.class.equals(targetType)) {
				return new BigInteger((String) value);
			} else {
				throw new IllegalArgumentException(
						"this code must not be reached");
			}
		}
		throw new IllegalArgumentException("no conversion supported");
	}
}
