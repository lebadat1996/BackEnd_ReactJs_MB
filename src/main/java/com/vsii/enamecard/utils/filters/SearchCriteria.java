package com.vsii.enamecard.utils.filters;

public class SearchCriteria<T> {
    private String key;
    private T value;
    private SearchOperator operation;

    public SearchCriteria() {
        // default constructor
    }

    public SearchCriteria(String key, SearchOperator operation, T value) {
        this.key = key;
        this.value = value;
        this.operation = operation;
    }

    // getters and setters, equals(), toString(), ... (omitted for brevity)

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public SearchOperator getOperation() {
        return operation;
    }

    public void setOperation(SearchOperator operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", operation=" + operation +
                '}';
    }
}
