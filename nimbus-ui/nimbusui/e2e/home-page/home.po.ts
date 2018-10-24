import { browser, by, element, promise, until, protractor, ElementFinder } from 'protractor';

export class HomePage {
  navigateTo(): promise.Promise<any> {
    return browser.get('/petclinic#/h/petclinicdashboard/vpDashboard');
  }

  getParagraphText(): promise.Promise<string> {
    const captionElement = element(by.tagName('caption'));
    return captionElement.getText();
  }

  getOwnersButton(): any {
    const ownersButton = element(by.css('div nm-button button'));
    return ownersButton;
  }

  getVeterinarians(): any {
    const veterinariansButton = element(by.id('Panelmenu-Veterinarians'));
    return veterinariansButton;
  }

  getOwners(): any {
    const ownersButton = element(by.id('Panelmenu-Owners'));
    return ownersButton;
  }

  getPets(): any {
    const petsButton = element(by.id('Panelmenu-Pets'));
    return petsButton;
  }

  getNotes(): any {
    const notesButton = element(by.id('Panelmenu-Notes'));
    return notesButton;
  }

}
