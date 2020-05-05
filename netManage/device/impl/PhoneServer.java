package ceeport.netManage.device.impl;

import java.awt.Image;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

import ceeport.netManage.device.IDevice;
import ceeport.netManage.device.state.DeviceState;

import static ceeport.netManage.device.state.DeviceState.*;

public class PhoneServer extends IDevice {

    private final static Image IMG_PHONE_SERVER_BROKEN_PNG = new ImageIcon("./img/phone_server_off2.png").getImage();
    private final static Image IMG_PHONE_SERVER_OFF_PNG = new ImageIcon("./img/phone_server_off.png").getImage();
    private final static Image IMG_PHONE_SERVER_ON_PNG = new ImageIcon("./img/phone_server_on.png").getImage();

    public PhoneServer(Integer id, String name, int x, int y,
                       DeviceState state, String serialNumber, String ipAddress,
                       Date timeActive, List<IDevice> children, String room) {
        super(id, name, name, x, y, state, serialNumber, ipAddress, timeActive, children,
                room);
    }

    public Image determineImageFromState() {
        switch (state) {
            case ON:
                return IMG_PHONE_SERVER_ON_PNG;
            case OFF:
                return IMG_PHONE_SERVER_OFF_PNG;
            default:
                return IMG_PHONE_SERVER_BROKEN_PNG;
        }
    }

}
