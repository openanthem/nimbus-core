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
export const signatureElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "930",
        "code": "q13",
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.Signature",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "cssClass": "",
                "dataEntryField": true,
                "captureType": "DEFAULT",
                "acceptLabel": "Save",
                "width": "345",
                "alias": "Signature",
                "scriptName": "testing",
                "clearLabel": "Clear",
                "type": "signature",
                "postEventOnChange": true,
                "height": "60"
            }
        },
        "type": {
            "collection": false,
            "nested": false,
            "name": "string"
        },
        "validation": {
            "constraints": [
                {
                    "name": "NotNull",
                    "attribute": {
                        "message": "The submission requires an acknowledgement signature",
                        "groups": []
                    }
                }
            ]
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "930",
    "path": "/petassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petForm/petFormBody/petAssessment_Accordion_tab/q13",
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
            "text": "13. Please provide signature:"
        }
    ],
    "elemLabels": {}
  };