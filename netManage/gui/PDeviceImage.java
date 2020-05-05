package ceeport.netManage.gui;

import java.awt.Color;
import java.awt.Image;

import ceeport.netManage.device.IDevice;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;
import ceeport.netManage.device.impl.AccessPointController;
import ceeport.netManage.device.impl.CentralSwitch;

public class PDeviceImage extends PImage {

    private IDevice device;
    private PText deviceName;
    // the connection from this node's parent to this node
    private PPath edge;
    private PActivity activity;

    public PPath getEdge() {
        return edge;
    }

    public void setEdge(PPath edge) {
        this.edge = edge;
    }

    public PText getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(PText deviceName) {
        this.deviceName = deviceName;
    }

    public IDevice getDevice() {
        return device;
    }

    public PDeviceImage(IDevice device) {
        super();
        this.device = device;
        device.setDeviceImage(this);

        Image img = device.determineImageFromState();

        final double iw = img.getWidth(null);
        final double ih = img.getHeight(null);
        setBounds(device.getX(), device.getY(), iw, ih);
        
        //deviceName = new PText(device.getName());
        deviceName = new PText(device.getViewName());
        if (device instanceof CentralSwitch) {
            deviceName.setX(device.getX() + 15);
            deviceName.setY(device.getY() + 51);

        } else {
            deviceName.setX(device.getX());
            deviceName.setY(device.getY() + ih + 5);
        }
        
        if (device instanceof AccessPointController) {
//            deviceName.setX(device.getX() + 30);
//            deviceName.setY(device.getY() + 27);
            deviceName.setX(device.getX() + 15);
            deviceName.setY(device.getY() + 51);
        }

        // paint white background of the text
        deviceName.setPaint(Color.white);
        addChild(deviceName);
        setNewImage(img);
    }

    public void setDevice(IDevice device) {
        this.device = device;
    }
        

    public void setNewImage(final Image newImage) {
        PBounds bounds = getBounds();
        super.setImage(newImage);
        setBounds(bounds);
    }
    
    
}
