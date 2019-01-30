export const textBoxElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "558",
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
    "configId": "558",
    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/firstName",
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
    "elemLabels": {},
    "leafState": "testing textbox leafState"
};