export const layoutServicePageParam: any = {
  config: {
    active: false,
    required: false,
    id: '495',
    code: 'vpHome',
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
        defaultPage: false,
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
      name: 'VLHome.VPHome',
      model: {
        paramConfigIds: ['497', '501']
      }
    }
  },
  enabled: true,
  visible: true,
  activeValidationGroups: [],

  configId: '495',
  path: '/home/vpHome',
  type: {
    model: {
      params: [
        {
          config: {
            uiStyles: {
              attributes: {
                alias: 'Footer'
              }
            }
          },
          enabled: true,
          visible: true,
          activeValidationGroups: [],

          configId: '497',
          path: '/home/vpHome/vsHomeHeader',
          type: {
            model: {
              params: [
                {
                  config: {
                    uiStyles: {
                      attributes: {
                        alias: ''
                      }
                    },
                    uiNatures: [
                      {
                        name: 'ViewConfig.FooterProperty',
                        attributes: {
                          value: 'DISCLAIMER'
                        }
                      }
                    ]
                  },
                  enabled: true,
                  visible: true,
                  activeValidationGroups: [],

                  configId: '499',
                  path: '/home/vpHome/vsHomeHeader/linkHomeLogo',
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
                      text: 'Anthem'
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
  labels: [
    {
      locale: 'en-US',
      text: 'testing label'
    }
  ],
  elemLabels: {}
};
