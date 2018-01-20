/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thinggovernor.model;

/**
 *
 * @author Peter Klein
 */
public class ConfigData {
    
    private String salsaUrl;
    private String useBlockchain;
    private String privateKey;

    public String getSalsaUrl() {
        return salsaUrl;
    }

    public void setSalsaUrl(String salsaUrl) {
        this.salsaUrl = salsaUrl;
    }

    public String getUseBlockchain() {
        return useBlockchain;
    }

    public void setUseBlockchain(String useBlockchain) {
        this.useBlockchain = useBlockchain;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
    
}
