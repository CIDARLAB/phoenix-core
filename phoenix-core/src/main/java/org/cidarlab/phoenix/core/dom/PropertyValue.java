/*
Copyright (c) 2012 Boston University.
All rights reserved.
Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the above
copyright notice and the following two paragraphs appear in all copies
of this software.

IN NO EVENT SHALL BOSTON UNIVERSITY BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
BOSTON UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

BOSTON UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND BOSTON UNIVERSITY HAS
NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
ENHANCEMENTS, OR MODIFICATIONS.
 */

package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.cidarlab.phoenix.core.exception.DOMException;

/**
 * A PropertyValue is a wrapper for a primitive type.
 *  
 * @author Ernst Oberortner
 */
public class PropertyValue 
	extends Property {

	private static final long serialVersionUID = 3074125010808580574L;

	private boolean bool;
	private String txt;
	private double num;
	private List<Double> numList;
	private List<String> txtList;

	/*
	 * the property value is an instance of a given property
	 */
	private Property property;

	/**
	 * The PropertyValue constructor requires a reference 
	 * to the corresponding Property. This helps for 
	 * type-checking.  
	 * 
	 * @param property 
	 */
	public PropertyValue(Property property) {
		super(property.getName(), property.getType());

		txt = new String();
		num = 0.0;
		bool = false;
		numList = new ArrayList<Double>();
		txtList = new ArrayList<String>();
		
		this.property = property;
	}

	/**
	 * The private/2 constructor is being used only internally 
	 * for ``array access''.
	 *  
	 * Example: a property value is of type txt[] and we'd like 
	 * to access the i-th element. The i-th element is of type 
	 * txt though and we have to create a new property value.
	 * 
	 * @param name
	 * @param pt
	 */
	private PropertyValue(String name, PropertyType pt) {
		super(name, pt);
		
		txt = new String();
		num = 0.0;
		bool = false;
		numList = new ArrayList<Double>();
		txtList = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @return the property of that the property value is an instance of
	 */
	public Property getProperty() {
		return this.property;
	}
	
	public void setTxt(String txt) {
		if (null != txt) {
			if (txt.startsWith("\"") && txt.endsWith("\"")) {
				this.txt = txt.substring(1, txt.length() - 1);
				return;
			}
		}
		this.txt = txt;
	}

	public String getTxt() {
		return this.txt;
	}

	public void setTxtList(ArrayList<String> txtList) {
		this.txtList = txtList;
	}

	public void addTxt(String s) {
		if (PropertyType.TXTLIST.equals(this.type)) {
			this.txtList.add(s);
		} else {
			System.err.println("cannot add a string to property "
					+ this.getName());
		}
	}

	public void setTxtList(List<String> txtList) {
		if (null == txtList)
			return;
		this.txtList = new ArrayList<String>();
		Iterator<String> it = txtList.iterator();
		while (it.hasNext()) {
			this.txtList.add(it.next());
		}
	}

	public List<String> getTxtList() {
		return this.txtList;
	}

	public void setNum(double num) {
		this.num = num;
	}

	public double getNum() {
		return this.num;
	}

	public void setNumList(List<Double> numList) {
		this.numList = numList;
	}

	public void addNum(double d) {
		if (PropertyType.NUMLIST.equals(this.type)) {
			this.numList.add(new Double(d));
		} else {
			System.err.println("cannot add a number to property "
					+ this.getName());
		}
	}

	public List<Double> getNumList() {
		return this.numList;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public boolean getBool() {
		return this.bool;
	}

	public void setValue(PropertyValue value) {
		if (PropertyType.NUM.equals(this.type)) {
			this.num = Double.parseDouble(String.valueOf(value.getNum()));
		} else if (PropertyType.NUMLIST.equals(this.type)) {
			this.numList = new ArrayList<Double>();
			for (int i = 0; i < value.getNumList().size(); i++) {
				this.numList.add(new Double(value.getNumList().get(i)));
			}
		} else if (PropertyType.TXT.equals(this.type)) {
			this.txt = new String(value.getTxt());
		} else if (PropertyType.TXTLIST.equals(this.type)) {
			this.txtList = new ArrayList<String>();
			for (int i = 0; i < value.getTxtList().size(); i++) {
				this.txtList.add(new String(value.getTxtList().get(i)));
			}
		} else if (PropertyType.BOOLEAN.equals(this.type)) {
			this.bool = new Boolean(String.valueOf(value.getBool()));
		}
	}

	public String getValue() {
		if (PropertyType.NUM.equals(this.type)) {
			String numberD = String.valueOf(this.num);
	        numberD = numberD.substring( numberD.indexOf ( "." )+1);
	        
	        try {
	        	// if the number is an integer, then 
	        	// we don't print the floating points
	        	if(Integer.parseInt(numberD) == 0) {
	        		return String.valueOf((int)this.num);
	        	}
	        } catch(Exception e) {
	        	return String.valueOf(this.num);
	        } 
	    } else if (PropertyType.NUMLIST.equals(this.type)) {
			return this.numList.toString();
		} else if (PropertyType.TXT.equals(this.type)) {
			return this.txt;
		} else if (PropertyType.TXTLIST.equals(this.type)) {
			return this.txtList.toString();
		} else if (PropertyType.BOOLEAN.equals(this.type)) {
			return String.valueOf(this.bool);
		}
		return new String();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(PropertyType.NUM.equals(this.type)) {
			
    		if(this.getNum() % 1 == 0) {
    			sb.append((int)this.getNum());
    		} else {
    			sb.append(this.getNum());
    		}

		} else if(PropertyType.NUMLIST.equals(this.type)) {
			sb.append("[");
			
			for(int i=0; i<this.getNumList().size(); i++) {
        		if((this.getNumList().get(i)).doubleValue() % 1 == 0) {
        			sb.append((this.getNumList().get(i)).intValue());
        		} else {
        			sb.append(this.getNumList().get(i));
        		}
        		
				if(i < this.getNumList().size() - 1) {
					sb.append(",");
				}
			}
			sb.append("]");
		} else if(PropertyType.TXT.equals(this.getType())) {
			sb.append("\"").append(this.getValue()).append("\"");
		} else if(PropertyType.TXTLIST.equals(this.getType())) {
			sb.append("[");
			for(int i=0; i<this.getTxtList().size(); i++) {
				sb.append("\"").append(this.getTxtList().get(i)).append("\"");
				if(i < this.getTxtList().size() - 1) {
					sb.append(",");
				}
			}
			sb.append("]");
		} else if(PropertyType.BOOLEAN.equals(this.getType())) {
			sb.append(this.getBool());
		}
		return sb.toString();
	}

	public boolean compareTo(PropertyValue objVariable) {
		if (this.equals(objVariable)) {
			return true;
		}
		return false;
	}

	public boolean equals(NamedElement objElement) {
		if (objElement instanceof PropertyValue) {
			PropertyValue objVar = (PropertyValue) objElement;
			if (this.getValue() == null && objVar == null
					|| this.getValue() != null
					&& this.getValue().equals(objVar.getValue())) {
				return true;
			}
		}
		return false;
	}

	public int getSize() {
		return this.size();
	}
	
	public int size() {
		if (PropertyType.NUM.equals(this.getType())
				|| PropertyType.BOOLEAN.equals(this.getType())) {
			return 1;
		} else if (PropertyType.NUMLIST.equals(this.getType())) {
			return this.getNumList().size();
		} else if (PropertyType.TXT.equals(this.getType())) {
			return this.getTxt().length();
		} else if (PropertyType.TXTLIST.equals(this.getType())) {
			return this.getTxtList().size();
		}
		return -1;
	}


	/**
	 * The get/1 method returns the element at index idx only 
	 * if the property value has an array type (i.e. txt[] or num[])
	 *  
	 * @param idx
	 * 
	 * @return
	 */
	public PropertyValue get(int idx) {
		if (PropertyType.NUMLIST.equals(this.getType())) {
			if (idx >= 0 && idx < this.getNumList().size()) {
				PropertyValue objVariable = 
						new PropertyValue(UUID.randomUUID().toString(), PropertyType.NUM);
				objVariable.setNum(this.numList.get(idx));
				return objVariable;
			}
		} else if (PropertyType.TXTLIST.equals(this.getType())) {
			if (idx >= 0 && idx < this.getTxtList().size()) {
				PropertyValue objVariable = 
						new PropertyValue(UUID.randomUUID().toString(), PropertyType.TXT);
				objVariable.setTxt(this.getTxtList().get(idx));
				return objVariable;
			}
		} else if (PropertyType.TXT.equals(this.getType())) {
			if (idx >= 0 && idx < this.getTxt().length()) {
				PropertyValue objVariable = 
						new PropertyValue(UUID.randomUUID().toString(), PropertyType.TXT);
				objVariable.setTxt(String.valueOf(this.getTxt().charAt(idx)));
				return objVariable;
			}
		}
		
		return null;
	}

	public void assign(NamedElement objElement)
			throws DOMException {

		if (objElement instanceof PropertyValue) {
			PropertyValue objVariable = (PropertyValue) objElement;
			if (this.getType().equals(objVariable.getType())) {
				this.setValue((PropertyValue) objElement);
			} else {
				throw new DOMException("Cannot assign a "
						+ objVariable.getType() + " value to a "
						+ this.getType() + " value!");
			}
		}
	}

	/**
	 * The set/2 method assigns a given NamedElement to the PropertyValue 
	 * at a given index.
	 * It throws a DOMException if the array index is out of bounds 
	 * and the types do not match.
	 * 
	 * @param idx
	 * @param objElement
	 * @throws DOMException
	 */
	public void set(int idx, PropertyValue pv)
			throws DOMException {
		
		// num[] <- num
		if (PropertyType.NUMLIST.equals(this.getType())
				&& PropertyType.NUM.equals(pv.getType())) {
			if (idx >= 0 && idx < this.numList.size()) {
				this.numList.set(idx, new Double(pv.getNum()));
			} else if(idx == this.numList.size()) {
				this.numList.add(new Double(pv.getNum()));
			} else {
				throw new DOMException(
						"The array index (" + idx
								+ ") is out of bounds!");
			}
		// txt[] <- txt
		} else if (PropertyType.TXTLIST.equals(this.getType())
				&& PropertyType.TXT.equals(pv.getType())) {
			if (idx >= 0 && idx < this.txtList.size()) {
				this.txtList.set(idx, new String(pv.getTxt()));
			} else if(this.txtList.size() == idx) {
				this.txtList.add(pv.getTxt());
			} else {
				throw new DOMException(
						"The array index (" + idx
								+ ") is out of bounds!");
			}
		// txt <- txt
		} else if (PropertyType.TXT.equals(this.getType())
				&& PropertyType.TXT.equals(pv.getType())) {
			
			String tmp = this.txt;
			StringBuilder sb = new StringBuilder();
			if (idx >= 0 && idx <= this.txt.length()) {
				sb.append(tmp.substring(0, idx)).append(pv.getTxt()).append(tmp.substring(idx+1, tmp.length()));
				this.txt = sb.toString();
			} else {
				throw new DOMException(
						"The array index (" + idx
								+ ") is out of bounds!");
			}
		} else {
			throw new DOMException("Cannot assign a "
					+ pv.getType() + " value to an element of a "
					+ this.getType() + " list!");
		}
	}

	public void set(String sElementName, NamedElement objElement)
			throws DOMException {
		throw new DOMException("This is not possible!");
	}

	public PropertyValue add(PropertyValue objVariable) 
			throws DOMException {
		PropertyValue retVal = (PropertyValue) null;
		if (null != objVariable) {
			if (PropertyType.TXT.equals(this.getType())) {
				// <TXT> + <TXT>
				if (PropertyType.TXT.equals(objVariable.getType())) {
					retVal = new PropertyValue(null, PropertyType.TXT);
					StringBuilder sb = new StringBuilder();
					sb.append(this.getTxt());
					sb.append(objVariable.getTxt());
					retVal.setTxt(sb.toString());

					// <TXT> + <TXTLIST>
				} else if (PropertyType.TXTLIST
						.equals(objVariable.getType())) {
					retVal = new PropertyValue(null, PropertyType.TXTLIST);

					ArrayList<String> lst = new ArrayList<String>();

					lst.add(this.getTxt());

					int size = objVariable.getTxtList().size();
					for (int i = 0; i < size; i++) {
						lst.add(objVariable.getTxtList().get(i));
					}

					retVal.setTxtList(lst);

					// <TXT> + <NUM>
				} else if (PropertyType.NUM.equals(objVariable.getType())) {
					retVal = new PropertyValue(null, PropertyType.TXT);
					retVal.setTxt(this.getTxt() + (int) objVariable.getNum());
				}
			} else if (PropertyType.TXTLIST.equals(this.getType())) {
				if (PropertyType.TXTLIST.equals(objVariable.getType())) {
					retVal = new PropertyValue(null, PropertyType.TXTLIST);

					ArrayList<String> lst = new ArrayList<String>();
					int size = this.getTxtList().size();
					for (int i = 0; i < size; i++) {
						lst.add(this.getTxtList().get(i));
					}

					size = objVariable.getTxtList().size();
					for (int i = 0; i < size; i++) {
						lst.add(objVariable.getTxtList().get(i));
					}

					retVal.setTxtList(lst);
				} else if (PropertyType.TXT.equals(objVariable.getType())) {

					retVal = new PropertyValue(null, PropertyType.TXTLIST);

					ArrayList<String> lst = new ArrayList<String>();
					int size = this.getTxtList().size();
					for (int i = 0; i < size; i++) {
						lst.add(this.getTxtList().get(i));
					}

					lst.add(objVariable.getTxt());

					retVal.setTxtList(lst);
				}
			} else if (PropertyType.NUM.equals(this.getType())) {
				// <NUM> + <NUM>
				if (PropertyType.NUM.equals(objVariable.getType())) {
					retVal = new PropertyValue(null, PropertyType.NUM);
					retVal.setNum(this.getNum() + objVariable.getNum());

					// <NUM> + <NUMLIST>
				} else if (PropertyType.NUMLIST
						.equals(objVariable.getType())) {
					retVal = new PropertyValue(null, PropertyType.NUMLIST);

					ArrayList<Double> lst = new ArrayList<Double>();

					lst.add(this.getNum());

					int size = objVariable.getNumList().size();
					for (int i = 0; i < size; i++) {
						lst.add(objVariable.getNumList().get(i));
					}

					retVal.setNumList(lst);

					// <NUM> + <TXT> -> <TXT>
				} else if (PropertyType.TXT.equals(objVariable.getType())) {
					retVal = new PropertyValue(null, PropertyType.TXT);
					try {
						retVal.setTxt((long)this.getNum() + 
								objVariable.getTxt());
					} catch(Exception e) {
						throw new DOMException(
								"I cannot concatenate "+this.getNum()+" and "+objVariable.getValue()+"!");
					}
				}
			} else if (PropertyType.NUMLIST.equals(this.getType())) {
				if (PropertyType.NUMLIST.equals(objVariable.getType())) {
					retVal = new PropertyValue(null, PropertyType.NUMLIST);

					ArrayList<Double> lst = new ArrayList<Double>();
					int size = this.getNumList().size();
					for (int i = 0; i < size; i++) {
						lst.add(this.getNumList().get(i));
					}

					size = objVariable.getNumList().size();
					for (int i = 0; i < size; i++) {
						lst.add(objVariable.getNumList().get(i));
					}

					retVal.setNumList(lst);
				} else if (PropertyType.NUM.equals(objVariable.getType())) {

					retVal = new PropertyValue(null, PropertyType.NUMLIST);

					ArrayList<Double> lst = new ArrayList<Double>();
					int size = this.getNumList().size();
					for (int i = 0; i < size; i++) {
						lst.add(this.getNumList().get(i));
					}
					lst.add(objVariable.getNum());

					retVal.setNumList(lst);
				}
			}
		}
		return retVal;
	}
	
	@Override
	public int hashCode() {
		int hash = this.getName().hashCode();
		hash += this.getType().hashCode();
		hash += this.getValue().hashCode();
		return hash;
	}
}
