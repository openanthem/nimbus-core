export const MockRichText = {
    "path": "/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/richTextbox",
    "configId": "503",
    "config": {
        "id": "503",
        "code": "richTextbox",
        "uiStyles": {
            "name": "ViewConfig.RichText",
            "attributes": {
                "toolbarFeatures": [
                    "HEADER",
                    "FONT",
                    "BOLD",
                    "ITALIC",
                    "UNDERLINE",
                    "STRIKE",
                    "COLOR",
                    "BACKGROUND",
                    "SCRIPT",
                    "SIZE",
                    "BLOCKQUOTE",
                    "CODE_BLOCK",
                    "LIST",
                    "INDENT",
                    "DIRECTION",
                    "ALIGN",
                    "LINK",
                    "IMAGE",
                    "VIDEO",
                    "CLEAN",
                    "VALUES_COMBOBOX"
                ],
                "formats": "",
                "cssClass": "nm-sample-style-class",
                "dataEntryField": true,
                "alias": "RichText",
                "placeholder": "Please enter a value",
                "controlId": "",
                "postEventOnChange": true,
                "readOnly": false,
                "cols": "",
            }
        },
        "validations": null,
        "uiNatures": null,
        "type": {
            "nested": false,
            "name": "string",
            "collection": false
        }
    },
    "labels": [
      {
        "locale": "en-US",
        "text": "Sample @RichText Component (w/ postEventOnChange = true)"
      }
    ],
    "values" : [
        {
            "label": "Value 1",
            "code": "#{VALUE_1}"
        }, {
            "label": "Value 2",
            "code": "#{VALUE_2}"
        }
    ],
    "enabled": true,
    "visible": true,
    "leafState": "<p>Hello <strong>World</strong>!</p>"
  };