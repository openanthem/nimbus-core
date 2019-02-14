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

export const inputMaskElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "570",
        "code": "testMask",
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.InputMask",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "slotChar": "_",
                "dataEntryField": true,
                "charRegex": "[A-Za-z]",
                "alias": "InputMask",
                "postEventOnChange": false,
                "mask": "999-999-9999",
                "maskPlaceHolder": ""
            }
        },
        "type": {
            "collection": false,
            "nested": false,
            "name": "string"
        },
        "validation": {
            "constraints": [{
                "name": "Pattern",
                "attribute": {
                    "regexp": "^[2-9]\\d{2}-\\d{3}-\\d{4}$",
                    "message": "Please enter phone number in xxx-xxx-xxxx format",
                    "groups": []
                }
            }, {
                "name": "NotNull",
                "attribute": {
                    "message": "Field is required.",
                    "groups": []
                }
            }]
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "570",
    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/testMask",
    "type": {
        "nested": false,
        "name": "string",
        "collection": false
    },
    "message": [],
    "values": [],
    "labels": [{
        "locale": "en-US",
        "text": "Test Mask"
    }],
    "elemLabels": {}
}