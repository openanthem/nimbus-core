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

export const formInput:any = {
    "config": {
        "active": false,
        "required": false,
        "id": "596",
        "code": "firstName",
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
    "configId": "596",
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
  };
  
  export const formPicklist: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "786",
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
                    "788"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "786",
    "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/category",
    "type": {
        "model": {
            "params": [
                {
                    "config": {
                      "active": false,
                      "required": false,
                      "id": "788",
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
                    "configId": "788",
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
  
  export const formTreeGrid: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "584",
        "code": "treegrid",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.TreeGrid",
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
                "alias": "TreeGrid"
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
                "id": "587",
                "type": {
                    "collection": false,
                    "nested": true,
                    "name": "PetHistoryLineItem",
                    "model": {
                      "paramConfigs": [
                          {
                              "active": false,
                              "required": false,
                              "id": "405",
                              "code": "name",
                              "validations": null,
                              "uiNatures": [],
                              "uiStyles": {
                                  "isLink": false,
                                  "isHidden": false,
                                  "name": "ViewConfig.TreeGridChild",
                                  "attributes": {
                                      "hidden": false,
                                      "readOnly": false,
                                      "submitButton": true,
                                      "showName": true,
                                      "pageSize": 25,
                                      "browserBack": false,
                                      "showAsLink": false,
                                      "rowExpander": false,
                                      "expandable": true,
                                      "applyValueStyles": false,
                                      "sortable": true,
                                      "filter": false,
                                      "filterMode": "equals",
                                      "cssClass": "",
                                      "filterValue": "",
                                      "datePattern": "",
                                      "alias": "TreeGridChild",
                                      "placeholder": "",
                                      "sortAs": "DEFAULT"
                                  }
                              },
                              "type": {
                                  "collection": false,
                                  "nested": false,
                                  "name": "string"
                              }
                          }
                      ],
                        "paramConfigIds": [
                            "405",
                            "406",
                            "407",
                            "408",
                            "412"
                        ]
                    }
                }
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "584",
    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/treegrid",
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
            "text": "Pet History Tree Grid"
        }
    ],
    "elemLabels": {
        "get": () => {}
    }
  };
  
  export const formTable: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "538",
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
                "id": "541",
                "type": {
                    "collection": false,
                    "nested": true,
                    "name": "CallLineItem",
                    "model": {
                        "paramConfigIds": [
                            "543",
                            "544",
                            "545"
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
    "configId": "538",
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
  };
  
  export const formInputSwitch:any = {
    "config": {
        "active": false,
        "required": false,
        "id": "533",
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
    "configId": "533",
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
  };
  
  export const formUpload: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "908",
        "code": "q9_b",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.FileUpload",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "metaData": [],
                "cssClass": "",
                "controlType": "FORMCONTROL",
                "dataEntryField": true,
                "multiple": true,
                "alias": "FileUpload",
                "type": ".png,.pdf",
                "cols": "",
                "url": ""
            }
        },
        "type": {
            "collection": false,
            "nested": false,
            "name": "string"
        }
    },
    "enabled": true,
    "visible": false,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "908",
    "path": "/petassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petForm/petFormBody/petAssessment_Accordion_tab/q9/q9_b",
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
            "text": "Please upload the relevant file/s"
        }
    ],
    "elemLabels": {}
  };
  
  export const formCheckBoxGrp: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "895",
        "code": "surveyLabel",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.CheckBoxGroup",
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
                "level": "0",
                "alias": "CheckBoxGroup",
                "controlId": "",
                "postEventOnChange": false,
                "cols": ""
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
    "configId": "895",
    "path": "/petassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petForm/petFormBody/petAssessment_Accordion_tab/surveyLabel",
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
            "text": "Please rate each part of your visit:"
        }
    ],
    "elemLabels": {}
  };
  
  export const formRadio: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "873",
        "code": "q1_a",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.Radio",
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
                "level": "0",
                "alias": "Radio",
                "controlId": "",
                "postEventOnChange": true,
                "cols": ""
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
    "configId": "873",
    "path": "/petassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petForm/petFormBody/petAssessment_Accordion_tab/q1/q1_a",
    "type": {
        "nested": false,
        "name": "string",
        "collection": false
    },
    "message": [],
    "values": [
        {
            "code": "Date Only",
            "label": "Date Only"
        },
        {
            "code": "Time Only",
            "label": "Time Only"
        },
        {
            "code": "Both Date and Time",
            "label": "Both Date and Time"
        }
    ],
    "labels": [
        {
            "locale": "en-US",
            "text": "1. Choose date/time format for the date of appointment"
        }
    ],
    "elemLabels": {}
  };
  
  export const formCalendar: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "766",
        "code": "dob",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.Calendar",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "readonlyInput": false,
                "dataEntryField": true,
                "labelClass": "anthem-label",
                "yearRange": "1910:2050",
                "showTime": false,
                "controlId": "",
                "yearNavigator": false,
                "type": "calendar",
                "monthNavigator": false,
                "postEventOnChange": true,
                "hourFormat": "12",
                "help": "",
                "timeOnly": false,
                "cssClass": "",
                "alias": "Calendar",
                "cols": ""
            }
        },
        "type": {
            "collection": false,
            "nested": false,
            "name": "LocalDate"
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "766",
    "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/dob",
    "type": {},
    "leafState": null,
    "message": [],
    "values": [],
    "labels": [
        {
            "locale": "en-US",
            "text": "Date of Birth"
        }
    ],
    "elemLabels": {}
  };
  
  export const formSignature: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "915",
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
                "width": "345",
                "acceptLabel": "Save",
                "alias": "Signature",
                "scriptName": "",
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
    "configId": "915",
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
  
  export const formTextArea: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "771",
        "code": "notes",
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.TextArea",
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
                "alias": "TextArea",
                "controlId": "",
                "type": "textarea",
                "rows": "5",
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
                    "name": "Max",
                    "attribute": {
                        "message": "Field does not meet min/max requirement.",
                        "value": 500,
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
    "configId": "771",
    "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/notes",
    "type": {
        "nested": false,
        "name": "string",
        "collection": false
    },
    "message": [],
    "values": [],
    "labels": [],
    "elemLabels": {}
  };
  
  export const formComboBox: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "481",
        "code": "notificationPreference",
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.ComboBox",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "help": "",
                "postButtonUrl": "",
                "cssClass": "",
                "dataEntryField": true,
                "labelClass": "anthem-label",
                "alias": "ComboBox",
                "controlId": "",
                "postEventOnChange": true,
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
    "configId": "481",
    "path": "/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/notificationPreference",
    "type": {
        "nested": false,
        "name": "string",
        "collection": false
    },
    "message": [],
    "values": [
        {
            "code": "do_not_send_notifications",
            "label": "Do Not Send Notifications"
        },
        {
            "code": "Email",
            "label": "Email"
        },
        {
            "code": "Physical_mail",
            "label": "Mail"
        },
        {
            "code": "Text",
            "label": "Text"
        }
    ],
    "labels": [
        {
            "locale": "en-US",
            "text": "Notification Preference"
        }
    ],
    "elemLabels": {}
  };
  
  export const formCheckBox: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "480",
        "code": "shouldUseNickname",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.CheckBox",
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
                "alias": "CheckBox",
                "controlId": "",
                "postEventOnChange": true,
                "cols": ""
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
    "configId": "480",
    "path": "/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/shouldUseNickname",
    "type": {},
    "message": [],
    "values": [],
    "labels": [
        {
            "locale": "en-US",
            "text": "Does user prefer nickname?"
        }
    ],
    "elemLabels": {}
  };
  
  export const formMultiSelectCard: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "589",
        "code": "testingmulticard",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.MultiSelectCard",
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
                "alias": "MultiSelectCard"
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
    "configId": "589",
    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/testingmulticard",
    "type": {
        "nested": false,
        "name": "string",
        "collection": false
    },
    "message": [],
    "values": [],
    "labels": [],
    "elemLabels": {}
  };
  
  export const formMultiSelect: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "579",
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
                "cols": "",
                "x": "y"
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
    "configId": "579",
    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/userGroups",
    "type": {},
    "message": [{ "messageArray": [{ "severity": "warn", "summary": "Warn Message", "detail": "Message is set", "life": 5000, "styleClass": "" }], "context": "TOAST", "type": "WARNING", "styleClass": "" }],
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

export const formRichText = {
    "config": {
        "id": "503",
        "code": "richTextbox",
        "uiStyles": {
            "name": "ViewConfig.RichText",
            "attributes": {
                "toolbarFeatures": [
                    "HEADER",
                    "FONT",
                    "BOLD",
                    "ITALIC",
                    "UNDERLINE",
                    "STRIKE",
                    "COLOR",
                    "BACKGROUND",
                    "SCRIPT",
                    "SIZE",
                    "BLOCKQUOTE",
                    "CODE_BLOCK",
                    "LIST",
                    "INDENT",
                    "DIRECTION",
                    "ALIGN",
                    "LINK",
                    "IMAGE",
                    "VIDEO",
                    "CLEAN"
                ],
                "formats": "",
                "cssClass": "",
                "dataEntryField": true,
                "alias": "RichText",
                "placeholder": "",
                "controlId": "",
                "postEventOnChange": true,
                "cols": ""
            }
        },
        "validations": null,
        "uiNatures": null,
        "type": {
            "nested": false,
            "name": "string",
            "collection": false
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "503",
    "path": "/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/richTextbox",
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
        "text": "Sample @RichText Component (w/ postEventOnChange = true)"
        }
    ],
    "elemLabels": {}
};