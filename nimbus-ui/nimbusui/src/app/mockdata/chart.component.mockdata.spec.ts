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

export const chartMockParam: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "57258",
        "code": "question123",
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.TextBox",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "help": "",
                "xAxisLabel":"Month",
                "yAxisLabel":"Count",
                "value": "BAR",
                "cssClass": "testing-css-class",
                "dataEntryField": true,
                "labelClass": "anthem-label",
                "alias": "TextBox",
                "controlId": "",
                "type": "text",
                "postEventOnChange": false,
                "cols": ""
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
                        "message": "",
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
    "configId": "57258",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/barGraph",
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
            "text": "Question 1"
        }
    ],
    "elemLabels": {},
    "leafState": [
        {
            "dataPoints": [
                {
                    "x": "visit",
                    "y": 28.0
                },
                {
                    "x": "notes",
                    "y": 1.0
                },
                {
                    "x": "pet",
                    "y": 3.0
                },
                {
                    "x": "owner",
                    "y": 50.0
                },
                {
                    "x": "veterinarian",
                    "y": 5.0
                }
            ],
            "legend": "SEARCH action"
        },
            {
            "dataPoints": [
                {
                    "x": "visitlandingview",
                    "y": 2.0
                },
                {
                    "x": "petclinicdashboard",
                    "y": 17.0
                },
                {
                    "x": "veterinarian",
                    "y": 5.0
                },
                {
                    "x": "veterinarianview",
                    "y": 5.0
                },
                {
                    "x": "ownerview",
                    "y": 12.0
                },
                {
                    "x": "owner",
                    "y": 27.0
                },
                {
                    "x": "visit",
                    "y": 2.0
                },
                {
                    "x": "ownerlandingview",
                    "y": 15.0
                },
                {
                    "x": "home",
                    "y": 29.0
                }
            ],
            "legend": "NEW action"
        }
        ]
  };