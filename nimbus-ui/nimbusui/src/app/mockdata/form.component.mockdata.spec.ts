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

export const formElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "529",
        "code": "vfForm",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.Form",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "submitUrl": "",
                "b": "",
                "cssClass": "",
                "showMessages": true,
                "alias": "Form",
                "navLink": "",
                "manualValidation": false
            }
        },
        "type": {
            "collection": false,
            "nested": true,
            "name": "VPOwnerInfo.VFForm",
            "model": {
                "paramConfigIds": [
                    "531",
                    "534",
                    "535"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "529",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm",
    "type": {
        "model": {
            "params": [
                {
                    "config": {
                      "active": false,
                      "required": false,
                      "id": "531",
                      "code": "testingaccordioninform",
                      "validations": null,
                      "uiNatures": [],
                      "uiStyles": {
                          "isLink": false,
                          "isHidden": false,
                          "name": "ViewConfig.Accordion",
                          "attributes": {
                              "hidden": false,
                              "readOnly": false,
                              "submitButton": true,
                              "showName": true,
                              "pageSize": 25,
                              "browserBack": false,
                              "showAsLink": false,
                              "activeIndex": "0",
                              "cssClass": "panel-default",
                              "showExpandAll": false,
                              "showMessages": false,
                              "multiple": false,
                              "alias": "Accordion"
                          }
                      },
                      "type": {
                          "collection": false,
                          "nested": true,
                          "name": "VPOwnerInfo.TestingAccordoion",
                          "model": {
                              "paramConfigIds": [
                                  "533"
                              ]
                          }
                      }
                  },
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "531",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "533",
                                      "code": "vaHipaaTemplate1",
                                      "validations": null,
                                      "uiNatures": [],
                                      "uiStyles": {
                                          "isLink": false,
                                          "isHidden": false,
                                          "name": "ViewConfig.AccordionTab",
                                          "attributes": {
                                              "hidden": false,
                                              "readOnly": false,
                                              "submitButton": true,
                                              "showName": true,
                                              "pageSize": 25,
                                              "browserBack": false,
                                              "showAsLink": false,
                                              "cssClass": "panel-default",
                                              "editable": false,
                                              "alias": "AccordionTab",
                                              "selected": false
                                          }
                                      },
                                      "type": {
                                          "collection": false,
                                          "nested": true,
                                          "name": "VPOwnerInfo.VSOwnerInfo",
                                          "model": {
                                              "paramConfigIds": [
                                                  "503"
                                              ]
                                          }
                                      }
                                  },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "533",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "config": {
                                                      "active": false,
                                                      "required": false,
                                                      "id": "503",
                                                      "code": "vcdOwnerInfo",
                                                      "validations": null,
                                                      "uiNatures": [],
                                                      "uiStyles": {
                                                          "isLink": false,
                                                          "isHidden": false,
                                                          "name": "ViewConfig.CardDetail",
                                                          "attributes": {
                                                              "hidden": false,
                                                              "readOnly": false,
                                                              "submitButton": true,
                                                              "showName": true,
                                                              "pageSize": 25,
                                                              "browserBack": false,
                                                              "showAsLink": false,
                                                              "border": false,
                                                              "cssClass": "contentBox right-gutter bg-alternate mt-0",
                                                              "draggable": false,
                                                              "expandable": false,
                                                              "editable": false,
                                                              "modelPath": "",
                                                              "alias": "CardDetail",
                                                              "imgSrc": ""
                                                          }
                                                      },
                                                      "type": {
                                                          "collection": false,
                                                          "nested": true,
                                                          "name": "VPOwnerInfo.VCDOwnerInfo",
                                                          "model": {
                                                              "paramConfigIds": [
                                                                  "505"
                                                              ]
                                                          }
                                                      }
                                                  },
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "503",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "505",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "config": {
                                                                                      "active": false,
                                                                                      "required": false,
                                                                                      "id": "507",
                                                                                      "code": "firstName",
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
                                                                                              "cssClass": "",
                                                                                              "datePattern": "",
                                                                                              "alias": "FieldValue",
                                                                                              "applyValueStyles": true,
                                                                                              "placeholder": "",
                                                                                              "inplaceEdit": false,
                                                                                              "type": "Field",
                                                                                              "cols": "2",
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
                                                                                    "configId": "507",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/firstName",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "test",
                                                                                    "previousLeafState": "test",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "First Name---127..."
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "config": {
                                                                                      "active": false,
                                                                                      "required": false,
                                                                                      "id": "508",
                                                                                      "code": "lastName",
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
                                                                                              "cssClass": "",
                                                                                              "datePattern": "",
                                                                                              "alias": "FieldValue",
                                                                                              "applyValueStyles": false,
                                                                                              "placeholder": "",
                                                                                              "inplaceEdit": false,
                                                                                              "type": "Field",
                                                                                              "cols": "4",
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
                                                                                    "configId": "508",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/lastName",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "1",
                                                                                    "previousLeafState": "1",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Last Name"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "config": {
                                                                                      "active": false,
                                                                                      "required": false,
                                                                                      "id": "509",
                                                                                      "code": "divider2",
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
                                                                                              "cssClass": "",
                                                                                              "datePattern": "",
                                                                                              "alias": "FieldValue",
                                                                                              "applyValueStyles": false,
                                                                                              "placeholder": "",
                                                                                              "inplaceEdit": false,
                                                                                              "type": "Divider",
                                                                                              "cols": "4",
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
                                                                                    "configId": "509",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/divider2",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "config": {
                                                                                      "active": false,
                                                                                      "required": false,
                                                                                      "id": "510",
                                                                                      "code": "addressGroup",
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
                                                                                              "cols": "1"
                                                                                          }
                                                                                      },
                                                                                      "type": {
                                                                                          "collection": false,
                                                                                          "nested": true,
                                                                                          "name": "VPOwnerInfo.AddressGroup",
                                                                                          "model": {
                                                                                              "paramConfigIds": [
                                                                                                  "512",
                                                                                                  "513",
                                                                                                  "514",
                                                                                                  "515"
                                                                                              ]
                                                                                          }
                                                                                      }
                                                                                  },
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "510",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                                    "type": {
                                                                                        "model": {
                                                                                            "params": [
                                                                                                {
                                                                                                    "config": {
                                                                                                      "active": false,
                                                                                                      "required": false,
                                                                                                      "id": "512",
                                                                                                      "code": "address",
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
                                                                                                              "showName": false,
                                                                                                              "pageSize": 25,
                                                                                                              "browserBack": false,
                                                                                                              "showAsLink": false,
                                                                                                              "inplaceEditType": "",
                                                                                                              "cssClass": "",
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
                                                                                                    "configId": "512",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/addressGroup/address",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "",
                                                                                                    "previousLeafState": "",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Address"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "config": {
                                                                                                      "active": false,
                                                                                                      "required": false,
                                                                                                      "id": "513",
                                                                                                      "code": "city",
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
                                                                                                              "showName": false,
                                                                                                              "pageSize": 25,
                                                                                                              "browserBack": false,
                                                                                                              "showAsLink": false,
                                                                                                              "inplaceEditType": "",
                                                                                                              "cssClass": "",
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
                                                                                                    "configId": "513",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/addressGroup/city",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "",
                                                                                                    "previousLeafState": "",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "City"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "config": {
                                                                                                      "active": false,
                                                                                                      "required": false,
                                                                                                      "id": "514",
                                                                                                      "code": "state",
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
                                                                                                              "showName": false,
                                                                                                              "pageSize": 25,
                                                                                                              "browserBack": false,
                                                                                                              "showAsLink": false,
                                                                                                              "inplaceEditType": "",
                                                                                                              "cssClass": "",
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
                                                                                                    "configId": "514",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/addressGroup/state",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "",
                                                                                                    "previousLeafState": "",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "State"
                                                                                                        }
                                                                                                    ],
                                                                                                    "elemLabels": {}
                                                                                                },
                                                                                                {
                                                                                                    "config": {
                                                                                                      "active": false,
                                                                                                      "required": false,
                                                                                                      "id": "515",
                                                                                                      "code": "zip",
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
                                                                                                              "showName": false,
                                                                                                              "pageSize": 25,
                                                                                                              "browserBack": false,
                                                                                                              "showAsLink": false,
                                                                                                              "inplaceEditType": "",
                                                                                                              "cssClass": "",
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
                                                                                                    "configId": "515",
                                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
                                                                                                    "type": {
                                                                                                        "nested": false,
                                                                                                        "name": "string",
                                                                                                        "collection": false
                                                                                                    },
                                                                                                    "leafState": "",
                                                                                                    "previousLeafState": "",
                                                                                                    "message": [],
                                                                                                    "values": [],
                                                                                                    "labels": [
                                                                                                        {
                                                                                                            "locale": "en-US",
                                                                                                            "text": "Zip"
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
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Address Group"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "config": {
                                                                                      "active": false,
                                                                                      "required": false,
                                                                                      "id": "516",
                                                                                      "code": "telephone",
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
                                                                                              "cssClass": "",
                                                                                              "datePattern": "",
                                                                                              "alias": "FieldValue",
                                                                                              "applyValueStyles": false,
                                                                                              "placeholder": "",
                                                                                              "inplaceEdit": false,
                                                                                              "type": "Field",
                                                                                              "cols": "4",
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
                                                                                    "configId": "516",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/telephone",
                                                                                    "type": {
                                                                                        "nested": false,
                                                                                        "name": "string",
                                                                                        "collection": false
                                                                                    },
                                                                                    "leafState": "1231231231",
                                                                                    "previousLeafState": "1231231231",
                                                                                    "message": [],
                                                                                    "values": [],
                                                                                    "labels": [
                                                                                        {
                                                                                            "locale": "en-US",
                                                                                            "text": "Telephone"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                }
                                                                            ]
                                                                        }
                                                                    },
                                                                    "message": [],
                                                                    "values": [],
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
                                                            "text": "testing card details label 108-"
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
                                        {
                                            "locale": "en-US",
                                            "text": "Intro & Permissions"
                                        }
                                    ],
                                    "elemLabels": {}
                                }
                            ]
                        }
                    },
                    "message": [],
                    "values": [],
                    "labels": [],
                    "elemLabels": {}
                },
                {
                    "config": {
                      "active": false,
                      "required": false,
                      "id": "534",
                      "code": "headerCallSection",
                      "validations": null,
                      "uiNatures": [],
                      "uiStyles": {
                          "isLink": false,
                          "isHidden": false,
                          "name": "ViewConfig.Paragraph",
                          "attributes": {
                              "hidden": false,
                              "readOnly": false,
                              "submitButton": true,
                              "showName": true,
                              "pageSize": 25,
                              "browserBack": false,
                              "showAsLink": false,
                              "cssClass": "font-weight-bold",
                              "alias": "Paragraph"
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
                    "configId": "534",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/headerCallSection",
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
                            "text": "Hello test 1 !! Welcome to PugsAndPaws. Below is your call history."
                        }
                    ],
                    "elemLabels": {}
                },
                {
                    "config": {
                      "active": false,
                      "required": false,
                      "id": "535",
                      "code": "callSection",
                      "uiStyles": null,
                      "validations": null,
                      "uiNatures": [],
                      "type": {
                          "collection": false,
                          "nested": true,
                          "name": "VPOwnerInfo.CallSection",
                          "model": {
                              "paramConfigIds": [
                                  "537",
                                  "538",
                                  "539",
                                  "540",
                                  "541",
                                  "543"
                              ]
                          }
                      }
                  },
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "535",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "537",
                                      "code": "gridVisibility",
                                      "uiStyles": null,
                                      "validations": null,
                                      "uiNatures": [],
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
                                    "configId": "537",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/gridVisibility",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "538",
                                      "code": "showHistory",
                                      "validations": null,
                                      "uiStyles": {
                                          "isLink": false,
                                          "isHidden": false,
                                          "name": "ViewConfig.Button",
                                          "attributes": {
                                              "hidden": false,
                                              "readOnly": false,
                                              "submitButton": true,
                                              "showName": true,
                                              "pageSize": 25,
                                              "browserBack": false,
                                              "showAsLink": false,
                                              "b": "$execute",
                                              "method": "GET",
                                              "formReset": true,
                                              "type": "button",
                                              "title": "",
                                              "url": "",
                                              "printPath": "",
                                              "cssClass": "text-sm-right",
                                              "payload": "",
                                              "alias": "Button",
                                              "style": "PLAIN",
                                              "imgSrc": "",
                                              "imgType": "FA"
                                          }
                                      },
                                      "type": {
                                          "collection": false,
                                          "nested": false,
                                          "name": "string"
                                      },
                                      "uiNatures": [
                                          {
                                              "name": "ViewConfig.Hints",
                                              "attributes": {
                                                  "hidden": false,
                                                  "readOnly": false,
                                                  "submitButton": true,
                                                  "showName": true,
                                                  "pageSize": 25,
                                                  "browserBack": false,
                                                  "showAsLink": false,
                                                  "value": "Right"
                                              }
                                          }
                                      ]
                                  },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "538",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/showHistory",
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
                                            "text": "Show Call History"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                  "config": {
                                    "active": false,
                                    "required": false,
                                    "id": "539",
                                    "code": "hideHistory",
                                    "validations": null,
                                    "uiStyles": {
                                        "isLink": false,
                                        "isHidden": false,
                                        "name": "ViewConfig.Button",
                                        "attributes": {
                                            "hidden": false,
                                            "readOnly": false,
                                            "submitButton": true,
                                            "showName": true,
                                            "pageSize": 25,
                                            "browserBack": false,
                                            "showAsLink": false,
                                            "b": "$execute",
                                            "method": "GET",
                                            "formReset": true,
                                            "type": "button",
                                            "title": "",
                                            "url": "",
                                            "printPath": "",
                                            "cssClass": "text-sm-right",
                                            "payload": "",
                                            "alias": "Button",
                                            "style": "PLAIN",
                                            "imgSrc": "",
                                            "imgType": "FA"
                                        }
                                    },
                                    "type": {
                                        "collection": false,
                                        "nested": false,
                                        "name": "string"
                                    },
                                    "uiNatures": [
                                        {
                                            "name": "ViewConfig.Hints",
                                            "attributes": {
                                                "hidden": false,
                                                "readOnly": false,
                                                "submitButton": true,
                                                "showName": true,
                                                "pageSize": 25,
                                                "browserBack": false,
                                                "showAsLink": false,
                                                "value": "Right"
                                            }
                                        }
                                    ]
                                },
                                    "enabled": false,
                                    "visible": false,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "539",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/hideHistory",
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
                                            "text": "Hide Call History"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
  
                                      "active": false,
                                      "required": false,
                                      "id": "540",
                                      "code": "medications1",
                                      "validations": null,
                                      "uiNatures": [],
                                      "uiStyles": {
                                          "isLink": false,
                                          "isHidden": false,
                                          "name": "ViewConfig.InputSwitch",
                                          "attributes": {
                                              "hidden": false,
                                              "readOnly": false,
                                              "submitButton": true,
                                              "showName": true,
                                              "pageSize": 25,
                                              "browserBack": false,
                                              "showAsLink": false,
                                              "orientation": "DEFAULT",
                                              "cssClass": "col-lg-5 inline",
                                              "dataEntryField": true,
                                              "alias": "InputSwitch",
                                              "controlId": "",
                                              "postEventOnChange": true
                                          }
                                      },
                                      "type": {
                                          "collection": false,
                                          "nested": false,
                                          "name": "boolean"
                                      }
                                  },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "540",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/medications1",
                                    "type": {},
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": " "
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "541",
                                      "code": "Formelementgroup",
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
                                          "name": "VPOwnerInfo.Formelementgroup",
                                          "model": {
                                              "paramConfigIds": []
                                          }
                                      }
                                  },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "541",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/Formelementgroup",
                                    "type": {
                                        "model": {
                                            "params": []
                                        }
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "543",
                                      "code": "gridWrapper",
                                      "uiStyles": null,
                                      "validations": null,
                                      "uiNatures": [],
                                      "type": {
                                          "collection": false,
                                          "nested": true,
                                          "name": "VPOwnerInfo.CallHistoryGridWrapper",
                                          "model": {
                                              "paramConfigIds": [
                                                  "545"
                                              ]
                                          }
                                      }
                                  },
                                    "enabled": false,
                                    "visible": false,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "543",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/gridWrapper",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": false,
                                                    "visible": false,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "545",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/gridWrapper/calls",
                                                    "type": {
                                                        "model": {
                                                            "params": []
                                                        }
                                                    },
                                                    "page": {
                                                        "last": true,
                                                        "totalPages": 1,
                                                        "totalElements": 0,
                                                        "size": 0,
                                                        "number": 0,
                                                        "first": true,
                                                        "numberOfElements": 0
                                                    },
                                                    "gridData": {
                                                        "collectionParams": []
                                                    },
                                                    "message": [],
                                                    "values": [],
                                                    "labels": [
                                                        {
                                                            "locale": "en-US",
                                                            "text": "Call History"
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                }
                                            ]
                                        }
                                    },
                                    "message": [],
                                    "values": [],
                                    "labels": [],
                                    "elemLabels": {}
                                }
                            ]
                        }
                    },
                    "message": [],
                    "values": [],
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
            "text": "testing form label-83"
        }
    ],
    "elemLabels": {}
  };
  
export const formModel: any = {
    "params": [
        {
            "config": {
              "active": false,
              "required": false,
              "id": "531",
              "code": "testingaccordioninform",
              "validations": null,
              "uiNatures": [],
              "uiStyles": {
                  "isLink": false,
                  "isHidden": false,
                  "name": "ViewConfig.Accordion",
                  "attributes": {
                      "hidden": false,
                      "readOnly": false,
                      "submitButton": true,
                      "showName": true,
                      "pageSize": 25,
                      "browserBack": false,
                      "showAsLink": false,
                      "activeIndex": "0",
                      "cssClass": "panel-default",
                      "showExpandAll": false,
                      "showMessages": false,
                      "multiple": false,
                      "alias": "Accordion"
                  }
              },
              "type": {
                  "collection": false,
                  "nested": true,
                  "name": "VPOwnerInfo.TestingAccordoion",
                  "model": {
                      "paramConfigIds": [
                          "533"
                      ]
                  }
              }
          },
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "531",
            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform",
            "type": {
                "model": {
                    "params": [
                        {
                            "config": {
                              "active": false,
                              "required": false,
                              "id": "533",
                              "code": "vaHipaaTemplate1",
                              "validations": null,
                              "uiNatures": [],
                              "uiStyles": {
                                  "isLink": false,
                                  "isHidden": false,
                                  "name": "ViewConfig.AccordionTab",
                                  "attributes": {
                                      "hidden": false,
                                      "readOnly": false,
                                      "submitButton": true,
                                      "showName": true,
                                      "pageSize": 25,
                                      "browserBack": false,
                                      "showAsLink": false,
                                      "cssClass": "panel-default",
                                      "editable": false,
                                      "alias": "AccordionTab",
                                      "selected": false
                                  }
                              },
                              "type": {
                                  "collection": false,
                                  "nested": true,
                                  "name": "VPOwnerInfo.VSOwnerInfo",
                                  "model": {
                                      "paramConfigIds": [
                                          "503"
                                      ]
                                  }
                              }
                          },
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "533",
                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1",
                            "type": {
                                "model": {
                                    "params": [
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "503",
                                              "code": "vcdOwnerInfo",
                                              "validations": null,
                                              "uiNatures": [],
                                              "uiStyles": {
                                                  "isLink": false,
                                                  "isHidden": false,
                                                  "name": "ViewConfig.CardDetail",
                                                  "attributes": {
                                                      "hidden": false,
                                                      "readOnly": false,
                                                      "submitButton": true,
                                                      "showName": true,
                                                      "pageSize": 25,
                                                      "browserBack": false,
                                                      "showAsLink": false,
                                                      "border": false,
                                                      "cssClass": "contentBox right-gutter bg-alternate mt-0",
                                                      "draggable": false,
                                                      "expandable": false,
                                                      "editable": false,
                                                      "modelPath": "",
                                                      "alias": "CardDetail",
                                                      "imgSrc": ""
                                                  }
                                              },
                                              "type": {
                                                  "collection": false,
                                                  "nested": true,
                                                  "name": "VPOwnerInfo.VCDOwnerInfo",
                                                  "model": {
                                                      "paramConfigIds": [
                                                          "505"
                                                      ]
                                                  }
                                              }
                                          },
                                            "enabled": true,
                                            "visible": true,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "503",
                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo",
                                            "type": {
                                                "model": {
                                                    "params": [
                                                        {
                                                            "config": {
                                                              "active": false,
                                                              "required": false,
                                                              "id": "505",
                                                              "code": "vcdbOwner",
                                                              "validations": null,
                                                              "uiNatures": [],
                                                              "uiStyles": {
                                                                  "isLink": false,
                                                                  "isHidden": false,
                                                                  "name": "ViewConfig.CardDetail.Body",
                                                                  "attributes": {
                                                                      "hidden": false,
                                                                      "readOnly": false,
                                                                      "submitButton": true,
                                                                      "showName": true,
                                                                      "pageSize": 25,
                                                                      "browserBack": false,
                                                                      "showAsLink": false,
                                                                      "cssClass": "",
                                                                      "alias": "CardDetailsBody"
                                                                  }
                                                              },
                                                              "type": {
                                                                  "collection": false,
                                                                  "nested": true,
                                                                  "name": "VPOwnerInfo.VCDBOwner",
                                                                  "model": {
                                                                      "paramConfigIds": [
                                                                          "507",
                                                                          "508",
                                                                          "509",
                                                                          "510",
                                                                          "516"
                                                                      ]
                                                                  }
                                                              }
                                                          },
                                                            "enabled": true,
                                                            "visible": true,
                                                            "activeValidationGroups": [],
                                                            "collectionParams": [],
                                                            "configId": "505",
                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner",
                                                            "type": {
                                                                "model": {
                                                                    "params": [
                                                                        {
                                                                            "config": {
                                                                              "active": false,
                                                                              "required": false,
                                                                              "id": "507",
                                                                              "code": "firstName",
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
                                                                                      "cssClass": "",
                                                                                      "datePattern": "",
                                                                                      "alias": "FieldValue",
                                                                                      "applyValueStyles": true,
                                                                                      "placeholder": "",
                                                                                      "inplaceEdit": false,
                                                                                      "type": "Field",
                                                                                      "cols": "2",
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
                                                                            "configId": "507",
                                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/firstName",
                                                                            "type": {
                                                                                "nested": false,
                                                                                "name": "string",
                                                                                "collection": false
                                                                            },
                                                                            "leafState": "test",
                                                                            "previousLeafState": "test",
                                                                            "message": [],
                                                                            "values": [],
                                                                            "labels": [
                                                                                {
                                                                                    "locale": "en-US",
                                                                                    "text": "First Name---127..."
                                                                                }
                                                                            ],
                                                                            "elemLabels": {}
                                                                        },
                                                                        {
                                                                            "config": {
                                                                              "active": false,
                                                                              "required": false,
                                                                              "id": "508",
                                                                              "code": "lastName",
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
                                                                                      "cssClass": "",
                                                                                      "datePattern": "",
                                                                                      "alias": "FieldValue",
                                                                                      "applyValueStyles": false,
                                                                                      "placeholder": "",
                                                                                      "inplaceEdit": false,
                                                                                      "type": "Field",
                                                                                      "cols": "4",
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
                                                                            "configId": "508",
                                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/lastName",
                                                                            "type": {
                                                                                "nested": false,
                                                                                "name": "string",
                                                                                "collection": false
                                                                            },
                                                                            "leafState": "1",
                                                                            "previousLeafState": "1",
                                                                            "message": [],
                                                                            "values": [],
                                                                            "labels": [
                                                                                {
                                                                                    "locale": "en-US",
                                                                                    "text": "Last Name"
                                                                                }
                                                                            ],
                                                                            "elemLabels": {}
                                                                        },
                                                                        {
                                                                            "config": {
                                                                              "active": false,
                                                                              "required": false,
                                                                              "id": "509",
                                                                              "code": "divider2",
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
                                                                                      "cssClass": "",
                                                                                      "datePattern": "",
                                                                                      "alias": "FieldValue",
                                                                                      "applyValueStyles": false,
                                                                                      "placeholder": "",
                                                                                      "inplaceEdit": false,
                                                                                      "type": "Divider",
                                                                                      "cols": "4",
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
                                                                            "configId": "509",
                                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/divider2",
                                                                            "type": {
                                                                                "nested": false,
                                                                                "name": "string",
                                                                                "collection": false
                                                                            },
                                                                            "message": [],
                                                                            "values": [],
                                                                            "labels": [],
                                                                            "elemLabels": {}
                                                                        },
                                                                        {
                                                                            "config": {
                                                                              "active": false,
                                                                              "required": false,
                                                                              "id": "510",
                                                                              "code": "addressGroup",
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
                                                                                      "cols": "1"
                                                                                  }
                                                                              },
                                                                              "type": {
                                                                                  "collection": false,
                                                                                  "nested": true,
                                                                                  "name": "VPOwnerInfo.AddressGroup",
                                                                                  "model": {
                                                                                      "paramConfigIds": [
                                                                                          "512",
                                                                                          "513",
                                                                                          "514",
                                                                                          "515"
                                                                                      ]
                                                                                  }
                                                                              }
                                                                          },
                                                                            "enabled": true,
                                                                            "visible": true,
                                                                            "activeValidationGroups": [],
                                                                            "collectionParams": [],
                                                                            "configId": "510",
                                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                            "type": {
                                                                                "model": {
                                                                                    "params": [
                                                                                        {
                                                                                            "config": {
                                                                                              "active": false,
                                                                                              "required": false,
                                                                                              "id": "512",
                                                                                              "code": "address",
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
                                                                                                      "showName": false,
                                                                                                      "pageSize": 25,
                                                                                                      "browserBack": false,
                                                                                                      "showAsLink": false,
                                                                                                      "inplaceEditType": "",
                                                                                                      "cssClass": "",
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
                                                                                            "configId": "512",
                                                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/addressGroup/address",
                                                                                            "type": {
                                                                                                "nested": false,
                                                                                                "name": "string",
                                                                                                "collection": false
                                                                                            },
                                                                                            "leafState": "",
                                                                                            "previousLeafState": "",
                                                                                            "message": [],
                                                                                            "values": [],
                                                                                            "labels": [
                                                                                                {
                                                                                                    "locale": "en-US",
                                                                                                    "text": "Address"
                                                                                                }
                                                                                            ],
                                                                                            "elemLabels": {}
                                                                                        },
                                                                                        {
                                                                                            "config": {
                                                                                              "active": false,
                                                                                              "required": false,
                                                                                              "id": "513",
                                                                                              "code": "city",
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
                                                                                                      "showName": false,
                                                                                                      "pageSize": 25,
                                                                                                      "browserBack": false,
                                                                                                      "showAsLink": false,
                                                                                                      "inplaceEditType": "",
                                                                                                      "cssClass": "",
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
                                                                                            "configId": "513",
                                                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/addressGroup/city",
                                                                                            "type": {
                                                                                                "nested": false,
                                                                                                "name": "string",
                                                                                                "collection": false
                                                                                            },
                                                                                            "leafState": "",
                                                                                            "previousLeafState": "",
                                                                                            "message": [],
                                                                                            "values": [],
                                                                                            "labels": [
                                                                                                {
                                                                                                    "locale": "en-US",
                                                                                                    "text": "City"
                                                                                                }
                                                                                            ],
                                                                                            "elemLabels": {}
                                                                                        },
                                                                                        {
                                                                                            "config": {
                                                                                              "active": false,
                                                                                              "required": false,
                                                                                              "id": "514",
                                                                                              "code": "state",
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
                                                                                                      "showName": false,
                                                                                                      "pageSize": 25,
                                                                                                      "browserBack": false,
                                                                                                      "showAsLink": false,
                                                                                                      "inplaceEditType": "",
                                                                                                      "cssClass": "",
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
                                                                                            "configId": "514",
                                                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/addressGroup/state",
                                                                                            "type": {
                                                                                                "nested": false,
                                                                                                "name": "string",
                                                                                                "collection": false
                                                                                            },
                                                                                            "leafState": "",
                                                                                            "previousLeafState": "",
                                                                                            "message": [],
                                                                                            "values": [],
                                                                                            "labels": [
                                                                                                {
                                                                                                    "locale": "en-US",
                                                                                                    "text": "State"
                                                                                                }
                                                                                            ],
                                                                                            "elemLabels": {}
                                                                                        },
                                                                                        {
                                                                                            "config": {
                                                                                              "active": false,
                                                                                              "required": false,
                                                                                              "id": "515",
                                                                                              "code": "zip",
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
                                                                                                      "showName": false,
                                                                                                      "pageSize": 25,
                                                                                                      "browserBack": false,
                                                                                                      "showAsLink": false,
                                                                                                      "inplaceEditType": "",
                                                                                                      "cssClass": "",
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
                                                                                            "configId": "515",
                                                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
                                                                                            "type": {
                                                                                                "nested": false,
                                                                                                "name": "string",
                                                                                                "collection": false
                                                                                            },
                                                                                            "leafState": "",
                                                                                            "previousLeafState": "",
                                                                                            "message": [],
                                                                                            "values": [],
                                                                                            "labels": [
                                                                                                {
                                                                                                    "locale": "en-US",
                                                                                                    "text": "Zip"
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
                                                                                {
                                                                                    "locale": "en-US",
                                                                                    "text": "Address Group"
                                                                                }
                                                                            ],
                                                                            "elemLabels": {}
                                                                        },
                                                                        {
                                                                            "config": {
                                                                              "active": false,
                                                                              "required": false,
                                                                              "id": "516",
                                                                              "code": "telephone",
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
                                                                                      "cssClass": "",
                                                                                      "datePattern": "",
                                                                                      "alias": "FieldValue",
                                                                                      "applyValueStyles": false,
                                                                                      "placeholder": "",
                                                                                      "inplaceEdit": false,
                                                                                      "type": "Field",
                                                                                      "cols": "4",
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
                                                                            "configId": "516",
                                                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/testingaccordioninform/vaHipaaTemplate1/vcdOwnerInfo/vcdbOwner/telephone",
                                                                            "type": {
                                                                                "nested": false,
                                                                                "name": "string",
                                                                                "collection": false
                                                                            },
                                                                            "leafState": "1231231231",
                                                                            "previousLeafState": "1231231231",
                                                                            "message": [],
                                                                            "values": [],
                                                                            "labels": [
                                                                                {
                                                                                    "locale": "en-US",
                                                                                    "text": "Telephone"
                                                                                }
                                                                            ],
                                                                            "elemLabels": {}
                                                                        }
                                                                    ]
                                                                }
                                                            },
                                                            "message": [],
                                                            "values": [],
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
                                                    "text": "testing card details label 108-"
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
                                {
                                    "locale": "en-US",
                                    "text": "Intro & Permissions"
                                }
                            ],
                            "elemLabels": {}
                        }
                    ]
                }
            },
            "message": [],
            "values": [],
            "labels": [],
            "elemLabels": {}
        },
        {
            "config": {
              "active": false,
              "required": false,
              "id": "534",
              "code": "headerCallSection",
              "validations": null,
              "uiNatures": [],
              "uiStyles": {
                  "isLink": false,
                  "isHidden": false,
                  "name": "ViewConfig.Paragraph",
                  "attributes": {
                      "hidden": false,
                      "readOnly": false,
                      "submitButton": true,
                      "showName": true,
                      "pageSize": 25,
                      "browserBack": false,
                      "showAsLink": false,
                      "cssClass": "font-weight-bold",
                      "alias": "Paragraph"
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
            "configId": "534",
            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/headerCallSection",
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
                    "text": "Hello test 1 !! Welcome to PugsAndPaws. Below is your call history."
                }
            ],
            "elemLabels": {}
        },
        {
            "config": {
              "active": false,
              "required": false,
              "id": "535",
              "code": "callSection",
              "uiStyles": null,
              "validations": null,
              "uiNatures": [],
              "type": {
                  "collection": false,
                  "nested": true,
                  "name": "VPOwnerInfo.CallSection",
                  "model": {
                      "paramConfigIds": [
                          "537",
                          "538",
                          "539",
                          "540",
                          "541",
                          "543"
                      ]
                  }
              }
          },
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "535",
            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection",
            "type": {
                "model": {
                    "params": [
                        {
                            "config": {
                              "active": false,
                              "required": false,
                              "id": "537",
                              "code": "gridVisibility",
                              "uiStyles": null,
                              "validations": null,
                              "uiNatures": [],
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
                            "configId": "537",
                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/gridVisibility",
                            "type": {
                                "nested": false,
                                "name": "string",
                                "collection": false
                            },
                            "message": [],
                            "values": [],
                            "labels": [],
                            "elemLabels": {}
                        },
                        {
                            "config": {
                              "active": false,
                              "required": false,
                              "id": "538",
                              "code": "showHistory",
                              "validations": null,
                              "uiStyles": {
                                  "isLink": false,
                                  "isHidden": false,
                                  "name": "ViewConfig.Button",
                                  "attributes": {
                                      "hidden": false,
                                      "readOnly": false,
                                      "submitButton": true,
                                      "showName": true,
                                      "pageSize": 25,
                                      "browserBack": false,
                                      "showAsLink": false,
                                      "b": "$execute",
                                      "method": "GET",
                                      "formReset": true,
                                      "type": "button",
                                      "title": "",
                                      "url": "",
                                      "printPath": "",
                                      "cssClass": "text-sm-right",
                                      "payload": "",
                                      "alias": "Button",
                                      "style": "PLAIN",
                                      "imgSrc": "",
                                      "imgType": "FA"
                                  }
                              },
                              "type": {
                                  "collection": false,
                                  "nested": false,
                                  "name": "string"
                              },
                              "uiNatures": [
                                  {
                                      "name": "ViewConfig.Hints",
                                      "attributes": {
                                          "hidden": false,
                                          "readOnly": false,
                                          "submitButton": true,
                                          "showName": true,
                                          "pageSize": 25,
                                          "browserBack": false,
                                          "showAsLink": false,
                                          "value": "Right"
                                      }
                                  }
                              ]
                          },
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "538",
                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/showHistory",
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
                                    "text": "Show Call History"
                                }
                            ],
                            "elemLabels": {}
                        },
                        {
                            "config": {
                              "active": false,
                              "required": false,
                              "id": "539",
                              "code": "hideHistory",
                              "validations": null,
                              "uiStyles": {
                                  "isLink": false,
                                  "isHidden": false,
                                  "name": "ViewConfig.Button",
                                  "attributes": {
                                      "hidden": false,
                                      "readOnly": false,
                                      "submitButton": true,
                                      "showName": true,
                                      "pageSize": 25,
                                      "browserBack": false,
                                      "showAsLink": false,
                                      "b": "$execute",
                                      "method": "GET",
                                      "formReset": true,
                                      "type": "button",
                                      "title": "",
                                      "url": "",
                                      "printPath": "",
                                      "cssClass": "text-sm-right",
                                      "payload": "",
                                      "alias": "Button",
                                      "style": "PLAIN",
                                      "imgSrc": "",
                                      "imgType": "FA"
                                  }
                              },
                              "type": {
                                  "collection": false,
                                  "nested": false,
                                  "name": "string"
                              },
                              "uiNatures": [
                                  {
                                      "name": "ViewConfig.Hints",
                                      "attributes": {
                                          "hidden": false,
                                          "readOnly": false,
                                          "submitButton": true,
                                          "showName": true,
                                          "pageSize": 25,
                                          "browserBack": false,
                                          "showAsLink": false,
                                          "value": "Right"
                                      }
                                  }
                              ]
                          },
                            "enabled": false,
                            "visible": false,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "539",
                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/hideHistory",
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
                                    "text": "Hide Call History"
                                }
                            ],
                            "elemLabels": {}
                        },
                        {
                            "config": {
                              "active": false,
                              "required": false,
                              "id": "540",
                              "code": "medications1",
                              "validations": null,
                              "uiNatures": [],
                              "uiStyles": {
                                  "isLink": false,
                                  "isHidden": false,
                                  "name": "ViewConfig.InputSwitch",
                                  "attributes": {
                                      "hidden": false,
                                      "readOnly": false,
                                      "submitButton": true,
                                      "showName": true,
                                      "pageSize": 25,
                                      "browserBack": false,
                                      "showAsLink": false,
                                      "orientation": "DEFAULT",
                                      "cssClass": "col-lg-5 inline",
                                      "dataEntryField": true,
                                      "alias": "InputSwitch",
                                      "controlId": "",
                                      "postEventOnChange": true
                                  }
                              },
                              "type": {
                                  "collection": false,
                                  "nested": false,
                                  "name": "boolean"
                              }
                          },
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "540",
                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/medications1",
                            "type": {},
                            "message": [],
                            "values": [],
                            "labels": [
                                {
                                    "locale": "en-US",
                                    "text": " "
                                }
                            ],
                            "elemLabels": {}
                        },
                        {
                            "config": {
                              "active": false,
                              "required": false,
                              "id": "541",
                              "code": "Formelementgroup",
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
                                  "name": "VPOwnerInfo.Formelementgroup",
                                  "model": {
                                      "paramConfigIds": []
                                  }
                              }
                          },
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "541",
                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/Formelementgroup",
                            "type": {
                                "model": {
                                    "params": []
                                }
                            },
                            "message": [],
                            "values": [],
                            "labels": [],
                            "elemLabels": {}
                        },
                        {
                            "config": {
                              "active": false,
                              "required": false,
                              "id": "543",
                              "code": "gridWrapper",
                              "uiStyles": null,
                              "validations": null,
                              "uiNatures": [],
                              "type": {
                                  "collection": false,
                                  "nested": true,
                                  "name": "VPOwnerInfo.CallHistoryGridWrapper",
                                  "model": {
                                      "paramConfigIds": [
                                          "545"
                                      ]
                                  }
                              }
                          },
                            "enabled": false,
                            "visible": false,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "543",
                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/gridWrapper",
                            "type": {
                                "model": {
                                    "params": [
                                        {
                                            "config": {
                                              "active": false,
                                              "required": false,
                                              "id": "545",
                                              "code": "calls",
                                              "validations": null,
                                              "uiNatures": [],
                                              "uiStyles": {
                                                  "isLink": false,
                                                  "isHidden": false,
                                                  "name": "ViewConfig.Grid",
                                                  "attributes": {
                                                      "hidden": false,
                                                      "readOnly": false,
                                                      "submitButton": true,
                                                      "showName": true,
                                                      "pageSize": 5,
                                                      "browserBack": false,
                                                      "showAsLink": false,
                                                      "headerCheckboxToggleAllPages": false,
                                                      "rowSelection": false,
                                                      "postButtonUrl": "",
                                                      "pagination": true,
                                                      "dataEntryField": true,
                                                      "postButtonTargetPath": "",
                                                      "postButtonUri": "",
                                                      "expandableRows": false,
                                                      "showHeader": true,
                                                      "postEventOnChange": false,
                                                      "lazyLoad": false,
                                                      "url": "",
                                                      "dataKey": "id",
                                                      "cssClass": "",
                                                      "clearAllFilters": false,
                                                      "postButtonLabel": "",
                                                      "alias": "Grid",
                                                      "onLoad": true,
                                                      "postButtonAlias": "",
                                                      "isTransient": true,
                                                      "postButton": false,
                                                      "export": false
                                                  }
                                              },
                                              "type": {
                                                  "collection": true,
                                                  "nested": true,
                                                  "name": "ArrayList",
                                                  "collectionType": "list",
                                                  "model": {
                                                      "paramConfigIds": []
                                                  },
                                                  "elementConfig": {
                                                      "id": "548",
                                                      "type": {
                                                          "collection": false,
                                                          "nested": true,
                                                          "name": "CallLineItem",
                                                          "model": {
                                                              "paramConfigIds": [
                                                                  "550",
                                                                  "551",
                                                                  "552"
                                                              ]
                                                          }
                                                      }
                                                  }
                                              }
                                          },
                                            "enabled": false,
                                            "visible": false,
                                            "activeValidationGroups": [],
                                            "collectionParams": [],
                                            "configId": "545",
                                            "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/gridWrapper/calls",
                                            "type": {
                                                "model": {
                                                    "params": []
                                                }
                                            },
                                            "page": {
                                                "last": true,
                                                "totalPages": 1,
                                                "totalElements": 0,
                                                "size": 0,
                                                "number": 0,
                                                "first": true,
                                                "numberOfElements": 0
                                            },
                                            "gridData": {
                                                "collectionParams": []
                                            },
                                            "message": [],
                                            "values": [],
                                            "labels": [
                                                {
                                                    "locale": "en-US",
                                                    "text": "Call History"
                                                }
                                            ],
                                            "elemLabels": {}
                                        }
                                    ]
                                }
                            },
                            "message": [],
                            "values": [],
                            "labels": [],
                            "elemLabels": {}
                        }
                    ]
                }
            },
            "message": [],
            "values": [],
            "labels": [],
            "elemLabels": {}
        }
    ]
  };

  export const textboxnotnullmodel: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "544",
        "code": "question1notnull",
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
                "cssClass": "",
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
    "configId": "544",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/question1notnull",
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
            "text": "Question 1 notnull"
        }
    ],
    "elemLabels": {}
  };
  
  export const textboxnotnullelement:any = {
    "config": {
        "active": false,
        "required": false,
        "id": "544",
        "code": "question1notnull",
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
                "cssClass": "",
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
    "configId": "544",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsHistory/vfForm/callSection/question1notnull",
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
            "text": "Question 1 notnull"
        }
    ],
    "elemLabels": {}
  };