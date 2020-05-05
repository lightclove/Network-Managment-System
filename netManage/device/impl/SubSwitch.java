package ceeport.netManage.device.impl;

import java.awt.Image;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

import ceeport.netManage.device.state.DeviceState;
import ceeport.netManage.device.IDevice;

public class SubSwitch extends IDevice {

    private final static Image IMG_SUB_SWTICH_BROKEN_PNG = new ImageIcon("./img/subswitch_off2.png").getImage();
    private final static Image IMG_SUB_SWTICH_OFF_PNG = new ImageIcon("./img/subswitch_off.png").getImage();
    private final static Image IMG_SUB_SWTICH_ON_PNG = new ImageIcon("./img/subswitch_on.png").getImage();

    public SubSwitch(Integer id, String name, int x, int y, DeviceState state,
            String serialNumber, String ipAddress, Date timeActive,
            List<IDevice> children, String room) {
        super(id, name, name, x, y, state, serialNumber, ipAddress, timeActive, children,
                room);
    }

    public Image determineImageFromState() {
        switch (state) {
            case ON:
                return IMG_SUB_SWTICH_ON_PNG;
            case OFF:
                return IMG_SUB_SWTICH_OFF_PNG;
            default:
                return IMG_SUB_SWTICH_BROKEN_PNG;
        }
    }

}
