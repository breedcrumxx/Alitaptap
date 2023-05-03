package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentSearch {

    @JsonProperty("search")
    private String Search;
    @JsonProperty("constraint")
    private String Constraint;
    @JsonProperty("filter")
    private String Filter = "ASC";

    public String getSearch() {
        return this.Search;
    }

    public void setSearch(String Search) {
        this.Search = Search;
    }

    public String getConstraint() {
        return this.Constraint;
    }

    public void setConstraint(String Constraint) {
        this.Constraint = Constraint;
    }

    public String getFilter() {
        return this.Filter;
    }

    public void setFilter(String Filter) {
        this.Filter = Filter;
    }

}
