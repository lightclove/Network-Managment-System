package ceeport.netManage.device.impl;

import java.awt.Image;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;

import ceeport.netManage.device.IDevice;
import ceeport.netManage.device.state.DeviceState;

public class AccessPointController extends IDevice {

    private final static Image IMG_CENTRAL_SWITCH_BROKEN_PNG = new ImageIcon("./img/central_switch_off2.png").getImage();
    private final static Image IMG_CENTRAL_SWITCH_OFF_PNG = new ImageIcon("./img/central_switch_off.png").getImage();
    private final static Image IMG_CENTRAL_SWITCH_ON_PNG = new ImageIcon("./img/central_switch_on.png").getImage();

    public AccessPointController(Integer id, String name, int x, int y, DeviceState state, String serialNumber, String ipAddress, Date timeActive, List<IDevice> children, String room) {
        super(id, name, name, x, y, state, serialNumber, ipAddress, timeActive, children, room);

    }

    public Image determineImageFromState() {
        switch (state) {
            case ON:
                return IMG_CENTRAL_SWITCH_ON_PNG;
            case OFF:
                return IMG_CENTRAL_SWITCH_OFF_PNG;
            default:
                return IMG_CENTRAL_SWITCH_BROKEN_PNG;
        }
    }

}
