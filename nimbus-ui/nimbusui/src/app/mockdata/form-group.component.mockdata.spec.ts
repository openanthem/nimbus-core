export const formGroupNmElementInputParam: any = {
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
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/question123",
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
    "elemLabels": {}
  };
  
  export const formGroupNmButtonParam: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "57265",
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
    "configId": "57265",
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
  };
  
  export const formGroupnmParagraphParam: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "57261",
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
    "configId": "57261",
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
  };
  
  export const formGroupParam: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "58336",
        "code": "vbg1",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.ButtonGroup",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "cssClass": "123text-sm-center",
                "alias": "ButtonGroup"
            }
        },
        "type": {
            "collection": false,
            "nested": true,
            "name": "VPOwnerInfo.VSPets.VBG1",
            "model": {
                "paramConfigIds": [
                    "58338",
                    "58339"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "58336",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/vbg1",
    "type": {
        "model": {
            "params": [
                {
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "58338",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/vbg1/cancel",
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
                            "text": "Cancel"
                        }
                    ],
                    "elemLabels": {}
                },
                {
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "58339",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/vbg1/ok",
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
                            "text": "Ok"
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
  };
  
  export const formGroupNmLinkParam: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "59337",
        "code": "create",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": true,
            "isHidden": false,
            "name": "ViewConfig.Link",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "b": "$executeAnd$nav",
                "cssClass": "",
                "method": "GET",
                "altText": "",
                "rel": "",
                "alias": "Link",
                "value": "DEFAULT",
                "imgSrc": "",
                "url": "",
                "target": ""
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
    "configId": "59337",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/create",
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
            "text": "Add Pet"
        }
    ],
    "elemLabels": {}
  };
  
  export const formGroupNmHeaderParam: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "60522",
        "code": "addOwnerHeader1",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.Header",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "size": "H3",
                "cssClass": "",
                "alias": "Header"
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
    "configId": "60522",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/addOwnerHeader1",
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
  
  export const formGroupNmPickListParam: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "60775",
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
                    "60777"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "60775",
    "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/category",
    "type": {
        "model": {
            "params": [
                {
                    "config": {
                      "active": false,
                      "required": false,
                      "id": "60777",
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
                    "configId": "60777",
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
  
  export const formGroupNmFormGridFiller: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "61632",
        "code": "formgridfiller",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.FormGridFiller",
            "attributes": {
                "hidden": false,
                "readOnly": false,
                "submitButton": true,
                "showName": true,
                "pageSize": 25,
                "browserBack": false,
                "showAsLink": false,
                "cssClass": "",
                "alias": "FormGridFiller",
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
    "configId": "61632",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/formgridfiller",
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
  
  export const formGroupNestedFrmGrpEle: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "62036",
        "code": "section12",
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
                "cssClass": "testing-span-cssClass",
                "alias": "FormElementGroup",
                "cols": "1"
            }
        },
        "type": {
            "collection": false,
            "nested": true,
            "name": "PetCareAssessment.Section12",
            "model": {
                "paramConfigIds": [
                    "62038",
                    "62039",
                    "62040",
                    "62041"
                ]
            }
        }
    },
    "enabled": false,
    "visible": false,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "62036",
    "path": "/petcareassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petCareForm/petCareAssessmentQuestionnaire/quetionnaire_Section1/section12",
    "type": {
        "model": {
            "params": [
                {
                    "config": {
                      "active": false,
                      "required": false,
                      "id": "62038",
                      "code": "question13",
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
                                      "message": "",
                                      "groups": []
                                  }
                              }
                          ]
                      }
                  },
                    "enabled": false,
                    "visible": false,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "62038",
                    "path": "/petcareassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petCareForm/petCareAssessmentQuestionnaire/quetionnaire_Section1/section12/question13",
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
                            "text": "Question 13"
                        }
                    ],
                    "elemLabels": {}
                },
                {
                    "config": {
                      "active": false,
                      "required": false,
                      "id": "62039",
                      "code": "question14",
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
                              "postEventOnChange": false,
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
                    "enabled": false,
                    "visible": false,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "62039",
                    "path": "/petcareassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petCareForm/petCareAssessmentQuestionnaire/quetionnaire_Section1/section12/question14",
                    "type": {},
                    "leafState": null,
                    "message": [],
                    "values": [],
                    "labels": [
                        {
                            "locale": "en-US",
                            "text": "Calendar with date only"
                        }
                    ],
                    "elemLabels": {}
                },
                {
                    "config": {
                      "active": false,
                      "required": false,
                      "id": "62040",
                      "code": "question15",
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
                              "cssClass": "questionGroup form-inline",
                              "dataEntryField": true,
                              "labelClass": "anthem-label",
                              "level": "0",
                              "alias": "Radio",
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
                    "enabled": false,
                    "visible": false,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "62040",
                    "path": "/petcareassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petCareForm/petCareAssessmentQuestionnaire/quetionnaire_Section1/section12/question15",
                    "type": {
                        "nested": false,
                        "name": "string",
                        "collection": false
                    },
                    "message": [],
                    "values": [
                        {
                            "code": "Yes",
                            "label": "Yes"
                        },
                        {
                            "code": "No",
                            "label": "No"
                        },
                        {
                            "code": "MayBe",
                            "label": "MayBe"
                        }
                    ],
                    "labels": [
                        {
                            "locale": "en-US",
                            "text": "Radio with inline syles"
                        }
                    ],
                    "elemLabels": {}
                },
                {
                    "config": {
                      "active": false,
                      "required": false,
                      "id": "62041",
                      "code": "section16",
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
                          "name": "PetCareAssessment.Section16",
                          "model": {
                              "paramConfigIds": [
                                  "62043",
                                  "62044",
                                  "62045"
                              ]
                          }
                      }
                  },
                    "enabled": false,
                    "visible": false,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "62041",
                    "path": "/petcareassessmentview/vpMain/vtMain/vsPetGeneralAssessment/petCareForm/petCareAssessmentQuestionnaire/quetionnaire_Section1/section12/section16",
                    "type": {
                        "model": {
                            "params": [
  
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
            "text": "Section 12"
        }
    ],
    "elemLabels": {}
  };