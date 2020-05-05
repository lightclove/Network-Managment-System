package ceeport.netManage.device.impl.phone;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;

import ceeport.netManage.device.impl.AccessPoint;
import ceeport.netManage.gui.PPhoneImage;

public class Phone {

    private final static Image IMG_PHONE_PNG = new ImageIcon("./img/phone.png").getImage();
    private final static Image IMG_EMPTY_PNG = new ImageIcon("./img/empty.png").getImage();

    private AccessPoint accessPoint;
    private PPhoneImage phoneImage;

    private Collection<PhoneClient> phoneClients;

    public Phone(AccessPoint accessPoint) {
        super();
        this.accessPoint = accessPoint;
        phoneClients = new ArrayList<PhoneClient>();
        phoneImage = new PPhoneImage(this);
    }

    public Image determineImageFromState() {
        if (!isShown()) {
            return IMG_EMPTY_PNG;
        }

        return IMG_PHONE_PNG;
    }

    public Collection<PhoneClient> getPhoneClients() {
        return phoneClients;
    }

    public void setPhoneClients(Collection<PhoneClient> phoneClients) {
        this.phoneClients = phoneClients;
    }

    public void render() {
        phoneImage.render();
    }

    public boolean isShown() {
        return !phoneClients.isEmpty();
    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(AccessPoint accessPoint) {
        this.accessPoint = accessPoint;
    }

    public PPhoneImage getPhoneImage() {
        return phoneImage;
    }

    public void setPhoneImage(PPhoneImage phoneImage) {
        this.phoneImage = phoneImage;
    }


	@Override
	public String toString() {
		return "Phone [accessPoint=" + accessPoint + ", phoneImage="
				+ phoneImage + ", phoneClients=" + phoneClients + "]";
	}

	
}
