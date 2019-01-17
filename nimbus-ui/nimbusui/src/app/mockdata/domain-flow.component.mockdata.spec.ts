export const domainAccordions = [
    {
        "enabled": true,
        "visible": false,
        "activeValidationGroups": [],
        "collectionParams": [],
        "configId": "14455",
        "path": "/home/vpHome/vmAddNote",
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
  ];
  
  
  
 export const domainItems = [
    {
        "label": "Home",
        "path": "/home/vpHome/vsHomeLeftBar/home",
        "page": "",
        "icon": "tasksIcon",
        "imgType": "FA",
        "url": "petclinicdashboard/vpDashboard",
        "type": "INTERNAL",
        "target": "",
        "rel": "",
        "routerLink": "/h/petclinicdashboard/vpDashboard",
        "code": "home",
        "expanded": false
    },
    {
        "label": "Veterinarians",
        "path": "/home/vpHome/vsHomeLeftBar/vets",
        "page": "",
        "icon": "caseHistoryIcon",
        "imgType": "FA",
        "url": "veterinarianview/vpVeterenarians",
        "type": "INTERNAL",
        "target": "",
        "rel": "",
        "routerLink": "/h/veterinarianview/vpVeterenarians",
        "code": "vets",
        "expanded": false
    },
    {
        "label": "Owners",
        "path": "/home/vpHome/vsHomeLeftBar/owners",
        "page": "",
        "icon": "caseHistoryIcon",
        "imgType": "FA",
        "url": "ownerlandingview/vpOwners",
        "type": "INTERNAL",
        "target": "",
        "rel": "",
        "routerLink": "/h/ownerlandingview/vpOwners",
        "code": "owners"
    },
    {
        "label": "Pets",
        "path": "/home/vpHome/vsHomeLeftBar/pets",
        "page": "",
        "icon": "caseHistoryIcon",
        "imgType": "FA",
        "url": "petview/vpAllPets",
        "type": "INTERNAL",
        "target": "",
        "rel": "",
        "routerLink": "/h/petview/vpAllPets",
        "code": "pets",
        "expanded": false
    },
    {
        "label": "Notes",
        "path": "/home/vpHome/vsHomeLeftBar/notes",
        "page": "",
        "icon": "notesIcon",
        "imgType": "FA",
        "url": "petclinicdashboard/vpNotes",
        "type": "INTERNAL",
        "target": "",
        "rel": "",
        "routerLink": "/h/petclinicdashboard/vpNotes",
        "code": "notes",
        "expanded": false
    }
  ];
  
 export const domainActionTray = {
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "14452",
    "path": "/home/vpHome/vsActionTray",
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
  };
  
 export const domainModalItems = [
    {
        "enabled": true,
        "visible": false,
        "activeValidationGroups": [],
        "collectionParams": [],
        "configId": "14455",
        "path": "/home/vpHome/vmAddNote",
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
  ];
  
  export const domainMockLayout: any = {
    model: {
      "params": [
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "1420",
                  "code": "vpHome",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": false,
                      "isHidden": false,
                      "name": "ViewConfig.Page",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": false,
                          "defaultPage": false,
                          "route": "",
                          "cssClass": "",
                          "fixLayout": false,
                          "alias": "Page",
                          "imgSrc": ""
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": true,
                      "name": "VLHome.VPHome",
                      "model": {
                          "paramConfigIds": [
                              "1422",
                              "1426"
                          ]
                      }
                  }
              },
              "alias": "Page",
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "1420",
              "path": "/home/vpHome",
              "type": {
                  "model": {
                      "params": [
                          {
                            "config": {
                              "active": false,
                              "required": false,
                              "id": "1422",
                              "code": "vsHomeHeader",
                              "validations": null,
                              "uiNatures": [],
                              "initializeComponent": () => {},
                              "uiStyles": {
                                  "isLink": false,
                                  "isHidden": false,
                                  "name": "ViewConfig.Section",
                                  "attributes": {
                                      "hidden": false,
                                      "readOnly": false,
                                      "submitButton": true,
                                      "showName": true,
                                      "pageSize": 25,
                                      "browserBack": false,
                                      "showAsLink": false,
                                      "cssClass": "",
                                      "alias": "Section",
                                      "value": "HEADER",
                                      "imgSrc": "",
                                      "imgType": "FA"
                                  }
                              },
                              "type": {
                                  "collection": false,
                                  "nested": true,
                                  "name": "VSHomeHeader",
                                  "model": {
                                      "paramConfigIds": [
                                          "1424",
                                          "1425"
                                      ]
                                  }
                              }
                          },
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "1422",
                              "path": "/home/vpHome/vsHomeHeader",
                              "type": {
                                  "model": {
                                      "params": [
                                          {
                                              "config": {
                                                "active": false,
                                                "required": false,
                                                "id": "1424",
                                                "code": "linkHomeLogo",
                                                "validations": null,
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
                                                        "imgSrc": "/images/anthem.png",
                                                        "url": "",
                                                        "target": ""
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
                                                            "value": "Left"
                                                        }
                                                    },
                                                    {
                                                        "name": "ViewConfig.PageHeader",
                                                        "attributes": {
                                                            "hidden": false,
                                                            "readOnly": false,
                                                            "submitButton": true,
                                                            "showName": true,
                                                            "pageSize": 25,
                                                            "browserBack": false,
                                                            "showAsLink": false,
                                                            "value": "LOGO"
                                                        }
                                                    }
                                                ]
                                            },
                                              "enabled": true,
                                              "visible": true,
                                              "activeValidationGroups": [],
                                              "collectionParams": [],
                                              "configId": "1424",
                                              "path": "/home/vpHome/vsHomeHeader/linkHomeLogo",
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
                                                      "text": "Anthem"
                                                  }
                                              ],
                                              "elemLabels": {}
                                          },
                                          {
                                              "config": {
                                                "active": false,
                                                "required": false,
                                                "id": "1425",
                                                "code": "title",
                                                "validations": null,
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
                                                        "cssClass": "",
                                                        "alias": "Paragraph"
                                                    }
                                                },
                                                "type": {
                                                    "collection": false,
                                                    "nested": false,
                                                    "name": "string"
                                                },
                                                "uiNatures": [
                                                    {
                                                        "name": "ViewConfig.PageHeader",
                                                        "attributes": {
                                                            "hidden": false,
                                                            "readOnly": false,
                                                            "submitButton": true,
                                                            "showName": true,
                                                            "pageSize": 25,
                                                            "browserBack": false,
                                                            "showAsLink": false,
                                                            "value": "APPTITLE"
                                                        }
                                                    }
                                                ]
                                            },
                                              "enabled": true,
                                              "visible": true,
                                              "activeValidationGroups": [],
                                              "collectionParams": [],
                                              "configId": "1425",
                                              "path": "/home/vpHome/vsHomeHeader/title",
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
                                                      "text": "Pet Clinic"
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
                              "id": "1426",
                              "code": "vsHomeLeftBar",
                              "validations": null,
                              "uiNatures": [],
                              "uiStyles": {
                                  "isLink": false,
                                  "isHidden": false,
                                  "name": "ViewConfig.MenuPanel",
                                  "attributes": {
                                      "hidden": false,
                                      "readOnly": false,
                                      "submitButton": true,
                                      "showName": true,
                                      "pageSize": 25,
                                      "browserBack": false,
                                      "showAsLink": false,
                                      "cssClass": "",
                                      "rel": "",
                                      "alias": "MenuPanel",
                                      "type": "INTERNAL",
                                      "imgSrc": "",
                                      "url": "",
                                      "target": "",
                                      "imgType": "FA"
                                  }
                              },
                              "type": {
                                  "collection": false,
                                  "nested": true,
                                  "name": "VSHomeLeftBar",
                                  "model": {
                                      "paramConfigIds": [
                                          "1428",
                                          "1429",
                                          "1433",
                                          "1434",
                                          "1435",
                                          "1436",
                                          "1437"
                                      ]
                                  }
                              }
                          },
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "1426",
                              "path": "/home/vpHome/vsHomeLeftBar",
                              "type": {
                                  "model": {
                                      "params": [
                                          {
                                              "config": {
                                                "active": false,
                                                "required": false,
                                                "id": "1428",
                                                "code": "home",
                                                "validations": null,
                                                "uiNatures": [],
                                                "uiStyles": {
                                                    "isLink": false,
                                                    "isHidden": false,
                                                    "name": "ViewConfig.MenuLink",
                                                    "attributes": {
                                                        "hidden": false,
                                                        "readOnly": false,
                                                        "submitButton": true,
                                                        "showName": true,
                                                        "pageSize": 25,
                                                        "browserBack": false,
                                                        "showAsLink": false,
                                                        "cssClass": "",
                                                        "rel": "",
                                                        "alias": "MenuLink",
                                                        "page": "",
                                                        "type": "INTERNAL",
                                                        "imgSrc": "notesIcon",
                                                        "url": "petclinicdashboard/vpDashboard",
                                                        "target": "",
                                                        "imgType": "FA"
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
                                              "configId": "1428",
                                              "path": "/home/vpHome/vsHomeLeftBar/home",
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
                                                      "text": "Home"
                                                  }
                                              ],
                                              "elemLabels": {}
                                          },
                                          {
                                              "config": {
                                                "active": false,
                                                "required": false,
                                                "id": "1429",
                                                "code": "vmpVisits",
                                                "validations": null,
                                                "uiNatures": [],
                                                "uiStyles": {
                                                    "isLink": false,
                                                    "isHidden": false,
                                                    "name": "ViewConfig.MenuPanel",
                                                    "attributes": {
                                                        "hidden": false,
                                                        "readOnly": false,
                                                        "submitButton": true,
                                                        "showName": true,
                                                        "pageSize": 25,
                                                        "browserBack": false,
                                                        "showAsLink": false,
                                                        "cssClass": "",
                                                        "rel": "",
                                                        "alias": "MenuPanel",
                                                        "type": "INTERNAL",
                                                        "imgSrc": "notesIcon",
                                                        "url": "",
                                                        "target": "",
                                                        "imgType": "SVG"
                                                    }
                                                },
                                                "type": {
                                                    "collection": false,
                                                    "nested": true,
                                                    "name": "VSHomeLeftBar.VMPVisits",
                                                    "model": {
                                                        "paramConfigIds": [
                                                            "1431",
                                                            "1432"
                                                        ]
                                                    }
                                                }
                                            },
                                              "enabled": true,
                                              "visible": true,
                                              "activeValidationGroups": [],
                                              "collectionParams": [],
                                              "configId": "1429",
                                              "path": "/home/vpHome/vsHomeLeftBar/vmpVisits",
                                              "type": {
                                                  "model": {
                                                      "params": [
                                                          {
                                                              "config": {
                                                                "active": false,
                                                                "required": false,
                                                                "id": "1431",
                                                                "code": "visits",
                                                                "validations": null,
                                                                "uiNatures": [],
                                                                "uiStyles": {
                                                                    "isLink": false,
                                                                    "isHidden": false,
                                                                    "name": "ViewConfig.MenuLink",
                                                                    "attributes": {
                                                                        "hidden": false,
                                                                        "readOnly": false,
                                                                        "submitButton": true,
                                                                        "showName": true,
                                                                        "pageSize": 25,
                                                                        "browserBack": false,
                                                                        "showAsLink": false,
                                                                        "cssClass": "",
                                                                        "rel": "",
                                                                        "alias": "MenuLink",
                                                                        "page": "",
                                                                        "type": "INTERNAL",
                                                                        "imgSrc": "notesIcon",
                                                                        "url": "visitlandingview/vpVisits",
                                                                        "target": "",
                                                                        "imgType": "FA"
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
                                                              "configId": "1431",
                                                              "path": "/home/vpHome/vsHomeLeftBar/vmpVisits/visits",
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
                                                                      "text": "Visits"
                                                                  }
                                                              ],
                                                              "elemLabels": {}
                                                          },
                                                          {
                                                              "config": {
                                                                "active": false,
                                                                "required": false,
                                                                "id": "1432",
                                                                "code": "visitsBulkAction",
                                                                "validations": null,
                                                                "uiNatures": [],
                                                                "uiStyles": {
                                                                    "isLink": false,
                                                                    "isHidden": false,
                                                                    "name": "ViewConfig.MenuLink",
                                                                    "attributes": {
                                                                        "hidden": false,
                                                                        "readOnly": false,
                                                                        "submitButton": true,
                                                                        "showName": true,
                                                                        "pageSize": 25,
                                                                        "browserBack": false,
                                                                        "showAsLink": false,
                                                                        "cssClass": "",
                                                                        "rel": "",
                                                                        "alias": "MenuLink",
                                                                        "page": "",
                                                                        "type": "INTERNAL",
                                                                        "imgSrc": "notesIcon",
                                                                        "url": "visitlandingview/vpVisitsBulkAction",
                                                                        "target": "",
                                                                        "imgType": "FA"
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
                                                              "configId": "1432",
                                                              "path": "/home/vpHome/vsHomeLeftBar/vmpVisits/visitsBulkAction",
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
                                                                      "text": "Visits - Bulk Action"
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
                                                      "text": "Visits"
                                                  }
                                              ],
                                              "elemLabels": {}
                                          },
                                          {
                                              "config": {
                                                "active": false,
                                                "required": false,
                                                "id": "1433",
                                                "code": "vets",
                                                "validations": null,
                                                "uiNatures": [],
                                                "uiStyles": {
                                                    "isLink": false,
                                                    "isHidden": false,
                                                    "name": "ViewConfig.MenuLink",
                                                    "attributes": {
                                                        "hidden": false,
                                                        "readOnly": false,
                                                        "submitButton": true,
                                                        "showName": true,
                                                        "pageSize": 25,
                                                        "browserBack": false,
                                                        "showAsLink": false,
                                                        "cssClass": "",
                                                        "rel": "",
                                                        "alias": "MenuLink",
                                                        "page": "",
                                                        "type": "INTERNAL",
                                                        "imgSrc": "notesIcon",
                                                        "url": "veterinarianview/vpVeterenarians",
                                                        "target": "",
                                                        "imgType": "FA"
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
                                              "configId": "1433",
                                              "path": "/home/vpHome/vsHomeLeftBar/vets",
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
                                                      "text": "Veterinarians"
                                                  }
                                              ],
                                              "elemLabels": {}
                                          },
                                          {
                                              "config": {
                                                "active": false,
                                                "required": false,
                                                "id": "1434",
                                                "code": "owners",
                                                "validations": null,
                                                "uiNatures": [],
                                                "uiStyles": {
                                                    "isLink": false,
                                                    "isHidden": false,
                                                    "name": "ViewConfig.MenuLink",
                                                    "attributes": {
                                                        "hidden": false,
                                                        "readOnly": false,
                                                        "submitButton": true,
                                                        "showName": true,
                                                        "pageSize": 25,
                                                        "browserBack": false,
                                                        "showAsLink": false,
                                                        "cssClass": "",
                                                        "rel": "",
                                                        "alias": "MenuLink",
                                                        "page": "",
                                                        "type": "INTERNAL",
                                                        "imgSrc": "notesIcon",
                                                        "url": "ownerlandingview/vpOwners",
                                                        "target": "",
                                                        "imgType": "FA"
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
                                              "configId": "1434",
                                              "path": "/home/vpHome/vsHomeLeftBar/owners",
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
                                                      "text": "Owners"
                                                  }
                                              ],
                                              "elemLabels": {}
                                          },
                                          {
                                              "config": {
                                                "active": false,
                                                "required": false,
                                                "id": "1435",
                                                "code": "pets",
                                                "validations": null,
                                                "uiNatures": [],
                                                "uiStyles": {
                                                    "isLink": false,
                                                    "isHidden": false,
                                                    "name": "ViewConfig.MenuLink",
                                                    "attributes": {
                                                        "hidden": false,
                                                        "readOnly": false,
                                                        "submitButton": true,
                                                        "showName": true,
                                                        "pageSize": 25,
                                                        "browserBack": false,
                                                        "showAsLink": false,
                                                        "cssClass": "",
                                                        "rel": "",
                                                        "alias": "MenuLink",
                                                        "page": "",
                                                        "type": "INTERNAL",
                                                        "imgSrc": "notesIcon",
                                                        "url": "petclinicdashboard/vpAllPets",
                                                        "target": "",
                                                        "imgType": "FA"
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
                                              "configId": "1435",
                                              "path": "/home/vpHome/vsHomeLeftBar/pets",
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
                                                      "text": "Pets"
                                                  }
                                              ],
                                              "elemLabels": {}
                                          },
                                          {
                                              "config": {
                                                "active": false,
                                                "required": false,
                                                "id": "1436",
                                                "code": "notes",
                                                "validations": null,
                                                "uiNatures": [],
                                                "uiStyles": {
                                                    "isLink": false,
                                                    "isHidden": false,
                                                    "name": "ViewConfig.MenuLink",
                                                    "attributes": {
                                                        "hidden": false,
                                                        "readOnly": false,
                                                        "submitButton": true,
                                                        "showName": true,
                                                        "pageSize": 25,
                                                        "browserBack": false,
                                                        "showAsLink": false,
                                                        "cssClass": "",
                                                        "rel": "",
                                                        "alias": "MenuLink",
                                                        "page": "",
                                                        "type": "INTERNAL",
                                                        "imgSrc": "notesIcon",
                                                        "url": "petclinicdashboard/vpNotes",
                                                        "target": "",
                                                        "imgType": "FA"
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
                                              "configId": "1436",
                                              "path": "/home/vpHome/vsHomeLeftBar/notes",
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
                                                      "text": "Notes"
                                                  }
                                              ],
                                              "elemLabels": {}
                                          },
                                          {
                                              "config": {
                                                "active": false,
                                                "required": false,
                                                "id": "1437",
                                                "code": "treegrid",
                                                "validations": null,
                                                "uiNatures": [],
                                                "uiStyles": {
                                                    "isLink": false,
                                                    "isHidden": false,
                                                    "name": "ViewConfig.MenuLink",
                                                    "attributes": {
                                                        "hidden": false,
                                                        "readOnly": false,
                                                        "submitButton": true,
                                                        "showName": true,
                                                        "pageSize": 25,
                                                        "browserBack": false,
                                                        "showAsLink": false,
                                                        "cssClass": "",
                                                        "rel": "",
                                                        "alias": "MenuLink",
                                                        "page": "",
                                                        "type": "INTERNAL",
                                                        "imgSrc": "",
                                                        "url": "petclinicdashboard/vpTreegridDemo",
                                                        "target": "",
                                                        "imgType": "FA"
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
                                              "configId": "1437",
                                              "path": "/home/vpHome/vsHomeLeftBar/treegrid",
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
                                                      "text": "Treegrid Demo"
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
  };