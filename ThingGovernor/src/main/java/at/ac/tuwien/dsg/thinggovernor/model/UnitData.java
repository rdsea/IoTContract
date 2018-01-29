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
public class UnitData {
    
    private String unit;
    private String serviceTemplate;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getServiceTemplate() {
        return serviceTemplate;
    }

    public void setServiceTemplate(String serviceTemplate) {
        this.serviceTemplate = serviceTemplate;
    }
    
}
