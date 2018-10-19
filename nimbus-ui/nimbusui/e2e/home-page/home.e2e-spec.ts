import { browser } from 'protractor';
import { HomePage } from './home.po';

describe('nimbus-ui HomePage', () => {
  let page: HomePage;
  page = new HomePage();

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('My Visits');    
  });

  it('1should display welcome message', () => {
    const ownersButton = page.getOwnersButton();
    ownersButton.click();
    browser.driver.sleep(500);
      expect(browser.driver.getCurrentUrl()).toContain('/petclinic#/h/ownerlandingview/vpOwners');  
  });

  it('2should display welcome message', () => {
    const veterinariansButton = page.getVeterinarians();
    veterinariansButton.click();
      browser.driver.sleep(500);
      expect(browser.driver.getCurrentUrl()).toContain('/petclinic#/h/veterinarianview/vpVeterenarians');  
  });

  it('3should display welcome message', () => {
    const panelOwnersButton = page.getOwners();
    panelOwnersButton.click();
      browser.driver.sleep(500);
      expect(browser.driver.getCurrentUrl()).toContain('/petclinic#/h/ownerlandingview/vpOwners');    
  });

  it('4should display welcome message', () => {
    const petsButton = page.getPets();
    petsButton.click();
      browser.driver.sleep(500);
      expect(browser.driver.getCurrentUrl()).toContain('/petclinic#/h/petview/vpAllPets');    
  });

  it('5should display welcome message', () => {
    const notesButton = page.getNotes();
    notesButton.click();
      browser.driver.sleep(500);
      expect(browser.driver.getCurrentUrl()).toContain('/petclinic#/h/petclinicdashboard/vpNotes');    
  });

});
