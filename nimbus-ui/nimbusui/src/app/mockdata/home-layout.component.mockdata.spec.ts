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

export const Homelayout:any = {
    "menu": [
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
            "code": "home"
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
            "code": "vets"
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
            "code": "pets"
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
            "code": "notes"
        }
    ],
    "topBar": {
        "branding": {
            "logo": {
                "configSvc": {
                    "flowConfigs": {
                        "ownerview": {
                            "model": {
                                "params": [ ]
                            },
                            "layout": "home"
                        },
                        "home": {
                            "model": {
                                "params": [ ]
                            }
                        }
                    }
                },
                "enabled": true,
                "visible": true,
                "activeValidationGroups": [],
                "collectionParams": [],
                "configId": "3923",
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
            }
        },
        "headerMenus": [],
        "accordions": [
            null
        ]
    },
    "footer": {
        "links": [
            null,
            null,
            null,
            null
        ]
    },
    "modalList": [
        null
    ]
  };