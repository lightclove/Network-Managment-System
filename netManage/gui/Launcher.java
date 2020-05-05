
package ceeport.netManage.gui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import ceeport.netManage.util.BsoriLogger;
import ceeport.netManage.snmp.SnmpHandler;


public class Launcher {
    // For the date & time output
    private final static long curTime = System.currentTimeMillis();
    private final static Date curDate = new Date(curTime);
    private final static String curStringDate = new SimpleDateFormat("dd.MM.yyyy").format(curTime);
    private final static String curStringTime = new SimpleDateFormat("HH:mm").format(curTime);
    
    //
    private static MainData mainDataModel; 
    public static MainFrame mainView;
    private static SnmpHandler snmpHandler;
	final static BsoriLogger LOGGER = BsoriLogger.getLogger(Launcher.class);
         /**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
            
            //Application logging:
//            try {
//
//                // Добавляем ещё файл "BSORI_Net_UI_Interface_Application_Log.htm".
//                HtmlFormatter htmlformatter = new HtmlFormatter();
//                FileHandler htmlfile = new FileHandler("BSORI_Net_UI_Interface_Application_Log_"+curStringDate+"_"+curStringTime+".htm");
//                // Устанавливаем html форматирование с помощью класса HtmlFormatter.
//                htmlfile.setFormatter(htmlformatter);
//                logger.addHandler(htmlfile);
//
//            } catch (SecurityException e) {
//                logger.log(Level.SEVERE,
//                        "Не удалось создать файл лога из-за политики безопасности.",
//                        e);
//            } catch (IOException e) {
//                logger.log(Level.SEVERE,
//                        "Не удалось создать файл лога из-за ошибки ввода-вывода.",
//                        e);
//            }
            
            startApp();
	}
        
    public static void startApp() throws IOException{
        
        System.out.println(curDate + "************************ Application is starting ************************");
        
        mainDataModel = MainData.getInstance();
        mainView = new MainFrame(mainDataModel);
        snmpHandler = SnmpHandler.getInstance();
        snmpHandler.setMainData(mainDataModel);
        snmpHandler.start();
    }
    public static void stopApp() throws IOException {

        System.out.println(curDate + "************************ Application has been stopped !!!************************");
        //logger.log(Level.SEVERE,curDate + "************************ Application has been restarted !!!************************");
        System.exit(0);
        // Приложение запускается из файла run.sh при загрузке ОС (автозагрузка), файлом run.sh, который запускает приложение(jar файл) в бесконечном цикле
        // При вызове метода restartApp(), приложение закрывается, но скрипт запускает его по новой.
        // содержимое файла run.sh
     
        //        #!/bin/bash
        //        while true; do
        //	  java -jar "./BSORI_NET_UI_Management.jar"
        //        done;
    }
    
}
