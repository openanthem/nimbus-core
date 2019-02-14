/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

export const inputLegendElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "591",
        "code": "Formelementgroup1",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.FormElementGroup",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "cssClass": "",
                "alias": "FormElementGroup",
                "cols": "1"
            }
        },
        "type": {
            "collection": false,
            "nested": true,
            "name": "VROwnerLanding.Formelementgroup1",
            "model": {
                "paramConfigIds": [
                    "593"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "591",
    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/Formelementgroup1",
    "type": {
        "model": {
            "params": [
                {
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "593",
                    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/Formelementgroup1/firstName",
                    "type": {
                        "nested": false,
                        "name": "string",
                        "collection": false
                    },
                    "message": [],
                    "values": [],
                    "labels": [
                        {
                            "locale": "en-US",
                            "text": "First Name---103"
                        }
                    ],
                    "elemLabels": {}
                }
            ]
        }
    },
    "message": [],
    "values": [],
    "labels": [                        {
        "locale": "en-US",
        "text": "First Name---103",
        "helpText": 'testing tooltip'
    }],
    "elemLabels": {}
};