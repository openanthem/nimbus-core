export const cardDetailsBodyElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "3443",
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
                    "3445"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "3443",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo",
    "type": {
        "model": {
            "params": [
                {
                    "config": {
                        "active": false,
                        "required": false,
                        "id": "3445",
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
                              "params": [
                                {
                                    "alias": "FieldValue",
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "3447",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/firstName",
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
                                    "configId": "3448",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/lastName",
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
                                    "configId": "3449",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/divider2",
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
                                    "configId": "3450",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/address",
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
                                    "configId": "3451",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/city",
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
                                    "configId": "3452",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/telephone",
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
                                    "configId": "3453",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/caseStatusDate",
                                    "type": {},
                                    "leafState": "2018-09-04T20:47:18.000Z",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Case Status Date"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "3454",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/caseStatusDate1",
                                    "type": {},
                                    "leafState": null,
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Case Status Date"
                                        }
                                    ],
                                    "elemLabels": {}
                                }
                            ],
                            "paramConfigIds": [
                              "3447",
                              "3448",
                              "3449",
                              "3450",
                              "3451",
                              "3452",
                              "3453",
                              "3454"
                            ]
                          }
                        }
                      },                      
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "3445",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "2478",
                                        "code": "test",
                                        "validations": null,
                                        "uiNatures": [],
                                        "uiStyles": {
                                            "isLink": false,
                                            "isHidden": false,
                                            "name": "ViewConfig.StaticText",
                                            "attributes": {
                                                "hidden": false,
                                                "readOnly": false,
                                                "submitButton": true,
                                                "showName": true,
                                                "pageSize": 25,
                                                "browserBack": false,
                                                "showAsLink": false,
                                                "cssClass": "",
                                                "contentId": "",
                                                "alias": "StaticText"
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
                                    "configId": "2478",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/test",
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
                                            "text": "testing static label"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "3447",
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
                                                "applyValueStyles": false,
                                                "placeholder": "",
                                                "inplaceEdit": true,
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
                                    "alias": "FieldValue",
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "3447",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/firstName",
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
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "3640",
                                        "code": "headerCallSection1",
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
                                    "configId": "3640",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/headerCallSection1",
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
                                            "text": "teting cardddetails paragraph..."
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "1314",
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
                                                    "1316"
                                                ]
                                            }
                                        }
                                    },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "1314",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/fgCardBodyCase1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "1316",
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
                                    "labels": [],
                                    "elemLabels": {}
                                },
                                {
                                  "config": {
                                    "active": false,
                                    "required": false,
                                    "id": "3448",
                                    "code": "first-Name",
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
                                            "inplaceEdit": true,
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
                                    "configId": "3448",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/lastName",
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
                                        "id": "5161",
                                        "code": "delete",
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
                                                "method": "GET",
                                                "cssClass": "",
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
                                    "configId": "5161",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner/delete",
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
                                            "text": "Delete Note"
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
            "text": "testing card details label"
        }
    ],
    "elemLabels": {}
  };
  
  export const cardDetailsHeaderElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "1441",
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
                "expandable": true,
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
                    "1443"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "1441",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo",
    "type": {
        "model": {
            "params": [
                {
                    "config": {
                        "active": false,
                        "required": false,
                        "id": "1443",
                        "code": "vcdbOwner1",
                        "validations": null,
                        "uiNatures": [],
                        "uiStyles": {
                            "isLink": false,
                            "isHidden": false,
                            "name": "ViewConfig.CardDetail.Header",
                            "attributes": {
                                "hidden": false,
                                "readOnly": false,
                                "submitButton": true,
                                "showName": true,
                                "pageSize": 25,
                                "browserBack": false,
                                "showAsLink": false,
                                "cssClass": "",
                                "alias": "CardDetailsHeader"
                            }
                        },
                        "type": {
                            "collection": false,
                            "nested": true,
                            "name": "VPOwnerInfo.VCDBOwner1",
                            "model": {
                                "paramConfigIds": [
                                    "1445",
                                    "1449",
                                    "1450"
                                ]
                            }
                        }
                    },
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "1443",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner1",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "1445",
                                        "code": "vbgDefault1",
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
                                            "name": "VPOwnerInfo.DefaultButtonGroup1",
                                            "model": {
                                                "paramConfigIds": [
                                                    "1447",
                                                    "1448"
                                                ]
                                            }
                                        }
                                    },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "1445",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner1/vbgDefault1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "1447",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner1/vbgDefault1/submit",
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
                                                            "text": "Submit"
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "1448",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner1/vbgDefault1/back",
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
                                                            "text": "Back"
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
                                        "id": "1449",
                                        "code": "headerCallSection11",
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
                                    "configId": "1449",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner1/headerCallSection11",
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
                                            "text": "teting cardddetails paragraph..."
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "config": {
                                        "active": false,
                                        "required": false,
                                        "id": "1450",
                                        "code": "firstName123",
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
                                    "configId": "1450",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsOwnerInfo/vcdOwnerInfo/vcdbOwner1/firstName123",
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
            "text": "testing card details label"
        }
    ],
    "elemLabels": {}
  };