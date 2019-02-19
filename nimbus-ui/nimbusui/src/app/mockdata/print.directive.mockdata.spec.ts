export const printServiceSubject: any = {"path":"/ownerlandingview/vpOwners","uiEvent":{"isTrusted":true},"printConfig":{"autoPrint":true,"closeAfterPrint":true,"delay":300,"useAppStyles":true,"useDelay":true}};

export const printDirectiveElement: any = {
    "config": {
        "active": false,
        "required": false,
        "id": "603",
        "code": "vpOwners",
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
                "defaultPage": true,
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
            "name": "VROwnerLanding.VPOwners",
            "model": {
                "paramConfigIds": [
                    "605"
                ]
            }
        }
    },
    "enabled": true,
    "visible": true,
    "activeValidationGroups": [],
    "collectionParams": [],
    "configId": "603",
    "path": "/ownerlandingview/vpOwners",
    "type": {
        "model": {
            "params": [
                {
                    "enabled": true,
                    "visible": true,
                    "activeValidationGroups": [],
                    "collectionParams": [],
                    "configId": "605",
                    "path": "/ownerlandingview/vpOwners/vtOwners",
                    "type": {
                        "model": {
                            "params": [
                                {
                                    "enabled": true,
                                    "visible": true,
                                    "activeValidationGroups": [],
                                    "collectionParams": [],
                                    "configId": "607",
                                    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "611",
                                                    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria",
                                                    "type": {
                                                        "model": {
                                                            "params": [
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "613",
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
                                                                    "elemLabels": {}
                                                                },
                                                                {
                                                                    "enabled": true,
                                                                    "visible": true,
                                                                    "activeValidationGroups": [],
                                                                    "collectionParams": [],
                                                                    "configId": "614",
                                                                    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/lastName",
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
                                                                    "configId": "615",
                                                                    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/vbgSearchOwner",
                                                                    "type": {
                                                                        "model": {
                                                                            "params": [
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "617",
                                                                                    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/vbgSearchOwner/search",
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
                                                                                            "text": "Search"
                                                                                        }
                                                                                    ],
                                                                                    "elemLabels": {}
                                                                                },
                                                                                {
                                                                                    "enabled": true,
                                                                                    "visible": true,
                                                                                    "activeValidationGroups": [],
                                                                                    "collectionParams": [],
                                                                                    "configId": "618",
                                                                                    "path": "/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/vbgSearchOwner/addOwner",
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
                                                                                            "text": "Add Owner"
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
                                    "configId": "619",
                                    "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners",
                                    "type": {
                                        "model": {
                                            "params": [
                                                {
                                                    "enabled": true,
                                                    "visible": true,
                                                    "activeValidationGroups": [],
                                                    "collectionParams": [],
                                                    "configId": "625",
                                                    "path": "/ownerlandingview/vpOwners/vtOwners/vsOwners/owners",
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
                                                            "text": "Owners"
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
};