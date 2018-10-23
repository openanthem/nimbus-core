import { browser } from 'protractor';
import { HomePage } from './home.po';

describe('nimbus-ui HomePage', () => {
  let page: HomePage;
  page = new HomePage();

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('My Visits');
  });

  it('on click of owners button should navigate to /petclinic#/h/ownerlandingview/vpOwners', () => {
    const ownersButton = page.getOwnersButton();
    ownersButton.click();
    browser.driver.sleep(500);
    expect(browser.driver.getCurrentUrl()).toContain('/petclinic#/h/ownerlandingview/vpOwners');
  });

  it('on click of veterinarians button should navigate to /petclinic#/h/veterinarianview/vpVeterenarians', () => {
    const veterinariansButton = page.getVeterinarians();
    veterinariansButton.click();
    browser.driver.sleep(500);
    expect(browser.driver.getCurrentUrl()).toContain('/petclinic#/h/veterinarianview/vpVeterenarians');
  });

  it('on click of owners button in left panel should navigate to /petclinic#/h/ownerlandingview/vpOwners', () => {
    const panelOwnersButton = page.getOwners();
    panelOwnersButton.click();
    browser.driver.sleep(500);
    expect(browser.driver.getCurrentUrl()).toContain('/petclinic#/h/ownerlandingview/vpOwners');
  });

  it('on click of pets button should navigate to /petclinic#/h/petview/vpAllPets', () => {
    const petsButton = page.getPets();
    petsButton.click();
    browser.driver.sleep(500);
    expect(browser.driver.getCurrentUrl()).toContain('/petclinic#/h/petview/vpAllPets');
  });

  it('on click of notes button should navigate to /petclinic#/h/petclinicdashboard/vpNotes', () => {
    const notesButton = page.getNotes();
    notesButton.click();
    browser.driver.sleep(500);
    expect(browser.driver.getCurrentUrl()).toContain('/petclinic#/h/petclinicdashboard/vpNotes');
  });

});
