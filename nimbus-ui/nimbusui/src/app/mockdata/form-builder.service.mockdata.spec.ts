export const formBuilderServiceElements: any = [
  {
    config: {
      active: false,
      required: false,
      id: '615',
      code: 'vbgSearchOwner',
      validations: null,
      uiNatures: [],
      uiStyles: {
        isLink: false,
        isHidden: false,
        name: 'ViewConfig.ButtonGroup',
        attributes: {
          hidden: false,
          readOnly: false,
          submitButton: true,
          showName: true,
          pageSize: 25,
          browserBack: false,
          showAsLink: false,
          cssClass: 'text-sm-center',
          alias: 'ButtonGroup'
        }
      },
      type: {
        collection: false,
        nested: true,
        name: 'VROwnerLanding.VBGSearchOwner',
        model: {
          paramConfigIds: ['617', '618']
        }
      }
    },
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
            config: {
              active: false,
              required: false,
              id: '617',
              code: 'search',
              validations: null,
              uiNatures: [],
              uiStyles: {
                isLink: false,
                isHidden: false,
                name: 'ViewConfig.Button',
                attributes: {
                  hidden: false,
                  readOnly: false,
                  submitButton: true,
                  showName: true,
                  pageSize: 25,
                  browserBack: false,
                  showAsLink: false,
                  b: '$execute',
                  method: 'GET',
                  formReset: true,
                  type: 'submit',
                  title: '',
                  url: '',
                  printPath: '',
                  cssClass: '',
                  payload: '',
                  alias: 'Button',
                  style: 'PRIMARY',
                  imgSrc: '',
                  imgType: 'FA'
                }
              },
              type: {
                collection: false,
                nested: false,
                name: 'string'
              }
            },
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
            config: {
              active: false,
              required: false,
              id: '618',
              code: 'addOwner',
              validations: null,
              uiNatures: [],
              uiStyles: {
                isLink: false,
                isHidden: false,
                name: 'ViewConfig.Button',
                attributes: {
                  hidden: false,
                  readOnly: false,
                  submitButton: true,
                  showName: true,
                  pageSize: 25,
                  browserBack: false,
                  showAsLink: false,
                  b: '$execute',
                  method: 'GET',
                  formReset: true,
                  type: 'button',
                  title: '',
                  url: '',
                  printPath: '',
                  cssClass: '',
                  payload: '',
                  alias: 'Button',
                  style: 'PRINT',
                  imgSrc: '',
                  imgType: 'FA'
                }
              },
              type: {
                collection: false,
                nested: false,
                name: 'string'
              }
            },
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
];
