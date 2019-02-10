export const textAreaElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "786",
        "code": "noteDescription1",
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
                "controlId": "test",
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
                    "name": "NotNull",
                    "attribute": {
                        "message": "Field is required.",
                        "groups": []
                    }
                },
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
    "configId": "786",
    "path": "/petview/vpAddEditPet/vtAddEditPet/vsAddEditPet/vfAddEditPet/noteDescription1",
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
            "text": "Note Description"
        }
    ],
    "elemLabels": {},
    "leafState": "testing leafstate"
  }