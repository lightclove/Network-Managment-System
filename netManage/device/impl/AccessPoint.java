package ceeport.netManage.device.impl;

import java.awt.Image;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

import ceeport.netManage.device.IDevice;
import ceeport.netManage.device.state.DeviceMode;
import ceeport.netManage.device.state.DeviceState;
import ceeport.netManage.device.impl.phone.Phone;

import static ceeport.netManage.device.state.DeviceState.*;

public class AccessPoint extends IDevice {
    public static final int SCREEN_UPDATE_INTERVAL = 1000; // 1 sec
    public static final int DEVICE_POLL_INTERVAL = 1000; // 1 sec
    private final static Image IMG_ACCESS_POINT_BROKEN_PNG = new ImageIcon("./img/access_point_off2.png").getImage();
    private final static Image IMG_ACCESS_POINT_OFF_PNG = new ImageIcon("./img/access_point_off.png").getImage();
    private final static Image IMG_ACCESS_POINT_ON_PNG = new ImageIcon("./img/access_point_on.png").getImage();
    private final static Image IMG_ACCESS_POINT_EMOFF_PNG = new ImageIcon("./img/access_point_emoff.png").getImage(); //AP is on, but emission is off
    protected DeviceMode deviceMode = DeviceMode.FULL;
    
    private Phone phone;

	public AccessPoint(Integer id, String name, String viewName, int x, int y,
                       DeviceState state, String serialNumber, String ipAddress,
                       Date timeActive, List<IDevice> children, String room) {
            super(id, name, viewName, x, y, state, serialNumber, ipAddress, timeActive, children, room);
    }

    public Image determineImageFromState() {
        switch (state) {
            case ON:
                return IMG_ACCESS_POINT_ON_PNG;

            case OFF:
                return IMG_ACCESS_POINT_OFF_PNG;

            case EMOFF:
                return IMG_ACCESS_POINT_EMOFF_PNG;

            default:
                return IMG_ACCESS_POINT_BROKEN_PNG;
        }
    }

    /**
     * @return the deviceMode
     */
    public DeviceMode getDeviceMode() {
        return deviceMode;
    }

    /**
     * @param deviceMode the deviceMode to set
     */
    public void setDeviceMode(DeviceMode deviceMode) {
        this.deviceMode = deviceMode;
    }

	public Phone getPhone() {
		if(phone == null) {
			phone = new Phone(this);
		}
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}


}
