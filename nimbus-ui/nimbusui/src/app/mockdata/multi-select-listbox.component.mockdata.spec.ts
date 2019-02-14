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

export const multiSelectListBoxElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "572",
        "code": "userGroups",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.MultiSelect",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "help": "",
                "cssClass": "",
                "dataEntryField": true,
                "labelClass": "anthem-label",
                "alias": "MultiSelect",
                "postEventOnChange": false,
                "cols": ""
            }
        },
        "type": {
            "collection": false,
            "nested": false,
            "name": "array-string"
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "572",
    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/userGroups",
    "type": {},
    "message": [],
    "values": [
        {
            "code": "Hospice",
            "label": "Not Clinically Appropriate"
        },
        {
            "code": "Deceased",
            "label": "Not Clinically Appropriate"
        },
        {
            "code": "Inappropriate Member Behavior",
            "label": "Not Compliant"
        },
        {
            "code": "Lost Eligibility",
            "label": "Not Clinically Appropriate"
        },
        {
            "code": "Unsuccessful Outreach",
            "label": "Unable to Contact"
        },
        {
            "code": "Refused Program",
            "label": "Progress Plateau"
        },
        {
            "code": "Referred to Another Program",
            "label": "Progress Plateau"
        }
    ],
    "labels": [],
    "elemLabels": {}
  };