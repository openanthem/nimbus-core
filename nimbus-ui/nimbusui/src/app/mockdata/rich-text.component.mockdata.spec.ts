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