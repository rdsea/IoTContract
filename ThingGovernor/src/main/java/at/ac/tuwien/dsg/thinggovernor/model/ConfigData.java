/*
 * Copyright 2018 Copyright Peter Klein, Hong-Linh Truong.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
