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

export const tabElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "564",
        "code": "tab",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.Tab",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "alias": "Tab"
            }
        },
        "type": {
            "collection": false,
            "nested": true,
            "name": "VROwnerLanding.TabView",
            "model": {
                "paramConfigIds": ["568", "585"]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "564",
    "path": "/ownerlandingview/vpOwners/vtOwners/tab",
    "type": {
        "model": {
            "params": [{
                "enabled": true,
                "visible": true,
                "activeValidationGroups": [],
                "collectionParams": [],
                "configId": "568",
                "path": "/ownerlandingview/vpOwners/vtOwners/tab/panel1",
                "type": {
                    "model": {
                        "params": [{
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "572",
                            "path": "/ownerlandingview/vpOwners/vtOwners/tab/panel1/panel2",
                            "type": {
                                "model": {
                                    "params": [{
                                        "enabled": true,
                                        "visible": true,
                                        "activeValidationGroups": [],
                                        "collectionParams": [],
                                        "configId": "576",
                                        "path": "/ownerlandingview/vpOwners/vtOwners/tab/panel1/panel2/panel3",
                                        "type": {
                                            "model": {
                                                "params": [{
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "582",
                                                    "path": "/ownerlandingview/vpOwners/vtOwners/tab/panel1/panel2/panel3/tabContent2",
                                                    "type": {
                                                        "model": {
                                                            "params": [{
                                                                "enabled": true,
                                                                "visible": true,
                                                                "activeValidationGroups": [],
                                                                "collectionParams": [],
                                                                "configId": "581",
                                                                "path": "/ownerlandingview/vpOwners/vtOwners/tab/panel1/panel2/panel3/tabContent2/testPara2",
                                                                "type": {
                                                                    "nested": false,
                                                                    "name": "string",
                                                                    "collection": false
                                                                },
                                                                "message": [],
                                                                "values": [],
                                                                "labels": [{
                                                                    "locale": "en-US",
                                                                    "text": "This is Tab 2"
                                                                }],
                                                                "elemLabels": {}
                                                            }]
                                                        }
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [],
                                                    "elemLabels": {}
                                                }]
                                            }
                                        },
                                        "message": [],
                                        "values": [],
                                        "labels": [{
                                            "locale": "en-US",
                                            "text": "Tab Panel 2"
                                        }],
                                        "elemLabels": {}
                                    }]
                                }
                            },
                            "message": [],
                            "values": [],
                            "labels": [],
                            "elemLabels": {}
                        }]
                    }
                },
                "message": [],
                "values": [],
                "labels": [{
                    "locale": "en-US",
                    "text": "Tab Panel 1"
                }],
                "elemLabels": {}
            }, {
                "enabled": true,
                "visible": true,
                "activeValidationGroups": [],
                "collectionParams": [],
                "configId": "585",
                "path": "/ownerlandingview/vpOwners/vtOwners/tab/panel2",
                "type": {
                    "model": {
                        "params": [{
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "582",
                            "path": "/ownerlandingview/vpOwners/vtOwners/tab/panel2/tabContent2",
                            "type": {
                                "model": {
                                    "params": [{
                                        "enabled": true,
                                        "visible": true,
                                        "activeValidationGroups": [],
                                        "collectionParams": [],
                                        "configId": "581",
                                        "path": "/ownerlandingview/vpOwners/vtOwners/tab/panel2/tabContent2/testPara2",
                                        "type": {
                                            "nested": false,
                                            "name": "string",
                                            "collection": false
                                        },
                                        "message": [],
                                        "values": [],
                                        "labels": [{
                                            "locale": "en-US",
                                            "text": "This is Tab 2"
                                        }],
                                        "elemLabels": {}
                                    }]
                                }
                            },
                            "message": [],
                            "values": [],
                            "labels": [],
                            "elemLabels": {}
                        }]
                    }
                },
                "message": [],
                "values": [],
                "labels": [{
                    "locale": "en-US",
                    "text": "Tab Panel 2"
                }],
                "elemLabels": {}
            }]
        }
    },
    "message": [],
    "values": [],
    "labels": [],
    "elemLabels": {}
}

