export const autocompleteElement: any = {
  config: {
    active: false,
    required: false,
    id: '530',
    code: 'xyz',
    validations: null,
    uiNatures: [],
    uiStyles: {
      isLink: false,
      isHidden: false,
      name: 'ViewConfig.Autocomplete',
      attributes: {
        hidden: false,
        readOnly: false,
        submitButton: true,
        showName: true,
        pageSize: 25,
        browserBack: false,
        showAsLink: false,
        dataEntryField: true,
        minLength: 2,
        display: 'label',
        alias: 'Autocomplete',
        postEventOnChange: true
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
  collectionParams: [],
  configId: '530',
  path:
    '/ownerview/vpAddEditOwner/vtAddEditOwner/vsAddEditOwner/vfAddEditOwner/xyz',
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
      text: 'Enter to search'
    }
  ],
  elemLabels: {}
};
