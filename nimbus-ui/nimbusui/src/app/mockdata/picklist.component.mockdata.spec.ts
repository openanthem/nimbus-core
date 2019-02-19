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

export const pickListElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "785",
        "code": "selected",
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.PickListSelected",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "alias": "PickListSelected",
                "postEventOnChange": true
            }
        },
        "type": {
            "collection": false,
            "nested": false,
            "name": "array-string"
        },
        "validation": {
            "constraints": [
                {
                    "name": "NotNull",
                    "attribute": {
                        "message": "Field is required.",
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
    "configId": "785",
    "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/category/selected",
    "type": {},
    "message": [],
    "values": [
        {
            "code": "Bobtail",
            "label": "American Bobtail"
        },
        {
            "code": "Curl",
            "label": "American Curl"
        },
        {
            "code": "White",
            "label": "American SnowWhite"
        },
        {
            "code": "Sporting",
            "label": "Sporting Group"
        },
        {
            "code": "Hound",
            "label": "Hound Group"
        },
        {
            "code": "Service",
            "label": "Service Group"
        },
        {
            "code": "Hound2",
            "label": "Hound Group"
        },
        {
            "code": "Hound3",
            "label": "Hound Group"
        },
        {
            "code": "Hound4",
            "label": "Hound Group"
        },
        {
            "code": "Hound5",
            "label": "Hound Group"
        },
        {
            "code": "Hound6",
            "label": "Hound Group"
        },
        {
            "code": "Hound7",
            "label": "Hound Group"
        },
        {
            "code": "Hound8",
            "label": "Hound Group"
        },
        {
            "code": "Hound9",
            "label": "Hound Group"
        },
        {
            "code": "Hound10",
            "label": "Hound Group"
        },
        {
            "code": "Hound11",
            "label": "Hound Group"
        },
        {
            "code": "Hound12",
            "label": "Hound Group"
        },
        {
            "code": "Hound13",
            "label": "Hound Group"
        },
        {
            "code": "Hound14",
            "label": "Hound Group"
        },
        {
            "code": "Hound15",
            "label": "Hound Group"
        },
        {
            "code": "Hound16",
            "label": "Hound Group"
        },
        {
            "code": "Hound17",
            "label": "Hound Group"
        },
        {
            "code": "Hound18",
            "label": "Hound Group"
        },
        {
            "code": "Hound19",
            "label": "Hound Group"
        },
        {
            "code": "Hound20",
            "label": "Hound Group"
        },
        {
            "code": "Hound21",
            "label": "Hound Group"
        },
        {
            "code": "Hound22",
            "label": "Hound Group"
        },
        {
            "code": "Hound23",
            "label": "Hound Group"
        },
        {
            "code": "Hound24",
            "label": "Hound Group"
        },
        {
            "code": "Hound25",
            "label": "Hound Group"
        },
        {
            "code": "Hound26",
            "label": "Hound Group"
        },
        {
            "code": "Hound27",
            "label": "Hound Group"
        },
        {
            "code": "Hound28",
            "label": "Hound Group"
        },
        {
            "code": "Hound29",
            "label": "Hound Group"
        },
        {
            "code": "Hound30",
            "label": "Hound Group"
        },
        {
            "code": "Hound31",
            "label": "Hound Group"
        },
        {
            "code": "Hound32",
            "label": "Hound Group"
        },
        {
            "code": "Hound33",
            "label": "Hound Group"
        },
        {
            "code": "Hound34",
            "label": "Hound Group"
        },
        {
            "code": "Hound35",
            "label": "Hound Group"
        },
        {
            "code": "Hound36",
            "label": "Hound Group"
        },
        {
            "code": "Hound37",
            "label": "Hound Group"
        }
    ],
    "labels": [],
    "elemLabels": {}
  };
  
  
  export const pickListParent: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "783",
        "code": "category",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.PickList",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "showSourceControls": false,
                "help": "",
                "cssClass": "",
                "dataEntryField": true,
                "labelClass": "anthem-label",
                "alias": "PickList",
                "targetHeader": "Selected Category",
                "showTargetControls": false,
                "sourceHeader": "Available Category",
                "cols": ""
            }
        },
        "type": {
            "collection": false,
            "nested": true,
            "name": "VPAddEditPet.PicklistType",
            "model": {
                "paramConfigIds": [
                    "785"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "783",
    "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/category",
    "type": {
        "model": {
            "params": [
                {
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "785",
                    "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/category/selected",
                    "type": {},
                    "message": [],
                    "values": [
                        {
                            "code": "Bobtail",
                            "label": "American Bobtail"
                        },
                        {
                            "code": "Curl",
                            "label": "American Curl"
                        },
                        {
                            "code": "White",
                            "label": "American SnowWhite"
                        },
                        {
                            "code": "Sporting",
                            "label": "Sporting Group"
                        },
                        {
                            "code": "Hound",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Service",
                            "label": "Service Group"
                        },
                        {
                            "code": "Hound2",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound3",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound4",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound5",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound6",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound7",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound8",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound9",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound10",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound11",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound12",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound13",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound14",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound15",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound16",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound17",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound18",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound19",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound20",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound21",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound22",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound23",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound24",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound25",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound26",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound27",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound28",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound29",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound30",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound31",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound32",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound33",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound34",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound35",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound36",
                            "label": "Hound Group"
                        },
                        {
                            "code": "Hound37",
                            "label": "Hound Group"
                        }
                    ],
                    "labels": [],
                    "elemLabels": {}
                }
            ]
        }
    },
    "message": [],
    "values": [],
    "labels": [
        {
            "locale": "en-US",
            "text": "Category"
        }
    ],
    "elemLabels": {}
  };