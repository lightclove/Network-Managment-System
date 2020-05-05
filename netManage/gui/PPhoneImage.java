package ceeport.netManage.gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Point2D;

import ceeport.netManage.device.impl.AccessPoint;
import ceeport.netManage.device.impl.phone.Phone;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;

public class PPhoneImage extends PImage {
	
	private PText numberOfClients;
	private PPath edge;
	private PLayer edgeLayer;
	private Phone phone;
	private AccessPoint accessPoint;
	
	public PPhoneImage(Phone phone) {
		super();
		this.phone = phone;
		this.accessPoint = phone.getAccessPoint();
		
		Image img = phone.determineImageFromState();

		int xOffset = -5;
		int yOffset, textYOffset;
		if(accessPoint.getY() < accessPoint.getParent().getY()) {
			yOffset = -75;
			textYOffset = 0;
		} else {
			yOffset = 108;
			textYOffset = -60;
		}
		
        final double iw = img.getWidth(null);
        final double ih = img.getHeight(null);
		setBounds(accessPoint.getX() + xOffset, accessPoint.getY() + yOffset, iw, ih);
	
        numberOfClients = new PText();
        numberOfClients.setX(getX() + 10);
        numberOfClients.setY(getY() + getHeight() + textYOffset);

        // paint white background of the text
        numberOfClients.setPaint(Color.white);
        addChild(numberOfClients);
        setNewImage(img);
	}
	
    public void setNewImage(final Image newImage) {
        PBounds bounds = getBounds();
        super.setImage(newImage);
        setBounds(bounds);
    }

    public void render() {
        PBounds bounds = getBounds();
		Image img = phone.determineImageFromState();
		
		// draw number of clients 
		if(phone.isShown()) {
			StringBuilder sb = new StringBuilder();
			sb.append('(').append(phone.getPhoneClients().size()).append(')');
			numberOfClients.setText(sb.toString());	
			
			// draw connection line 
			drawEdgesFromParentToChild(accessPoint.getDeviceImage(), this);
                } else {
                    numberOfClients.setText("");
                    if (edge != null) {
                        edge.reset();
                    }
                    
                }
		
		setImage(img);
		setBounds(bounds);    	
    }
    
	
    private void drawEdgesFromParentToChild(PImage parentDeviceImage, PImage childDeviceImage) {

        Point2D.Double parentBounds;
        Point2D.Double childBounds;
 
        if (edge != null) {
            edge.reset();
        } else {
            edge = new PPath();
            edgeLayer.addChild(edge);
        }
        
        parentBounds = (Point2D.Double) parentDeviceImage.getBounds().getCenter2D();
        childBounds = (Point2D.Double) childDeviceImage.getBounds().getCenter2D();
        edge.moveTo((float) parentBounds.getX(), (float) parentBounds.getY());
        edge.lineTo((float) childBounds.getX(), (float) childBounds.getY());
        edge.repaint();

    }    
    
	public PText getNumberOfClients() {
		return numberOfClients;
	}
	public void setNumberOfClients(PText numberOfClients) {
		this.numberOfClients = numberOfClients;
	}
	public PPath getEdge() {
		return edge;
	}
	public void setEdge(PPath edge) {
		this.edge = edge;
	}
	public PLayer getEdgeLayer() {
		return edgeLayer;
	}
	public void setEdgeLayer(PLayer edgeLayer) {
		this.edgeLayer = edgeLayer;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public AccessPoint getAccessPoint() {
		return accessPoint;
	}

	public void setAccessPoint(AccessPoint accessPoint) {
		this.accessPoint = accessPoint;
	}

}
