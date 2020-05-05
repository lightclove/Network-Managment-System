
package ceeport.tools;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Settings
 * @author t10000
 */
@XmlRootElement(namespace = "langustSpectrum")
public class Settings {
    
    private int localPort = 10000;
    private String dstAddr = "localhost";
    private int dstPort = 10001;
    private int samplesSize = 2048;
    private int accumSize = 100000;
    private int udpTimeout = 300;
    private String language = "ru";


    public int getLocalPort() {
        return localPort;
    }

    
    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    
    public String getDstAddr() {
        return dstAddr;
    }

    
    public void setDstAddr(String dstAddr) {
        this.dstAddr = dstAddr;
    }

    
    public int getDstPort() {
        return dstPort;
    }

    
    public void setDstPort(int dstPort) {
        this.dstPort = dstPort;
    }

    
    public int getSamplesSize() {
        return samplesSize;
    }

    
    public void setSamplesSize(int samplesSize) {
        this.samplesSize = samplesSize;
    }

    
    public int getAccumSize() {
        return accumSize;
    }

    
    public void setAccumSize(int accumSize) {
        this.accumSize = accumSize;
    }

    
    public int getUdpTimeout() {
        return udpTimeout;
    }

    
    public void setUdpTimeout(int udpTimeout) {
        this.udpTimeout = udpTimeout;
    }

    
    public String getLanguage() {
        return language;
    }

    
    public void setLanguage(String language) {
        this.language = language;
    }
    
}
