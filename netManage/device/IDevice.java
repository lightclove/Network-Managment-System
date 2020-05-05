package ceeport.netManage.device;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Image;
import java.util.Date;
import java.util.List;


import ceeport.netManage.device.state.DeviceState;
import ceeport.netManage.gui.PDeviceImage;
import ceeport.netManage.device.state.LinkState;
import ceeport.netManage.gui.MainData;
import edu.umd.cs.piccolo.nodes.PPath;
import ceeport.netManage.device.impl.PhoneServer;


public abstract class IDevice {
	
	public static final int SCREEN_UPDATE_INTERVAL = 5000;
	public static final int DEVICE_POLL_INTERVAL = 5000;
	protected Integer id;
	protected String name;
	private String viewName;
	protected int x;
	protected int y;
	protected DeviceState state;
	protected LinkState link = LinkState.ON;
	protected IDevice parent;
	protected List<IDevice> children;
	protected PDeviceImage deviceImage;
	protected String serialNumber;
	protected String ipAddress;
	protected Date timeActive;
	private String sysUpTime;

	public IDevice(Integer id, String name, String viewName, int x, int y, DeviceState state, String serialNumber, String ipAddress, Date timeActive, List<IDevice> children, String room) {
		this.id = id;
		this.name = name;
		this.viewName = viewName;
		this.x = x;
		this.y = y;
		this.state = state;
		this.serialNumber = serialNumber;
		this.ipAddress = ipAddress;
		this.timeActive = timeActive;
		
		this.children = children;
		
		if(children != null) {
			for(IDevice device: children) {
				device.setParent(this);
			}
		}
	}
	
	public List<IDevice> getChildren() {
		return children;
	}
	public void setChildren(List<IDevice> children) {
		this.children = children;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
        /**
         * @return the viewName
         */
        public String getViewName() {
            return viewName;
        }

        /**
         * @param viewName the viewName to set
         */
        public void setViewName(String viewName) {
            this.viewName = viewName;
        }
        
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public DeviceState getState() {
		return state;
	}

        public void setState(DeviceState state) {
            this.state = state;
            if (state == DeviceState.BROKEN) {
                if (children != null) {
                    for (IDevice child : getChildren()) {
                        //child.link = LinkState.BROKEN;
                        child.fireChange();
                    }
                }
            }
            fireChange();
        }
        
        public void disableIncludingAllChildren() {
            state = DeviceState.OFF;
            // = LinkState.BROKEN;
            fireChange();

            if (getChildren() != null) {
                for (IDevice child : getChildren()) {
                    child.disableIncludingAllChildren();
                }
            }
        }
        
//        public void disableIncludingAllChildren_() {
//            if (this instanceof PhoneServer) {
//                state = DeviceState.BROKEN;
//                //link = LinkState.BROKEN;
//
//                fireChange();
//            }
//            else{
//                state = DeviceState.OFF;
//                //link = LinkState.OFF;
//                fireChange();
//            }
//
//            if (getChildren() != null) {
//                for (IDevice child : getChildren()) {
//                    child.disableIncludingAllChildren_();
//                }
//            }
//        }
    
        public void disableDevice() {  
            if (this instanceof PhoneServer) {
                state = DeviceState.ON;
                fireChange();
            }
            else{
                state = DeviceState.OFF;
                //link = LinkState.OFF;
                fireChange();
            }
        }
        
        
	public void toggleState() {
		if(state != DeviceState.BROKEN) {
			if(state ==DeviceState.ON) {
				state = DeviceState.OFF;
			} else {
				state = DeviceState.ON;
			}
			setState(state);
			fireChange();
		}
	}
    
    public void determineEdgeStyleFromPing() {
        PPath edge = getDeviceImage().getEdge();
        if (edge == null) {
            return;
        }

        switch (link) {
            case ON:
                edge.setStroke(new BasicStroke(1));
                edge.setStrokePaint(Color.GREEN);
                break;

            case OFF:
                edge.setStroke(new BasicStroke(1));
                edge.setStrokePaint(Color.RED);
                break;
        }
        edge.repaint();
    }

    public abstract Image determineImageFromState();
    

	
	public IDevice getParent() {
		return parent;
	}

	public void setParent(IDevice parent) {
		this.parent = parent;
	}
	
	public PDeviceImage getDeviceImage() {
		return deviceImage;
	}

	public void setDeviceImage(PDeviceImage deviceImage) {
		this.deviceImage = deviceImage;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getTimeActive() {
		return timeActive;
	}

	public void setTimeActive(Date timeActive) {
		this.timeActive = timeActive;
	}
        
	private void fireChange() {
   		Image newImage = determineImageFromState();
   		getDeviceImage().setNewImage(newImage);
                //getDeviceImage().updateCoordinatesDeviceImage(this);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((serialNumber == null) ? 0 : serialNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IDevice other = (IDevice) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (serialNumber == null) {
			if (other.serialNumber != null)
				return false;
		} else if (!serialNumber.equals(other.serialNumber))
			return false;
		return true;
	}
	
	public void updateState() throws Throwable {
		MainData.updateDevice(this);
	}

	public String getSysUpTime() {
		return sysUpTime;
	}

	public void setSysUpTime(String sysUpTime) {
		this.sysUpTime = sysUpTime;
		fireChange();
	}

	/**
	 * @return the link
	 */
	public LinkState getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(LinkState link) {
		this.link = link;
	}


}
