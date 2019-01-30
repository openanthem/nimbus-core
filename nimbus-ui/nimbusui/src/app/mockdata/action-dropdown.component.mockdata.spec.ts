export const MockActionDropdownLink = {
    "activeValidationGroups": [],
    "alias": "Link",
    "collectionParams": [],
    "config": {
        "active": false,
        "code": "edit",
        "id": "593",
        "required": false,
        "type": {
            "collection": false,
            "nested": false,
            "name": "string"
        },
        "uiNatures": [],
        "uiStyles": {
            "isLink": true,
            "isHidden": false,
            "name": "ViewConfig.Link",
            "attributes": {
                "alias": "Link",
                "altText": "",
                "b": "$executeAnd$nav",
                "browserBack": false,
                "cssClass": "mockLink",
                "hidden": false,
                "imgSrc": "edit.png",
                "method": "GET",
                "pageSize": 25,
                "readOnly": false,
                "rel": "",
                "showAsLink": false,
                "showName": true,
                "submitButton": true,
                "target": "",
                "url": "",
                "value": "DEFAULT"
            }
        },
        "validations": null
    },
    "configId": "593",
    "elemLabels": {},
    "enabled": true,
    "labels": [
        {
            "locale": "en-US",
            "text": "Edit"
        }
    ],
    "message": [],
    "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/edit",
    "type": {
        "collection": false,
        "name": "string",
        "nested": false
    },
    "values": [],
    "visible": true
};

export const actionDropDownRowData: any = {
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
    "elemId": "0",
    "nestedGridParam": [
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "583",
            "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "585",
                            "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/edit",
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
                            "configId": "586",
                            "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/ownerInfo",
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
        null
    ]
};

export const actionDropDownElement: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "613",
      "code": "vlmCaseItemLinks",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": true,
          "isHidden": false,
          "name": "ViewConfig.LinkMenu",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "cssClass": "dropdownTrigger",
              "alias": "LinkMenu",
              "imgSrc": "fa-cloud",
              "imgType": "FA"
          }
      },
      "type": {
          "collection": false,
          "nested": true,
          "name": "OwnerLineItem.VLMCaseItemLinks",
          "model": {
              "paramConfigIds": [
                  "615",
                  "616"
              ]
          }
      }
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "613",
  "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks",
  "type": {
      "model": {
          "params": [
              {
                  "config": {
                      "active": false,
                      "required": false,
                      "id": "615",
                      "code": "edit",
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
                              "imgSrc": "edit.png",
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
                  "configId": "615",
                  "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/edit",
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
                  "config": {
                      "active": false,
                      "required": false,
                      "id": "616",
                      "code": "ownerInfo",
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
                              "imgSrc": "task.svg",
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
                  "configId": "616",
                  "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/ownerInfo",
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
};

export const actionDropDownParams:any = [
  {
      "active": false,
      "required": false,
      "id": "615",
      "code": "edit",
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
              "imgSrc": "edit.png",
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
  {
      "active": false,
      "required": false,
      "id": "616",
      "code": "ownerInfo",
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
              "imgSrc": "task.svg",
              "url": "",
              "target": ""
          }
      },
      "type": {
          "collection": false,
          "nested": false,
          "name": "string"
      }
  }
];

export const ActionDropDownLinkElement: any = {
  "config": {
      "active": false,
      "required": false,
      "id": "451",
      "code": "vlm1",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": true,
          "isHidden": false,
          "name": "ViewConfig.LinkMenu",
          "attributes": {
              "hidden": false,
              "readOnly": false,
              "submitButton": true,
              "showName": true,
              "pageSize": 25,
              "browserBack": false,
              "showAsLink": false,
              "cssClass": "dropdownTrigger",
              "alias": "LinkMenu",
              "imgSrc": "",
              "imgType": "FA"
          }
      },
      "type": {
          "collection": false,
          "nested": true,
          "name": "VSHomeHeader.VLM1",
          "model": {
              "paramConfigIds": [
                  "453"
              ]
          }
      }
  },
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "451",
  "path": "/home/vpHome/vsHomeHeader/testingLinkMenu/vlm1",
  "type": {
      "model": {
          "params": [
              {
                  "config": {
                      "active": false,
                      "required": false,
                      "id": "453",
                      "code": "delete1",
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
                  "configId": "453",
                  "path": "/home/vpHome/vsHomeHeader/testingLinkMenu/vlm1/delete1",
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
                          "text": "Remove Pet"
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

export const actionDropDownLinkParams:any = [{
  "enabled": true,
  "visible": true,
  "activeValidationGroups": [],
  "collectionParams": [],
  "configId": "613",
  "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks",
  "type": {
      "model": {
          "params": [
              {
                  "enabled": true,
                  "visible": true,
                  "activeValidationGroups": [],
                  "collectionParams": [],
                  "configId": "615",
                  "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/edit",
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
                  "configId": "616",
                  "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners/0/vlmCaseItemLinks/ownerInfo",
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
}];