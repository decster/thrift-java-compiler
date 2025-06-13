package com.github.decster.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A const value is something parsed that could be a map, set, list, struct
 * or whatever.
 * Corresponds to t_const_value.h in the C++ implementation.
 */
public class TConstValue implements Comparable<TConstValue> {
    public enum Type {
        CV_INTEGER,
        CV_DOUBLE,
        CV_STRING,
        CV_MAP,
        CV_LIST,
        CV_IDENTIFIER,
        CV_UNKNOWN
    }

    private long intVal;
    private double doubleVal;
    private String stringVal;
    private Map<TConstValue, TConstValue> mapVal;
    private List<TConstValue> listVal;
    private String identifierVal;
    private TEnum enumVal;
    private Type valType;

    public TConstValue() {
        this.intVal = 0;
        this.doubleVal = 0.0;
        this.enumVal = null;
        this.valType = Type.CV_UNKNOWN;
        this.mapVal = new TreeMap<>();
        this.listVal = new ArrayList<>();
    }

    public TConstValue(long val) {
        this();
        setInteger(val);
    }

    public TConstValue(double val) {
        this();
        setDouble(val);
    }

    public TConstValue(String val) {
        this();
        setString(val);
    }

    public Type getType() {
        return valType;
    }

    public void setInteger(long val) {
        intVal = val;
        valType = Type.CV_INTEGER;
    }

    public void setDouble(double val) {
        doubleVal = val;
        valType = Type.CV_DOUBLE;
    }

    public void setString(String val) {
        stringVal = val;
        valType = Type.CV_STRING;
    }

    public void setIdentifier(String val) {
        identifierVal = val;
        valType = Type.CV_IDENTIFIER;
    }

    public void setMap(Map<TConstValue, TConstValue> val) {
        mapVal = val;
        valType = Type.CV_MAP;
    }

    public void setList(List<TConstValue> val) {
        listVal = val;
        valType = Type.CV_LIST;
    }

    public long getInteger() {
        return intVal;
    }

    public double getDouble() {
        return doubleVal;
    }

    public String getString() {
        return stringVal;
    }

    public String getIdentifier() {
        return identifierVal;
    }

    public Map<TConstValue, TConstValue> getMap() {
        return mapVal;
    }

    public List<TConstValue> getList() {
        return listVal;
    }

    public TEnum getEnum() {
        return enumVal;
    }

    public void setEnum(TEnum val) {
        enumVal = val;
    }

    public String getIdentifierName() {
        String ret = getIdentifier();
        int s = ret.indexOf('.');
        if (s == -1) {
            throw new RuntimeException("error: identifier " + ret + " is unqualified!");
        }
        ret = ret.substring(s + 1);
        s = ret.indexOf('.');
        if (s != -1) {
            ret = ret.substring(s + 1);
        }
        return ret;
    }

    public String getIdentifierWithParent() {
        String ret = getIdentifier();
        int s = ret.indexOf('.');
        if (s == -1) {
            throw new RuntimeException("error: identifier " + ret + " is unqualified!");
        }
        int s2 = ret.indexOf('.', s + 1);
        if (s2 != -1) {
            ret = ret.substring(s + 1);
        }
        return ret;
    }

    @Override
    public String toString() {
        switch (valType) {
            case CV_INTEGER:
                return Long.toString(intVal);
            case CV_DOUBLE:
                return Double.toString(doubleVal);
            case CV_STRING:
                return "\"" + stringVal + "\"";
            case CV_MAP:
                return "map[" + mapVal.size() + "]";
            case CV_LIST:
                return "list[" + listVal.size() + "]";
            case CV_IDENTIFIER:
                return identifierVal;
            default:
                return "unknown";
        }
    }
    @Override
    public int compareTo(TConstValue other) {
        if (this.valType != other.valType) {
            return this.valType.compareTo(other.valType);
        }
        switch (this.valType) {
            case CV_INTEGER:
                return Long.compare(this.intVal, other.intVal);
            case CV_DOUBLE:
                return Double.compare(this.doubleVal, other.doubleVal);
            case CV_STRING:
                return this.stringVal.compareTo(other.stringVal);
            case CV_IDENTIFIER:
                return this.identifierVal.compareTo(other.identifierVal);
            case CV_MAP:
                // Lexicographical comparison of map entries
                if (this.mapVal.size() != other.mapVal.size()) {
                    return Integer.compare(this.mapVal.size(), other.mapVal.size());
                }
                // Compare entries in order (TreeMap ensures consistent ordering)
                for (Map.Entry<TConstValue, TConstValue> thisEntry : this.mapVal.entrySet()) {
                    TConstValue otherValue = other.mapVal.get(thisEntry.getKey());
                    if (otherValue == null) {
                        return 1; // This map has a key the other doesn't
                    }
                    int valueCompare = thisEntry.getValue().compareTo(otherValue);
                    if (valueCompare != 0) {
                        return valueCompare;
                    }
                }
                return 0;
            case CV_LIST:
                // Lexicographical comparison of list elements
                int minSize = Math.min(this.listVal.size(), other.listVal.size());
                for (int i = 0; i < minSize; i++) {
                    int comparison = this.listVal.get(i).compareTo(other.listVal.get(i));
                    if (comparison != 0) {
                        return comparison;
                    }
                }
                return Integer.compare(this.listVal.size(), other.listVal.size());
            case CV_UNKNOWN:
            default:
                return 0;
        }
    }
}
