
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

    }
    
}
