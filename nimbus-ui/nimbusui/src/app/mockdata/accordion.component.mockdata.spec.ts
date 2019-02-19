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

export const accordionElementWithForm: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "48264",
        "code": "patientName1",
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
                "showMessages": false,
                "showExpandAll": true,
                "multiple": false,
                "alias": "Accordion"
            }
        },
        "type": {
            "collection": false,
            "nested": true,
            "name": "VPOwnerInfo.VSPets.PetAssessment_Name1",
            "model": {
                "paramConfigIds": [
                    "48266"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "48264",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1",
    "type": {
        "model": {
            "params": [
                {
                    "config": {
                      "active": false,
                      "required": false,
                      "id": "48266",
                      "code": "petName_Accordion_tab1",
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
                              "editable": true,
                              "alias": "AccordionTab",
                              "selected": false,
                              "info": "testing info in pheader"
                          }
                      },
                      "type": {
                          "collection": false,
                          "nested": true,
                          "name": "VPOwnerInfo.VSPets.PetAssessment_Name_Tab1",
                          "model": {
                              "paramConfigIds": [
                                  "48268",
                                  "48269",
                                  "48270",
                                  "48271",
                                  "48272",
                                  "48273",
                                  "48277",
                                  "48282",
                                  "48312",
                                  "48330",
                                  "48336"
                              ]
                          }
                      }
                  },
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "48266",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "48268",
                                      "code": "testingTab",
                                      "validations": null,
                                      "uiNatures": [],
                                      "uiStyles": {
                                          "isLink": false,
                                          "isHidden": false,
                                          "name": "ViewConfig.TabInfo",
                                          "attributes": {
                                              "hidden": false,
                                              "readOnly": false,
                                              "submitButton": true,
                                              "showName": true,
                                              "pageSize": 25,
                                              "browserBack": false,
                                              "showAsLink": false,
                                              "cssClass": "",
                                              "alias": "TabInfo",
                                              "info": "testing tab info label"
                                          }
                                      },
                                      "type": {
                                          "collection": false,
                                          "nested": false,
                                          "name": "string"
                                      }
                                  },
                                  "alias": "TabInfo",
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "48268",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/testingTab",
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
                                            "text": "testing tabinfo..."
                                        }
                                    ],
                                    "elemLabels": {},
                                    "leafState": 'testing p-header'
                                },
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "48269",
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
                                  "alias": "Image",
                                  "leafState": "testing nm-image",
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "48269",
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
                                },
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "48270",
                                      "code": "txt1",
                                      "validations": null,
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
                                      }
                                  },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "48270",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/Testform/patientName1/petName_Accordion_tab1/txt1",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
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
                            "text": "testing accordion..193"
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
  
  export const accordionElementWithNoForm: any = {
      "config": {
          "active": false,
          "required": false,
          "id": "49992",
          "code": "patientName1",
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
                  "showMessages": false,
                  "showExpandAll": true,
                  "multiple": false,
                  "alias": "Accordion"
              }
          },
          "type": {
              "collection": false,
              "nested": true,
              "name": "VPOwnerInfo.VSPets.PetAssessment_Name1",
              "model": {
                  "paramConfigIds": [
                      "49994"
                  ]
              }
          }
      },
      "enabled": true,
      "visible": true,
      "activeValidationGroups": [],
      "collectionParams": [],
      "configId": "49992",
      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1",
      "type": {
          "model": {
              "params": [
                  {
                      "config": {
                          "active": false,
                          "required": false,
                          "id": "49994",
                          "code": "petName_Accordion_tab1",
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
                                  "editable": true,
                                  "alias": "AccordionTab",
                                  "selected": false
                              }
                          },
                          "type": {
                              "collection": false,
                              "nested": true,
                              "name": "VPOwnerInfo.VSPets.PetAssessment_Name_Tab1",
                              "model": {
                                  "paramConfigIds": [
                                      "49996",
                                      "49997",
                                      "49998",
                                      "49999",
                                      "50000",
                                      "50001",
                                      "50005",
                                      "50010",
                                      "50040",
                                      "50058",
                                      "50064"
                                  ]
                              }
                          }
                      },
                      "enabled": true,
                      "visible": true,
                      "activeValidationGroups": [],
                      "collectionParams": [],
                      "configId": "49994",
                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1",
                      "type": {
                          "model": {
                              "params": [
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "49996",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/testingTab",
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
                                              "text": "testing tabinfo..."
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "49997",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/question123",
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
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "49998",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/txt1",
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
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "49999",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/petForm_1a1",
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
                                              "text": "First Name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "50000",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/headerCallSection1",
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
                                              "text": "Hello!! Welcome to Pet Clinic. This application is the reference implementation for Nimbus Framework."
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "config": {
                                          "active": false,
                                          "required": false,
                                          "id": "50001",
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
                                                  "cssClass": "text-sm-center",
                                                  "alias": "ButtonGroup"
                                              }
                                          },
                                          "type": {
                                              "collection": false,
                                              "nested": true,
                                              "name": "VPOwnerInfo.VSPets.VBG1",
                                              "model": {
                                                  "paramConfigIds": [
                                                      "50003",
                                                      "50004"
                                                  ]
                                              }
                                          }
                                      },
                                      "alias": "ButtonGroup",
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "50001",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vbg1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "50003",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vbg1/cancel",
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
                                                      "configId": "50004",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vbg1/ok",
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
                                  },
                                  {
                                      "config": {
                                          "active": false,
                                          "required": false,
                                          "id": "50005",
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
                                      "alias": "Link",
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "50005",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/create",
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
                                  },
                                  {
                                      "config": {
                                          "active": false,
                                          "required": false,
                                          "id": "50010",
                                          "code": "owners1",
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
                                                  "rowSelection": false,
                                                  "postButtonUrl": "",
                                                  "pagination": true,
                                                  "dataEntryField": true,
                                                  "postButtonTargetPath": "",
                                                  "postButtonUri": "",
                                                  "expandableRows": true,
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
                                                  "isTransient": false,
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
                                                  "id": "50013",
                                                  "type": {
                                                      "collection": false,
                                                      "nested": true,
                                                      "name": "OwnerLineItem",
                                                      "model": {
                                                          "paramConfigIds": [
                                                              "50015",
                                                              "50016",
                                                              "50017",
                                                              "50018",
                                                              "50019",
                                                              "50020",
                                                              "50021",
                                                              "50022",
                                                              "50023",
                                                              "50027"
                                                          ]
                                                      }
                                                  }
                                              }
                                          }
                                      },
                                      "alias": "Grid",
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [
                                          {
                                              "enabled": true,
                                              "visible": true,
                                              "activeValidationGroups": [],
                                              "collectionParams": [],
                                              "configId": "50023",
                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks",
                                              "type": {
                                                  "model": {
                                                      "params": [
                                                          {
                                                              "enabled": true,
                                                              "visible": true,
                                                              "activeValidationGroups": [],
                                                              "collectionParams": [],
                                                              "configId": "50025",
                                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks/edit",
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
                                                                      "text": "Edit"
                                                                  }
                                                              ],
                                                              "elemLabels": {}
                                                          },
                                                          {
                                                              "enabled": true,
                                                              "visible": true,
                                                              "activeValidationGroups": [],
                                                              "collectionParams": [],
                                                              "configId": "50026",
                                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks/ownerInfo",
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
                                                                      "text": "Owner Info"
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
                                              "enabled": true,
                                              "visible": true,
                                              "activeValidationGroups": [],
                                              "collectionParams": [],
                                              "configId": "50027",
                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/expandedRowContent",
                                              "type": {
                                                  "model": {
                                                      "params": [
                                                          {
                                                              "enabled": true,
                                                              "visible": true,
                                                              "activeValidationGroups": [],
                                                              "collectionParams": [],
                                                              "configId": "50033",
                                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/expandedRowContent/pets",
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
                                                                  "numberOfElements": 0,
                                                                  "first": true
                                                              },
                                                              "gridData": {
                                                                  "collectionParams": []
                                                              },
                                                              "message": [],
                                                              "values": [],
                                                              "labels": [
                                                                  {
                                                                      "locale": "en-US",
                                                                      "text": "Pets"
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
                                              "enabled": true,
                                              "visible": true,
                                              "activeValidationGroups": [],
                                              "collectionParams": [],
                                              "configId": "50023",
                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks",
                                              "type": {
                                                  "model": {
                                                      "params": [
                                                          {
                                                              "enabled": true,
                                                              "visible": true,
                                                              "activeValidationGroups": [],
                                                              "collectionParams": [],
                                                              "configId": "50025",
                                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks/edit",
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
                                                                      "text": "Edit"
                                                                  }
                                                              ],
                                                              "elemLabels": {}
                                                          },
                                                          {
                                                              "enabled": true,
                                                              "visible": true,
                                                              "activeValidationGroups": [],
                                                              "collectionParams": [],
                                                              "configId": "50026",
                                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks/ownerInfo",
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
                                                                      "text": "Owner Info"
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
                                              "enabled": true,
                                              "visible": true,
                                              "activeValidationGroups": [],
                                              "collectionParams": [],
                                              "configId": "50027",
                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/expandedRowContent",
                                              "type": {
                                                  "model": {
                                                      "params": [
                                                          {
                                                              "enabled": true,
                                                              "visible": true,
                                                              "activeValidationGroups": [],
                                                              "collectionParams": [],
                                                              "configId": "50033",
                                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/expandedRowContent/pets",
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
                                                                  "numberOfElements": 0,
                                                                  "first": true
                                                              },
                                                              "gridData": {
                                                                  "collectionParams": []
                                                              },
                                                              "message": [],
                                                              "values": [],
                                                              "labels": [
                                                                  {
                                                                      "locale": "en-US",
                                                                      "text": "Pets"
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
                                              "enabled": true,
                                              "visible": true,
                                              "activeValidationGroups": [],
                                              "collectionParams": [],
                                              "configId": "50023",
                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks",
                                              "type": {
                                                  "model": {
                                                      "params": [
                                                          {
                                                              "enabled": true,
                                                              "visible": true,
                                                              "activeValidationGroups": [],
                                                              "collectionParams": [],
                                                              "configId": "50025",
                                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks/edit",
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
                                                                      "text": "Edit"
                                                                  }
                                                              ],
                                                              "elemLabels": {}
                                                          },
                                                          {
                                                              "enabled": true,
                                                              "visible": true,
                                                              "activeValidationGroups": [],
                                                              "collectionParams": [],
                                                              "configId": "50026",
                                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks/ownerInfo",
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
                                                                      "text": "Owner Info"
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
                                              "enabled": true,
                                              "visible": true,
                                              "activeValidationGroups": [],
                                              "collectionParams": [],
                                              "configId": "50027",
                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/expandedRowContent",
                                              "type": {
                                                  "model": {
                                                      "params": [
                                                          {
                                                              "enabled": true,
                                                              "visible": true,
                                                              "activeValidationGroups": [],
                                                              "collectionParams": [],
                                                              "configId": "50033",
                                                              "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/expandedRowContent/pets",
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
                                                                  "numberOfElements": 0,
                                                                  "first": true
                                                              },
                                                              "gridData": {
                                                                  "collectionParams": []
                                                              },
                                                              "message": [],
                                                              "values": [],
                                                              "labels": [
                                                                  {
                                                                      "locale": "en-US",
                                                                      "text": "Pets"
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
                                      ],
                                      "configId": "50010",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "50013",
                                                      "collectionElem": true,
                                                      "elemId": "0",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50015",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/id",
                                                                      "type": {},
                                                                      "leafState": 52,
                                                                      "previousLeafState": 52,
                                                                      "message": [],
                                                                      "values": [],
                                                                      "labels": [],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50016",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/firstName",
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
                                                                              "text": "First Name"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50017",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/lastName",
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50018",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/nickname",
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
                                                                              "text": "Nickname"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50019",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/shouldUseNickname",
                                                                      "type": {},
                                                                      "leafState": false,
                                                                      "previousLeafState": false,
                                                                      "message": [],
                                                                      "values": [],
                                                                      "labels": [],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50020",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/status",
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
                                                                              "text": "Status"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50021",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/ownerCity",
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
                                                                              "text": "Owner City"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50022",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/telephone",
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
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50023",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50025",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks/edit",
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
                                                                                              "text": "Edit"
                                                                                          }
                                                                                      ],
                                                                                      "elemLabels": {}
                                                                                  },
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50026",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/vlmCaseItemLinks/ownerInfo",
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
                                                                                              "text": "Owner Info"
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50027",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/expandedRowContent",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50033",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/0/expandedRowContent/pets",
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
                                                                                          "numberOfElements": 0,
                                                                                          "first": true
                                                                                      },
                                                                                      "gridData": {
                                                                                          "collectionParams": []
                                                                                      },
                                                                                      "message": [],
                                                                                      "values": [],
                                                                                      "labels": [
                                                                                          {
                                                                                              "locale": "en-US",
                                                                                              "text": "Pets"
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
                                                      "leafState": {
                                                          "id": 52,
                                                          "firstName": "test",
                                                          "lastName": "1",
                                                          "shouldUseNickname": false,
                                                          "ownerCity": "",
                                                          "telephone": "1231231231",
                                                          "vlmCaseItemLinks": {},
                                                          "expandedRowContent": {
                                                              "pets": []
                                                          },
                                                          "elemId": "0"
                                                      },
                                                      "previousLeafState": {
                                                          "id": 52,
                                                          "firstName": "test",
                                                          "lastName": "1",
                                                          "shouldUseNickname": false,
                                                          "ownerCity": "",
                                                          "telephone": "1231231231",
                                                          "vlmCaseItemLinks": {},
                                                          "expandedRowContent": {
                                                              "pets": []
                                                          },
                                                          "elemId": "0"
                                                      },
                                                      "message": [],
                                                      "values": [],
                                                      "labels": [],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "50013",
                                                      "collectionElem": true,
                                                      "elemId": "1",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50015",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/id",
                                                                      "type": {},
                                                                      "leafState": 51,
                                                                      "previousLeafState": 51,
                                                                      "message": [],
                                                                      "values": [],
                                                                      "labels": [],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50016",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/firstName",
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
                                                                              "text": "First Name"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50017",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/lastName",
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50018",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/nickname",
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
                                                                              "text": "Nickname"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50019",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/shouldUseNickname",
                                                                      "type": {},
                                                                      "leafState": false,
                                                                      "previousLeafState": false,
                                                                      "message": [],
                                                                      "values": [],
                                                                      "labels": [],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50020",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/status",
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
                                                                              "text": "Status"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50021",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/ownerCity",
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
                                                                              "text": "Owner City"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50022",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/telephone",
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
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50023",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50025",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks/edit",
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
                                                                                              "text": "Edit"
                                                                                          }
                                                                                      ],
                                                                                      "elemLabels": {}
                                                                                  },
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50026",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/vlmCaseItemLinks/ownerInfo",
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
                                                                                              "text": "Owner Info"
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50027",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/expandedRowContent",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50033",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/1/expandedRowContent/pets",
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
                                                                                          "numberOfElements": 0,
                                                                                          "first": true
                                                                                      },
                                                                                      "gridData": {
                                                                                          "collectionParams": []
                                                                                      },
                                                                                      "message": [],
                                                                                      "values": [],
                                                                                      "labels": [
                                                                                          {
                                                                                              "locale": "en-US",
                                                                                              "text": "Pets"
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
                                                      "leafState": {
                                                          "id": 51,
                                                          "firstName": "test",
                                                          "lastName": "1",
                                                          "shouldUseNickname": false,
                                                          "ownerCity": "",
                                                          "telephone": "1231231231",
                                                          "vlmCaseItemLinks": {},
                                                          "expandedRowContent": {
                                                              "pets": []
                                                          },
                                                          "elemId": "1"
                                                      },
                                                      "previousLeafState": {
                                                          "id": 51,
                                                          "firstName": "test",
                                                          "lastName": "1",
                                                          "shouldUseNickname": false,
                                                          "ownerCity": "",
                                                          "telephone": "1231231231",
                                                          "vlmCaseItemLinks": {},
                                                          "expandedRowContent": {
                                                              "pets": []
                                                          },
                                                          "elemId": "1"
                                                      },
                                                      "message": [],
                                                      "values": [],
                                                      "labels": [],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "50013",
                                                      "collectionElem": true,
                                                      "elemId": "2",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50015",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/id",
                                                                      "type": {},
                                                                      "leafState": 53,
                                                                      "previousLeafState": 53,
                                                                      "message": [],
                                                                      "values": [],
                                                                      "labels": [],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50016",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/firstName",
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
                                                                              "text": "First Name"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50017",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/lastName",
                                                                      "type": {
                                                                          "nested": false,
                                                                          "name": "string",
                                                                          "collection": false
                                                                      },
                                                                      "leafState": "123",
                                                                      "previousLeafState": "123",
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50018",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/nickname",
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
                                                                              "text": "Nickname"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50019",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/shouldUseNickname",
                                                                      "type": {},
                                                                      "leafState": false,
                                                                      "previousLeafState": false,
                                                                      "message": [],
                                                                      "values": [],
                                                                      "labels": [],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50020",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/status",
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
                                                                              "text": "Status"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50021",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/ownerCity",
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
                                                                              "text": "Owner City"
                                                                          }
                                                                      ],
                                                                      "elemLabels": {}
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50022",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/telephone",
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
                                                                  },
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50023",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50025",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks/edit",
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
                                                                                              "text": "Edit"
                                                                                          }
                                                                                      ],
                                                                                      "elemLabels": {}
                                                                                  },
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50026",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/vlmCaseItemLinks/ownerInfo",
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
                                                                                              "text": "Owner Info"
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50027",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/expandedRowContent",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50033",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/owners1/2/expandedRowContent/pets",
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
                                                                                          "numberOfElements": 0,
                                                                                          "first": true
                                                                                      },
                                                                                      "gridData": {
                                                                                          "collectionParams": []
                                                                                      },
                                                                                      "message": [],
                                                                                      "values": [],
                                                                                      "labels": [
                                                                                          {
                                                                                              "locale": "en-US",
                                                                                              "text": "Pets"
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
                                                      "leafState": {
                                                          "id": 53,
                                                          "firstName": "test",
                                                          "lastName": "123",
                                                          "shouldUseNickname": false,
                                                          "ownerCity": "",
                                                          "telephone": "1231231231",
                                                          "vlmCaseItemLinks": {},
                                                          "expandedRowContent": {
                                                              "pets": []
                                                          },
                                                          "elemId": "2"
                                                      },
                                                      "previousLeafState": {
                                                          "id": 53,
                                                          "firstName": "test",
                                                          "lastName": "123",
                                                          "shouldUseNickname": false,
                                                          "ownerCity": "",
                                                          "telephone": "1231231231",
                                                          "vlmCaseItemLinks": {},
                                                          "expandedRowContent": {
                                                              "pets": []
                                                          },
                                                          "elemId": "2"
                                                      },
                                                      "message": [],
                                                      "values": [],
                                                      "labels": [],
                                                      "elemLabels": {}
                                                  }
                                              ]
                                          }
                                      },
                                      "page": {
                                          "last": true,
                                          "totalPages": 1,
                                          "totalElements": 3,
                                          "size": 0,
                                          "number": 0,
                                          "numberOfElements": 3,
                                          "first": true
                                      },
                                      "gridData": {
                                          "collectionParams": [],
                                          "leafState": [
                                              {
                                                  "id": 52,
                                                  "firstName": "test",
                                                  "lastName": "1",
                                                  "shouldUseNickname": false,
                                                  "ownerCity": "",
                                                  "telephone": "1231231231",
                                                  "vlmCaseItemLinks": {},
                                                  "expandedRowContent": {
                                                      "pets": []
                                                  },
                                                  "elemId": "0"
                                              },
                                              {
                                                  "id": 51,
                                                  "firstName": "test",
                                                  "lastName": "1",
                                                  "shouldUseNickname": false,
                                                  "ownerCity": "",
                                                  "telephone": "1231231231",
                                                  "vlmCaseItemLinks": {},
                                                  "expandedRowContent": {
                                                      "pets": []
                                                  },
                                                  "elemId": "1"
                                              },
                                              {
                                                  "id": 53,
                                                  "firstName": "test",
                                                  "lastName": "123",
                                                  "shouldUseNickname": false,
                                                  "ownerCity": "",
                                                  "telephone": "1231231231",
                                                  "vlmCaseItemLinks": {},
                                                  "expandedRowContent": {
                                                      "pets": []
                                                  },
                                                  "elemId": "2"
                                              }
                                          ],
                                          "stateMap": [
                                              {
                                                  "id": {},
                                                  "firstName": {},
                                                  "lastName": {},
                                                  "nickname": {},
                                                  "shouldUseNickname": {},
                                                  "status": {},
                                                  "ownerCity": {},
                                                  "telephone": {},
                                                  "vlmCaseItemLinks": {},
                                                  "expandedRowContent": {}
                                              },
                                              {
                                                  "id": {},
                                                  "firstName": {},
                                                  "lastName": {},
                                                  "nickname": {},
                                                  "shouldUseNickname": {},
                                                  "status": {},
                                                  "ownerCity": {},
                                                  "telephone": {},
                                                  "vlmCaseItemLinks": {},
                                                  "expandedRowContent": {}
                                              },
                                              {
                                                  "id": {},
                                                  "firstName": {},
                                                  "lastName": {},
                                                  "nickname": {},
                                                  "shouldUseNickname": {},
                                                  "status": {},
                                                  "ownerCity": {},
                                                  "telephone": {},
                                                  "vlmCaseItemLinks": {},
                                                  "expandedRowContent": {}
                                              }
                                          ]
                                      },
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Owners"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "config": {
                                          "active": false,
                                          "required": false,
                                          "id": "50040",
                                          "code": "vcdOwnerInfo1",
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
                                                      "50042"
                                                  ]
                                              }
                                          }
                                      },
                                      "alias": "CardDetail",
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "50040",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "50042",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50044",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/firstName",
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50045",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/lastName",
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50046",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/divider2",
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50047",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/addressGroup",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50049",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/addressGroup/address",
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
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50050",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/addressGroup/city",
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
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50051",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/addressGroup/state",
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
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50052",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/addressGroup/zip",
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50053",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdOwnerInfo1/vcdbOwner/telephone",
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
                                  },
                                  {
                                      "config": {
                                          "active": false,
                                          "required": false,
                                          "id": "50058",
                                          "code": "vcdgConcerns1",
                                          "validations": null,
                                          "uiNatures": [],
                                          "uiStyles": {
                                              "isLink": false,
                                              "isHidden": false,
                                              "name": "ViewConfig.CardDetailsGrid",
                                              "attributes": {
                                                  "hidden": false,
                                                  "readOnly": false,
                                                  "submitButton": true,
                                                  "showName": true,
                                                  "pageSize": 25,
                                                  "browserBack": false,
                                                  "showAsLink": false,
                                                  "editUrl": "",
                                                  "cssClass": "",
                                                  "draggable": false,
                                                  "alias": "CardDetailsGrid",
                                                  "onLoad": true
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
                                                  "id": "50061",
                                                  "type": {
                                                      "collection": false,
                                                      "nested": true,
                                                      "name": "VPOwnerInfo.VSOwnerInfo",
                                                      "model": {
                                                          "paramConfigIds": [
                                                              "50063"
                                                          ]
                                                      }
                                                  }
                                              }
                                          }
                                      },
                                      "alias": "CardDetailsGrid",
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "50058",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "50061",
                                                      "collectionElem": true,
                                                      "elemId": "0",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50063",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50042",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner",
                                                                                      "type": {
                                                                                          "model": {
                                                                                              "params": [
                                                                                                  {
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50044",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/firstName",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50045",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/lastName",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50046",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/divider2",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50047",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                                                      "type": {
                                                                                                          "model": {
                                                                                                              "params": [
                                                                                                                  {
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50049",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/addressGroup/address",
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
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50050",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/addressGroup/city",
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
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50051",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/addressGroup/state",
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
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50052",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50053",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/0/vcdOwnerInfo/vcdbOwner/telephone",
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
                                                      "labels": [],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "50061",
                                                      "collectionElem": true,
                                                      "elemId": "1",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50063",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50042",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner",
                                                                                      "type": {
                                                                                          "model": {
                                                                                              "params": [
                                                                                                  {
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50044",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/firstName",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50045",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/lastName",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50046",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/divider2",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50047",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                                                      "type": {
                                                                                                          "model": {
                                                                                                              "params": [
                                                                                                                  {
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50049",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/addressGroup/address",
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
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50050",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/addressGroup/city",
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
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50051",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/addressGroup/state",
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
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50052",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50053",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/1/vcdOwnerInfo/vcdbOwner/telephone",
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
                                                      "labels": [],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "50061",
                                                      "collectionElem": true,
                                                      "elemId": "2",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50063",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": true,
                                                                                      "visible": true,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50042",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner",
                                                                                      "type": {
                                                                                          "model": {
                                                                                              "params": [
                                                                                                  {
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50044",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/firstName",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50045",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/lastName",
                                                                                                      "type": {
                                                                                                          "nested": false,
                                                                                                          "name": "string",
                                                                                                          "collection": false
                                                                                                      },
                                                                                                      "leafState": "123",
                                                                                                      "previousLeafState": "123",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50046",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/divider2",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50047",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                                                      "type": {
                                                                                                          "model": {
                                                                                                              "params": [
                                                                                                                  {
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50049",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/addressGroup/address",
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
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50050",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/addressGroup/city",
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
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50051",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/addressGroup/state",
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
                                                                                                                      "enabled": true,
                                                                                                                      "visible": true,
                                                                                                                      "activeValidationGroups": [],
                                                                                                                      "collectionParams": [],
                                                                                                                      "configId": "50052",
                                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
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
                                                                                                      "enabled": true,
                                                                                                      "visible": true,
                                                                                                      "activeValidationGroups": [],
                                                                                                      "collectionParams": [],
                                                                                                      "configId": "50053",
                                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vcdgConcerns1/2/vcdOwnerInfo/vcdbOwner/telephone",
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
                                              "text": "testing grid label-181"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "config": {
                                          "active": false,
                                          "required": false,
                                          "id": "50064",
                                          "code": "vfForm1",
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
                                                  "showMessages": false,
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
                                                      "50066",
                                                      "50067"
                                                  ]
                                              }
                                          }
                                      },
                                      "alias": "Form",
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "50064",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "50066",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/headerCallSection",
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
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "50067",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50069",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection/gridVisibility",
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
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50070",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection/showHistory",
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
                                                                      "enabled": false,
                                                                      "visible": false,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50071",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection/hideHistory",
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
                                                                      "enabled": false,
                                                                      "visible": false,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "50072",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection/gridWrapper",
                                                                      "type": {
                                                                          "model": {
                                                                              "params": [
                                                                                  {
                                                                                      "enabled": false,
                                                                                      "visible": false,
                                                                                      "activeValidationGroups": [],
                                                                                      "collectionParams": [],
                                                                                      "configId": "50074",
                                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/patientName1/petName_Accordion_tab1/vfForm1/callSection/gridWrapper/calls",
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
                                                                                          "numberOfElements": 0,
                                                                                          "first": true
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
                              "text": "testing accordion..193"
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