package com.blog.demo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomHashMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = 1L;

//    public CustomHashMap(int initialCapacity) {
//        super(initialCapacity);
//    }
//
//    public CustomHashMap(int initialCapacity, float loadFactor) {
//        super(initialCapacity, loadFactor);
//    }
//
//    public CustomHashMap(Map<? extends K, ? extends V> m) {
//        super(m);
//    }
//
//    @Override
//    public V put(K key, V value) {
//        return super.put(key, value);
//    }
//
//    @Override
//    public V get(Object key) {
//        return super.get(key);
//    }
//
//    @Override
//    public V remove(Object key) {
//        return super.remove(key);
//    }
//
//    @Override
//    public boolean containsKey(Object key) {
//        return super.containsKey(key);
//    }
//
//    @Override
//    public boolean containsValue(Object value) {
//        return super.containsValue(value);
//    }
//
//    @Override
//    public void clear() {
//        super.clear();
//    }
//
//    @Override
//    public int size() {
//        return super.size();
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return super.isEmpty();
//    }
//
//    public V putIfAbsent(K key, V value) {
//        return super.putIfAbsent(key, value);
//    }
//
//    public boolean remove(Object key, Object value) {
//        return super.remove(key, value);
//    }
//
//    public boolean replace(K key, V oldValue, V newValue) {
//        return super.replace(key, oldValue, newValue);
//    }

    public V replace(K key, V value) {
        return super.replace(key, value);
    }

    public int getInt(Object key){
        // Get the value from the map
        String value = (String)super.get(key);

        int nullvalue = 0;

            try {
                // Try to parse the string value to an integer
                @SuppressWarnings("unchecked")
                int parsedValue = (int) Integer.valueOf((String) value);
                return parsedValue;
            } catch (NumberFormatException e) {
//                return Integer.parseInt(e.getMessage());
            }
        return nullvalue;
    
	}
    
    @Override
    public V get(Object key) {
        V value = super.get(key);
        if (value == null) {
            // Return an empty string if the value is null
            return (V) "";
        }
        return value;
    }
    
    public double getDouble(Object key) {
        // Get the value from the map
        String value = (String) super.get(key);
        double defaultValue = 0.0;

        // Check if the value is null or empty
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            // Try to parse the string value to a double
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // If parsing fails, return the default value
            return defaultValue;
        }
    }
    
    public String getString(String key) {
        // Get the value from the map
        String value = (String)super.get(key);
        String nullvalue = "";
            try {
                String defaultValue = "";
                String finalValue = value != null ? value : defaultValue;
                return finalValue;
            } catch (NumberFormatException e) {
            	return nullvalue;
            }
	}
    
    public void putAllData(Object obj) {

        if (obj instanceof Map<?, ?>) {
            Map<?, ?> otherMap = (Map<?, ?>) obj;
            for (Object key : otherMap.keySet()) {
                this.put((K) key, (V) otherMap.get(key));
            }
        } else if(obj instanceof LinkedHashMap){
        	LinkedHashMap otherMap = (LinkedHashMap) obj;
            for (K key : this.keySet()) {
                if (otherMap.containsKey(key)) {
                    this.put(key, (V) otherMap.get(key));
                }
            }
        }
        else {
        	throw new IllegalArgumentException("Providing object doesn't match anything");
        }
    }   
}