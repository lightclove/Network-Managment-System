/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ceeport.tools;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author iGate
 */
class FileTest {

    public static void fileTest(String encoding,String filePath ,String dirPath  ) throws UnsupportedEncodingException {
        PrintWriter pw = new PrintWriter(
        //new OutputStreamWriter(System.out, "UTF-8"), true);
        //new OutputStreamWriter(System.out, "cp866"), true);
        new OutputStreamWriter(System.out, encoding), true);
        //File f = new File("FileTest.java");
        File f = new File(filePath);
        pw.println();
        pw.println("Файл \"" + f.getName() + "\" "
                + (f.exists() ? "" : "не ") + "существует");
        pw.println("Вы " + (f.canRead() ? "" : "не ") + "можете читать файл");
        pw.println("Вы " + (f.canWrite() ? "" : "не ") + "можете записывать в файл");
        pw.println("Длина файла " + f.length() + " б");
        pw.println();
        //File d = new File("C:\\Users\\iGate\\Stend_new\\Jars");
        File d = new File(dirPath);
        pw.println("Содержимое каталога:");
        ArrayList pwa = new ArrayList();
        
        if (d.exists() && d.isDirectory()) {
            String[] s = d.list();
            for (int i = 0; i < s.length; i++) {
                pw.println(s[i]);

            }
            for(Object ss: pwa){
                System.out.println(ss);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        FileTest ft = new FileTest();
        ft.fileTest("UTF-8","C:\\Users\\iGate\\Stend_new\\Jars\\delete_this.txt","C:\\Users\\iGate\\Stend_new\\Jars" );
    }
}
