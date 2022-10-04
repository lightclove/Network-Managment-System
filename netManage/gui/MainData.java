package ceeport.netManage.gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ceeport.netManage.device.IDevice;
import ceeport.netManage.device.impl.PhoneServer;
import ceeport.netManage.device.impl.SubSwitch;
import ceeport.netManage.device.state.DeviceState;
import ceeport.netManage.device.impl.AccessPoint;
import ceeport.netManage.device.impl.AccessPointController;
import ceeport.netManage.device.impl.CentralSwitch;
import ceeport.netManage.snmp.SnmpHandler;
import ceeport.netManage.util.BsoriLogger;

public class MainData {
	
	final static BsoriLogger LOGGER = BsoriLogger.getLogger(MainData.class);

    private static MainData instance;
    private int newNodeXPos = 857;
    private int newNodeYPos = 387;
    private Map<Integer, IDevice> devicesMap;
    
    private int initialKTDxPos;
    private int initialKTDyPos;
    public boolean kTDValidation;
        
    public boolean checkKTDcoordinates(int xPos, int yPos){
        if(xPos != initialKTDxPos){
            if(yPos != initialKTDxPos){
            	LOGGER.error("Coordinates was changed. Validation is not passed!");
                System.exit(0);
                return false;
            } 
        }
        //System.out.println("Coordinates accepted");
        return true;
    }
    //_____________________

    private MainData() throws IOException {
        devicesMap = new LinkedHashMap<Integer, IDevice>();
        initData();
    }

    public static synchronized MainData getInstance() throws IOException {
        if (instance == null) {
            instance = new MainData();
        }
        return instance;
    }

    public IDevice findDeviceById(Integer id) {
        return devicesMap.get(id);
    }

    public IDevice findDeviceByName(String name) {
        for (IDevice device : devicesMap.values()) {
            if (device.getName().equals(name)) {
                return device;
            }
        }

        return null;
    }

    public IDevice findDeviceByIp(String ipAddress) {
        for (IDevice device : devicesMap.values()) {
            if (device.getIpAddress() != null && device.getIpAddress().equals(ipAddress)) {
                return device;
            }
        }

        return null;
    }

    public Collection<IDevice> getAllDevices() {
        return devicesMap.values();
    }
    
    private void setXYOffset(int xOffset, int yOffset) {
        for (IDevice i : getAllDevices()) {
            i.setX(i.getX() + xOffset);
            i.setY(i.getY() + yOffset);
//            if (i.getId() == findDeviceById(51).getId()) {// У,ираем с картинки КТД, что,ы не смущал нас своим миганием
//                initialKTDxPos +=xOffset;
//                initialKTDyPos +=yOffset; //
//            }
        }
    }

    private void initData() throws IOException {
        // Loading ip adresses from "name-ip.properties" file of the appropriate name
        Properties p1 = new Properties();
        Properties p2 = new Properties();
        p1.load(new InputStreamReader(new FileInputStream("./name-ip.properties"), "UTF-8"));
        p2.load(new InputStreamReader(new FileInputStream("./placement.properties"), "UTF-8"));

        // add left top pane access points (BBPI 1-10)
        int xPos = 40;
        int yPos = 50;
        int xStep = 32;

        
        // Эти ББПИ относятся к А1 
        IDevice A92 = new AccessPoint(1, "A92", "1", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A92"), new Date(), null, p2.getProperty("A92"));
        devicesMap.put(A92.getId(), A92);

        xPos += xStep;
        IDevice A93 = new AccessPoint(2, "A93", "2", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A93"), new Date(), null, p2.getProperty("A93"));
        devicesMap.put(A93.getId(), A93);

        xPos += xStep;
        IDevice A94 = new AccessPoint(3, "A94", "3", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A94"), new Date(), null, p2.getProperty("A94"));
        devicesMap.put(A94.getId(), A94);

        xPos += 30;
        IDevice A98 = new AccessPoint(4, "A98", "4", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A98"), new Date(), null, p2.getProperty("A98"));
        devicesMap.put(A98.getId(), A98);

        xPos += xStep;
        IDevice A99 = new AccessPoint(5, "A99", "5", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A99"), new Date(), null, p2.getProperty("A99"));
        devicesMap.put(A99.getId(), A99);

        xPos += 30;
        xPos += xStep;
        // Эти ББПИ относятся к А2 
        IDevice A95 = new AccessPoint(6, "A95", "6", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A95"), new Date(), null, p2.getProperty("A95"));
        devicesMap.put(A95.getId(), A95);

        xPos += xStep;
        IDevice A96 = new AccessPoint(7, "A96", "7", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A96"), new Date(), null, p2.getProperty("A96"));
        devicesMap.put(A96.getId(), A96);

        xPos += xStep;
        IDevice A97 = new AccessPoint(8, "A97", "8", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A97"), new Date(), null, p2.getProperty("A97"));
        devicesMap.put(A97.getId(), A97);

        xPos += xStep;
        IDevice A100 = new AccessPoint(9, "A100", "9", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A100"), new Date(), null, p2.getProperty("A100"));
        devicesMap.put(A100.getId(), A100);

        xPos += xStep;
        IDevice A101 = new AccessPoint(10, "A101", "10", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A101"), new Date(), null, p2.getProperty("A101"));
        devicesMap.put(A101.getId(), A101);
        
        xPos += xStep;
        IDevice A102 = new AccessPoint(39, "A102", "11", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A102"), new Date(), null, p2.getProperty("A102"));
        devicesMap.put(A102.getId(), A102);

        // Эти ББПИ относятся к А4 
        xPos = 480;

        IDevice A103 = new AccessPoint(11, "A103", "12", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A103"), new Date(), null, p2.getProperty("A103"));
        devicesMap.put(A103.getId(), A103);

        xPos += xStep;
        IDevice A104 = new AccessPoint(12, "A104", "13", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A104"), new Date(), null, p2.getProperty("A104"));
        devicesMap.put(A104.getId(), A104);

        xPos += xStep;
        IDevice A105 = new AccessPoint(13, "A105", "14", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A105"), new Date(), null, p2.getProperty("A105"));
        devicesMap.put(A105.getId(), A105);
        // Эти ББПИ относятся к А5 
        xPos = 647;
        IDevice A106 = new AccessPoint(14, "A106", "15", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A106"), new Date(), null, p2.getProperty("A106"));
        devicesMap.put(A106.getId(), A106);

        xPos += xStep;
        IDevice A107 = new AccessPoint(15, "A107", "16", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A107"), new Date(), null, p2.getProperty("A107"));
        devicesMap.put(A107.getId(), A107);

        xPos += xStep;
        IDevice A108 = new AccessPoint(16, "A108", "17", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A108"), new Date(), null, p2.getProperty("A108"));
        devicesMap.put(A108.getId(), A108);

        xPos += xStep;
        IDevice A109 = new AccessPoint(17, "A109", "18", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A109"), new Date(), null, p2.getProperty("A109"));
        devicesMap.put(A109.getId(), A109);

        // Эти ББПИ относятся к А8 
        xPos = 22;
        yPos = 475;
        
        IDevice A110 = new AccessPoint(18, "A110", "19", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A110"), new Date(), null, p2.getProperty("A110"));
        devicesMap.put(A110.getId(), A110);

        xPos += xStep;
        IDevice A111 = new AccessPoint(19, "A111", "20", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A111"), new Date(), null, p2.getProperty("A111"));
        devicesMap.put(A111.getId(), A111);
       
        xPos += xStep;
        
        IDevice A112 = new AccessPoint(20, "A112", "21", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A112"), new Date(), null, p2.getProperty("A112"));
        devicesMap.put(A112.getId(), A112);
        
        xPos += xStep;
        
        IDevice A116 = new AccessPoint(21, "A116", "22", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A116"), new Date(), null, p2.getProperty("A116"));
        devicesMap.put(A116.getId(), A116);
        
        xPos += xStep;
                
        IDevice A117 = new AccessPoint(22, "A117", "23", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A117"), new Date(), null, p2.getProperty("A117"));
        devicesMap.put(A117.getId(), A117);

        xPos += xStep;
        IDevice A118 = new AccessPoint(23, "A118", "24", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A118"), new Date(), null, p2.getProperty("A118"));
        devicesMap.put(A118.getId(), A118);
         // Эти ББПИ относятся к А9 
        xPos += 15;
        xPos += xStep;
        
        IDevice A113 = new AccessPoint(24, "A113", "25", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A113"), new Date(), null, p2.getProperty("A113"));
        devicesMap.put(A113.getId(), A113);

        xPos += xStep;
        IDevice A114 = new AccessPoint(25, "A114", "26", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A114"), new Date(), null, p2.getProperty("A114"));
        devicesMap.put(A114.getId(), A114);

        xPos += xStep;
        IDevice A115 = new AccessPoint(26, "A115", "27", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A115"), new Date(), null, p2.getProperty("A115"));
        devicesMap.put(A115.getId(), A115);

        xPos += xStep;
        IDevice A119 = new AccessPoint(27, "A119", "28", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A119"), new Date(), null, p2.getProperty("A119"));
        devicesMap.put(A119.getId(), A119);

        xPos += xStep;
        IDevice A120 = new AccessPoint(28, "A120", "29", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A120"), new Date(), null, p2.getProperty("A120"));
        devicesMap.put(A120.getId(), A120);
        
        xPos += 70;
        xPos += xStep;
        
         // Эти ББПИ относятся к А90 
        xPos = 480;
        yPos = 478;
        
        IDevice A121 = new AccessPoint(29, "A121", "30", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A121"), new Date(), null, p2.getProperty("A121"));
        devicesMap.put(A121.getId(), A121);

        xPos += xStep;
        IDevice A122 = new AccessPoint(30, "A122", "31", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A122"), new Date(), null, p2.getProperty("A122"));
        devicesMap.put(A122.getId(), A122);

        xPos += xStep;

        IDevice A123 = new AccessPoint(31, "A123", "32", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A123"), new Date(), null, p2.getProperty("A123"));
        devicesMap.put(A123.getId(), A123);

        xPos += xStep;
        IDevice A127 = new AccessPoint(32, "A127", "33", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A127"), new Date(), null, p2.getProperty("A127"));
        devicesMap.put(A127.getId(), A127);

        xPos += xStep;
        IDevice A128 = new AccessPoint(33, "A128", "34", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A128"), new Date(), null, p2.getProperty("A128"));
        devicesMap.put(A128.getId(), A128);
         // Эти ББПИ относятся к А91 
        xPos += 60;
        IDevice A124 = new AccessPoint(34, "A124", "35", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A124"), new Date(), null, p2.getProperty("A124"));
        devicesMap.put(A124.getId(), A124);

        xPos += xStep;
        IDevice A125 = new AccessPoint(35, "A125", "36", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A125"), new Date(), null, p2.getProperty("A125"));
        devicesMap.put(A125.getId(), A125);

        xPos += xStep;
        IDevice A126 = new AccessPoint(36, "A126", "37", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A126"), new Date(), null, p2.getProperty("A126"));
        devicesMap.put(A126.getId(), A126);

        //xPos += 30;

        xPos += xStep;
        IDevice A129 = new AccessPoint(37, "A129", "38", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A129"), new Date(), null, p2.getProperty("A129"));
        devicesMap.put(A129.getId(), A129);

        xPos += xStep;
        IDevice A130 = new AccessPoint(38, "A130", "39", xPos, yPos,
                DeviceState.OFF, null, p1.getProperty("A130"), new Date(), null, p2.getProperty("A130"));
        devicesMap.put(A130.getId(), A130);


        // add sub switches:
        // BSKP 1
        xPos = 77;
        yPos = 177;

        List<IDevice> A1Children = new ArrayList<IDevice>();
        A1Children.add(A92);
        A1Children.add(A93);
        A1Children.add(A94);
        A1Children.add(A98);
        A1Children.add(A99);

        IDevice A1 = new SubSwitch(41, "БСКП1-А1", xPos, yPos, DeviceState.OFF, null, p1.getProperty("БСКП1-А1"), new Date(), A1Children, p1.getProperty("БСКП1-А1"));
        devicesMap.put(A1.getId(), A1);

        // BSKP 2
        xPos = 247;
        yPos = 171;

        List<IDevice> A2Children = new ArrayList<IDevice>();
        A2Children.add(A95);
        A2Children.add(A96);
        A2Children.add(A97);
        A2Children.add(A100);
        A2Children.add(A101);
        A2Children.add(A102);

        IDevice A2 = new SubSwitch(42, "БСКП2-А2", xPos, yPos, DeviceState.OFF, null, p1.getProperty("БСКП2-А2"), new Date(), A2Children, p1.getProperty("БСКП2-А2"));
        devicesMap.put(A2.getId(), A2);

        // BSKP4 
        xPos = 490;
        yPos = 171;
        
        List<IDevice> A4Children = new ArrayList<IDevice>();
        A4Children.add(A103);
        A4Children.add(A104);
        A4Children.add(A105);

        IDevice A4 = new SubSwitch(43, "БСКП4-А4", xPos, yPos, DeviceState.OFF, null, p1.getProperty("БСКП4-А4"), new Date(), A4Children, p1.getProperty("БСКП4-А4"));
        devicesMap.put(A4.getId(), A4);

        // BSKP 5
        xPos = 637;
        yPos = 181;

        List<IDevice> A5Children = new ArrayList<IDevice>();
        
        A5Children.add(A106);
        A5Children.add(A107);
        A5Children.add(A108);
        A5Children.add(A109);
        
        IDevice A5 = new SubSwitch(44, "БСКП5-А5", xPos, yPos, DeviceState.OFF, null, p1.getProperty("БСКП5-А5"), new Date(), A5Children, p1.getProperty("БСКП5-А5"));
        devicesMap.put(A5.getId(), A5);

        // BSKP 5 - A8
        xPos = 98;
        yPos = 360;
        

        List<IDevice>A8Children = new ArrayList<IDevice>();
        
        A8Children.add(A110);
        A8Children.add(A111);
        A8Children.add(A112);
        A8Children.add(A116);
        A8Children.add(A117);
        A8Children.add(A118);

        IDevice A8 = new SubSwitch(45, "БСКП8-А8", xPos, yPos, DeviceState.OFF, null, p1.getProperty("БСКП8-А8"), new Date(), A8Children, p1.getProperty("БСКП8-А8"));
        devicesMap.put(A8.getId(), A8);

        // BSKP 6 - A9
        xPos = 243;
        yPos = 369;

        List<IDevice> A9Children = new ArrayList<IDevice>();
        A9Children.add(A113);
        A9Children.add(A114);
        A9Children.add(A115);
        A9Children.add(A119);
        A9Children.add(A120);

        IDevice A9 = new SubSwitch(46, "БСКП3-А9", xPos, yPos, DeviceState.OFF, null, p1.getProperty("БСКП3-А9"), new Date(), A9Children, p2.getProperty("БСКП3-А9"));
        devicesMap.put(A9.getId(), A9);
        
        // BSKP 7 - A91
        xPos = 512;
        yPos = 369;
        
        List<IDevice> A90Children = new ArrayList<IDevice>();
        A90Children.add(A121);
        A90Children.add(A122);
        A90Children.add(A123);
        A90Children.add(A127);
        A90Children.add(A128);

        IDevice A90 = new SubSwitch(48, "БСКП6-А90", xPos, yPos, DeviceState.OFF, null, p1.getProperty("БСКП6-А90"), new Date(), A90Children, p2.getProperty("БСКП6-А90"));
        devicesMap.put(A90.getId(), A90);
        
        // BSKP 8 - A91
        xPos = 648;
        yPos = 362;

        List<IDevice> A91Children = new ArrayList<IDevice>();
        A91Children.add(A124);
        A91Children.add(A125);
        A91Children.add(A126);
        A91Children.add(A129);
        A91Children.add(A130);
       
        IDevice A91 = new SubSwitch(47, "БСКП7-А91", xPos, yPos, DeviceState.OFF, null, p1.getProperty("БСКП7-А91"), new Date(), A91Children, p2.getProperty("БСКП7-А91"));
        devicesMap.put(A91.getId(), A91);

        // KTD - A? access point controller
        xPos = 361; initialKTDxPos = xPos;
        yPos = 200; initialKTDyPos = yPos;
                
        // BSK central switch
        xPos = 361;
        yPos = 265;

        List<IDevice> BSKChildren = new ArrayList<IDevice>();

        BSKChildren.add(A1);
        BSKChildren.add(A2);
        BSKChildren.add(A4);

        BSKChildren.add(A5);
        BSKChildren.add(A8);
        BSKChildren.add(A9);
        BSKChildren.add(A91);
        BSKChildren.add(A90);

        IDevice BSK = new CentralSwitch(49, "БСК", xPos, yPos, DeviceState.OFF, null, p1.getProperty("БСК"), new Date(), BSKChildren, p2.getProperty("БСК"));
        devicesMap.put(BSK.getId(), BSK);

        // PBX BUVO phone server
        xPos = 385;
        yPos = 371;

        List<IDevice> A7Children = new ArrayList<IDevice>();
        A7Children.add(BSK);

        IDevice A7 = new PhoneServer(50, "БУВО", xPos, yPos, DeviceState.ON, null, "10.168.2.5", new Date(), A7Children, p2.getProperty("БУВО"));
        devicesMap.put(A7.getId(), A7);
        setXYOffset(40, 100);

    }

    /**
     * @return the devicesMap
     */
    public Map<Integer, IDevice> getDevicesMap() {
        return devicesMap;
    }

    /**
     * @param devicesMap the devicesMap to set
     */
    public void setDevicesMap(Map<Integer, IDevice> devicesMap) {
        this.devicesMap = devicesMap;
    }

    public static void updateDevice(IDevice device) throws Throwable {
        SnmpHandler.getInstance().pollSwitches(device);
        //SnmpHandler.getInstance().pollAccessPointController(device);
    }
}
