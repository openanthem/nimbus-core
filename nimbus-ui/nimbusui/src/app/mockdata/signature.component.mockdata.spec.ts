export const signatureElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "930",
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
                "acceptLabel": "Save",
                "width": "345",
                "alias": "Signature",
                "scriptName": "testing",
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
    "configId": "930",
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