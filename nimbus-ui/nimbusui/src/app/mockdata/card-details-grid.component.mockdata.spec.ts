export const cardDetailsGridElement = {
    "config": {
        "active": false,
        "required": false,
        "id": "1475",
        "code": "vcdgConcerns",
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
                "id": "1478",
                "type": {
                    "collection": false,
                    "nested": true,
                    "name": "VPOwnerInfo.VSOwnerInfo",
                    "model": {
                        "paramConfigIds": [
                            "1480"
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
    "configId": "1475",
    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns",
    "type": {
        "model": {
            "params": [
                {
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "1478",
                    "collectionElem": true,
                    "elemId": "0",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "1480",
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
                                                  "1482"
                                              ]
                                          }
                                      }
                                  },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "1480",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "1482",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "1484",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/firstName",
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
                                                                    "configId": "1485",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/lastName",
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
                                                                    "configId": "1486",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/divider2",
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
                                                                    "configId": "1487",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "1489",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/addressGroup/address",
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
                                                                                    "configId": "1490",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/addressGroup/city",
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
                                                                                    "configId": "1491",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/addressGroup/state",
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
                                                                                    "configId": "1492",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
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
                                                                    "configId": "1493",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/0/vcdOwnerInfo/vcdbOwner/telephone",
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
                    "configId": "1478",
                    "collectionElem": true,
                    "elemId": "1",
                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "config": {
                                      "active": false,
                                      "required": false,
                                      "id": "1480",
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
                                                  "1482"
                                              ]
                                          }
                                      }
                                  },
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "1480",
                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "1482",
                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "1484",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/firstName",
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
                                                                    "configId": "1485",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/lastName",
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
                                                                    "configId": "1486",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/divider2",
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
                                                                    "configId": "1487",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "1489",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/address",
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
                                                                                    "configId": "1490",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/city",
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
                                                                                    "configId": "1491",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/state",
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
                                                                                    "configId": "1492",
                                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
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
                                                                    "configId": "1493",
                                                                    "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/telephone",
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
  };
  
  export const cardDetailsGridNewElement = {
      "enabled": true,
      "visible": true,
      "activeValidationGroups": [],
      "collectionParams": [],
      "configId": "1478",
      "collectionElem": true,
      "elemId": "1",
      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1",
      "type": {
          "model": {
              "params": [
                  {
                      "config": {
                        "active": false,
                        "required": false,
                        "id": "1480",
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
                                    "1482"
                                ]
                            }
                        }
                    },
                      "enabled": true,
                      "visible": true,
                      "activeValidationGroups": [],
                      "collectionParams": [],
                      "configId": "1480",
                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo",
                      "type": {
                          "model": {
                              "params": [
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "1482",
                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "1484",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/firstName",
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
                                                      "configId": "1485",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/lastName",
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
                                                      "configId": "1486",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/divider2",
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
                                                      "configId": "1487",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup",
                                                      "type": {
                                                          "model": {
                                                              "params": [
                                                                  {
                                                                      "enabled": true,
                                                                      "visible": true,
                                                                      "activeValidationGroups": [],
                                                                      "collectionParams": [],
                                                                      "configId": "1489",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/address",
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
                                                                      "configId": "1490",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/city",
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
                                                                      "configId": "1491",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/state",
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
                                                                      "configId": "1492",
                                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/addressGroup/zip",
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
                                                      "configId": "1493",
                                                      "path": "/ownerview/vpOwnerInfo/vtOwnerInfo/vsPets/vcdgConcerns/1/vcdOwnerInfo/vcdbOwner/telephone",
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
  };