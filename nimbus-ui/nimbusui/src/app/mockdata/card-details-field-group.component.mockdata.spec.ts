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

export const cardDetailsFieldGroupElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "3750",
        "code": "fgCardBodyCase1",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.FieldValueGroup",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "cssClass": "",
                "alias": "FieldValueGroup",
                "cols": "5"
            }
        },
        "type": {
            "collection": false,
            "nested": true,
            "name": "VPOwnerInfo.FieldGroup_CardBodyCase1",
            "model": {
                "paramConfigIds": [
                    "3752"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "3750",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/fgCardBodyCase1",
    "type": {
        "model": {
            "params": [
                {
                  "config": {
                    "active": false,
                    "required": false,
                    "id": "3752",
                    "code": "id",
                    "validations": null,
                    "uiNatures": [],
                    "uiStyles": {
                        "isLink": false,
                        "isHidden": false,
                        "name": "ViewConfig.FieldValue",
                        "attributes": {
                            "hidden": false,
                            "readOnly": false,
                            "submitButton": true,
                            "showName": true,
                            "pageSize": 25,
                            "browserBack": false,
                            "showAsLink": false,
                            "inplaceEditType": "",
                            "cssClass": "label-left align-right",
                            "datePattern": "",
                            "alias": "FieldValue",
                            "applyValueStyles": false,
                            "placeholder": "",
                            "inplaceEdit": false,
                            "type": "Field",
                            "cols": "1",
                            "imgSrc": ""
                        }
                    },
                    "type": {
                        "collection": false,
                        "nested": false,
                        "name": "string"
                    }
                },
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "3752",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/fgCardBodyCase1/id",
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
                            "text": "Case ID"
                        }
                    ],
                    "elemLabels": {}
                }
            ]
        }
    },
    "message": [],
    "values": [],
    "labels": [
    ],
    "elemLabels": {}
  };
  
  export const newCardDetailsFieldGroupElement: any = {
    "config": {
      "active": false,
      "required": false,
      "id": "3752",
      "code": "id",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.FieldValue",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "inplaceEditType": "",
              "cssClass": "label-left align-right",
              "datePattern": "",
              "alias": "FieldValue",
              "applyValueStyles": false,
              "placeholder": "",
              "inplaceEdit": false,
              "type": "Field",
              "cols": "1",
              "imgSrc": ""
          }
      },
      "type": {
          "collection": false,
          "nested": false,
          "name": "string"
      }
  },
      "enabled": true,
      "visible": true,
      "activeValidationGroups": [],
      "collectionParams": [],
      "configId": "3752",
      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/fgCardBodyCase1/id",
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
              "text": "Case ID"
          }
      ],
      "elemLabels": {}
  };