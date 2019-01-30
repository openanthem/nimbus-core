export const tableParams: any = [
    {
        "active": false,
        "required": false,
        "id": "665",
        "code": "id",
        "validations": null,
        "uiNatures": [],
        "uiStyles": {
            "isLink": false,
            "isHidden": false,
            "name": "ViewConfig.GridColumn",
            "attributes": {
                "hidden": true,
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
                "filter": true,
                "filterMode": "equals",
                "cssClass": "",
                "filterValue": "",
                "datePattern": "",
                "alias": "GridColumn",
                "placeholder": "",
                "sortAs": "DEFAULT"
            }
        },
        "type": {
            "collection": false,
            "nested": false,
            "name": "long"
        },
        "field": "id",
        "exportable": false
    },
    {
      "active": false,
      "required": false,
      "id": "666",
      "code": "appointment",
      "validations": null,
      "uiNatures": [],
      "uiStyles": {
          "isLink": false,
          "isHidden": false,
          "name": "ViewConfig.GridColumn",
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
              "filter": true,
              "filterMode": "equals",
              "cssClass": "",
              "filterValue": "",
              "datePattern": "",
              "alias": "GridColumn",
              "placeholder": "",
              "sortAs": "DEFAULT"
          }
      },
      "type": {
          "collection": false,
          "nested": false,
          "name": "LocalDate"
      },
      "label": "Appointment Date",
      "field": "appointment",
      "header": "Appointment Date",
      "exportable": true
  },
  {
    "active": false,
    "required": false,
    "id": "667",
    "code": "ownerName",
    "validations": null,
    "uiNatures": [],
    "uiStyles": {
        "isLink": false,
        "isHidden": false,
        "name": "ViewConfig.GridColumn",
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
            "filter": true,
            "filterMode": "equals",
            "cssClass": "",
            "filterValue": "",
            "datePattern": "",
            "alias": "GridColumn",
            "placeholder": "",
            "sortAs": "DEFAULT"
        }
    },
    "type": {
        "collection": false,
        "nested": false,
        "name": "string"
    },
    "label": "Owner's name",
    "field": "ownerName",
    "header": "Owner's name",
    "exportable": true
  },
  {
    "active": false,
    "required": false,
    "id": "668",
    "code": "petName",
    "validations": null,
    "uiNatures": [],
    "uiStyles": {
        "isLink": false,
        "isHidden": false,
        "name": "ViewConfig.GridColumn",
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
            "filter": true,
            "filterMode": "equals",
            "cssClass": "",
            "filterValue": "",
            "datePattern": "",
            "alias": "GridColumn",
            "placeholder": "",
            "sortAs": "DEFAULT"
        }
    },
    "type": {
        "collection": false,
        "nested": false,
        "name": "string"
    },
    "label": "Pet's name",
    "field": "petName",
    "header": "Pet's name",
    "exportable": true
  },
  {
    "active": false,
    "required": false,
    "id": "669",
    "code": "reasonForVisit",
    "validations": null,
    "uiNatures": [],
    "uiStyles": {
        "isLink": false,
        "isHidden": false,
        "name": "ViewConfig.GridColumn",
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
            "filter": true,
            "filterMode": "equals",
            "cssClass": "",
            "filterValue": "",
            "datePattern": "",
            "alias": "GridColumn",
            "placeholder": "",
            "sortAs": "DEFAULT"
        }
    },
    "type": {
        "collection": false,
        "nested": false,
        "name": "string"
    },
    "label": "Reason For Visit",
    "field": "reasonForVisit",
    "header": "Reason For Visit",
    "exportable": true
  },
  {
    "active": false,
    "required": false,
    "id": "670",
    "code": "status",
    "validations": null,
    "uiNatures": [],
    "uiStyles": {
        "isLink": true,
        "isHidden": false,
        "name": "ViewConfig.GridColumn",
        "attributes": {
            "hidden": false,
            "readOnly": false,
            "submitButton": true,
            "showName": true,
            "pageSize": 25,
            "browserBack": false,
            "showAsLink": true,
            "rowExpander": false,
            "expandable": true,
            "applyValueStyles": false,
            "sortable": true,
            "filter": true,
            "filterMode": "equals",
            "cssClass": "",
            "filterValue": "",
            "datePattern": "",
            "alias": "GridColumn",
            "placeholder": "",
            "sortAs": "DEFAULT"
        }
    },
    "type": {
        "collection": false,
        "nested": false,
        "name": "string"
    },
    "label": "Status",
    "field": "status",
    "header": "Status",
    "exportable": true
  },
  {
    "active": false,
    "required": false,
    "id": "671",
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
    ],
    "label": "testsing btn in table",
    "field": "showHistory",
    "header": "testsing btn in table",
    "exportable": false
  },
  {
    "active": false,
    "required": false,
    "id": "672",
    "code": "petQuestionnaire",
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
            "imgSrc": "edit.png",
            "url": "",
            "target": ""
        }
    },
    "type": {
        "collection": false,
        "nested": false,
        "name": "string"
    },
    "label": "Pet Questionnaire",
    "field": "petQuestionnaire",
    "header": "Pet Questionnaire",
    "exportable": false
  },
  {
    "active": false,
    "required": false,
    "id": "673",
    "code": "vlmVisitItemLinks",
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
        "name": "VisitLineItem.VLMVisitItemLinks",
        "model": {
            "paramConfigIds": [
                "675",
                "676"
            ]
        }
    },
    "field": "vlmVisitItemLinks",
    "exportable": false
  }
  ];
  
  
export const tableElement: any = {
      "config": {
          "active": false,
          "required": false,
          "id": "705",
          "code": "visitBulkAction",
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
                  "pageSize": 3,
                  "browserBack": false,
                  "showAsLink": false,
                  "headerCheckboxToggleAllPages": false,
                  "rowSelection": true,
                  "postButtonUrl": "",
                  "pagination": true,
                  "dataEntryField": true,
                  "postButtonTargetPath": "ids",
                  "postButtonUri": "../actionCancelVisits",
                  "expandableRows": true,
                  "showHeader": true,
                  "postEventOnChange": false,
                  "lazyLoad": false,
                  "url": "",
                  "dataKey": "id",
                  "cssClass": "",
                  "clearAllFilters": true,
                  "postButtonLabel": "Cancel",
                  "alias": "Grid",
                  "onLoad": true,
                  "postButtonAlias": "",
                  "isTransient": false,
                  "postButton": true,
                  "export": true
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
                  "id": "708",
                  "type": {
                      "collection": false,
                      "nested": true,
                      "name": "VisitLineItem",
                      "model": {
                          "paramConfigIds": [
                              "673",
                              "674",
                              "675",
                              "676",
                              "677",
                              "678",
                              "679",
                              "680",
                              "681",
                              "685"
                          ]
                      }
                  }
              }
          }
      },
      "enabled": true,
      "visible": true,
      "activeValidationGroups": [],
      "collectionParams": [
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "678",
                  "code": "status",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": true,
                      "isHidden": false,
                      "name": "ViewConfig.GridColumn",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": true,
                          "rowExpander": false,
                          "expandable": true,
                          "applyValueStyles": false,
                          "sortable": true,
                          "filter": true,
                          "filterMode": "equals",
                          "cssClass": "",
                          "filterValue": "",
                          "datePattern": "",
                          "alias": "GridColumn",
                          "placeholder": "",
                          "sortAs": "DEFAULT"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Status",
                  "field": "status",
                  "header": "Status",
                  "exportable": true
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "678",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/status",
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
                      "text": "Status"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "679",
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
                  ],
                  "label": "testsing btn in table",
                  "field": "showHistory",
                  "header": "testsing btn in table",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "679",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/showHistory",
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
                      "text": "testsing btn in table"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "680",
                  "code": "petQuestionnaire",
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
                          "imgSrc": "edit.png",
                          "url": "",
                          "target": ""
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Pet Questionnaire",
                  "field": "petQuestionnaire",
                  "header": "Pet Questionnaire",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "680",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/petQuestionnaire",
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
                      "text": "Pet Questionnaire"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "681",
                  "code": "vlmVisitItemLinks",
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
                      "name": "VisitLineItem.VLMVisitItemLinks",
                      "model": {
                          "paramConfigIds": [
                              "683",
                              "684"
                          ]
                      }
                  },
                  "field": "vlmVisitItemLinks",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "681",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "683",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks/petQuestionnaire",
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
                                      "text": "Pet Questionnaire"
                                  }
                              ],
                              "elemLabels": {}
                          },
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "684",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks/petCareQuestionnaire",
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
                                      "text": "Pet Care Questionnaire"
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
                  "id": "685",
                  "code": "expandedRowContent1",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": false,
                      "isHidden": false,
                      "name": "ViewConfig.GridRowBody",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": false,
                          "cssClass": "",
                          "asynchronous": false,
                          "alias": "GridRowBody"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": true,
                      "name": "VisitLineItem.ExpandedRowContent1",
                      "model": {
                          "paramConfigIds": [
                              "691"
                          ]
                      }
                  },
                  "field": "expandedRowContent1",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "685",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/expandedRowContent1",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "691",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/expandedRowContent1/pets",
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
              "config": {
                  "active": false,
                  "required": false,
                  "id": "678",
                  "code": "status",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": true,
                      "isHidden": false,
                      "name": "ViewConfig.GridColumn",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": true,
                          "rowExpander": false,
                          "expandable": true,
                          "applyValueStyles": false,
                          "sortable": true,
                          "filter": true,
                          "filterMode": "equals",
                          "cssClass": "",
                          "filterValue": "",
                          "datePattern": "",
                          "alias": "GridColumn",
                          "placeholder": "",
                          "sortAs": "DEFAULT"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Status",
                  "field": "status",
                  "header": "Status",
                  "exportable": true
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "678",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/status",
              "type": {
                  "nested": false,
                  "name": "string",
                  "collection": false
              },
              "leafState": "Cancelled",
              "previousLeafState": "Cancelled",
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
              "config": {
                  "active": false,
                  "required": false,
                  "id": "679",
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
                  ],
                  "label": "testsing btn in table",
                  "field": "showHistory",
                  "header": "testsing btn in table",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "679",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/showHistory",
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
                      "text": "testsing btn in table"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "680",
                  "code": "petQuestionnaire",
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
                          "imgSrc": "edit.png",
                          "url": "",
                          "target": ""
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Pet Questionnaire",
                  "field": "petQuestionnaire",
                  "header": "Pet Questionnaire",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "680",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/petQuestionnaire",
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
                      "text": "Pet Questionnaire"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "681",
                  "code": "vlmVisitItemLinks",
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
                      "name": "VisitLineItem.VLMVisitItemLinks",
                      "model": {
                          "paramConfigIds": [
                              "683",
                              "684"
                          ]
                      }
                  },
                  "field": "vlmVisitItemLinks",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "681",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "683",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks/petQuestionnaire",
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
                                      "text": "Pet Questionnaire"
                                  }
                              ],
                              "elemLabels": {}
                          },
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "684",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks/petCareQuestionnaire",
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
                                      "text": "Pet Care Questionnaire"
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
                  "id": "685",
                  "code": "expandedRowContent1",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": false,
                      "isHidden": false,
                      "name": "ViewConfig.GridRowBody",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": false,
                          "cssClass": "",
                          "asynchronous": false,
                          "alias": "GridRowBody"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": true,
                      "name": "VisitLineItem.ExpandedRowContent1",
                      "model": {
                          "paramConfigIds": [
                              "691"
                          ]
                      }
                  },
                  "field": "expandedRowContent1",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "685",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/expandedRowContent1",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "691",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/expandedRowContent1/pets",
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
              "config": {
                  "active": false,
                  "required": false,
                  "id": "678",
                  "code": "status",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": true,
                      "isHidden": false,
                      "name": "ViewConfig.GridColumn",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": true,
                          "rowExpander": false,
                          "expandable": true,
                          "applyValueStyles": false,
                          "sortable": true,
                          "filter": true,
                          "filterMode": "equals",
                          "cssClass": "",
                          "filterValue": "",
                          "datePattern": "",
                          "alias": "GridColumn",
                          "placeholder": "",
                          "sortAs": "DEFAULT"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Status",
                  "field": "status",
                  "header": "Status",
                  "exportable": true
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "678",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/status",
              "type": {
                  "nested": false,
                  "name": "string",
                  "collection": false
              },
              "leafState": "Cancelled",
              "previousLeafState": "Cancelled",
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
              "config": {
                  "active": false,
                  "required": false,
                  "id": "679",
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
                  ],
                  "label": "testsing btn in table",
                  "field": "showHistory",
                  "header": "testsing btn in table",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "679",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/showHistory",
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
                      "text": "testsing btn in table"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "680",
                  "code": "petQuestionnaire",
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
                          "imgSrc": "edit.png",
                          "url": "",
                          "target": ""
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Pet Questionnaire",
                  "field": "petQuestionnaire",
                  "header": "Pet Questionnaire",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "680",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/petQuestionnaire",
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
                      "text": "Pet Questionnaire"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "681",
                  "code": "vlmVisitItemLinks",
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
                      "name": "VisitLineItem.VLMVisitItemLinks",
                      "model": {
                          "paramConfigIds": [
                              "683",
                              "684"
                          ]
                      }
                  },
                  "field": "vlmVisitItemLinks",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "681",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "683",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks/petQuestionnaire",
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
                                      "text": "Pet Questionnaire"
                                  }
                              ],
                              "elemLabels": {}
                          },
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "684",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks/petCareQuestionnaire",
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
                                      "text": "Pet Care Questionnaire"
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
                  "id": "685",
                  "code": "expandedRowContent1",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": false,
                      "isHidden": false,
                      "name": "ViewConfig.GridRowBody",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": false,
                          "cssClass": "",
                          "asynchronous": false,
                          "alias": "GridRowBody"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": true,
                      "name": "VisitLineItem.ExpandedRowContent1",
                      "model": {
                          "paramConfigIds": [
                              "691"
                          ]
                      }
                  },
                  "field": "expandedRowContent1",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "685",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/expandedRowContent1",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "691",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/expandedRowContent1/pets",
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
              "config": {
                  "active": false,
                  "required": false,
                  "id": "678",
                  "code": "status",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": true,
                      "isHidden": false,
                      "name": "ViewConfig.GridColumn",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": true,
                          "rowExpander": false,
                          "expandable": true,
                          "applyValueStyles": false,
                          "sortable": true,
                          "filter": true,
                          "filterMode": "equals",
                          "cssClass": "",
                          "filterValue": "",
                          "datePattern": "",
                          "alias": "GridColumn",
                          "placeholder": "",
                          "sortAs": "DEFAULT"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Status",
                  "field": "status",
                  "header": "Status",
                  "exportable": true
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "678",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/status",
              "type": {
                  "nested": false,
                  "name": "string",
                  "collection": false
              },
              "leafState": "Cancelled",
              "previousLeafState": "Cancelled",
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
              "config": {
                  "active": false,
                  "required": false,
                  "id": "679",
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
                  ],
                  "label": "testsing btn in table",
                  "field": "showHistory",
                  "header": "testsing btn in table",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "679",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/showHistory",
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
                      "text": "testsing btn in table"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "680",
                  "code": "petQuestionnaire",
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
                          "imgSrc": "edit.png",
                          "url": "",
                          "target": ""
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Pet Questionnaire",
                  "field": "petQuestionnaire",
                  "header": "Pet Questionnaire",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "680",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/petQuestionnaire",
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
                      "text": "Pet Questionnaire"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "681",
                  "code": "vlmVisitItemLinks",
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
                      "name": "VisitLineItem.VLMVisitItemLinks",
                      "model": {
                          "paramConfigIds": [
                              "683",
                              "684"
                          ]
                      }
                  },
                  "field": "vlmVisitItemLinks",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "681",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "683",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks/petQuestionnaire",
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
                                      "text": "Pet Questionnaire"
                                  }
                              ],
                              "elemLabels": {}
                          },
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "684",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks/petCareQuestionnaire",
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
                                      "text": "Pet Care Questionnaire"
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
                  "id": "685",
                  "code": "expandedRowContent1",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": false,
                      "isHidden": false,
                      "name": "ViewConfig.GridRowBody",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": false,
                          "cssClass": "",
                          "asynchronous": false,
                          "alias": "GridRowBody"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": true,
                      "name": "VisitLineItem.ExpandedRowContent1",
                      "model": {
                          "paramConfigIds": [
                              "691"
                          ]
                      }
                  },
                  "field": "expandedRowContent1",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "685",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/expandedRowContent1",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "691",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/expandedRowContent1/pets",
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
              "config": {
                  "active": false,
                  "required": false,
                  "id": "678",
                  "code": "status",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": true,
                      "isHidden": false,
                      "name": "ViewConfig.GridColumn",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": true,
                          "rowExpander": false,
                          "expandable": true,
                          "applyValueStyles": false,
                          "sortable": true,
                          "filter": true,
                          "filterMode": "equals",
                          "cssClass": "",
                          "filterValue": "",
                          "datePattern": "",
                          "alias": "GridColumn",
                          "placeholder": "",
                          "sortAs": "DEFAULT"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Status",
                  "field": "status",
                  "header": "Status",
                  "exportable": true
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "678",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/status",
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
              "config": {
                  "active": false,
                  "required": false,
                  "id": "679",
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
                  ],
                  "label": "testsing btn in table",
                  "field": "showHistory",
                  "header": "testsing btn in table",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "679",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/showHistory",
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
                      "text": "testsing btn in table"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "680",
                  "code": "petQuestionnaire",
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
                          "imgSrc": "edit.png",
                          "url": "",
                          "target": ""
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Pet Questionnaire",
                  "field": "petQuestionnaire",
                  "header": "Pet Questionnaire",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "680",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/petQuestionnaire",
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
                      "text": "Pet Questionnaire"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "681",
                  "code": "vlmVisitItemLinks",
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
                      "name": "VisitLineItem.VLMVisitItemLinks",
                      "model": {
                          "paramConfigIds": [
                              "683",
                              "684"
                          ]
                      }
                  },
                  "field": "vlmVisitItemLinks",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "681",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "683",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks/petQuestionnaire",
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
                                      "text": "Pet Questionnaire"
                                  }
                              ],
                              "elemLabels": {}
                          },
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "684",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks/petCareQuestionnaire",
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
                                      "text": "Pet Care Questionnaire"
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
                  "id": "685",
                  "code": "expandedRowContent1",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": false,
                      "isHidden": false,
                      "name": "ViewConfig.GridRowBody",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": false,
                          "cssClass": "",
                          "asynchronous": false,
                          "alias": "GridRowBody"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": true,
                      "name": "VisitLineItem.ExpandedRowContent1",
                      "model": {
                          "paramConfigIds": [
                              "691"
                          ]
                      }
                  },
                  "field": "expandedRowContent1",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "685",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/expandedRowContent1",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "691",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/expandedRowContent1/pets",
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
              "config": {
                  "active": false,
                  "required": false,
                  "id": "678",
                  "code": "status",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": true,
                      "isHidden": false,
                      "name": "ViewConfig.GridColumn",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": true,
                          "rowExpander": false,
                          "expandable": true,
                          "applyValueStyles": false,
                          "sortable": true,
                          "filter": true,
                          "filterMode": "equals",
                          "cssClass": "",
                          "filterValue": "",
                          "datePattern": "",
                          "alias": "GridColumn",
                          "placeholder": "",
                          "sortAs": "DEFAULT"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Status",
                  "field": "status",
                  "header": "Status",
                  "exportable": true
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "678",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/status",
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
              "config": {
                  "active": false,
                  "required": false,
                  "id": "679",
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
                  ],
                  "label": "testsing btn in table",
                  "field": "showHistory",
                  "header": "testsing btn in table",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "679",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/showHistory",
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
                      "text": "testsing btn in table"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "680",
                  "code": "petQuestionnaire",
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
                          "imgSrc": "edit.png",
                          "url": "",
                          "target": ""
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": false,
                      "name": "string"
                  },
                  "label": "Pet Questionnaire",
                  "field": "petQuestionnaire",
                  "header": "Pet Questionnaire",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "680",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/petQuestionnaire",
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
                      "text": "Pet Questionnaire"
                  }
              ],
              "elemLabels": {}
          },
          {
              "config": {
                  "active": false,
                  "required": false,
                  "id": "681",
                  "code": "vlmVisitItemLinks",
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
                      "name": "VisitLineItem.VLMVisitItemLinks",
                      "model": {
                          "paramConfigIds": [
                              "683",
                              "684"
                          ]
                      }
                  },
                  "field": "vlmVisitItemLinks",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "681",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "683",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks/petQuestionnaire",
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
                                      "text": "Pet Questionnaire"
                                  }
                              ],
                              "elemLabels": {}
                          },
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "684",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks/petCareQuestionnaire",
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
                                      "text": "Pet Care Questionnaire"
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
                  "id": "685",
                  "code": "expandedRowContent1",
                  "validations": null,
                  "uiNatures": [],
                  "uiStyles": {
                      "isLink": false,
                      "isHidden": false,
                      "name": "ViewConfig.GridRowBody",
                      "attributes": {
                          "hidden": false,
                          "readOnly": false,
                          "submitButton": true,
                          "showName": true,
                          "pageSize": 25,
                          "browserBack": false,
                          "showAsLink": false,
                          "cssClass": "",
                          "asynchronous": false,
                          "alias": "GridRowBody"
                      }
                  },
                  "type": {
                      "collection": false,
                      "nested": true,
                      "name": "VisitLineItem.ExpandedRowContent1",
                      "model": {
                          "paramConfigIds": [
                              "691"
                          ]
                      }
                  },
                  "field": "expandedRowContent1",
                  "exportable": false
              },
              "enabled": true,
              "visible": true,
              "activeValidationGroups": [],
              "collectionParams": [],
              "configId": "685",
              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/expandedRowContent1",
              "type": {
                  "model": {
                      "params": [
                          {
                              "enabled": true,
                              "visible": true,
                              "activeValidationGroups": [],
                              "collectionParams": [],
                              "configId": "691",
                              "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/expandedRowContent1/pets",
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
      "configId": "705",
      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction",
      "type": {
          "model": {
              "params": [
                  {
                      "enabled": true,
                      "visible": true,
                      "activeValidationGroups": [],
                      "collectionParams": [],
                      "configId": "708",
                      "collectionElem": true,
                      "elemId": "0",
                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0",
                      "type": {
                          "model": {
                              "params": [
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "673",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/id",
                                      "type": {},
                                      "leafState": 1,
                                      "previousLeafState": 1,
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
                                      "configId": "674",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/appointment",
                                      "type": {},
                                      "leafState": "2018-09-21T04:00:00.000Z",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Appointment Date"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "675",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/ownerName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "test 1",
                                      "previousLeafState": "test 1",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Owner's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "676",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/petName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "test pet",
                                      "previousLeafState": "test pet",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Pet's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "677",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/reasonForVisit",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "regular visit",
                                      "previousLeafState": "regular visit",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Reason For Visit"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "678",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/status",
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
                                      "configId": "679",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/showHistory",
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
                                              "text": "testsing btn in table"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "680",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/petQuestionnaire",
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
                                              "text": "Pet Questionnaire"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "681",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "683",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks/petQuestionnaire",
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
                                                              "text": "Pet Questionnaire"
                                                          }
                                                      ],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "684",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                              "text": "Pet Care Questionnaire"
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
                                      "configId": "685",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/expandedRowContent1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "691",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/expandedRowContent1/pets",
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
                          "id": 1,
                          "appointment": "2018-09-21T04:00:00.000Z",
                          "ownerName": "test 1",
                          "petName": "test pet",
                          "reasonForVisit": "regular visit",
                          "status": "",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "0"
                      },
                      "previousLeafState": {
                          "id": 1,
                          "appointment": "2018-09-21T04:00:00.000Z",
                          "ownerName": "test 1",
                          "petName": "test pet",
                          "reasonForVisit": "regular visit",
                          "status": "",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
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
                      "configId": "708",
                      "collectionElem": true,
                      "elemId": "1",
                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1",
                      "type": {
                          "model": {
                              "params": [
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "673",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/id",
                                      "type": {},
                                      "leafState": 2,
                                      "previousLeafState": 2,
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
                                      "configId": "674",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/appointment",
                                      "type": {},
                                      "leafState": null,
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Appointment Date"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "675",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/ownerName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "null",
                                      "previousLeafState": "null",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Owner's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "676",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/petName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "cat",
                                      "previousLeafState": "cat",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Pet's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "677",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/reasonForVisit",
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
                                              "text": "Reason For Visit"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "678",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/status",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "Cancelled",
                                      "previousLeafState": "Cancelled",
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
                                      "configId": "679",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/showHistory",
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
                                              "text": "testsing btn in table"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "680",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/petQuestionnaire",
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
                                              "text": "Pet Questionnaire"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "681",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "683",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks/petQuestionnaire",
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
                                                              "text": "Pet Questionnaire"
                                                          }
                                                      ],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "684",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                              "text": "Pet Care Questionnaire"
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
                                      "configId": "685",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/expandedRowContent1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "691",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/expandedRowContent1/pets",
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
                          "id": 2,
                          "ownerName": "null",
                          "petName": "cat",
                          "status": "Cancelled",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "1",
                          "appointment": null
                      },
                      "previousLeafState": {
                          "id": 2,
                          "ownerName": "null",
                          "petName": "cat",
                          "status": "Cancelled",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "1",
                          "appointment": null
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
                      "configId": "708",
                      "collectionElem": true,
                      "elemId": "2",
                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2",
                      "type": {
                          "model": {
                              "params": [
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "673",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/id",
                                      "type": {},
                                      "leafState": 3,
                                      "previousLeafState": 3,
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
                                      "configId": "674",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/appointment",
                                      "type": {},
                                      "leafState": null,
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Appointment Date"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "675",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/ownerName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "null",
                                      "previousLeafState": "null",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Owner's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "676",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/petName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "cat",
                                      "previousLeafState": "cat",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Pet's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "677",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/reasonForVisit",
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
                                              "text": "Reason For Visit"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "678",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/status",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "Cancelled",
                                      "previousLeafState": "Cancelled",
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
                                      "configId": "679",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/showHistory",
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
                                              "text": "testsing btn in table"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "680",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/petQuestionnaire",
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
                                              "text": "Pet Questionnaire"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "681",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "683",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks/petQuestionnaire",
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
                                                              "text": "Pet Questionnaire"
                                                          }
                                                      ],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "684",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                              "text": "Pet Care Questionnaire"
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
                                      "configId": "685",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/expandedRowContent1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "691",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/expandedRowContent1/pets",
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
                          "id": 3,
                          "ownerName": "null",
                          "petName": "cat",
                          "status": "Cancelled",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "2",
                          "appointment": null
                      },
                      "previousLeafState": {
                          "id": 3,
                          "ownerName": "null",
                          "petName": "cat",
                          "status": "Cancelled",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "2",
                          "appointment": null
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
                      "configId": "708",
                      "collectionElem": true,
                      "elemId": "3",
                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3",
                      "type": {
                          "model": {
                              "params": [
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "673",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/id",
                                      "type": {},
                                      "leafState": 4,
                                      "previousLeafState": 4,
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
                                      "configId": "674",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/appointment",
                                      "type": {},
                                      "leafState": null,
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Appointment Date"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "675",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/ownerName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "null",
                                      "previousLeafState": "null",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Owner's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "676",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/petName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "cat",
                                      "previousLeafState": "cat",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Pet's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "677",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/reasonForVisit",
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
                                              "text": "Reason For Visit"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "678",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/status",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "Cancelled",
                                      "previousLeafState": "Cancelled",
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
                                      "configId": "679",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/showHistory",
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
                                              "text": "testsing btn in table"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "680",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/petQuestionnaire",
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
                                              "text": "Pet Questionnaire"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "681",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "683",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks/petQuestionnaire",
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
                                                              "text": "Pet Questionnaire"
                                                          }
                                                      ],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "684",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                              "text": "Pet Care Questionnaire"
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
                                      "configId": "685",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/expandedRowContent1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "691",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/expandedRowContent1/pets",
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
                          "id": 4,
                          "ownerName": "null",
                          "petName": "cat",
                          "status": "Cancelled",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "3",
                          "appointment": null
                      },
                      "previousLeafState": {
                          "id": 4,
                          "ownerName": "null",
                          "petName": "cat",
                          "status": "Cancelled",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "3",
                          "appointment": null
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
                      "configId": "708",
                      "collectionElem": true,
                      "elemId": "4",
                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4",
                      "type": {
                          "model": {
                              "params": [
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "673",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/id",
                                      "type": {},
                                      "leafState": 5,
                                      "previousLeafState": 5,
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
                                      "configId": "674",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/appointment",
                                      "type": {},
                                      "leafState": null,
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Appointment Date"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "675",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/ownerName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "null",
                                      "previousLeafState": "null",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Owner's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "676",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/petName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "cat",
                                      "previousLeafState": "cat",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Pet's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "677",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/reasonForVisit",
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
                                              "text": "Reason For Visit"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "678",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/status",
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
                                      "configId": "679",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/showHistory",
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
                                              "text": "testsing btn in table"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "680",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/petQuestionnaire",
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
                                              "text": "Pet Questionnaire"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "681",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "683",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks/petQuestionnaire",
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
                                                              "text": "Pet Questionnaire"
                                                          }
                                                      ],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "684",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                              "text": "Pet Care Questionnaire"
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
                                      "configId": "685",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/expandedRowContent1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "691",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/expandedRowContent1/pets",
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
                          "id": 5,
                          "ownerName": "null",
                          "petName": "cat",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "4",
                          "appointment": null
                      },
                      "previousLeafState": {
                          "id": 5,
                          "ownerName": "null",
                          "petName": "cat",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "4",
                          "appointment": null
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
                      "configId": "708",
                      "collectionElem": true,
                      "elemId": "5",
                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5",
                      "type": {
                          "model": {
                              "params": [
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "673",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/id",
                                      "type": {},
                                      "leafState": 6,
                                      "previousLeafState": 6,
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
                                      "configId": "674",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/appointment",
                                      "type": {},
                                      "leafState": null,
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Appointment Date"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "675",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/ownerName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "null",
                                      "previousLeafState": "null",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Owner's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "676",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/petName",
                                      "type": {
                                          "nested": false,
                                          "name": "string",
                                          "collection": false
                                      },
                                      "leafState": "cat",
                                      "previousLeafState": "cat",
                                      "message": [],
                                      "values": [],
                                      "labels": [
                                          {
                                              "locale": "en-US",
                                              "text": "Pet's name"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "677",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/reasonForVisit",
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
                                              "text": "Reason For Visit"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "678",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/status",
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
                                      "configId": "679",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/showHistory",
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
                                              "text": "testsing btn in table"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "680",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/petQuestionnaire",
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
                                              "text": "Pet Questionnaire"
                                          }
                                      ],
                                      "elemLabels": {}
                                  },
                                  {
                                      "enabled": true,
                                      "visible": true,
                                      "activeValidationGroups": [],
                                      "collectionParams": [],
                                      "configId": "681",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "683",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks/petQuestionnaire",
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
                                                              "text": "Pet Questionnaire"
                                                          }
                                                      ],
                                                      "elemLabels": {}
                                                  },
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "684",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                              "text": "Pet Care Questionnaire"
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
                                      "configId": "685",
                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/expandedRowContent1",
                                      "type": {
                                          "model": {
                                              "params": [
                                                  {
                                                      "enabled": true,
                                                      "visible": true,
                                                      "activeValidationGroups": [],
                                                      "collectionParams": [],
                                                      "configId": "691",
                                                      "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/expandedRowContent1/pets",
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
                          "id": 6,
                          "ownerName": "null",
                          "petName": "cat",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "5",
                          "appointment": null
                      },
                      "previousLeafState": {
                          "id": 6,
                          "ownerName": "null",
                          "petName": "cat",
                          "vlmVisitItemLinks": {},
                          "expandedRowContent1": {
                              "pets": []
                          },
                          "elemId": "5",
                          "appointment": null
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
          "totalElements": 6,
          "size": 0,
          "number": 0,
          "first": true,
          "numberOfElements": 6
      },
      "gridData": {
          "collectionParams": [],
          "leafState": [
              {
                  "id": 1,
                  "appointment": "2018-09-21T04:00:00.000Z",
                  "ownerName": "test 1",
                  "petName": "test pet",
                  "reasonForVisit": "regular visit",
                  "status": "",
                  "vlmVisitItemLinks": {},
                  "expandedRowContent1": {
                      "pets": []
                  },
                  "elemId": "0"
              },
              {
                  "id": 2,
                  "ownerName": "null",
                  "petName": "cat",
                  "status": "Cancelled",
                  "vlmVisitItemLinks": {},
                  "expandedRowContent1": {
                      "pets": []
                  },
                  "elemId": "1",
                  "appointment": null
              }
          ],
          "stateMap": [
              {
                  "id": {},
                  "appointment": {},
                  "ownerName": {},
                  "petName": {},
                  "reasonForVisit": {},
                  "status": {},
                  "showHistory": {},
                  "petQuestionnaire": {},
                  "vlmVisitItemLinks": {},
                  "expandedRowContent1": {}
              },
              {
                  "id": {},
                  "appointment": {},
                  "ownerName": {},
                  "petName": {},
                  "reasonForVisit": {},
                  "status": {},
                  "showHistory": {},
                  "petQuestionnaire": {},
                  "vlmVisitItemLinks": {},
                  "expandedRowContent1": {}
              },
              {
                  "id": {},
                  "appointment": {},
                  "ownerName": {},
                  "petName": {},
                  "reasonForVisit": {},
                  "status": {},
                  "showHistory": {},
                  "petQuestionnaire": {},
                  "vlmVisitItemLinks": {},
                  "expandedRowContent1": {}
              },
              {
                  "id": {},
                  "appointment": {},
                  "ownerName": {},
                  "petName": {},
                  "reasonForVisit": {},
                  "status": {},
                  "showHistory": {},
                  "petQuestionnaire": {},
                  "vlmVisitItemLinks": {},
                  "expandedRowContent1": {}
              },
              {
                  "id": {},
                  "appointment": {},
                  "ownerName": {},
                  "petName": {},
                  "reasonForVisit": {},
                  "status": {},
                  "showHistory": {},
                  "petQuestionnaire": {},
                  "vlmVisitItemLinks": {},
                  "expandedRowContent1": {}
              },
              {
                  "id": {},
                  "appointment": {},
                  "ownerName": {},
                  "petName": {},
                  "reasonForVisit": {},
                  "status": {},
                  "showHistory": {},
                  "petQuestionnaire": {},
                  "vlmVisitItemLinks": {},
                  "expandedRowContent1": {}
              }
          ]
      },
      "message": [],
      "values": [],
      "labels": [
          {
              "locale": "en-US",
              "text": "Bulk Visits",
              "helpText": "testing tooltip"
          }
      ],
      "elemLabels": {
          "get": () => {}
      }
  };

  export const tableGridValueUpdate: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "692",
        "code": "visitBulkAction",
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
                "pageSize": 3,
                "browserBack": false,
                "showAsLink": false,
                "headerCheckboxToggleAllPages": false,
                "rowSelection": true,
                "postButtonUrl": "",
                "pagination": true,
                "dataEntryField": true,
                "postButtonTargetPath": "ids",
                "postButtonUri": "../actionCancelVisits",
                "expandableRows": true,
                "showHeader": true,
                "postEventOnChange": false,
                "lazyLoad": false,
                "url": "",
                "dataKey": "id",
                "cssClass": "",
                "clearAllFilters": true,
                "postButtonLabel": "Cancel",
                "alias": "Grid",
                "onLoad": true,
                "postButtonAlias": "",
                "isTransient": false,
                "postButton": true,
                "export": true
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
                "id": "695",
                "type": {
                    "collection": false,
                    "nested": true,
                    "name": "VisitLineItem",
                    "model": {
                        "paramConfigIds": [
                            "660",
                            "661",
                            "662",
                            "663",
                            "664",
                            "665",
                            "666",
                            "667",
                            "668",
                            "672"
                        ]
                    }
                }
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "665",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/status",
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
            "configId": "666",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/showHistory",
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
                    "text": "testsing btn in table"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "667",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/petQuestionnaire",
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
                    "text": "Pet Questionnaire"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "668",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "670",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks/petQuestionnaire",
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
                                    "text": "Pet Questionnaire"
                                }
                            ],
                            "elemLabels": {}
                        },
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "671",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks/petCareQuestionnaire",
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
                                    "text": "Pet Care Questionnaire"
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
            "configId": "672",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/expandedRowContent1",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "678",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/expandedRowContent1/pets",
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
            "configId": "665",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/status",
            "type": {
                "nested": false,
                "name": "string",
                "collection": false
            },
            "leafState": "Cancelled",
            "previousLeafState": "Cancelled",
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
            "configId": "666",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/showHistory",
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
                    "text": "testsing btn in table"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "667",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/petQuestionnaire",
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
                    "text": "Pet Questionnaire"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "668",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "670",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks/petQuestionnaire",
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
                                    "text": "Pet Questionnaire"
                                }
                            ],
                            "elemLabels": {}
                        },
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "671",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks/petCareQuestionnaire",
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
                                    "text": "Pet Care Questionnaire"
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
            "configId": "672",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/expandedRowContent1",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "678",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/expandedRowContent1/pets",
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
            "configId": "665",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/status",
            "type": {
                "nested": false,
                "name": "string",
                "collection": false
            },
            "leafState": "Cancelled",
            "previousLeafState": "Cancelled",
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
            "configId": "666",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/showHistory",
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
                    "text": "testsing btn in table"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "667",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/petQuestionnaire",
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
                    "text": "Pet Questionnaire"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "668",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "670",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks/petQuestionnaire",
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
                                    "text": "Pet Questionnaire"
                                }
                            ],
                            "elemLabels": {}
                        },
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "671",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks/petCareQuestionnaire",
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
                                    "text": "Pet Care Questionnaire"
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
            "configId": "672",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/expandedRowContent1",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "678",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/expandedRowContent1/pets",
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
            "configId": "665",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/status",
            "type": {
                "nested": false,
                "name": "string",
                "collection": false
            },
            "leafState": "Cancelled",
            "previousLeafState": "Cancelled",
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
            "configId": "666",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/showHistory",
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
                    "text": "testsing btn in table"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "667",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/petQuestionnaire",
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
                    "text": "Pet Questionnaire"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "668",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "670",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks/petQuestionnaire",
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
                                    "text": "Pet Questionnaire"
                                }
                            ],
                            "elemLabels": {}
                        },
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "671",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks/petCareQuestionnaire",
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
                                    "text": "Pet Care Questionnaire"
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
            "configId": "672",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/expandedRowContent1",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "678",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/expandedRowContent1/pets",
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
            "configId": "665",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/status",
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
            "configId": "666",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/showHistory",
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
                    "text": "testsing btn in table"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "667",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/petQuestionnaire",
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
                    "text": "Pet Questionnaire"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "668",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "670",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks/petQuestionnaire",
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
                                    "text": "Pet Questionnaire"
                                }
                            ],
                            "elemLabels": {}
                        },
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "671",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks/petCareQuestionnaire",
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
                                    "text": "Pet Care Questionnaire"
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
            "configId": "672",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/expandedRowContent1",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "678",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/expandedRowContent1/pets",
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
            "configId": "665",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/status",
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
            "configId": "666",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/showHistory",
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
                    "text": "testsing btn in table"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "667",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/petQuestionnaire",
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
                    "text": "Pet Questionnaire"
                }
            ],
            "elemLabels": {}
        },
        {
            "enabled": true,
            "visible": true,
            "activeValidationGroups": [],
            "collectionParams": [],
            "configId": "668",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "670",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks/petQuestionnaire",
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
                                    "text": "Pet Questionnaire"
                                }
                            ],
                            "elemLabels": {}
                        },
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "671",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks/petCareQuestionnaire",
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
                                    "text": "Pet Care Questionnaire"
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
            "configId": "672",
            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/expandedRowContent1",
            "type": {
                "model": {
                    "params": [
                        {
                            "enabled": true,
                            "visible": true,
                            "activeValidationGroups": [],
                            "collectionParams": [],
                            "configId": "678",
                            "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/expandedRowContent1/pets",
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
    "configId": "692",
    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction",
    "type": {
        "model": {
            "params": [
                {
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "695",
                    "collectionElem": true,
                    "elemId": "0",
                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "660",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/id",
                                    "type": {},
                                    "leafState": 1,
                                    "previousLeafState": 1,
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
                                    "configId": "661",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/appointment",
                                    "type": {},
                                    "leafState": "2018-09-21T04:00:00.000Z",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Appointment Date"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "662",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/ownerName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "test 1",
                                    "previousLeafState": "test 1",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Owner's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "663",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/petName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "test pet",
                                    "previousLeafState": "test pet",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Pet's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "664",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/reasonForVisit",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "regular visit",
                                    "previousLeafState": "regular visit",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Reason For Visit"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "665",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/status",
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
                                    "configId": "666",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/showHistory",
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
                                            "text": "testsing btn in table"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "667",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/petQuestionnaire",
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
                                            "text": "Pet Questionnaire"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "668",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "670",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks/petQuestionnaire",
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
                                                            "text": "Pet Questionnaire"
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "671",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                            "text": "Pet Care Questionnaire"
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
                                    "configId": "672",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/expandedRowContent1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "678",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/0/expandedRowContent1/pets",
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
                        "id": 1,
                        "appointment": "2018-09-21T04:00:00.000Z",
                        "ownerName": "test 1",
                        "petName": "test pet",
                        "reasonForVisit": "regular visit",
                        "status": "",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "0"
                    },
                    "previousLeafState": {
                        "id": 1,
                        "appointment": "2018-09-21T04:00:00.000Z",
                        "ownerName": "test 1",
                        "petName": "test pet",
                        "reasonForVisit": "regular visit",
                        "status": "",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
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
                    "configId": "695",
                    "collectionElem": true,
                    "elemId": "1",
                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "660",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/id",
                                    "type": {},
                                    "leafState": 2,
                                    "previousLeafState": 2,
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
                                    "configId": "661",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/appointment",
                                    "type": {},
                                    "leafState": null,
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Appointment Date"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "662",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/ownerName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "null",
                                    "previousLeafState": "null",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Owner's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "663",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/petName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "cat",
                                    "previousLeafState": "cat",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Pet's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "664",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/reasonForVisit",
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
                                            "text": "Reason For Visit"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "665",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/status",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "Cancelled",
                                    "previousLeafState": "Cancelled",
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
                                    "configId": "666",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/showHistory",
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
                                            "text": "testsing btn in table"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "667",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/petQuestionnaire",
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
                                            "text": "Pet Questionnaire"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "668",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "670",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks/petQuestionnaire",
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
                                                            "text": "Pet Questionnaire"
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "671",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                            "text": "Pet Care Questionnaire"
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
                                    "configId": "672",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/expandedRowContent1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "678",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/1/expandedRowContent1/pets",
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
                        "id": 2,
                        "ownerName": "null",
                        "petName": "cat",
                        "status": "Cancelled",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "1",
                        "appointment": null
                    },
                    "previousLeafState": {
                        "id": 2,
                        "ownerName": "null",
                        "petName": "cat",
                        "status": "Cancelled",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "1",
                        "appointment": null
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
                    "configId": "695",
                    "collectionElem": true,
                    "elemId": "2",
                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "660",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/id",
                                    "type": {},
                                    "leafState": 3,
                                    "previousLeafState": 3,
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
                                    "configId": "661",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/appointment",
                                    "type": {},
                                    "leafState": null,
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Appointment Date"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "662",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/ownerName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "null",
                                    "previousLeafState": "null",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Owner's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "663",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/petName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "cat",
                                    "previousLeafState": "cat",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Pet's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "664",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/reasonForVisit",
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
                                            "text": "Reason For Visit"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "665",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/status",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "Cancelled",
                                    "previousLeafState": "Cancelled",
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
                                    "configId": "666",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/showHistory",
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
                                            "text": "testsing btn in table"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "667",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/petQuestionnaire",
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
                                            "text": "Pet Questionnaire"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "668",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "670",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks/petQuestionnaire",
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
                                                            "text": "Pet Questionnaire"
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "671",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                            "text": "Pet Care Questionnaire"
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
                                    "configId": "672",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/expandedRowContent1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "678",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/2/expandedRowContent1/pets",
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
                        "id": 3,
                        "ownerName": "null",
                        "petName": "cat",
                        "status": "Cancelled",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "2",
                        "appointment": null
                    },
                    "previousLeafState": {
                        "id": 3,
                        "ownerName": "null",
                        "petName": "cat",
                        "status": "Cancelled",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "2",
                        "appointment": null
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
                    "configId": "695",
                    "collectionElem": true,
                    "elemId": "3",
                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "660",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/id",
                                    "type": {},
                                    "leafState": 4,
                                    "previousLeafState": 4,
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
                                    "configId": "661",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/appointment",
                                    "type": {},
                                    "leafState": null,
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Appointment Date"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "662",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/ownerName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "null",
                                    "previousLeafState": "null",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Owner's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "663",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/petName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "cat",
                                    "previousLeafState": "cat",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Pet's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "664",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/reasonForVisit",
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
                                            "text": "Reason For Visit"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "665",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/status",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "Cancelled",
                                    "previousLeafState": "Cancelled",
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
                                    "configId": "666",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/showHistory",
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
                                            "text": "testsing btn in table"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "667",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/petQuestionnaire",
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
                                            "text": "Pet Questionnaire"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "668",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "670",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks/petQuestionnaire",
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
                                                            "text": "Pet Questionnaire"
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "671",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                            "text": "Pet Care Questionnaire"
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
                                    "configId": "672",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/expandedRowContent1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "678",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/3/expandedRowContent1/pets",
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
                        "id": 4,
                        "ownerName": "null",
                        "petName": "cat",
                        "status": "Cancelled",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "3",
                        "appointment": null
                    },
                    "previousLeafState": {
                        "id": 4,
                        "ownerName": "null",
                        "petName": "cat",
                        "status": "Cancelled",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "3",
                        "appointment": null
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
                    "configId": "695",
                    "collectionElem": true,
                    "elemId": "4",
                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "660",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/id",
                                    "type": {},
                                    "leafState": 5,
                                    "previousLeafState": 5,
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
                                    "configId": "661",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/appointment",
                                    "type": {},
                                    "leafState": null,
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Appointment Date"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "662",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/ownerName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "null",
                                    "previousLeafState": "null",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Owner's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "663",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/petName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "cat",
                                    "previousLeafState": "cat",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Pet's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "664",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/reasonForVisit",
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
                                            "text": "Reason For Visit"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "665",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/status",
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
                                    "configId": "666",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/showHistory",
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
                                            "text": "testsing btn in table"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "667",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/petQuestionnaire",
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
                                            "text": "Pet Questionnaire"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "668",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "670",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks/petQuestionnaire",
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
                                                            "text": "Pet Questionnaire"
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "671",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                            "text": "Pet Care Questionnaire"
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
                                    "configId": "672",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/expandedRowContent1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "678",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/4/expandedRowContent1/pets",
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
                        "id": 5,
                        "ownerName": "null",
                        "petName": "cat",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "4",
                        "appointment": null
                    },
                    "previousLeafState": {
                        "id": 5,
                        "ownerName": "null",
                        "petName": "cat",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "4",
                        "appointment": null
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
                    "configId": "695",
                    "collectionElem": true,
                    "elemId": "5",
                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "660",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/id",
                                    "type": {},
                                    "leafState": 6,
                                    "previousLeafState": 6,
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
                                    "configId": "661",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/appointment",
                                    "type": {},
                                    "leafState": null,
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Appointment Date"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "662",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/ownerName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "null",
                                    "previousLeafState": "null",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Owner's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "663",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/petName",
                                    "type": {
                                        "nested": false,
                                        "name": "string",
                                        "collection": false
                                    },
                                    "leafState": "cat",
                                    "previousLeafState": "cat",
                                    "message": [],
                                    "values": [],
                                    "labels": [
                                        {
                                            "locale": "en-US",
                                            "text": "Pet's name"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "664",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/reasonForVisit",
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
                                            "text": "Reason For Visit"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "665",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/status",
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
                                    "configId": "666",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/showHistory",
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
                                            "text": "testsing btn in table"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "667",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/petQuestionnaire",
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
                                            "text": "Pet Questionnaire"
                                        }
                                    ],
                                    "elemLabels": {}
                                },
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "668",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "670",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks/petQuestionnaire",
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
                                                            "text": "Pet Questionnaire"
                                                        }
                                                    ],
                                                    "elemLabels": {}
                                                },
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "671",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/vlmVisitItemLinks/petCareQuestionnaire",
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
                                                            "text": "Pet Care Questionnaire"
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
                                    "configId": "672",
                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/expandedRowContent1",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "678",
                                                    "path": "/visitlandingview/vpVisitsBulkAction/vtVisitBulkAction/vsVisitsBulkAction/visitBulkAction/5/expandedRowContent1/pets",
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
                        "id": 6,
                        "ownerName": "null",
                        "petName": "cat",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "5",
                        "appointment": null
                    },
                    "previousLeafState": {
                        "id": 6,
                        "ownerName": "null",
                        "petName": "cat",
                        "vlmVisitItemLinks": {},
                        "expandedRowContent1": {
                            "pets": []
                        },
                        "elemId": "5",
                        "appointment": null
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
        "totalElements": 6,
        "size": 0,
        "number": 0,
        "first": true,
        "numberOfElements": 6
    },
    "gridData": {
        "leafState": [
            {
                "id": 1,
                "appointment": "2018-09-21T04:00:00.000Z",
                "ownerName": "test 1",
                "petName": "test pet",
                "reasonForVisit": "regular visit",
                "status": "",
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {
                    "pets": []
                },
                "elemId": "0",
                "nestedGridParam": [
                    null,
                    null,
                    null,
                    null,
                    null
                ]
            },
            {
                "id": 2,
                "ownerName": "gridvalueupdate",
                "petName": "gridvalueupdate",
                "status": "gridvalueupdate",
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {
                    "pets": []
                },
                "elemId": "1",
                "nestedGridParam": [
                    null,
                    null,
                    null,
                    null,
                    null
                ],
                "appointment": null
            },
            {
                "id": 3,
                "ownerName": "gridvalueupdate1",
                "petName": "gridvalueupdate1",
                "status": "gridvalueupdate1",
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {
                    "pets": []
                },
                "elemId": "2",
                "nestedGridParam": [
                    null,
                    null,
                    null,
                    null,
                    null
                ],
                "appointment": null
            },
            {
                "id": 4,
                "ownerName": "null",
                "petName": "cat",
                "status": "Cancelled",
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {
                    "pets": []
                },
                "elemId": "3",
                "nestedGridParam": [
                    null,
                    null,
                    null,
                    null,
                    null
                ],
                "appointment": null
            },
            {
                "id": 5,
                "ownerName": "null",
                "petName": "cat",
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {
                    "pets": []
                },
                "elemId": "4",
                "nestedGridParam": [
                    null,
                    null,
                    null,
                    null,
                    null
                ],
                "appointment": null
            },
            {
                "id": 6,
                "ownerName": "null",
                "petName": "cat",
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {
                    "pets": []
                },
                "elemId": "5",
                "nestedGridParam": [
                    null,
                    null,
                    null,
                    null,
                    null
                ],
                "appointment": null
            }
        ],
        "stateMap": [
            {
                "id": {},
                "appointment": {},
                "ownerName": {},
                "petName": {},
                "reasonForVisit": {},
                "status": {},
                "showHistory": {},
                "petQuestionnaire": {},
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {}
            },
            {
                "id": {},
                "appointment": {},
                "ownerName": {},
                "petName": {},
                "reasonForVisit": {},
                "status": {},
                "showHistory": {},
                "petQuestionnaire": {},
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {}
            },
            {
                "id": {},
                "appointment": {},
                "ownerName": {},
                "petName": {},
                "reasonForVisit": {},
                "status": {},
                "showHistory": {},
                "petQuestionnaire": {},
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {}
            },
            {
                "id": {},
                "appointment": {},
                "ownerName": {},
                "petName": {},
                "reasonForVisit": {},
                "status": {},
                "showHistory": {},
                "petQuestionnaire": {},
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {}
            },
            {
                "id": {},
                "appointment": {},
                "ownerName": {},
                "petName": {},
                "reasonForVisit": {},
                "status": {},
                "showHistory": {},
                "petQuestionnaire": {},
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {}
            },
            {
                "id": {},
                "appointment": {},
                "ownerName": {},
                "petName": {},
                "reasonForVisit": {},
                "status": {},
                "showHistory": {},
                "petQuestionnaire": {},
                "vlmVisitItemLinks": {},
                "expandedRowContent1": {}
            }
        ]
    },
    "message": [],
    "values": [],
    "labels": [
        {
            "locale": "en-US",
            "text": "Bulk Visits",
            "helpText": "testing tooltip"
        }
    ],
    "elemLabels": {}
};