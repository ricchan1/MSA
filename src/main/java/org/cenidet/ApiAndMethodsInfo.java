package org.cenidet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ApiAndMethodsInfo implements Serializable {
    private int numMethods = 0;
    private int numMethodsPost = 0;
    private int numMethodsPut = 0;
    private int numMethodsDelete = 0;
    private int numMethodsGet = 0;
    private int numParams = 0;
    private int numReturns = 0;
    private String apiName;
    private String className = "";
    private String packageName = "";
    private String errors = "";
    private List<String> methodsList;
    private List<String> variableList;
    private List<String> variableType;
    private List<List<String>> parameters;
    private List<List<String>> returns = new ArrayList<>();

    public ApiAndMethodsInfo() {}

    public int getNumMethods() {
        return numMethods;
    }

    public void setNumMethods(int numMethods) {
        this.numMethods = numMethods;
    }

    public int getNumMethodsPost() {
        return numMethodsPost;
    }

    public void setNumMethodsPost(int numMethodsPost) {
        this.numMethodsPost = numMethodsPost;
    }

    public int getNumMethodsPut() {
        return numMethodsPut;
    }

    public void setNumMethodsPut(int numMethodsPut) {
        this.numMethodsPut = numMethodsPut;
    }

    public int getNumMethodsDelete() {
        return numMethodsDelete;
    }

    public void setNumMethodsDelete(int numMethodsDelete) {
        this.numMethodsDelete = numMethodsDelete;
    }

    public int getNumMethodsGet() {
        return numMethodsGet;
    }

    public void setNumMethodsGet(int numMethodsGet) {
        this.numMethodsGet = numMethodsGet;
    }

    public int getNumParams() {
        return numParams;
    }

    public void setNumParams(int numParams) {
        this.numParams = numParams;
    }

    public int getNumReturns() {
        return numReturns;
    }

    public void setNumReturns(int numReturns) {
        this.numReturns = numReturns;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public List<String> getMethodsList() {
        return methodsList;
    }

    public void setMethodsList(List<String> methodsList) {
        this.methodsList = methodsList;
    }

    public List<String> getVariableList() {
        return variableList;
    }

    public void setVariableList(List<String> variableList) {
        this.variableList = variableList;
    }

    public List<String> getVariableType() {
        return variableType;
    }

    public void setVariableType(List<String> variableType) {
        this.variableType = variableType;
    }

    public List<List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<List<String>> parameters) {
        this.parameters = parameters;
    }

    public List<List<String>> getReturns() {
        return returns;
    }

    public void setReturns(List<List<String>> returns) {
        this.returns = returns;
    }
}
