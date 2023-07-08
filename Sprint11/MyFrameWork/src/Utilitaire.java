/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1762.framework;

import etu1762.framework.annotation.AnnotationUrl;
import etu1762.framework.annotation.AnnotationScop;
import etu1762.framework.annotation.Authentification;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.text.*;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ITU
 */
public class Utilitaire {

    public Utilitaire() {
    }
    
    public static String[] getDataFromURL(String url) {
        String[] urlData = url.split("/");
        String[] data = new String[urlData.length - 1];
        for (int i = 0; i < data.length; i++) {
            System.out.println("URL " + urlData[i + 1]);
            data[i] = urlData[i + 1];
        }
        return data;
    }
    
    public List<String> getAllPackages(List<String> packages, String path, String debut) {
        String concat = ".";
        if(packages == null)
            packages = new ArrayList<>();
        if(debut == null) {
            debut = "";
            concat = "";
        }

        File dossier = new File(path);
        File[] files = dossier.listFiles();
        for (File file : files) {
            if(file.isDirectory()) {
                packages.add(debut + concat + file.getName());
                System.out.println(debut + concat + file.getName());
                packages = getAllPackages(packages, file.getPath(), debut + concat + file.getName());
            }
        }

        return packages;
    }

    public String getAuthentification(Method method) {
        String ans = "*";
        if(method.isAnnotationPresent(Authentification.class)) {
            ans = method.getAnnotation(Authentification.class).required();

            System.out.println("\t> " + method.getName() + " --> " + ans);
        }

        return ans;
    }
    
    public void addMappingUrl(HashMap<String, Mapping> mappingUrls, HashMap<String, Object> classInstances, String packageName) {
        String path =  this.getClass().getClassLoader().getResource("").getPath().toString().replace("%20", " ");
        
        File pack = new File(path + packageName.replace('.', '\\'));
        File[] allClass = pack.listFiles();
        
        String[] pck = packageName.split("\\.");
        String pckName = packageName;
        if(pck.length > 0) {
            pckName = pck[pck.length - 1];
        }
        
         try {
                for(int i = 0; i < allClass.length; i++) {
                    try {
                        String className = pckName + "." + allClass[i].getName().split(".class")[0];
                        Class MyClass = Class.forName(className);
                        //System.out.println("Name : " + MyClass.getName());
        
                        Method[] listMethode = MyClass.getDeclaredMethods();

                        //ajouter dans la list si la class est annotee AnnotationScop
                        if(MyClass.isAnnotationPresent(AnnotationScop.class)) {
                            System.out.println("\t> " + MyClass.getName() + " Annotee [AnnotationScop]");
                            classInstances.put(MyClass.getName(), null);
                        }

                        for(int x = 0; x < listMethode.length; x++) {
                            if(listMethode[x].getAnnotation(AnnotationUrl.class) != null) {
                                System.out.println("\t> " + listMethode[x].getName() + " --> " + listMethode[x].getAnnotation(AnnotationUrl.class).url());
                                Mapping mapping = new Mapping(className, listMethode[x].getName(), getAuthentification(listMethode[x]));
                                mappingUrls.put(listMethode[x].getAnnotation(AnnotationUrl.class).url(), mapping);
                            }
                        }
                    } catch(Exception e) {
//                        e.printStackTrace();
                    }
                }
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
    
    public void fillMappingUrlValues(HashMap<String, Mapping> mappingUrls, HashMap<String, Object> classInstances) {
        String path =  this.getClass().getClassLoader().getResource("").getPath().toString().replace("%20", " ");
        System.out.println("Path: " + path);
        List<String> allPackage = getAllPackages(null, path, null);
         
        for(int i = 0; i < allPackage.size(); i++) {
            addMappingUrl(mappingUrls, classInstances, allPackage.get(i));
        }
    }

    public  Object castToAppropriateClass(String valueInst, Class<?> classInst) {
        System.out.println("ClassType: " + classInst.getSimpleName());
        try {
            if(classInst.getSimpleName() == "int" || classInst.getSimpleName() == "Integer") {
                return Integer.parseInt(valueInst); // try to parse the valueInst as an integer
            } else if(classInst.getSimpleName() == "double" || classInst.getSimpleName() == "Double") {
                return Double.parseDouble(valueInst); // try to parse the valueInst as a double
            } else if(classInst.getSimpleName() == "Date") { 
                return new SimpleDateFormat("yyyy-MM-dd").parse(valueInst); // try to parse the valueInst as a date
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return valueInst; // return the value as a string
    }
}
