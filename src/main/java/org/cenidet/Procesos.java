package org.cenidet;

import antlr.gramaticaLexer;
import antlr.gramaticaParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Procesos {
    private final List<String> filesWErrors = new ArrayList<>();
    private final List<String[]> lineasCSV = new ArrayList<>();
    public Procesos() {}

    public String fileContent(String ruta) {
        String linea;
        StringBuilder cadena = new StringBuilder();
        try {
            File archivo = new File(ruta);
            try (FileReader reader = new FileReader(archivo); BufferedReader br = new BufferedReader(reader)) {
                while((linea = br.readLine()) != null) {
                    cadena.append(linea).append("\n");
                }
            }
        } catch (IOException ex) {
            cadena = new StringBuilder(ex.getMessage());
        }

        return cadena.toString();
    }

    public String[] divideText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return new String[]{text};
        } else {
            int index = text.lastIndexOf('\\', maxLength);
            if (index == -1) {
                return new String[]{text.substring(0, maxLength), text.substring(maxLength)};
            } else {
                return new String[]{text.substring(0, index + 1), text.substring(index + 1)};
            }
        }
    }

    public ApiAndMethodsInfo extractInfoFromFile(String filePath) {
        try {
            ApiAndMethodsInfo info = new ApiAndMethodsInfo();
            gramaticaLexer lexer = new gramaticaLexer(CharStreams.fromFileName(filePath));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            gramaticaParser parser = new gramaticaParser(tokens);
            gramaticaParser.ApiGatewayContext context = parser.apiGateway();

            gramaticaVisitor visitor = new gramaticaVisitor();
            visitor.visit(context);

            info.setMethodsList(visitor.getMethodsList());
            info.setVariableType(visitor.getVariableTypeList());
            info.setVariableList(visitor.getVariableList());
            info.setNumMethods(visitor.getNumMethods());
            info.setPackageName(visitor.getPackageName());
            info.setClassName(visitor.getClassName());
            info.setApiName(visitor.getApiName());
            info.setParameters(visitor.getParameters());
            info.setNumMethodsPost(visitor.getNumMethodsPost());
            info.setNumMethodsPut(visitor.getNumMethodsPut());
            info.setNumMethodsDelete(visitor.getNumMethodsDelete());
            info.setNumMethodsGet(visitor.getNumMethodsGet());
            info.setNumParams(visitor.getNumParams());
            info.setNumReturns(visitor.getNumReturns());
            info.setReturns(visitor.getReturns());
            info.setErrors(visitor.getErrors());

            return info;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public FileData extractFileInfo(String filePath) {
        FileData fileData = new FileData();
        File file = new File(filePath);
        fileData.setFileName(file.getName());
        fileData.setFilePath(filePath);
        fileData.setFileContent(fileContent(filePath));

        return fileData;
    }

    public String buildStringWithInfo(@NotNull ApiAndMethodsInfo info, String fileName, String filePath) {
        List<String> methods = info.getMethodsList();
        List<String> variableType = info.getVariableType();
        List<String> variables = info.getVariableList();
        List<List<String>> parameters = info.getParameters();
        List<List<String>> returns = info.getReturns();
        int numMethods = info.getNumMethods();
        int numMethodsPost = info.getNumMethodsPost();
        int numMethodsPut = info.getNumMethodsPut();
        int numMethodsDelete = info.getNumMethodsDelete();
        int numMethodsGet = info.getNumMethodsGet();
        int numParams = info.getNumParams();
        int numReturns = info.getNumReturns();
        String className = info.getClassName();
        String apiName = info.getApiName();
        String errors = info.getErrors();

        StringBuilder cadena = new StringBuilder(("--------------------------------------------\n"));
        cadena.append("Resultados del archivo \"")
                .append(fileName).append("\"\n")
                .append("Ruta: ")
                .append(filePath).append("\n")
                .append("Nombre de clase: ")
                .append(className).append("\n")
                .append("Nombre de API: ");

        if(apiName.isBlank() || apiName.isEmpty()) {
            cadena.append("No aplica").append("\n");
        } else {
            cadena.append(apiName).append("\n");
        }

        cadena.append("Parámetros encontrados: \n");

        if(variables.isEmpty()) {
            cadena.append("\tNo se encontraron parámetros.\n");
        } else {
            int cont = 1;
            for(String variable : variables) {
                cadena.append("\tParámetro ")
                        .append(cont).append(": ")
                        .append(variable).append(". Tipo: ")
                        .append(variableType.get(cont - 1))
                        .append("\n");
                cont++;
            }
        }

        cadena.append("Número de operaciones de este MS (M): ")
                .append(numMethods)
                .append("\n");

        if(methods.isEmpty()) {
            cadena.append("\tNo hay métodos\n");
        } else {
            int cont = 1, contParams = 0, contReturns = 0;
            for(String method : methods) {
                cadena.append("\tMétodo ")
                        .append(cont).append(": ")
                        .append(method)
                        .append(" - Parámetros: ");
                if(!parameters.isEmpty()) {
                    if(parameters.get(contParams).isEmpty())
                        cadena.append("No hay parámetros");
                    else {
                        List<String> params = parameters.get(contParams);
                        int numP = params.size();
                        cadena.append(params);
                        cadena.append(" (").append(params.size());
                        if(numP == 1)
                            cadena.append(" parámetro (IPR))");
                        else
                            cadena.append(" parámetros (IPR))");
                    }
                    cadena.append("\n");
                    contParams++;
                } else {
                    cadena.append("No hay parámetros");
                }
                cadena.append("\tParámetros de salida:\n");
                if(!returns.isEmpty()) {
                    if(returns.get(contReturns).isEmpty())
                        cadena.append("\t\tNo hay parámetros de salida");
                    else {
                        List<String> ret = returns.get(contReturns);
                        int numR = ret.size();
                        cadena.append("\t\t").append(ret);
                        cadena.append(" (").append(ret.size());
                        if(numR == 1)
                            cadena.append(" parámetro (OPR))");
                        else
                            cadena.append(" parámetros (OPR))");
                    }
                    contReturns++;
                }
                cadena.append("\n");
                cont++;
            }
            cadena.append("Total de apariciones de parámetros de entrada y salida (MF): ")
                    .append(numParams + numReturns).append("\n");

            int numUP = 0, numUR = 0;
            if(!parameters.isEmpty())
                numUP = getNumUniqueParams(parameters);
            if(!returns.isEmpty())
                numUR = getNumUniqueParams(returns);
            cadena.append("Total de parámetros de entrada y salida únicos (F): ")
                    .append(numUP + numUR).append("\n");
        }

        cadena.append("Total de parámetros de entrada de este MS (FP): ").append(numParams).append("\n");
        cadena.append("Total de parámetros de salida de este MS (CP): ").append(numReturns).append("\n");

        int valPost = numMethodsPost * 4;
        int valPut = numMethodsPut * 3;
        int valDelete = numMethodsDelete * 2;
        int total = valPost + valPut + valDelete + numMethodsGet;

        cadena.append("Número de operaciones de creación: ").append(numMethodsPost).append("\n");
        cadena.append("\tPeso (POST): ").append(valPost).append("\n");
        cadena.append("Número de operaciones de actualización: ").append(numMethodsPut).append("\n");
        cadena.append("\tPeso (PUT): ").append(valPut).append("\n");
        cadena.append("Número de operaciones de eliminación: ").append(numMethodsDelete).append("\n");
        cadena.append("\tPeso (DELETE): ").append(valDelete).append("\n");
        cadena.append("Número de operaciones de lectura: ").append(numMethodsGet).append("\n");
        cadena.append("\tPeso (GET): ").append(numMethodsGet).append("\n");
        cadena.append("\tTotal (O): ").append(total).append("\n");

        cadena.append("Errores encontrados:\n");
        if(errors.isBlank()) {
            cadena.append("\tNo hay errores\n");
        } else {
            filesWErrors.add(filePath);
            cadena.append("\t")
                    .append(errors)
                    .append("\n");
        }

        genCSVStrings(info, fileName, filePath);

        return cadena.toString();
    }

    private static int getNumUniqueParams(List<List<String>> listParams) {
        Set<String> uniqueParamsSet = new HashSet<>();
        for(List<String> params : listParams) {
            uniqueParamsSet.addAll(params);
        }
        List<String> uniqueParamsList = new ArrayList<>(uniqueParamsSet);
        return uniqueParamsList.size();
    }

    public String getFilesWErrors() {
        StringBuilder cadena = new StringBuilder();
        if(filesWErrors.isEmpty())
            return "";
        else {
            for(String filePath : filesWErrors) {
                cadena.append(filePath).append("\n");
            }
            return cadena.toString();
        }
    }

    public void genCSVStrings(@NotNull ApiAndMethodsInfo info, String fileName, String filePath) {
        this.lineasCSV.clear();
        List<String> methods = info.getMethodsList();
        List<String> variables = info.getVariableList();
        List<List<String>> parameters = info.getParameters();
        List<List<String>> returns = info.getReturns();
        int numMethods = info.getNumMethods();
        int numMethodsPost = info.getNumMethodsPost();
        int numMethodsPut = info.getNumMethodsPut();
        int numMethodsDelete = info.getNumMethodsDelete();
        int numMethodsGet = info.getNumMethodsGet();
        int numParams = info.getNumParams();
        int numReturns = info.getNumReturns();
        String className = info.getClassName();
        String apiName = info.getApiName();

        lineasCSV.add(new String[]{"Nombre del archivo:", fileName, "", "", "", "", ""});
        lineasCSV.add(new String[]{"", "", "", "", "", "", ""});
        lineasCSV.add(new String[]{"Datos del archivo", "", "", "", "", "", ""});
        lineasCSV.add(new String[]{"Ruta del archivo:", filePath, "", "", "", "", ""});
        lineasCSV.add(new String[]{"Nombre de clase:", className, "", "", "", "", ""});

        if(apiName.isBlank() || apiName.isEmpty()) {
            lineasCSV.add(new String[]{"API:", "No aplica", "", "", "", "", ""});
        } else {
            lineasCSV.add(new String[]{"API:", apiName, "", "", "", "", ""});
        }
        lineasCSV.add(new String[]{"Parámetros globales encontrados:", "", "", "", "", "", ""});
        if(variables.isEmpty()) {
            lineasCSV.add(new String[]{"", "No hay parámetros globales", "", "", "", "", ""});
        } else {
            int cont = 1;
            for(String variable : variables) {
                lineasCSV.add(new String[]{"", String.valueOf(cont), variable, "", "", "", ""});
                cont++;
            }
        }
        lineasCSV.add(new String[]{"Operaciones totales en este servicio (M):",
                String.valueOf(numMethods), "", "", "", "", ""});
        lineasCSV.add(new String[]{"Operaciones encontradas:", "", "", "", "", "", ""});
        if(methods.isEmpty()) {
            lineasCSV.add(new String[]{"", "No hay operaciones", "", "", "", "", ""});
        } else {
            lineasCSV.add(new String[]{"", "", "Operación", "Parámetros de entrada",
                    "(IPR) No. de parámetros de entrada", "Parámetros de salida",
                    "(OPR) No. de parámetros de salida"});
            int cont = 1, contParams = 0, contReturns = 0;
            for(String method : methods) {
                String[] cadena = new String[7];
                cadena[0] = "";
                cadena[1] = String.valueOf(cont);
                cadena[2] = method;
                if(!parameters.isEmpty()) {
                    if(parameters.get(contParams).isEmpty()) {
                        cadena[3] = "No hay parámetros de entrada";
                        cadena[4] = "0";
                    } else {
                        List<String> params = parameters.get(contParams);
                        int numP = params.size();
                        StringBuilder cadenaParams = new StringBuilder();
                        for(String p : params) {
                            String delCom = p.replace(",", "'");
                            cadenaParams.append(delCom).append("  ");
                        }
                        cadena[3] = cadenaParams.toString();
                        cadena[4] = String.valueOf(numP);
                    }
                    contParams++;
                } else {
                    cadena[3] = "No hay parámetros de entrada";
                    cadena[4] = "0";
                }
                if(!returns.isEmpty()) {
                    if(returns.get(contReturns).isEmpty()){
                        cadena[5] = "No hay parámetros de salida";
                        cadena[6] = "0";
                    } else {
                        List<String> ret = returns.get(contReturns);
                        int numR = ret.size();
                        StringBuilder cadenaRet = new StringBuilder();
                        for(String r : ret) {
                            String delCom = r.replace(",", "'");
                            cadenaRet.append(delCom).append("  ");
                        }
                        cadena[5] = cadenaRet.toString();
                        cadena[6] = String.valueOf(numR);
                    }
                    contReturns++;
                }
                lineasCSV.add(cadena);
                cont++;
            }

            lineasCSV.add(new String[]{"(MF) Total de apariciones de parámetros de entrada y salida:",
                    String.valueOf(numParams), "", "", "", "", ""});

            int numUP = 0, numUR = 0;
            if(!parameters.isEmpty())
                numUP = getNumUniqueParams(parameters);
            if(!returns.isEmpty())
                numUR = getNumUniqueParams(returns);
            lineasCSV.add(new String[]{"(F) Total de parámetros de entrada y salida únicos: ",
                    String.valueOf((numUP + numUR)), "", "", "", "", ""});


        }
        lineasCSV.add(new String[]{"(FP) Total de parámetros de entrada de este MS:",
                String.valueOf(numParams), "", "", "", "", ""});
        lineasCSV.add(new String[]{"(CP) Total de parámetros de salida de este MS:",
                String.valueOf(numReturns), "", "", "", "", ""});
        lineasCSV.add(new String[]{"Peso de operaciones:", "", "", "", "", "", ""});
        lineasCSV.add(new String[]{"", "", "No. de operaciones", "Peso", "", "", ""});

        int valPost = numMethodsPost * 4;
        int valPut = numMethodsPut * 3;
        int valDelete = numMethodsDelete * 2;
        int total = valPost + valPut + valDelete + numMethodsGet;

        lineasCSV.add(new String[]{"", "POST", String.valueOf(numMethodsPost),
                String.valueOf(valPost), "", "", ""});
        lineasCSV.add(new String[]{"", "PUT", String.valueOf(numMethodsPut),
                String.valueOf(valPut), "", "", ""});
        lineasCSV.add(new String[]{"", "DELETE", String.valueOf(numMethodsDelete),
                String.valueOf(valDelete), "", "", ""});
        lineasCSV.add(new String[]{"", "GET", String.valueOf(numMethodsGet),
                String.valueOf((numMethodsGet)), "", "", ""});
        lineasCSV.add(new String[]{"", "", "(O) Total", String.valueOf(total), "", "", ""});

        lineasCSV.add(new String[]{"", "", "", "", "", "", ""});
        lineasCSV.add(new String[]{"", "", "", "", "", "", ""});
    }

    public List<String[]> getLineasCSV() {
        return lineasCSV;
    }
}
