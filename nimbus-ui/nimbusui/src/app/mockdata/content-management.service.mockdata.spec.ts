/**
 * @license
 * Copyright 2016-2019 the original author or authors.
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
export const cmsParam: any = {
  config: {
    active: false,
    required: false,
    id: '603',
    code: 'vpOwners',
    validations: null,
    uiNatures: [],
    uiStyles: {
      isLink: false,
      isHidden: false,
      name: 'ViewConfig.Page',
      attributes: {
        hidden: false,
        readOnly: false,
        submitButton: true,
        showName: true,
        pageSize: 25,
        browserBack: false,
        showAsLink: false,
        defaultPage: true,
        route: '',
        cssClass: '',
        fixLayout: false,
        alias: 'Page',
        imgSrc: ''
      }
    },
    type: {
      collection: false,
      nested: true,
      name: 'VROwnerLanding.VPOwners',
      model: {
        paramConfigIds: ['605']
      }
    }
  },
  enabled: true,
  visible: true,
  activeValidationGroups: [],

  configId: '603',
  path: '/ownerlandingview/vpOwners',
  type: {
    model: {
      params: [
        {
          enabled: true,
          visible: true,
          activeValidationGroups: [],

          configId: '605',
          path: '/ownerlandingview/vpOwners/vtOwners',
          type: {
            model: {
              params: [
                {
                  enabled: true,
                  visible: true,
                  activeValidationGroups: [],

                  configId: '607',
                  path:
                    '/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria',
                  type: {
                    model: {
                      params: [
                        {
                          enabled: true,
                          visible: true,
                          activeValidationGroups: [],

                          configId: '611',
                          path:
                            '/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria',
                          type: {
                            model: {
                              params: [
                                {
                                  enabled: true,
                                  visible: true,
                                  activeValidationGroups: [],

                                  configId: '613',
                                  path:
                                    '/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/firstName',
                                  type: {
                                    nested: false,
                                    name: 'string',
                                    collection: false
                                  },
                                  message: [],
                                  values: [],
                                  labels: [
                                    {
                                      locale: 'en-US',
                                      text: 'First Name'
                                    }
                                  ],
                                  elemLabels: {}
                                },
                                {
                                  enabled: true,
                                  visible: true,
                                  activeValidationGroups: [],

                                  configId: '614',
                                  path:
                                    '/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/lastName',
                                  type: {
                                    nested: false,
                                    name: 'string',
                                    collection: false
                                  },
                                  message: [],
                                  values: [],
                                  labels: [
                                    {
                                      locale: 'en-US',
                                      text: 'Last Name'
                                    }
                                  ],
                                  elemLabels: {}
                                },
                                {
                                  enabled: true,
                                  visible: true,
                                  activeValidationGroups: [],

                                  configId: '615',
                                  path:
                                    '/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/vbgSearchOwner',
                                  type: {
                                    model: {
                                      params: [
                                        {
                                          enabled: true,
                                          visible: true,
                                          activeValidationGroups: [],

                                          configId: '617',
                                          path:
                                            '/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/vbgSearchOwner/search',
                                          type: {
                                            nested: false,
                                            name: 'string',
                                            collection: false
                                          },
                                          message: [],
                                          values: [],
                                          labels: [
                                            {
                                              locale: 'en-US',
                                              text: 'Search'
                                            }
                                          ],
                                          elemLabels: {}
                                        },
                                        {
                                          enabled: true,
                                          visible: true,
                                          activeValidationGroups: [],

                                          configId: '618',
                                          path:
                                            '/ownerlandingview/vpOwners/vtOwners/vsSearchOwnerCriteria/vfSearchOwnerCriteria/vbgSearchOwner/addOwner',
                                          type: {
                                            nested: false,
                                            name: 'string',
                                            collection: false
                                          },
                                          message: [],
                                          values: [],
                                          labels: [
                                            {
                                              locale: 'en-US',
                                              text: 'Add Owner'
                                            }
                                          ],
                                          elemLabels: {}
                                        }
                                      ]
                                    }
                                  },
                                  message: [],
                                  values: [],
                                  labels: [],
                                  elemLabels: {}
                                }
                              ]
                            }
                          },
                          message: [],
                          values: [],
                          labels: [],
                          elemLabels: {}
                        }
                      ]
                    }
                  },
                  message: [],
                  values: [],
                  labels: [],
                  elemLabels: {}
                },
                {
                  enabled: true,
                  visible: true,
                  activeValidationGroups: [],

                  configId: '619',
                  path: '/ownerlandingview/vpOwners/vtOwners/vsOwners',
                  type: {
                    model: {
                      params: [
                        {
                          enabled: true,
                          visible: true,
                          activeValidationGroups: [],

                          configId: '625',
                          path:
                            '/ownerlandingview/vpOwners/vtOwners/vsOwners/owners',
                          type: {
                            model: {
                              params: []
                            }
                          },
                          page: {
                            last: true,
                            totalPages: 1,
                            totalElements: 0,
                            size: 0,
                            number: 0,
                            first: true,
                            numberOfElements: 0
                          },
                          tableBasedData: {
                            collectionParams: []
                          },
                          message: [],
                          values: [],
                          labels: [
                            {
                              locale: 'en-US',
                              text: 'Owners'
                            }
                          ],
                          elemLabels: {}
                        }
                      ]
                    }
                  },
                  message: [],
                  values: [],
                  labels: [],
                  elemLabels: {}
                }
              ]
            }
          },
          message: [],
          values: [],
          labels: [],
          elemLabels: {}
        }
      ]
    }
  },
  message: [],
  values: [],
  labels: [
    {
      locale: 'en-US',
      text: 'Owners'
    }
  ],
  elemLabels: {}
};
